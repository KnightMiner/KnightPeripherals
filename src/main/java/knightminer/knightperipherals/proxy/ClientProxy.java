package knightminer.knightperipherals.proxy;

import knightminer.knightperipherals.client.ModelLoader;
import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenders() {
		registerItemRender(ModItems.turtleUpgrade, 0, "turtle_claw");
		registerItemRender(ModItems.turtleUpgrade, 1, "mining_laser");
		registerItemRender(ModItems.turtleUpgrade, 2, "sensor");

		MinecraftForge.EVENT_BUS.register(new ModelLoader());
	}

	private static void registerItemRender(Item item, int meta, String name) {
		ModelResourceLocation location = new ModelResourceLocation(Reference.MOD_ID + ":" + name, "inventory");
		ModelBakery.registerItemVariants(item, location);
		TurtleUtil.getMesher().register(item, meta, location);
	}
}
