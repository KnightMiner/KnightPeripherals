package knightminer.knightperipherals.turtles;

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
import knightminer.knightperipherals.KnightPeripherals;
import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurtleLaser implements ITurtleUpgrade {
	private static final ItemStack stack = new ItemStack(ModItems.turtleUpgrade, 1, 1);

	public int getLegacyUpgradeID() {
		return Reference.UPGRADE_LEGACY_LASER;
	}

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.UPGRADE_LASER);
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "turtleUpgrade.laser";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.craftTurtleLaser) {
			return stack.copy();
		}
		else {
			KnightPeripherals.logger.info("Recipe for mining laser turtle disabled");
			return null;
		}
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction) {
		switch (verb) {
			case Attack:
				// yes, its a laser, but it is for mining, plus I'd rather not
				// have two functions act the same
				return TurtleCommandResult.failure("No weapon to attack with");
			case Dig:
				// back out if there is no fuel
				// note that the laser does still fire if its journey is limited
				// by fuel, it just does not go as far
				if (turtle.isFuelNeeded() && turtle.getFuelLevel() == 0)
					return TurtleCommandResult.failure("No fuel");

				// start block location, used to make boxes for particles and
				// alike
				World world = turtle.getWorld();
				BlockPos startPos = turtle.getPosition();
				BlockPos pos = turtle.getPosition();

				// ending fuel cost, the fuel cost is less if a block is found
				// closer
				int fuelCost = 0;

				// find the nearest block within range, but don't break stuff if
				// not within range
				for (int i = 0; i < Config.laserRange; i++) {
					pos = pos.offset(direction);
					fuelCost++; // increase the fuel cost

					// if we found a block, attempt to break it
					if (!world.isAirBlock(pos)) {
						IBlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						// don't break liquids or unbreakable blocks
						if (block.getBlockHardness(world, pos) != -1 && !block.getMaterial().isLiquid()) {
							// add the item drop to the inventory
							List<ItemStack> drops = block.getDrops(world, pos, state, 0);
							if (drops.size() > 0)
								TurtleUtil.addItemListToInv(drops, turtle);

							// and then break the block with sound
							if (!world.isRemote)
								world.destroyBlock(pos, false);
						}

						// keep going if the block is replaceable (like tall
						// grass), otherwise stop now
						if (!block.getMaterial().isReplaceable())
							break;
					}

					// also stop now if we reached the maximum distance for the
					// current fuel level
					if (turtle.getFuelLevel() == fuelCost)
						break;
				}

				// fuel is consumed all at once for efficiency, we already check
				// earlier if we have enough
				if (turtle.isFuelNeeded())
					turtle.consumeFuel(fuelCost);

				// coordinates for bounding boxes and particles
				int boxXStart = Math.min(startPos.getX(), pos.getX());
				int boxYStart = Math.min(startPos.getY(), pos.getY());
				int boxZStart = Math.min(startPos.getZ(), pos.getZ());
				int boxXEnd = Math.max(startPos.getX(), pos.getX());
				int boxYEnd = Math.max(startPos.getY(), pos.getY());
				int boxZEnd = Math.max(startPos.getZ(), pos.getZ());

				// damage entities in the laser beam
				// note that it does not specify a type of entity, so that means
				// entities such as items and paintings will also be damaged
				AxisAlignedBB box = new AxisAlignedBB(boxXStart, boxYStart, boxZStart, boxXEnd + 1.0D, boxYEnd + 1.0D,
				        boxZEnd + 1.0D);
				List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
				for (Entity entity : entities)
					entity.attackEntityFrom(DamageSource.onFire, 10);

				// draw the laser
				for (float px = boxXStart; px <= boxXEnd; px += 0.1D)
					for (float py = boxYStart; py <= boxYEnd; py += 0.1D)
						for (float pz = boxZStart; pz <= boxZEnd; pz += 0.1D)
							((WorldServer)world).spawnParticle(
									EnumParticleTypes.REDSTONE,
									px + 0.5, py + 0.5, pz + 0.5,
									0, -1.0, 1.0, 1.0, 1);

				// we fired the laser, so return true
				return TurtleCommandResult.success();

			// should never happen
			default:
				return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
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
	public void update(ITurtleAccess turtle, TurtleSide side) {}

}
