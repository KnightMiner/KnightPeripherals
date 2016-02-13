package knightminer.knightperipherals.turtles;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.turtles.peripherals.PeripheralClaw;
import knightminer.knightperipherals.util.ModLogger;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurtleClaw implements ITurtleUpgrade
{
	private static final ItemStack stack = new ItemStack(ModItems.turtleClaw, 1);
	
	@Override
	public int getLegacyUpgradeID()
	{
		return Reference.UPGRADE_LEGACY_CLAW;
	}

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.UPGRADE_CLAW);
	}

	@Override
	public String getUnlocalisedAdjective()
	{
		return "turtleUpgrade.claw";
	}

	@Override
	public TurtleUpgradeType getType()
	{
		return TurtleUpgradeType.Peripheral;
	}
	
	@Override
	public ItemStack getCraftingItem()
	{
		if (Config.craftTurtleClaw)
		{
			return stack;
		} else
		{
			ModLogger.logger.info("Recipe for clicking turtle disabled");
			return null;
		}
	}

    @SideOnly( Side.CLIENT )
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) {
		IBakedModel model = TurtleUtil.getMesher().getItemModel(stack);
		Matrix4f transform = TurtleUtil.getTransforms(side);
		return Pair.of(model, transform);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side)
	{
		return new PeripheralClaw(turtle, side);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction)
	{
		return null;
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side)
	{

	}
	
}
