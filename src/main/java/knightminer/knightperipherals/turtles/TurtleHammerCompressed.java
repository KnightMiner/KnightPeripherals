package knightminer.knightperipherals.turtles;

import java.util.Collection;

import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.registries.helpers.Smashable;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.ModIds;
import knightminer.knightperipherals.reference.Reference;
import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class TurtleHammerCompressed extends TurtleHammer {

	private static final Item item = GameRegistry.findItem( ModIds.EX_COMPRESSUM, ModIds.EX_COMPRESSUM_HAMMER );
	
	// Itemstack for crafting/display
	@Override
	protected Item getItem()
	{
		return item;
	}
	
	// Reward list
	protected Collection<Smashable> getRewards(Block block, int meta)
	{
		return CompressedHammerRegistry.getRewards(block, meta);
	}

	// Config boolean
	@Override
	protected boolean canCraft()
	{
		return Config.craftTurtleCompressedHammer;
	}
	
	@Override
	public int getUpgradeID()
	{
		return Reference.UPGRADE_HAMMER_COMPRESSED;
	}

	@Override
	public String getUnlocalisedAdjective()
	{
		return "turtleUpgrade.hammerCompressed";
	}
}
