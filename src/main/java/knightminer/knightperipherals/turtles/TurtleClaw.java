package knightminer.knightperipherals.turtles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import knightminer.knightperipherals.KnightPeripherals;
import knightminer.knightperipherals.init.ModIcons;
import knightminer.knightperipherals.init.ModItems;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.turtles.peripherals.PeripheralClaw;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleClaw implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return Reference.UPGRADE_CLAW;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "turtleUpgrade.claw";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.craftTurtleClaw) {
			return new ItemStack(ModItems.turtleUpgrade, 1, 0);
		}
		else {
			KnightPeripherals.logger.info("Recipe for clicking turtle disabled");
			return null;
		}
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralClaw(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		if (side == TurtleSide.Left) {
			return ModIcons.turtleClawLeft;
		}
		else {
			return ModIcons.turtleClawRight;
		}
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}

}
