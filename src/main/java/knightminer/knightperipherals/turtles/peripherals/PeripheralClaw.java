package knightminer.knightperipherals.turtles.peripherals;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.turtles.peripherals.tasks.TaskClawClick;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class PeripheralClaw implements IPeripheral {
	
	private ITurtleAccess turtle;

	public PeripheralClaw(ITurtleAccess turtle) {
		this.turtle = turtle;
	}

	@Override
	public String getType() {
		return "claw";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]
			{
				"click",
				"clickUp",
				"clickDown"
			};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
	throws LuaException, InterruptedException
	{
		// TODO: attempt config option for click delay
		if ( method == 0 || method == 1 || method == 2 )
		{
			// test the first parameter for a boolean
			if (arguments.length > 0 && !(arguments[0] instanceof Boolean))
			{
				throw new LuaException("Bad argument #1 (expected boolean)");
			}
			
			// setup the parameter
			Boolean sneaking = false;
			if (arguments.length > 0)
			{
				sneaking = (Boolean) arguments[0];
			}
			
			// process the direction based on method
			// return 0 for up, 1 for down, and 2-5 for sides
			int direction = method == 1 ? 1 : method == 2 ? 0 : turtle.getDirection();
			int x = turtle.getPosition().posX + Facing.offsetsXForSide[direction];
			int y = turtle.getPosition().posY + Facing.offsetsYForSide[direction];
			int z = turtle.getPosition().posZ + Facing.offsetsZForSide[direction];
			
			// find our block and cancel if it is air
			// prevents nearly all other turtle peripherals from being useless
			World world = turtle.getWorld();
			if (world.isAirBlock(x,y,z))
			{
				return new Object[]{ false, "No block at target" };
			}
			
			// Check for fuel requirement
			int fuelCost = Config.clawFuelCost;
			if (fuelCost > 0 && turtle.isFuelNeeded() && turtle.getFuelLevel() < fuelCost)
			{
				return new Object[]{ false, "Not enough fuel" };
			}
			
			// call the data using context.executeMainTaskThread to make it thread safe and have a 1 tick delay
			return context.executeMainThreadTask( new TaskClawClick( turtle, world, direction, x, y, z, sneaking, fuelCost ) );
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
	}

	@Override
	public void detach(IComputerAccess computer) {

	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

}
