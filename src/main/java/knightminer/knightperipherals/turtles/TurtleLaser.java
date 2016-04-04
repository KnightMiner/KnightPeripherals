package knightminer.knightperipherals.turtles;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.ModLogger;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class TurtleLaser implements ITurtleUpgrade {
	private static final Item item = ModItems.miningLaser;
	
	@Override
	public int getUpgradeID()
	{
		return Reference.UPGRADE_LASER;
	}

	@Override
	public String getUnlocalisedAdjective()
	{
		return "turtleUpgrade.laser";
	}

	@Override
	public TurtleUpgradeType getType()
	{
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem()
	{
		if (Config.craftTurtleLaser)
		{
			return new ItemStack(item, 1);
		} else
		{
			ModLogger.logger.info("Recipe for mining laser turtle disabled");
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
				// yes, its a laser, but it is for mining, plus I'd rather not have two functions act the same
				return TurtleCommandResult.failure("No weapon to attack with");
			case Dig:
				// start block location
				World world = turtle.getWorld();
				ChunkCoordinates turtlePos = turtle.getPosition();
				int startX = turtlePos.posX;
				int startY = turtlePos.posY;
				int startZ = turtlePos.posZ;
				int x = startX;
				int y = startY;
				int z = startZ;
				
				// ending fuel cost, the fuel cost is less if a block is found closer
				int fuelCost = 0;

				// find the nearest block within range, but don't break stuff if not within range
				boolean found = false;
				for (int i = 0; i < Config.laserRange; i++)
				{
					x += Facing.offsetsXForSide[direction];
					y += Facing.offsetsYForSide[direction];
					z += Facing.offsetsZForSide[direction];
					fuelCost++; // increase the fuel cost
					
					// if we find a block here, stop as we are done
					if ( !world.isAirBlock(x, y, z) )
					{
						found = true;
						break;
					}
				}
				
				// back out if there is not enough fuel
				if ( turtle.getFuelLevel() < fuelCost )
					return TurtleCommandResult.failure("Not enough fuel");
				
				turtle.consumeFuel(fuelCost);
				
				// if we have a block, try and break it, though no error will be thrown if unbreakable (rather false means the laser did not fire)
				if ( found )
				{
					Block block = world.getBlock(x, y, z);
					// don't break liquids or unbreakable blocks
					if ( block.getBlockHardness(world, x, y, z) != -1 && !block.getMaterial().isLiquid() )
					{
						// add the item drop to the inventory
						List<ItemStack> drops = block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
						if ( drops.size() > 0 )
							TurtleUtil.addItemListToInv( drops, turtle );
						
						// and then break the block with sound
						if (!world.isRemote)
							world.func_147480_a(x, y, z, false);
					}
				}
				
				// box coordinates for entity damage and particles, to make sure we have the directions right for the block edges
				int boxX1 = Math.min(startX, x);
				int boxX2 = Math.max(startX, x);
				int boxY1 = Math.min(startY, y);
				int boxY2 = Math.max(startY, y);
				int boxZ1 = Math.min(startZ, z);
				int boxZ2 = Math.max(startZ, z);
				
				// damage entities in the laser beam
				// note that it does not specify a type of entity, so that means entities such as items and paintings will also be damaged
				AxisAlignedBB box = AxisAlignedBB.getBoundingBox(boxX1, boxY1, boxZ1, boxX2 + 1.0D, boxY2 + 1.0D, boxZ2 + 1.0D);
				@SuppressWarnings("unchecked")
				List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
				for ( Entity entity : entities )
					entity.attackEntityFrom(DamageSource.onFire, 10);
				
				// draw the laser
				for ( double px = boxX1; px <= boxX2; px += 0.1D )
					for ( double py = boxY1; py <= boxY2; py += 0.1D )
						for ( double pz = boxZ1; pz <= boxZ2; pz += 0.1D )
						{
							ModLogger.logger.info("Spawning particle at " + px + "," + py + "," + pz);
							world.spawnParticle("reddust", px + 0.5D, py + 0.5D, pz + 0.5D, -1.0D, 0.5D, 1.0D);
						}
				
				// we fired the laser, so return true
				return TurtleCommandResult.success();
				
			// should never happen
			default: 
				return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side)
	{
		return item.getIconFromDamage(0);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side){}

}
