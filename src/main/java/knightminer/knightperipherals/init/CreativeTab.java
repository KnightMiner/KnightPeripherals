package knightminer.knightperipherals.init;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import knightminer.knightperipherals.reference.ModIds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CreativeTab {
	public static final CreativeTabs tab = new CreativeTabs("tabKnightPeripherals") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return ModItems.turtleUpgrade;
		}

		// add turtle upgrades to the creative tab
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllReleventItems(List list) {
			// keep all normal items
			super.displayAllReleventItems(list);

			// list of upgrade IDs
			List<Integer> upgrades = Turtles.list;

			if (!upgrades.isEmpty()) {
				// turtle IDs
				ItemStack turtle = GameRegistry.findItemStack(ModIds.COMPUTERCRAFT, ModIds.COMPUTERCRAFT_TURTLE, 1);
				ItemStack turtleAdv = GameRegistry.findItemStack(ModIds.COMPUTERCRAFT, ModIds.COMPUTERCRAFT_TURTLEADV,
	                    1);

				// add normal turtles
				for (int i : upgrades) {
					ItemStack upgrade = turtle.copy();
					upgrade.stackTagCompound = new NBTTagCompound();
					upgrade.stackTagCompound.setShort("leftUpgrade", (short) i);
					list.add(upgrade);
				}

				// add advance turtles
				for (int i : upgrades) {
					ItemStack upgrade = turtleAdv.copy();
					upgrade.stackTagCompound = new NBTTagCompound();
					upgrade.stackTagCompound.setShort("leftUpgrade", (short) i);
					list.add(upgrade);
				}
			}
		}

	};
}
