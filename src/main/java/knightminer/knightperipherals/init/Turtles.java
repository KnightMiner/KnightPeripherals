package knightminer.knightperipherals.init;

import java.util.ArrayList;

import cpw.mods.fml.common.Loader;
import dan200.computercraft.api.ComputerCraftAPI;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.turtles.*;
import knightminer.knightperipherals.util.ModLogger;

public class Turtles {
	public static ArrayList<Integer> list = new ArrayList<Integer>();
	
	public static void register()
	{
		if (Config.enableTurtleClaw)
		{
			ComputerCraftAPI.registerTurtleUpgrade( new TurtleClaw() );
			list.add( Reference.UPGRADE_CLAW);
		} else
		{
			ModLogger.logger.info( "Skipping registering clicking turtle, upgrade disabled");
		}
		
		if ( Loader.isModLoaded( ModIds.EXNIHILO ) ) {
			if (Config.enableTurtleHammer)
			{
				ModLogger.logger.info("Found Ex Nihilo, registering hammer peripheral");
				ComputerCraftAPI.registerTurtleUpgrade( new TurtleExNihiloHammer() );
				list.add( Reference.UPGRADE_HAMMER);
			} else
			{
				ModLogger.logger.info("Skipping registering smashing turtle, upgrade disabled");
			}
		} else {
			ModLogger.logger.info("Cannot find Ex Nihilo, skipping smashing turtle");
		}
		
		if (Config.enableTurtleTnt)
		{
			ComputerCraftAPI.registerTurtleUpgrade( new TurtleTnt() );
			list.add( Reference.UPGRADE_TNT);
		} else
		{
			ModLogger.logger.info( "Skipping registering explosive turtle, upgrade disabled");
		}
		
		if (Config.enableTurtleBow)
		{
			ComputerCraftAPI.registerTurtleUpgrade( new TurtleBow() );
			list.add( Reference.UPGRADE_BOW);
		} else
		{
			ModLogger.logger.info( "Skipping registering ranged turtle, upgrade disabled");
		}
	}
}
