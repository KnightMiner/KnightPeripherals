package knightminer.knightperipherals.proxy;

import knightminer.knightperipherals.init.ModItems;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenders()
	{
		ModItems.registerRenders();
	}
}
