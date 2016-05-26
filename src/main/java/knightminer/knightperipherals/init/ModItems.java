package knightminer.knightperipherals.init;

import cpw.mods.fml.common.registry.GameRegistry;
import knightminer.knightperipherals.items.ItemUpgrade;
import knightminer.knightperipherals.reference.Config;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModItems {
	/**
	 * Contains all the upgrade items used by KnightPeripherals. Current metadatas:
	 * <li>0: Turtle Claw</li>
	 * <li>1: Mining Laser</li>
	 * <li>2: Sensor</li>
	 */
	public static Item turtleUpgrade;

	public static void register() {
		turtleUpgrade = new ItemUpgrade();
		// legacy name, yes the mod is less than a year old, I did not think ahead good
		GameRegistry.registerItem(turtleUpgrade, "turtle_claw");
	}

	public static void addRecipes() {
		if (Config.craftTurtleClaw)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(turtleUpgrade, 1, 0), "I I", "RQR", " I ",
					'I', "ingotIron", 'R', "dustRedstone", 'Q', "gemQuartz"));

		if (Config.craftTurtleLaser)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(turtleUpgrade, 1, 1), " D ", "IGI", "IRI",
					'I', "ingotIron", 'G', "ingotGold", 'R', "dustRedstone", 'D', "blockDiamond"));

		if (Config.craftTurtleSensor)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(turtleUpgrade, 1, 2), "I I", " I ", "GNG",
					'I', "ingotIron", 'G', "ingotGold", 'N', Blocks.noteblock));
	}
}
