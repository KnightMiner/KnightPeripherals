package knightminer.knightperipherals.util;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurtleUtil {
	
	/**
	 * Sets the position of the fake player to that of a turtle
	 * @param player the player whose position will change
	 * @param turtle the turtle for the new coordinates
	 */
	public static void setPlayerPosition( FakePlayer player, ITurtleAccess turtle )
	{
		player.setPosition(turtle.getPosition().getX(), turtle.getPosition().getY(), turtle.getPosition().getZ());
	}
	
	/**
	 * Adds an item to a turtle's inventory by finding the first available slot
	 * Credit: austinv11
	 * Updated to 1.8 on 2/2/2016
	 * @param turtle The turtle to add items to
	 * @param stack ItemStack to add to the inventory
	 */
	public static void addToInv(ITurtleAccess turtle, ItemStack stack) {
		boolean drop = true;
		IInventory inv = turtle.getInventory();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack currentStack = inv.getStackInSlot(i);
			if (currentStack == null) {
				inv.setInventorySlotContents(i, stack);
				drop = false;
				break;
			}
			if (currentStack.isStackable() && currentStack.isItemEqual(stack)) {
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
			EnumFacing dir = turtle.getDirection();
			BlockPos pos = turtle.getPosition().offset(dir);
			turtle.getWorld().spawnEntityInWorld(new EntityItem(turtle.getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack.copy()));
		}
	}
	
	/**
	 * Adds a list of items to the turtle's inventory
	 * Credit: austinv11
	 * @param list
	 * @param turtle
	 */
	public static void addItemListToInv(List<ItemStack> list, ITurtleAccess turtle) {
		for (ItemStack item : list) {
			addToInv(turtle, item);
		}
	}
	
	/**
	 * Gets the closest entity to a turtle
	 * Modified from functions by austinv11 on 2/2/2016
	 * @param turtle Turtle calling the command
	 * @param player Fake player
	 * @param dir EnumFacing of the turtle
	 * @return Nearest entity to the turtle
	 */
	public static Entity getClosestEntity(ITurtleAccess turtle, FakePlayer player, EnumFacing dir) {
		BlockPos pos = turtle.getPosition().offset(dir);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		AxisAlignedBB box = new AxisAlignedBB(x, y, z, x+1.0D, y+1.0D, z+1.0D);
		List<Entity> entities = turtle.getWorld().getEntitiesWithinAABBExcludingEntity(player, box);
		
		Vec3 from = new Vec3(player.posX, player.posY, player.posZ);
		Entity returnVal = null;
		double lastDistance = Double.MAX_VALUE;
		for (Entity entity : entities) {
			Vec3 to = new Vec3(entity.posX, entity.posY, entity.posZ);
			if (to.distanceTo(from) < lastDistance)
				returnVal = entity;
		}
		return returnVal;
	}
	
	/**
	 * Turns an entities drops into ItemStacks
	 * Credit: austinv11
	 * @param drops Items that dropped from the entity
	 * @return Entity drops as ItemStacks
	 */
	public static List<ItemStack> entityItemsToItemStack(List<EntityItem> drops) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for (EntityItem e : drops) {
			stacks.add(e.getEntityItem());
		}
		return stacks;
	}
	
	/**
	 * Finds the center to use for clicking a block
	 * Credit: Cypher121, modified on 2/5/2016
	 * @param dir EnumFacing which is being clicked from
	 * @return a Vector3f of the location
	 */
	public static float[] getCenterOfSide(EnumFacing dir) {
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

	@SideOnly(Side.CLIENT)
	private static ItemModelMesher mesher;
	/**
	 * Gets the item model mesher
	 * Credit: SquidDev
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public static ItemModelMesher getMesher() {
		ItemModelMesher instance = mesher;
		if (instance == null) instance = mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		return instance;
	}
	
	/**
	 * Get item transforms data
	 * Credit: SquidDev
	 * @param side TurtleSide containing the tool or model
	 * @return
	 */
	public static Matrix4f getTransforms(TurtleSide side)
	{
		float xOffset = side == TurtleSide.Left ? -0.40625F : 0.40625F;
		return new Matrix4f(0.0F, 0.0F, -1.0F, 1.0F + xOffset, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
}
