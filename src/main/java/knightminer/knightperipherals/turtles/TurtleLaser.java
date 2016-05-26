package knightminer.knightperipherals.turtles;

import java.util.List;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import knightminer.knightperipherals.util.KnightPeripheralsPacketHandler;
import knightminer.knightperipherals.util.TurtleParticleMessage;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class TurtleLaser implements ITurtleUpgrade {
	private static final ItemStack item = new ItemStack(ModItems.turtleUpgrade, 1, 1);

	@Override
	public int getUpgradeID() {
		return Reference.UPGRADE_LASER;
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
			return item.copy();
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
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		switch (verb) {
			case Attack:
				// yes, its a laser, but it is for mining, plus I'd rather not
				// have two functions act the same
				return TurtleCommandResult.failure("No weapon to attack with");
			case Dig:
				// back out if there is no fuel
				// note that the laser does still fire if its journey is limited
				// by fuel, it just does not go as far
				if (turtle.getFuelLevel() == 0)
					return TurtleCommandResult.failure("No fuel");

				// start block location, used to make boxes for particles and
				// alike
				World world = turtle.getWorld();
				ChunkCoordinates turtlePos = turtle.getPosition();
				int startX = turtlePos.posX;
				int startY = turtlePos.posY;
				int startZ = turtlePos.posZ;
				int x = startX;
				int y = startY;
				int z = startZ;

				// ending fuel cost, the fuel cost is less if a block is found
				// closer
				int fuelCost = 0;

				// find the nearest block within range, but don't break stuff if
				// not within range
				for (int i = 0; i < Config.laserRange; i++) {
					x += Facing.offsetsXForSide[direction];
					y += Facing.offsetsYForSide[direction];
					z += Facing.offsetsZForSide[direction];
					fuelCost++; // increase the fuel cost

					// if we found a block, attempt to break it
					if (!world.isAirBlock(x, y, z)) {
						Block block = world.getBlock(x, y, z);
						// don't break liquids or unbreakable blocks
						if (block.getBlockHardness(world, x, y, z) != -1 && !block.getMaterial().isLiquid()) {
							// add the item drop to the inventory
							List<ItemStack> drops = block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
							if (drops.size() > 0)
								TurtleUtil.addItemListToInv(drops, turtle);

							// and then break the block with sound
							if (!world.isRemote)
								world.func_147480_a(x, y, z, false);
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
				turtle.consumeFuel(fuelCost);

				// damage entities in the laser beam
				// note that it does not specify a type of entity, so that means
				// entities such as items and paintings will also be damaged
				AxisAlignedBB box = AxisAlignedBB.getBoundingBox(Math.min(startX, x), Math.min(startY, y),
				        Math.min(startZ, z), Math.max(startX, x) + 1.0D, Math.max(startY, y) + 1.0D,
				        Math.max(startZ, z) + 1.0D);
				@SuppressWarnings("unchecked")
				List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
				for (Entity entity : entities)
					entity.attackEntityFrom(DamageSource.onFire, 10);

				// draw the laser
				KnightPeripheralsPacketHandler.INSTANCE.sendToAllAround(
				        new TurtleParticleMessage(0, startX, startY, startZ, x, y, z),
				        new TargetPoint(world.provider.dimensionId, x, y, z, 64));

				// we fired the laser, so return true
				return TurtleCommandResult.success();

			// should never happen
			default:
				return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return item.getItem().getIconFromDamage(item.getItemDamage());
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}

}
