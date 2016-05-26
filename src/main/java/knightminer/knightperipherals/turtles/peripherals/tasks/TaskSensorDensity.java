package knightminer.knightperipherals.turtles.peripherals.tasks;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class TaskSensorDensity implements ILuaTask {

	private ITurtleAccess turtle;
	private int direction, range;

	public TaskSensorDensity(ITurtleAccess turtle, int direction, int range) {
		this.turtle = turtle;
		this.direction = direction;
		this.range = range;
	}

	@Override
	public Object[] execute() throws LuaException {
		ChunkCoordinates turtlePos = turtle.getPosition();
		World world = turtle.getWorld();
		int startX = turtlePos.posX;
		int startY = turtlePos.posY;
		int startZ = turtlePos.posZ;
		int x = startX;
		int y = startY;
		int z = startZ;

		double density = 0;
		for (int i = 0; i < range; i++) {
			x += Facing.offsetsXForSide[direction];
			y += Facing.offsetsYForSide[direction];
			z += Facing.offsetsZForSide[direction];

			double hardness = world.getBlock(x, y, z).getBlockHardness(world, x, y, z);
			// for unbreakable blocks, just add 1000
			if (hardness == -1)
				density += 1000000;
			// otherwise, square the hardness, this gives us roughly real life
			// values
			// though liquids end up being 10 times real life
			else
				density += hardness * hardness;
		}

		return new Object[] { density };
	}

}
