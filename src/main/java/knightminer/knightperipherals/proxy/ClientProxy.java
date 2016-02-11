package knightminer.knightperipherals.proxy;

import knightminer.knightperipherals.init.ModIcons;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerIcons()
	{
		MinecraftForge.EVENT_BUS.register(new ModIcons());
	}
}
