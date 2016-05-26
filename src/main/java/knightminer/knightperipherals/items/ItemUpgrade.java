package knightminer.knightperipherals.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import knightminer.knightperipherals.init.CreativeTab;
import knightminer.knightperipherals.reference.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemUpgrade extends Item {

	public IIcon[] icons;
	private static String[] names = new String[] { "turtle_claw", "mining_laser", "sensor" };

	public ItemUpgrade() {
		this.setCreativeTab(CreativeTab.tab);
		this.setUnlocalizedName("upgrade");
		this.setHasSubtypes(true);
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

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.icons = new IIcon[names.length];

		for (int i = 0; i < this.icons.length; ++i) {
			this.icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + names[i]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.icons[meta];
	}
}
