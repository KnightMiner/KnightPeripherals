package knightminer.knightperipherals.turtles.peripherals.tasks;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TaskSensorDensity implements ILuaTask {

	private ITurtleAccess turtle;
	private EnumFacing direction;
	private int range;

	public TaskSensorDensity(ITurtleAccess turtle, EnumFacing direction, int range) {
		this.turtle = turtle;
		this.direction = direction;
		this.range = range;
	}

	@Override
	public Object[] execute() throws LuaException {
		BlockPos pos = turtle.getPosition();
		World world = turtle.getWorld();

		double density = 0;
		for (int i = 0; i < range; i++) {
			pos = pos.offset(direction);

			double hardness = world.getBlockState(pos).getBlock().getBlockHardness(world, pos);
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
