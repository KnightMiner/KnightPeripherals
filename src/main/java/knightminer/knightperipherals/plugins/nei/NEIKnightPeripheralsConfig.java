package knightminer.knightperipherals.plugins.nei;

import java.util.List;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.minecraftforge.fml.common.registry.GameRegistry;
import knightminer.knightperipherals.init.Turtles;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.ModLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NEIKnightPeripheralsConfig implements IConfigureNEI  {

	@Override
	public void loadConfig() {
		ModLogger.logger.info("Loading NEI pluggin");
		List<String> upgrades = Turtles.list;
		
		if (!upgrades.isEmpty())
		{
			ItemStack turtle = GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT_TURTLE, 0, 1, null);
			ItemStack turtleAdv = GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT_TURTLEADV, 0, 1, null);

			// add normal turtles
			for (String s: upgrades)
			{
				ItemStack upgrade = turtle.copy();
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("leftUpgrade", s);
				upgrade.setTagCompound(tag);
				API.addItemListEntry(upgrade);
			}
			
			// add advance turtles
			for (String s: upgrades)
			{
				ItemStack upgrade = turtleAdv.copy();
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("leftUpgrade", s);
				upgrade.setTagCompound(tag);
				API.addItemListEntry(upgrade);
			}
		}
	}

	@Override
	public String getName() {
		return Reference.MOD_ID;
	}

	@Override
	public String getVersion() {
		return Reference.VERSION;
	}

}
