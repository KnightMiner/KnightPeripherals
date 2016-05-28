package knightminer.knightperipherals.turtles.peripherals.tasks;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleSide;
import knightminer.knightperipherals.util.FakePlayerProvider;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class TaskClawClick implements ILuaTask {

	// data to inherit from PeripheralClaw
	private ITurtleAccess turtle;
	private TurtleSide side;
	private World world;
	private EnumFacing direction;
	private BlockPos pos;
	private int fuelCost;
	private Boolean sneaking;

	public TaskClawClick(ITurtleAccess turtle, TurtleSide side, World world, EnumFacing direction, BlockPos pos,
	        Boolean sneaking, int fuelCost) {
		this.turtle = turtle;
		this.side = side;
		this.world = world;
		this.direction = direction;
		this.pos = pos;
		this.sneaking = sneaking;
		this.fuelCost = fuelCost;
	}

	@Override
	public Object[] execute() throws LuaException {

		// grab some additional data before we get started
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		EnumFacing face = direction.getOpposite();
		float[] clickPoint = TurtleUtil.getCenterOfSide(face);

		// find the currently selected item/stack
		IInventory inv = turtle.getInventory();
		int slot = turtle.getSelectedSlot();
		ItemStack stack = inv.getStackInSlot(slot);

		// if we have an ItemStack, get the item from that. We don't want a null
		// pointer error
		Item item = stack == null ? null : stack.getItem();

		// set up data for the fake player: ItemStack and position
		FakePlayer fakePlayer = FakePlayerProvider.get(turtle);
		fakePlayer.setCurrentItemOrArmor(0, stack);
		fakePlayer.setSneaking(sneaking);

		// queue event, and cancel if the event is canceled
		PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(fakePlayer, Action.RIGHT_CLICK_BLOCK,
		        turtle.getWorld(), pos, face);
		if (event.isCanceled()) {
			return new Object[] { false, "Click event canceled" };
		}

		// first try an item which is used before a block is activated
		Boolean clicked = false;
		if (item != null) {
			clicked = item.onItemUseFirst(stack, fakePlayer, world, pos, face, clickPoint[0], clickPoint[1],
			        clickPoint[2]);
		}

		// next, try the block directly
		if (!clicked) {
			clicked = block != null && block.onBlockActivated(world, pos, state, fakePlayer, face, clickPoint[0],
			        clickPoint[1], clickPoint[2]);
		}
		// if that did not work, try the item's main action
		if (!clicked && (item != null)) {
			clicked = item.onItemUse(stack, fakePlayer, world, pos, face, clickPoint[0], clickPoint[1], clickPoint[2]);
		}

		TurtleAnimation animation = side == TurtleSide.Left ? TurtleAnimation.SwingLeftTool
		        : TurtleAnimation.SwingRightTool;
		turtle.playAnimation(animation);

		// if the ItemStack changed, replace it with the new version
		ItemStack newStack = fakePlayer.getCurrentEquippedItem();
		if (newStack == null || newStack.stackSize == 0) {
			inv.setInventorySlotContents(slot, null);
		}
		else if (newStack != stack) {
			inv.setInventorySlotContents(slot, newStack);
		}

		// If enabled, remove some fuel
		// We already validated it earlier
		if (fuelCost > 0 && turtle.isFuelNeeded()) {
			turtle.consumeFuel(fuelCost);
		}

		return new Object[] { true, clicked };
	}

}
