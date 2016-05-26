package knightminer.knightperipherals.turtles.peripherals.tasks;

import java.util.List;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class TaskSensorSonar implements ILuaTask {

	private ITurtleAccess turtle;
	private int direction, range;

	public TaskSensorSonar(ITurtleAccess turtle, int direction, int range) {
		this.turtle = turtle;
		this.direction = direction;
		this.range = range;
	}

	@Override
	public Object[] execute() throws LuaException {
		ChunkCoordinates turtlePos = turtle.getPosition();
		World world = turtle.getWorld();
		int x = turtlePos.posX;
		int y = turtlePos.posY;
		int z = turtlePos.posZ;

		int distance = 0;
		boolean found = false;
		for (int i = 0; i < range; i++) {
			x += Facing.offsetsXForSide[direction];
			y += Facing.offsetsYForSide[direction];
			z += Facing.offsetsZForSide[direction];

			// first, make sure the block is either a liquid or air, if not end
			// note liquids increase the value
			int result = 1;
			if (world.getBlock(x, y, z).getMaterial().isLiquid())
				result = 2; // liquids add two to the distance
			else if (!world.isAirBlock(x, y, z)) {
				distance += 1;
				found = true;
				break;
			}
			distance += result;

			// next, check if the block contains an entity
			// the reason I check this each block rather than getting a final
			// distance is so liquids get added right
			// as otherwise the distance to an entity never includes a liquid
			AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
			@SuppressWarnings("unchecked")
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
