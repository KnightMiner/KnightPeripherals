package knightminer.knightperipherals.plugins.nei;

import java.util.List;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.registry.GameRegistry;
import knightminer.knightperipherals.init.Turtles;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.ModLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NEIKnightPeripheralsConfig implements IConfigureNEI  {

	@Override
	public void loadConfig() {
		ModLogger.logger.info("Loading NEI plugin");
		List<Integer> upgrades = Turtles.list;
		
		if (!upgrades.isEmpty())
		{
			ItemStack turtle = GameRegistry.findItemStack(ModIds.COMPUTERCRAFT, ModIds.COMPUTERCRAFT_TURTLE, 1);
			ItemStack turtleAdv = GameRegistry.findItemStack(ModIds.COMPUTERCRAFT, ModIds.COMPUTERCRAFT_TURTLEADV, 1);

			// add normal turtles
			for (int i: upgrades)
			{
				ItemStack upgrade = turtle.copy();
				upgrade.stackTagCompound = new NBTTagCompound();
				upgrade.stackTagCompound.setShort("leftUpgrade", (short) i);
				API.addItemListEntry(upgrade);
			}
			
			// add advance turtles
			for (int i: upgrades)
			{
				ItemStack upgrade = turtleAdv.copy();
				upgrade.stackTagCompound = new NBTTagCompound();
				upgrade.stackTagCompound.setShort("leftUpgrade", (short) i);
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
