package knightminer.knightperipherals.turtles.peripherals.tasks;

import dan200.computercraft.api.lua.ILuaTask;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import knightminer.knightperipherals.util.FakePlayerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class TaskTntDrop implements ILuaTask {

	private ITurtleAccess turtle;
	private World world;
	private int x, y, z, slot;
	private IInventory inv;
	private ItemStack stack;

	public TaskTntDrop(ITurtleAccess turtle, World world, int x, int y, int z, IInventory inv, ItemStack stack,
	        int slot) {
		this.turtle = turtle;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.inv = inv;
		this.stack = stack;
		this.slot = slot;
	}

	@Override
	public Object[] execute() throws LuaException {
		// blame this guy for the TNT drop
		FakePlayer fakePlayer = FakePlayerProvider.get(turtle);

		// decrease the stack size, deleting the stack if needed
		stack.stackSize -= 1;
		if (stack.stackSize == 0) {
			inv.setInventorySlotContents(slot, null);
		}

		// summon the TNT
		Entity tnt = new EntityTNTPrimed(world, x + 0.5D, y + 0.5D, z + 0.5D, fakePlayer);
		if (!world.isRemote)
			world.spawnEntityInWorld(tnt);

		return new Object[] { true };
	}

}
