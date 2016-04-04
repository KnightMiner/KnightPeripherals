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
		
		enableTurtleClaw = config.getBoolean("enable", "turtle_claw", true, "Enables the clicking turtle");
		craftTurtleClaw = config.getBoolean("craft", "turtle_claw", true, "Allows the clicking turtle and turtle claw to be crafted");
		clawFuelCost = config.getInt("fuelCost", "turtle_claw", 0, 0, 100, "Fuel cost for clicking");

		enableTurtleHammer = config.getBoolean("enable", "turtle_hammer", true, "Enables the smashing turtle (requires Ex Nihilo)");
		craftTurtleHammer = config.getBoolean("craft", "turtle_hammer", true, "Allows the smashing turtle to be crafted");

		enableTurtleCompressedHammer = config.getBoolean("enable", "turtle_compressed_hammer", true, "Enables the crushing turtle (requires Ex Compressum)");
		craftTurtleCompressedHammer = config.getBoolean("craft", "turtle_compressed_hammer", true, "Allows the crushing turtle to be crafted");
		
		enableTurtleBow = config.getBoolean("enable", "turtle_bow", true, "Enables the ranged turtle");
		craftTurtleBow = config.getBoolean("craft", "turtle_bow", true, "Allows the ranged turtle to be crafted");
		arrowsRequired = config.getBoolean("requireArrows", "turtle_bow", true, "Are arrows required for the turtle to shoot arrows");
		
		enableTurtleTnt = config.getBoolean("enable", "turtle_tnt", true, "Enables the explosive turtle");
		craftTurtleTnt = config.getBoolean("craft", "turtle_tnt", true, "Allows the explosive turtle to be crafted");
		tntPower = config.getInt("explosionPower", "turtle_tnt", 5, 1, 10, "Explosion power when the turtle self desctucts");
		
		enableTurtleLaser = config.getBoolean("enable", "turtle_laser", true, "Enables the mining laser turtle");
		craftTurtleLaser = config.getBoolean("craft", "turtle_laser", true, "Allows the mining laser turtle to be crafted");
		laserRange = config.getInt("range", "turtle_laser", 8, 1, 16, "Maximum break range for the mining laser");
		
		if (config.hasChanged())
			config.save();
	}

}
