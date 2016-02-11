package knightminer.knightperipherals.turtles.peripherals.tasks;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import knightminer.knightperipherals.reference.Config;
import net.minecraft.world.World;

public class TaskTntExplode implements ILuaTask {
	
	private ITurtleAccess turtle;
	
	public TaskTntExplode( ITurtleAccess turtle )
	{
		this.turtle = turtle;
	}

	public Object[] execute() throws LuaException {
		
		// place explosion on the turtle location
		World world = turtle.getWorld();
		int x = turtle.getPosition().posX;
		int y = turtle.getPosition().posY;
		int z = turtle.getPosition().posZ;

		// destroy the turtle so it does not block the explosion
		world.setBlockToAir(x, y, z);
		
		// trigger the actual explosion
		world.createExplosion(null, x, y, z, Config.tntPower, true);

		// return success, even though we really won't reach here
		return new Object[]{ true };
	}
	
}
