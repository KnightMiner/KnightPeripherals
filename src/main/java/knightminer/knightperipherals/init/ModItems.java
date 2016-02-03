package knightminer.knightperipherals.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import knightminer.knightperipherals.items.ItemBase;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModItems {
	public static Item turtleClaw;

	public static void register()
	{
		if (Config.enableTurtleClaw)
		{
			turtleClaw = new ItemBase().setUnlocalizedName("turtleClaw");
			GameRegistry.registerItem(turtleClaw, "turtle_claw");
		}
	}
	
	public static void registerRenders()
	{
		if (Config.enableTurtleClaw)
		{
			registerRender(turtleClaw, "turtle_claw");
		}
	}
	
	private static void registerRender(Item item, String name)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + name, "inventory"));
	}
	
	public static void addRecipes()
	{
		if (Config.enableTurtleClaw && Config.craftTurtleClaw)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(turtleClaw, 1), "I I", "RQR", " I ", 'I', "ingotIron", 'R', "dustRedstone", 'Q', "gemQuartz"));
		}
	}
}
