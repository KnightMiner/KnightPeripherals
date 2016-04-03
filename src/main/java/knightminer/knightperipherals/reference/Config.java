package knightminer.knightperipherals.reference;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static boolean enableTurtleClaw, enableTurtleHammer, enableTurtleBow, enableTurtleTnt, enableTurtleCompressedHammer, enableTurtleLaser;
	public static boolean craftTurtleClaw, craftTurtleHammer, craftTurtleBow, craftTurtleTnt, craftTurtleCompressedHammer, craftTurtleLaser;
	public static boolean arrowsRequired;
	public static int clawFuelCost, tntPower, laserRange;
	
	public static void register(File file)
	{
		final Configuration config = new Configuration(file);
		
		config.load();
		
		enableTurtleClaw = config.getBoolean("turtle_claw", "enable", true, "Enables the clicking turtle");
		craftTurtleClaw = config.getBoolean("turtle_claw", "craft", true, "Allows the clicking turtle and turtle claw to be crafted");
		clawFuelCost = config.getInt("turtle_claw", "fuelCost", 0, 0, 100, "Fuel cost for clicking");

		enableTurtleHammer = config.getBoolean("turtle_hammer", "enable", true, "Enables the smashing turtle (requires Ex Nihilo)");
		craftTurtleHammer = config.getBoolean("turtle_hammer", "craft", true, "Allows the smashing turtle to be crafted");

		enableTurtleCompressedHammer = config.getBoolean("turtle_compressed_hammer", "enable", true, "Enables the crushing turtle (requires Ex Compressum)");
		craftTurtleCompressedHammer = config.getBoolean("turtle_compressed_hammer", "craft", true, "Allows the crushing turtle to be crafted");
		
		enableTurtleBow = config.getBoolean("turtle_bow", "enable", true, "Enables the ranged turtle");
		craftTurtleBow = config.getBoolean("turtle_bow", "craft", true, "Allows the ranged turtle to be crafted");
		arrowsRequired = config.getBoolean("turtle_bow", "requireArrows", true, "Are arrows required for the turtle to shoot arrows");
		
		enableTurtleTnt = config.getBoolean("turtle_tnt", "enable", true, "Enables the explosive turtle");
		craftTurtleTnt = config.getBoolean("turtle_tnt", "craft", true, "Allows the explosive turtle to be crafted");
		tntPower = config.getInt("turtle_tnt", "explosionPower", 5, 1, 10, "Explosion power when the turtle self desctucts");
		
		enableTurtleLaser = config.getBoolean("turtle_laser", "enable", true, "Enables the mining laser turtle");
		craftTurtleLaser = config.getBoolean("turtle_laser", "craft", true, "Allows the mining laser turtle to be crafted");
		laserRange = config.getInt("turtle_laser", "explosionPower", 5, 1, 16, "Maximum break range for the mining laser");
		
		if (config.hasChanged())
			config.save();
	}

}
