package knightminer.knightperipherals.init;

import cpw.mods.fml.common.registry.GameRegistry;
import knightminer.knightperipherals.items.ItemBase;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModItems {
	public static Item turtleClaw;

	public static void register()
	{
		if (Config.enableTurtleClaw)
		{
			turtleClaw = new ItemBase().setUnlocalizedName("turtleClaw").setTextureName("turtle_claw");
			GameRegistry.registerItem(turtleClaw, "turtle_claw");
		}
	}
	
	public static void addRecipes()
	{
		if (Config.enableTurtleClaw && Config.craftTurtleClaw)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(turtleClaw, 1), "I I", "RQR", " I ", 'I', "ingotIron", 'R', "dustRedstone", 'Q', "gemQuartz"));
		}
	}
}
