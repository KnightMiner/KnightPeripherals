package knightminer.knightperipherals.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import knightminer.knightperipherals.init.CreativeTab;
import knightminer.knightperipherals.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item {
	
	public ItemBase(){
		this.setCreativeTab(CreativeTab.tab);
	}
	
	@Override
    public Item setTextureName(String string)
    {
        this.iconString = Reference.RESOURCE_LOCATION + ":" + string;
        return this;
    }
}
