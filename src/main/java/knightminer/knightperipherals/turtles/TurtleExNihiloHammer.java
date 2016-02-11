package knightminer.knightperipherals.turtles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.FakePlayerProvider;
import knightminer.knightperipherals.util.ModLogger;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class TurtleExNihiloHammer implements ITurtleUpgrade {
	
	public static HashMap<Entity, ITurtleAccess> map = new HashMap<Entity, ITurtleAccess>();

	@Override
	public int getUpgradeID()
	{
		return Reference.UPGRADE_HAMMER;
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
			return GameRegistry.findItemStack( ModIds.EXNIHILO, ModIds.EXNIHILO_HAMMER, 0 );
		} else
		{
			ModLogger.logger.info("Recipe for smashing turtle disabled");
			return null;
		}
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side)
	{
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction)
	{
		switch (verb)
		{
			case Attack:
				// grab a fake player to use for basic checks, along with as a target for when mobs get mad
				FakePlayer fakePlayer = FakePlayerProvider.get(turtle);
				TurtleUtil.setPlayerPosition(fakePlayer, turtle);
				
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
				int x = turtle.getPosition().posX + Facing.offsetsXForSide[direction];
				int y = turtle.getPosition().posY + Facing.offsetsYForSide[direction];
				int z = turtle.getPosition().posZ + Facing.offsetsZForSide[direction];
				World world = turtle.getWorld();
				
				// we cannot mine air
				if ( !world.isAirBlock(x, y, z) )
				{
					// find the block to dig
					Block block = world.getBlock(x, y, z);
					int blockMeta = world.getBlockMetadata(x, y, z);
					
					// get a list of products for this block
					ArrayList<Smashable> rewards = HammerRegistry.getRewards(block, blockMeta);
					
					// if we have something
					if ( rewards != null && rewards.size() > 0 )
					{
						// place all results in the inventory
						Iterator<Smashable> it = rewards.iterator();
						while(it.hasNext())
						{
							Smashable reward = it.next();
										
							if ( world.rand.nextFloat() <= reward.chance )
							{
								TurtleUtil.addToInv(turtle, new ItemStack(reward.item, 1, reward.meta));
							}
							
						}
					// otherwise, check if a tool is needed at all
					} else
					{
						if (block.getMaterial().isToolNotRequired() )
						{
							// and place the normal block drops into the inventory
							List<ItemStack> drops = block.getDrops(world, x, y, z, blockMeta, 0);
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
					// all cases leading here mean we dug the block, so remove the block, play a sound, and return true
					world.setBlockToAir(x, y, z);
					
					if (!world.isRemote )
						world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, block.stepSound.getBreakSound(), 1.0F, 0.8F);
					
					return TurtleCommandResult.success();
				}
				// block is air
				return TurtleCommandResult.failure("Nothing to dig here");
				
			// should never happen
			default: 
				return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side)
	{
		return GameRegistry.findItem(ModIds.EXNIHILO, ModIds.EXNIHILO_HAMMER).getIconFromDamage(0);
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
