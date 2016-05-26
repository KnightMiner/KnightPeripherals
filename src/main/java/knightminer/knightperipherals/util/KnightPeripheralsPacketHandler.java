package knightminer.knightperipherals.util;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import knightminer.knightperipherals.reference.Reference;

public class KnightPeripheralsPacketHandler {
	private static int id = 0;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	public static void init() {
		// used to add particles to the turtle for the mining laser beam and
		// alike
		INSTANCE.registerMessage(TurtleParticleMessage.TurtleParticleHandler.class, TurtleParticleMessage.class, id++,
		        Side.CLIENT);
	}
}
