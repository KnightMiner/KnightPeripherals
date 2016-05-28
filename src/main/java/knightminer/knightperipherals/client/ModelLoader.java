package knightminer.knightperipherals.client;

import java.io.IOException;

import com.google.common.base.Function;

import knightminer.knightperipherals.KnightPeripherals;
import knightminer.knightperipherals.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelLoader {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) {
		loadModel(event, "turtle_tnt_left");
		loadModel(event, "turtle_tnt_right");
	}

	/**
	 * @author SquidDev
	 */
	@SideOnly(Side.CLIENT)
	private void loadModel(ModelBakeEvent event, String name) {
		try {
			IModel e = event.modelLoader.getModel(new ResourceLocation(Reference.RESOURCE_LOCATION, "block/" + name));
			IFlexibleBakedModel bakedModel = e.bake(e.getDefaultState(), DefaultVertexFormats.ITEM,
			        new Function<ResourceLocation, TextureAtlasSprite>() {
				        @Override
				        public TextureAtlasSprite apply(ResourceLocation location) {
					        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
				        }
			        });
			event.modelRegistry.putObject(
			        new ModelResourceLocation(Reference.RESOURCE_LOCATION + ":" + name, "inventory"), bakedModel);
		}
		catch (IOException e) {
			KnightPeripherals.logger.error("Could not load model: " + name, e);
		}
	}
}
