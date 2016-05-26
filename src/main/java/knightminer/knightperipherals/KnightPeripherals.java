package knightminer.knightperipherals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.init.Turtles;
import knightminer.knightperipherals.proxy.CommonProxy;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.KnightPeripheralsPacketHandler;
import knightminer.knightperipherals.util.LuaTimer;
import knightminer.knightperipherals.util.TurtleDropCollector;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:ComputerCraft;after:exnihilo;after:NotEnoughItems;")
public class KnightPeripherals {
	public static final Logger logger = LogManager.getLogger(Reference.MOD_NAME);

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.register(event.getSuggestedConfigurationFile());
		KnightPeripheralsPacketHandler.init();

		ModItems.register();
		ModItems.addRecipes();

		proxy.registerIcons();

		// keep track of entity item drops to place in turtle inventories
		MinecraftForge.EVENT_BUS.register(new TurtleDropCollector());
		// delays for computers
		FMLCommonHandler.instance().bus().register(new LuaTimer());
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
