package knightminer.knightperipherals.turtles.peripherals.tasks;

import org.lwjgl.util.vector.Vector3f;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import knightminer.knightperipherals.util.FakePlayerProvider;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class TaskClawClick implements ILuaTask {
	
	// data to inherit from PeripheralClaw
	ITurtleAccess turtle;
	World world;
	int direction, x, y, z, fuelCost;
	Boolean sneaking;
	
	public TaskClawClick(ITurtleAccess turtle, World world, int direction, int x, int y, int z, Boolean sneaking, int fuelCost) {
		this.turtle = turtle;
		this.world = world;
		this.direction = direction;
		this.x = x;
		this.y = y;
		this.z = z;
		this.sneaking = sneaking;
		this.fuelCost = fuelCost;
	}

	@Override
	public Object[] execute()
	throws LuaException {
		
		// grab some additional data before we get started
		Block block = world.getBlock(x, y, z);
		int side = Facing.oppositeSide[direction];
		Vector3f clickPoint = TurtleUtil.getCenterOfSide( side );
		
		// find the currently selected item/stack
		IInventory inv = turtle.getInventory();
		int slot = turtle.getSelectedSlot();
		ItemStack stack = inv.getStackInSlot( slot );
		
		// if we have an itemstack, get the item from that. We don't want a null pointer error
		Item item = stack == null ? null : stack.getItem();
		
		// set up data for the fake player: itemstack and position
		FakePlayer fakePlayer = FakePlayerProvider.get( turtle );
		fakePlayer.setCurrentItemOrArmor(0, stack);
		fakePlayer.setSneaking(sneaking);
		TurtleUtil.setPlayerPosition(fakePlayer, turtle);
		
		// queue event, and cancel if the event is canceled
		PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(fakePlayer, Action.RIGHT_CLICK_BLOCK, x, y, z, side, turtle.getWorld());
		if (event.isCanceled()) {
			return new Object[]{ false, "Click event canceled" };
		}
		
		// first try an item which is used before a block is activated
		Boolean clicked = false;
		if (item != null) {
			clicked = item.onItemUseFirst(stack, fakePlayer, world, x, y, z, side, clickPoint.getX(), clickPoint.getY(), clickPoint.getZ());
		}
		
		// next, try the block directly
		if (!clicked)
		{
			clicked = block != null && block.onBlockActivated(world, x, y, z, fakePlayer, side, clickPoint.getX(), clickPoint.getY(), clickPoint.getZ());
		}
		// if that did not work, try the item's main action
		if (!clicked && (item != null)) {
			clicked = item.onItemUse(stack, fakePlayer, world, x, y, z, side, clickPoint.getX(), clickPoint.getY(), clickPoint.getZ());
		}
		
		// If enabled, remove some fuel
		// We already validated it earlier
		if (fuelCost > 0 && turtle.isFuelNeeded())
		{
			turtle.consumeFuel(fuelCost);
		}
		
		return new Object[]{ true, clicked };
	}

}
