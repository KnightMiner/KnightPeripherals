package knightminer.knightperipherals.turtles.peripherals.tasks;

import java.util.List;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TaskSensorSonar implements ILuaTask {

	private ITurtleAccess turtle;
	private EnumFacing direction;
	private int range;

	public TaskSensorSonar(ITurtleAccess turtle, EnumFacing direction, int range) {
		this.turtle = turtle;
		this.direction = direction;
		this.range = range;
	}

	@Override
	public Object[] execute() throws LuaException {
		BlockPos pos = turtle.getPosition();
		World world = turtle.getWorld();

		int distance = 0;
		boolean found = false;
		for (int i = 0; i < range; i++) {
			pos = pos.offset(direction);

			// first, make sure the block is either a liquid or air, if not then
			// end
			// note liquids increase the value
			int result = 1;
			if (world.getBlockState(pos).getBlock().getMaterial().isLiquid())
				result = 2; // liquids add two to the distance
			else if (!world.isAirBlock(pos)) {
				distance += 1;
				found = true;
				break;
			}
			distance += result;

			// next, check if the block contains an entity
			// the reason I check this each block rather than getting a final
			// distance is so liquids get added right
			// as otherwise the distance to an entity never includes a liquid
			AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D,
			        pos.getY() + 1.0D, pos.getZ() + 1.0D);
			List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
			if (!entities.isEmpty()) {
				found = true;
				break;
			}
		}

		if (!found)
			distance = -1;

		return new Object[] { distance };
	}

}
