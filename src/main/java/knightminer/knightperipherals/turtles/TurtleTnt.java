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
import knightminer.knightperipherals.KnightPeripherals;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.turtles.peripherals.PeripheralTnt;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurtleTnt implements ITurtleUpgrade {
	@SideOnly(Side.CLIENT)
	private IBakedModel modelLeft, modelRight;

	@Override
	public int getLegacyUpgradeID() {
		return Reference.UPGRADE_LEGACY_TNT;
	}

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.UPGRADE_TNT);
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "turtleUpgrade.tnt";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.craftTurtleTnt) {
			return new ItemStack(Blocks.tnt, 1);
		}
		else {
			KnightPeripherals.logger.info("Recipe for self-destructing turtle disabled");
			return null;
		}
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralTnt(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) {
		if (modelLeft == null) {
			ModelManager manager = TurtleUtil.getMesher().getModelManager();
			modelLeft = manager
			        .getModel(new ModelResourceLocation(Reference.RESOURCE_LOCATION + ":turtle_tnt_left", "inventory"));
			modelRight = manager.getModel(
			        new ModelResourceLocation(Reference.RESOURCE_LOCATION + ":turtle_tnt_right", "inventory"));
		}
		return Pair.of(side == TurtleSide.Left ? modelLeft : modelRight, null);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}
}
