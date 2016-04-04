package knightminer.knightperipherals.util;

import java.util.ArrayList;
import java.util.List;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

public class TurtleUtil {
	
	/**
	 * Adds an item to a turtle's inventory by finding the first available slot
	 * Credit: austinv11, modified 4/3/2016
	 * @param turtle The turtle to add items to
	 * @param stack ItemStack to add to the inventory
	 */
	public static void addToInv(ITurtleAccess turtle, ItemStack stack) {
		boolean drop = true;
		IInventory inv = turtle.getInventory();
		ChunkCoordinates coords = turtle.getPosition();
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack currentStack = inv.getStackInSlot(i);
			if (currentStack == null) {
				inv.setInventorySlotContents(i, stack);
				drop = false;
				break;
			}
			if (currentStack.isStackable() && currentStack.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(currentStack, stack)) {
				int space = currentStack.getMaxStackSize() - currentStack.stackSize;
				if (stack.stackSize > space) {
					currentStack.stackSize = currentStack.getMaxStackSize();
					stack.stackSize -= space;
					drop = true;
				} else {
					currentStack.stackSize += stack.stackSize;
					stack.stackSize = 0;
					drop = false;
					break;
				}
			}
		}
		if (drop) {
			int dir = turtle.getDirection();
			turtle.getWorld().spawnEntityInWorld(new EntityItem(turtle.getWorld(), coords.posX+Facing.offsetsXForSide[dir], coords.posY+Facing.offsetsYForSide[dir]+1, coords.posZ+Facing.offsetsZForSide[dir], stack.copy()));
		}
	}
	
	/**
	 * Adds a list of items to the turtle's inventory
	 * Credit: austinv11
	 * @param items
	 * @param turtle
	 */
	public static void addItemListToInv(List<ItemStack> items, ITurtleAccess turtle) {
		for (ItemStack item : items) {
			addToInv(turtle, item);
		}
	}
	
	/**
	 * Gets the closest entity to a turtle
	 * Modified from functions by austinv11 on 1/28/2016
	 * @param turtle Turtle calling the command
	 * @param player Fake player
	 * @param dir Direction the turtle is facing
	 * @return Nearest entity to the turtle
	 */
	@SuppressWarnings("unchecked")
	public static Entity getClosestEntity(ITurtleAccess turtle, FakePlayer player, int dir) {
		int x = turtle.getPosition().posX+Facing.offsetsXForSide[dir];
		int y = turtle.getPosition().posY+Facing.offsetsYForSide[dir];
		int z = turtle.getPosition().posZ+Facing.offsetsZForSide[dir];
		
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x+1.0D, y+1.0D, z+1.0D);
		List<Entity> entities = turtle.getWorld().getEntitiesWithinAABBExcludingEntity(player, box);
		
		Vec3 from = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
		Entity returnVal = null;
		double lastDistance = Double.MAX_VALUE;
		for (Entity entity : entities) {
			Vec3 to = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
			if (to.distanceTo(from) < lastDistance)
				returnVal = entity;
		}
		return returnVal;
	}
	
	/**
	 * Turns an entities drops into TtemStacks
	 * Credit: austinv11
	 * @param entities Items that dropped from the entity
	 * @return Entity drops as ItemStacks
	 */
	public static ArrayList<ItemStack> entityItemsToItemStack(ArrayList<EntityItem> entities) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for (EntityItem e : entities) {
			stacks.add(e.getEntityItem());
		}
		return stacks;
	}
	
	/**
	 * Finds the center to use for clicking a block
	 * Credit: Cypher121, modified on 2/5/2016
	 * @param dir ForgeDirection which is being clicked from
	 * @return a float array of the location
	 */
	public static float[] getCenterOfSide(ForgeDirection dir) {
		switch (dir) {
			case UP:
  			  return new float[]{ 0.5f, 1f, 0.5f };
			case DOWN:
				return new float[]{ 0.5f, 0f, 0.5f};
			case NORTH:
				return new float[]{ 0.5f, 0.5f, 0f};
			case SOUTH:
				return new float[]{ 0.5f, 0.5f, 1f};
			case WEST:
				return new float[]{ 0f, 0.5f, 0.5f};
			case EAST:
				return new float[]{ 1f, 0.5f, 0.5f};
		default:
			return null;
		}
	}

	/**
	 * Finds the center to use for clicking a block
	 * Credit: Cypher121
	 * @param side integer representing the side which is being clicked from
	 * @return a float array of the location
	 */
	public static float[] getCenterOfSide(int side) {
		return getCenterOfSide(ForgeDirection.getOrientation(side));
	}
	
}
