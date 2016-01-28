package knightminer.knightperipherals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.ComputerCraftAPI;
import knightminer.knightperipherals.init.CreativeTab;
import knightminer.knightperipherals.init.ModIcons;
import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.init.Turtles;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.turtles.TurtleClaw;
import knightminer.knightperipherals.turtles.TurtleExNihiloHammer;
import net.minecraftforge.common.MinecraftForge;

@Mod( modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:ComputerCraft;after:exnihilo" )
public class KnightPeripherals {
	
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event ) {
		Config.register(event.getSuggestedConfigurationFile());
        
		ModItems.register();
		ModItems.addRecipes();
        
		// load block icons for turtles
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT)
		{
			MinecraftForge.EVENT_BUS.register(new ModIcons());
		}
		
		// keep track of entity item drops
		MinecraftForge.EVENT_BUS.register(new TurtleExNihiloHammer());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Proxy, TileEntity, entity, GUI, Packet Registering

		Turtles.register();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Registry checking
	}

}
