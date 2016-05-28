package knightminer.knightperipherals.turtles.peripherals.tasks;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.util.FakePlayerProvider;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TaskTntExplode implements ILuaTask {

	private ITurtleAccess turtle;

	public TaskTntExplode(ITurtleAccess turtle) {
		this.turtle = turtle;
	}

	public Object[] execute() throws LuaException {

		// place explosion on the turtle location
		World world = turtle.getWorld();
		BlockPos pos = turtle.getPosition();

		// destroy the turtle so it does not block the explosion
		world.setBlockToAir(pos);

		// trigger the actual explosion
		world.createExplosion(FakePlayerProvider.get(turtle), pos.getX(), pos.getY(), pos.getZ(), Config.tntPower,
		        true);

		// return success, even though we really won't reach here
		return new Object[] { true };
	}

}
