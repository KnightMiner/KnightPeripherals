package knightminer.knightperipherals.reference;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	public static boolean enableTurtleClaw, enableTurtleHammer, enableTurtleBow, enableTurtleTnt,
	        enableTurtleCompressedHammer, enableTurtleLaser, enableTurtleSensor;
	public static boolean craftTurtleClaw, craftTurtleHammer, craftTurtleBow, craftTurtleTnt,
	        craftTurtleCompressedHammer, craftTurtleLaser, craftTurtleSensor;
	public static boolean arrowsRequired;
	public static int clawFuelCost, clawDelay, tntPower, laserRange, sensorRange;

	public static void register(File file) {
		final Configuration config = new Configuration(file);

		config.load();

		// Clicking turtle
		config.addCustomCategoryComment("turtle_claw",
		        "Configuration for clicking turtles. They can simulate a player right click on the block in front of them");
		enableTurtleClaw = config.getBoolean("enable", "turtle_claw", true, "Enables the clicking turtle");
		craftTurtleClaw = config.getBoolean("craft", "turtle_claw", true,
		        "Allows the clicking turtle and turtle claw to be crafted");
		clawFuelCost = config.getInt("fuelCost", "turtle_claw", 0, 0, 100, "Fuel cost for clicking");
		clawDelay = config.getInt("delay", "turtle_claw", 4, 0, 20, "Delay after clicking in ticks");

		// Smasking turtle
		config.addCustomCategoryComment("turtle_hammer",
		        "Configuration for smashing turtles. They can break a block and obtain Ex Nihilo hammer drops from it");
		enableTurtleHammer = config.getBoolean("enable", "turtle_hammer", true,
		        "Enables the smashing turtle (requires Ex Nihilo)");
		craftTurtleHammer = config.getBoolean("craft", "turtle_hammer", true,
		        "Allows the smashing turtle to be crafted");

		// Crushing turtle
		config.addCustomCategoryComment("turtle_compressed_hammer",
		        "Configuration for crushing turtles. They can break a block and obtain Ex Compressum compressed hammer drops from it");
		enableTurtleCompressedHammer = config.getBoolean("enable", "turtle_compressed_hammer", true,
		        "Enables the crushing turtle (requires Ex Compressum)");
		craftTurtleCompressedHammer = config.getBoolean("craft", "turtle_compressed_hammer", true,
		        "Allows the crushing turtle to be crafted");

		// Ranged turtle
		config.addCustomCategoryComment("turtle_bow",
		        "Configuration for ranged turtles. They can fire arrows from the turtle's inventory, acting like melee turtles");
		enableTurtleBow = config.getBoolean("enable", "turtle_bow", true, "Enables the ranged turtle");
		craftTurtleBow = config.getBoolean("craft", "turtle_bow", true, "Allows the ranged turtle to be crafted");
		arrowsRequired = config.getBoolean("requireArrows", "turtle_bow", true,
		        "Are arrows required for the turtle to shoot arrows");

		// Ranged turtle
		config.addCustomCategoryComment("turtle_tnt",
		        "Configuration for explosive turtles. They can drop TNT and self destruct");
		enableTurtleTnt = config.getBoolean("enable", "turtle_tnt", true, "Enables the explosive turtle");
		craftTurtleTnt = config.getBoolean("craft", "turtle_tnt", true, "Allows the explosive turtle to be crafted");
		tntPower = config.getInt("explosionPower", "turtle_tnt", 5, 1, 10,
		        "Explosion power when the turtle self desctucts");

		// Ranged turtle
		config.addCustomCategoryComment("turtle_laser",
		        "Configuration for mining laser turtles. They can mine blocks at a distance at the cost of using fuel to break the block");
		enableTurtleLaser = config.getBoolean("enable", "turtle_laser", true, "Enables the mining laser turtle");
		craftTurtleLaser = config.getBoolean("craft", "turtle_laser", true,
		        "Allows the mining laser turtle to be crafted");
		laserRange = config.getInt("range", "turtle_laser", 8, 1, 32, "Maximum breaking range for the mining laser");

		// Sensor turtle
		config.addCustomCategoryComment("turtle_sensor",
		        "Configuration for sensor turtles. They can detect various things in the world around them");
		enableTurtleSensor = config.getBoolean("enable", "turtle_sensor", true, "Enables the sensor turtle");
		craftTurtleSensor = config.getBoolean("craft", "turtle_sensor", true, "Allows the sensor turtle to be crafted");
		sensorRange = config.getInt("range", "turtle_sensor", 64, 1, 128, "Maximum range for the sensor");

		if (config.hasChanged())
			config.save();
	}

}
