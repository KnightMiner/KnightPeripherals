package knightminer.knightperipherals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.init.Turtles;
import knightminer.knightperipherals.proxy.CommonProxy;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.LuaTimer;
import knightminer.knightperipherals.util.TurtleDropCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:ComputerCraft;after:exnihilo;")
public class KnightPeripherals {

	public static final Logger logger = LogManager.getLogger(Reference.MOD_NAME);

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.register(event.getSuggestedConfigurationFile());

		ModItems.register();
		ModItems.addRecipes();

		// keep track of entity item drops
		MinecraftForge.EVENT_BUS.register(new TurtleDropCollector());

		// lua timers
		MinecraftForge.EVENT_BUS.register(new LuaTimer());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Proxy, TileEntity, entity, GUI, Packet Registering

		// load block icons for turtles
		proxy.registerRenders();
		Turtles.register();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Registry checking
	}

}
