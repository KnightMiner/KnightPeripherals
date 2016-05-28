package knightminer.knightperipherals.turtles.peripherals;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.turtles.peripherals.tasks.TaskSensorDensity;
import knightminer.knightperipherals.turtles.peripherals.tasks.TaskSensorSonar;
import knightminer.knightperipherals.util.LuaTimer;
import net.minecraft.util.EnumFacing;

public class PeripheralSensor implements IPeripheral {
	private ITurtleAccess turtle;

	public PeripheralSensor(ITurtleAccess turtle) {
		this.turtle = turtle;
	}

	@Override
	public String getType() {
		return "sensor";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "getRange", "sonar", "sonarUp", "sonarDown", "density", "densityUp", "densityDown" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
	        throws LuaException, InterruptedException {

		// we use the same range arg for all methods, so process it outside of
		// the switch
		int range = Config.sensorRange;
		if (method != 0) { // except the getter of course
			if (arguments.length > 0 && !(arguments[0] instanceof Double))
				throw new LuaException("Bad argument #1 (expected number)");

			if (arguments.length > 0) {
				// yes, I have to cast it three times since the classes can only
				// be cast to the corresponding primitive type
				int rangeArg = (int) (double) (Double) arguments[0];
				// if the arg is too big, throw an exception
				if (rangeArg > range)
					throw new LuaException("Range cannot be greater than " + range);
				else
					range = rangeArg;
			}
		}

		switch (method) {
			// getRange - returns the maximum range for the sensor
			case 0:
				return new Object[] { range };

			// sonar - returns the distance to the nearest block/entity, though
			// liquids count as two blocks
			case 1:
			case 2:
			case 3: {

				// setup the parameters
				EnumFacing direction = method == 2 ? EnumFacing.UP
				        : method == 3 ? EnumFacing.DOWN : turtle.getDirection();

				// execute main task
				return context.executeMainThreadTask(new TaskSensorSonar(turtle, direction, range));
			}

				// density - adds the density of blocks within range
			case 4:
			case 5:
			case 6: {

				// setup the parameters
				EnumFacing direction = method == 5 ? EnumFacing.UP
				        : method == 6 ? EnumFacing.DOWN : turtle.getDirection();

				// run before the delay, just stylistic
				Object[] result = context.executeMainThreadTask(new TaskSensorDensity(turtle, direction, range));

				// delay the command to discourage using it to find exact
				// hardness values of a block by setting the range
				LuaTimer.delay(context, computer, range);

				return result;
			}
		}

		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {}

	@Override
	public void detach(IComputerAccess computer) {}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

}
