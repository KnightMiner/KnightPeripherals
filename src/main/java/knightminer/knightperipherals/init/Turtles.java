package knightminer.knightperipherals.init;

import java.util.ArrayList;

import dan200.computercraft.api.ComputerCraftAPI;
import knightminer.knightperipherals.KnightPeripherals;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.turtles.TurtleBow;
import knightminer.knightperipherals.turtles.TurtleClaw;
import knightminer.knightperipherals.turtles.TurtleHammer;
import knightminer.knightperipherals.turtles.TurtleLaser;
import knightminer.knightperipherals.turtles.TurtleSensor;
import knightminer.knightperipherals.turtles.TurtleTnt;
import net.minecraftforge.fml.common.Loader;

public class Turtles {
	public static ArrayList<String> list = new ArrayList<String>();

	public static void register() {
		// Clicking Turtle
		if (Config.enableTurtleClaw) {
			ComputerCraftAPI.registerTurtleUpgrade(new TurtleClaw());
			list.add(Reference.UPGRADE_CLAW);
		}
		else {
			KnightPeripherals.logger.info("Skipping registering clicking turtle, upgrade disabled");
		}

		// Smashing Turtle
		if (Loader.isModLoaded(ModIds.EXNIHILO)) {
			if (Config.enableTurtleHammer) {
				KnightPeripherals.logger.info("Found Ex Nihilo, registering hammer peripheral");
				ComputerCraftAPI.registerTurtleUpgrade(new TurtleHammer());
				list.add(Reference.UPGRADE_HAMMER);
			}
			else {
				KnightPeripherals.logger.info("Skipping registering smashing turtle, upgrade disabled");
			}
		}
		else {
			KnightPeripherals.logger.info("Cannot find Ex Nihilo, skipping smashing turtle");
		}

		// Explosive Turtle
		if (Config.enableTurtleTnt) {
			ComputerCraftAPI.registerTurtleUpgrade(new TurtleTnt());
			list.add(Reference.UPGRADE_TNT);
		}
		else {
			KnightPeripherals.logger.info("Skipping registering explosive turtle, upgrade disabled");
		}

		// Ranged Turtle
		if (Config.enableTurtleBow) {
			ComputerCraftAPI.registerTurtleUpgrade(new TurtleBow());
			list.add(Reference.UPGRADE_BOW);
		}
		else {
			KnightPeripherals.logger.info("Skipping registering ranged turtle, upgrade disabled");
		}

		// Mining Laser Turtle
		if (Config.enableTurtleLaser) {
			ComputerCraftAPI.registerTurtleUpgrade(new TurtleLaser());
			list.add(Reference.UPGRADE_LASER);
		}
		else {
			KnightPeripherals.logger.info("Skipping registering mining laser turtle, upgrade disabled");
		}

		// Sensor Turtle
		if (Config.enableTurtleSensor) {
			ComputerCraftAPI.registerTurtleUpgrade(new TurtleSensor());
			list.add(Reference.UPGRADE_SENSOR);
		}
		else {
			KnightPeripherals.logger.info("Skipping registering sensor turtle, upgrade disabled");
		}
	}
}
