package knightminer.knightperipherals.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import knightminer.knightperipherals.reference.ModIds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CreativeTab {
	public static final CreativeTabs tab = new CreativeTabs("tabKnightPeripherals")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return ModItems.turtleClaw;
		}

		// add turtle upgrades to the creative tab
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllReleventItems(List list)
		{
			// keep all normal items
			super.displayAllReleventItems(list);
			
			// list of upgrade IDs
			ArrayList<String> upgrades = Turtles.list;
			
			if (!upgrades.isEmpty())
			{
				// turtle IDs
				ItemStack turtle = GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT_TURTLE, 0, 1, null);
				ItemStack turtleAdv = GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT_TURTLEADV, 0, 1, null);
				
				// add normal turtles
				for (String s: upgrades)
				{
					ItemStack upgrade = turtle.copy();
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("leftUpgrade", s);
					upgrade.setTagCompound(tag);
					list.add(upgrade);
				}
				
				// add advance turtles
				for (String s: upgrades)
				{
					ItemStack upgrade = turtleAdv.copy();
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("leftUpgrade", s);
					upgrade.setTagCompound(tag);
					list.add(upgrade);
				}
			}
		}
		
	};
}
