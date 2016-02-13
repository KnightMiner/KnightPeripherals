package knightminer.knightperipherals.proxy;

import knightminer.knightperipherals.client.ModelLoader;
import knightminer.knightperipherals.init.ModItems;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenders()
	{
		ModItems.registerRenders();
		MinecraftForge.EVENT_BUS.register(new ModelLoader());
	}
}
