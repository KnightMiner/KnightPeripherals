package knightminer.knightperipherals.reference;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static boolean enableTurtleClaw;
	public static boolean enableTurtleHammer;
	
	public static boolean craftTurtleClaw;
	public static boolean craftTurtleHammer;
	
	public static int clawFuelCost;
	
	public static void register(File file)
	{
		final Configuration config = new Configuration(file);
		
		config.load();
		
		enableTurtleClaw = config.get("turtle_claw", "enable", true, "Enables the clicking turtle").getBoolean(true);
		craftTurtleClaw = config.get("turtle_claw", "craft", true, "Allows the clicking turtle and turtle claw to be crafted").getBoolean(true);
		clawFuelCost = config.get("turtle_claw", "fuelCost", 0, "Fuel cost for clicking. Limit 0 - 100000, default 0", 0, 100000).getInt(0);

		enableTurtleHammer = config.get("turtle_hammer", "enable", true, "Enables the smashing turtle (requires Ex Nihilo)").getBoolean(true);
		craftTurtleHammer = config.get("turtle_hammer", "craft", true, "Allows the smashing turtle to be crafted").getBoolean(true);
		
		if (config.hasChanged())
			config.save();
	}

}
