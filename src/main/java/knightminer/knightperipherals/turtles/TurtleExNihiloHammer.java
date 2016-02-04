package knightminer.knightperipherals.turtles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import exnihilo2.registries.hammering.HammerRegistry;
import exnihilo2.registries.hammering.HammerReward;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.FakePlayerProvider;
import knightminer.knightperipherals.util.ModLogger;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurtleExNihiloHammer implements ITurtleUpgrade {
	
	private static ItemStack stack = GameRegistry.makeItemStack(ModIds.EXNIHILO_HAMMER, 0, 1, null);
	
	public static HashMap<Entity, ITurtleAccess> map = new HashMap<Entity, ITurtleAccess>();

	@Override
	public int getLegacyUpgradeID()
	{
		return Reference.UPGRADE_LEGACY_HAMMER;
	}

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.UPGRADE_HAMMER);
	}

	@Override
	public String getUnlocalisedAdjective()
	{
		return "turtleUpgrade.hammer";
	}

	@Override
	public TurtleUpgradeType getType()
	{
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem()
	{
		if (Config.craftTurtleHammer)
		{
			return stack;
		} else
		{
			ModLogger.logger.info("Recipe for smashing turtle disabled");
			return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) {
		IBakedModel model = TurtleUtil.getMesher().getItemModel(stack);
		Matrix4f transform = TurtleUtil.getTransforms(side);
		return Pair.of(model, transform);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side)
	{
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction)
	{
		switch (verb)
		{
			case Attack:
				// grab a fake player to use for basic checks
				FakePlayer fakePlayer = FakePlayerProvider.get(turtle);
				
				// find the closest entity
				Entity entity = TurtleUtil.getClosestEntity(turtle, fakePlayer, direction);
				
				// if we found something and can attack it
				if (entity != null && entity.canAttackWithItem() && !entity.hitByEntity(fakePlayer))
				{
					// mark it to collect the drops, then attack
					map.put(entity, turtle);
					if (entity.attackEntityFrom(DamageSource.causePlayerDamage(fakePlayer), 7)) {
						return TurtleCommandResult.success();
					}
				}
				
				// otherwise return failure
				return TurtleCommandResult.failure();
			case Dig:
				// find the block location
				BlockPos pos = turtle.getPosition().offset(direction);
				World world = turtle.getWorld();
				
				// we cannot mine air
				if ( !world.isAirBlock(pos) )
				{
					// find the block to dig
					IBlockState state = world.getBlockState(pos);
					Block block = state.getBlock();
					
					// get a list of products for this block
					//ArrayList<Smashable> rewards = HammerRegistry.getRewards(block, blockMeta);
					
					// if we have something
					if ( HammerRegistry.isHammerable(state)
						&& (block.getMaterial().isToolNotRequired()
						|| block.getHarvestLevel(state) <= 3 ))
					{
						// place all results in the inventory
						ArrayList<HammerReward> rewards = HammerRegistry.getEntryForBlockState(state).getRewards();
						Iterator<HammerReward> it = rewards.iterator();
						while(it.hasNext())
						{
							HammerReward reward = it.next();
										
							if ( world.rand.nextInt(100) <= reward.getBaseChance() )
							{
								TurtleUtil.addToInv(turtle, reward.getItem().copy());
							}
							
						}
					// otherwise, check if a tool is needed at all
					} else
					{
						if (block.getMaterial().isToolNotRequired() )
						{
							// and place the normal block drops into the inventory
							List<ItemStack> drops = block.getDrops(world, pos, state, 0);
							if( drops.size() > 0 )
							{
								TurtleUtil.addItemListToInv( drops, turtle );
							}
							
						} else
						{
							// block cannot be properly broken
							return TurtleCommandResult.failure("Block is unbreakable");
						}
					}
					// all cases leading here mean we dug the block, so remove the block and return true
					world.setBlockToAir(pos);
					return TurtleCommandResult.success();
				}
				// block is air
				return TurtleCommandResult.failure("Nothing to dig here");
				
			// should never happen
			default: 
				return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
		}
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side)
	{
	}
	
	// keep track of entity drops to pull into the turtle's inventory
	// Credit: austinv11
	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		if (map.containsKey(event.entity)) {
			TurtleUtil.addItemListToInv(TurtleUtil.entityItemsToItemStack(event.drops), map.get(event.entity));
			event.setCanceled(true);
			map.remove(event.entity);
		}
	}

}
