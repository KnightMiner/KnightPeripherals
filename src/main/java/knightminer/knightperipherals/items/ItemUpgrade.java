package knightminer.knightperipherals.items;

import java.util.List;

import knightminer.knightperipherals.init.CreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {

	public static String[] names = new String[] { "turtle_claw", "mining_laser", "sensor" };

	public ItemUpgrade() {
		this.setCreativeTab(CreativeTab.tab);
		this.setUnlocalizedName("upgrade");
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int damage = stack.getItemDamage();
		if (damage >= names.length)
			damage = 0;

		return this.getUnlocalizedName() + "." + names[damage];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		for (int i = 0; i < names.length; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}
}
