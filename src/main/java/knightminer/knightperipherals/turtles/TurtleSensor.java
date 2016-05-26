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
import knightminer.knightperipherals.turtles.peripherals.PeripheralSensor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleSensor implements ITurtleUpgrade {
	private static final ItemStack item = new ItemStack(ModItems.turtleUpgrade, 1, 1);

	@Override
	public int getUpgradeID() {
		return Reference.UPGRADE_SENSOR;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "turtleUpgrade.sensor";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.craftTurtleSensor) {
			return item.copy();
		}
		else {
			KnightPeripherals.logger.info("Recipe for sensor turtle disabled");
			return null;
		}
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralSensor(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		if (side == TurtleSide.Left) {
			return ModIcons.turtleSensorLeft;
		}
		else {
			return ModIcons.turtleSensorRight;
		}
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}
}
