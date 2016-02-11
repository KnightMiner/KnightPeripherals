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
import knightminer.knightperipherals.init.ModIcons;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.FakePlayerProvider;
import knightminer.knightperipherals.util.ModLogger;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class TurtleBow implements ITurtleUpgrade
{
	
	@Override
	public int getUpgradeID()
	{
		return Reference.UPGRADE_BOW;
	}

	@Override
	public String getUnlocalisedAdjective()
	{
		return "turtleUpgrade.bow";
	}

	@Override
	public TurtleUpgradeType getType()
	{
		return TurtleUpgradeType.Tool;
	}
	
	@Override
	public ItemStack getCraftingItem()
	{
		if (Config.craftTurtleBow)
		{
			return new ItemStack(Items.bow, 1);
		} else
		{
			ModLogger.logger.info("Recipe for ranged turtle disabled");
			return null;
		}
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side)
	{
		return null;
	}

	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction)
	{
		switch (verb)
		{
			case Attack:
				World world = turtle.getWorld();
				// return 0 for down, 1 for up, or turtle facing otherwise
				int xOffset = Facing.offsetsXForSide[direction];
				int yOffset = Facing.offsetsYForSide[direction];
				int zOffset = Facing.offsetsZForSide[direction];
				
				int x = turtle.getPosition().posX + xOffset;
				int y = turtle.getPosition().posY + yOffset;
				int z = turtle.getPosition().posZ + zOffset;
				
				// only fire the arrow into air or a liquid
				if (!world.isAirBlock(x, y, z) && !world.getBlock(x,y,z).getMaterial().isLiquid())
				{
					return TurtleCommandResult.failure("Block at target");
				}
				
				// find TNT to use in the selected slot
				IInventory inv = turtle.getInventory();
				int selected = turtle.getSelectedSlot();
				ItemStack stack = inv.getStackInSlot( selected );
				
				// if arrows are required and we don't have them, stop
				if (Config.arrowsRequired && (stack == null || stack.getItem() != Items.arrow))
				{
					return TurtleCommandResult.failure("No arrows found in slot");
				}
				
				// get the directions for the player
				// the arrow gets most of it's data automatically based on the player
				// so lets just manipulate it rather than messing with all that data ourselves
				FakePlayer fakePlayer = FakePlayerProvider.get(turtle);
				float xrot = 0, yrot = 0;
				switch (direction)
				{
					case 0:	xrot = 90.0F; break; // down
					case 1: xrot = -90.0F; break;  // up
					case 2:	yrot = 180.0F; break;  // north
					case 3:	yrot = 0.0F; break;  //south
					case 4:	yrot = 90.0F; break;  // west
					case 5:	yrot = 270.0F; break;  // east
				}
				fakePlayer.rotationYaw = yrot;
				fakePlayer.rotationPitch = xrot;
				
				EntityArrow arrow = new EntityArrow(world, fakePlayer, 1.0F);
				
				// boost the arrow to Power I
				// makes the ranged turtle a bit closer to the melee turtle as we are also consuming items
				arrow.setDamage(arrow.getDamage() + 1);
				arrow.setIsCritical(true);
				
				// set the arrow's position, so it does not come oddly out of the top of the turtle
				arrow.setPosition(x + 0.5D - (xOffset / 2.0D), y + 0.5D - (yOffset / 2.0D), z + 0.5D - (zOffset / 2.0D));
				
				// if arrows are required, decrease the stack size and delete the stack if needed
				if (Config.arrowsRequired)
				{
					stack.stackSize -= 1;
					if (stack.stackSize == 0)
					{
						inv.setInventorySlotContents(selected, null);
					}
				}
				
				// play a sound and spawn the arrow
				if (!world.isRemote)
				{
					world.playSoundAtEntity(fakePlayer, "random.bow", 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
					world.spawnEntityInWorld(arrow);
				}
				
				// return success, as the arrow was fired
				return TurtleCommandResult.success();
			case Dig:
				return TurtleCommandResult.failure("No tool to dig with");
				
			// should never happen
			default: 
				return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
		}
	}
	
	@SideOnly( Side.CLIENT )
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side)
	{
		//turtles rotate the icon 90 degrees, and the easiest way to rotate it back is to rotate my own texture
		return ModIcons.turtleBow;
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side){}
	
}
