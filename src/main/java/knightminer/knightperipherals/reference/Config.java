package knightminer.knightperipherals.reference;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static boolean enableTurtleClaw, enableTurtleHammer, enableTurtleBow, enableTurtleTnt;
	public static boolean craftTurtleClaw, craftTurtleHammer, craftTurtleBow, craftTurtleTnt;
	public static boolean arrowsRequired;
	public static int clawFuelCost, tntPower;
	
	public static void register(File file)
	{
		final Configuration config = new Configuration(file);
		
		config.load();
		
		enableTurtleClaw = config.get("turtle_claw", "enable", true, "Enables the clicking turtle").getBoolean(true);
		craftTurtleClaw = config.get("turtle_claw", "craft", true, "Allows the clicking turtle and turtle claw to be crafted").getBoolean(true);
		clawFuelCost = config.get("turtle_claw", "fuelCost", 0, "Fuel cost for clicking. Limit 0 - 100000, default 0", 0, 100000).getInt(0);

		enableTurtleHammer = config.get("turtle_hammer", "enable", true, "Enables the smashing turtle (requires Ex Nihilo)").getBoolean(true);
		craftTurtleHammer = config.get("turtle_hammer", "craft", true, "Allows the smashing turtle to be crafted").getBoolean(true);
		
		enableTurtleBow = config.get("turtle_bow", "enable", true, "Enables the ranged turtle").getBoolean(true);
		craftTurtleBow = config.get("turtle_bow", "craft", true, "Allows the ranged turtle to be crafted").getBoolean(true);
		arrowsRequired = config.get("turtle_bow", "requireArrows", true, "Are arrows required for the turtle to shoot arrows").getBoolean(true);
		
		enableTurtleTnt = config.get("turtle_tnt", "enable", true, "Enables the explosive turtle").getBoolean(true);
		craftTurtleTnt = config.get("turtle_tnt", "craft", true, "Allows the explosive turtle to be crafted").getBoolean(true);
		tntPower = config.get("turtle_tnt", "explosionPower", 5, "Explosion power when the turtle self desctucts, default 5", 1, 10).getInt(5);
		
		if (config.hasChanged())
			config.save();
	}

}
