package knightminer.knightperipherals.turtles;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import knightminer.knightperipherals.reference.Config;
import knightminer.knightperipherals.reference.Reference;
import knightminer.knightperipherals.util.FakePlayerProvider;
import knightminer.knightperipherals.util.ModLogger;
import knightminer.knightperipherals.util.TurtleUtil;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurtleBow implements ITurtleUpgrade
{
	private static final ItemStack stack = new ItemStack(Items.bow, 1);
	
	@Override
	public int getLegacyUpgradeID()
	{
		return Reference.UPGRADE_LEGACY_BOW;
	}
	
	@Override
	public ResourceLocation getUpgradeID()
	{
		return new ResourceLocation(Reference.UPGRADE_BOW);
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
			return stack;
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

	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction)
	{
		switch (verb)
		{
			case Attack:
				World world = turtle.getWorld();
				// return 0 for down, 1 for up, or turtle facing otherwise
				BlockPos pos = turtle.getPosition().offset(direction);
				
				// only fire the arrow into air or a liquid
				if (!world.isAirBlock(pos) && !world.getBlockState(pos).getBlock().getMaterial().isLiquid())
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

				// also get coordinates for the arrow while here to save on a switch
				double x = pos.getX() + 0.5D,
						y = pos.getY() + 0.5D,
						z = pos.getZ() + 0.5D;
				switch (direction)
				{
					case DOWN:
						xrot = 90.0F;
						y += 0.5D;
						break;
					case UP:
						xrot = -90.0F;
						y -= 0.5D;
						break;
					case NORTH:
						yrot = 180.0F;
						z += 0.5D;
						break;
					case SOUTH:
						yrot = 0.0F;
						z -= 0.5D;
						break;
					case WEST:
						yrot = 90.0F;
						x += 0.5D;
						break;
					case EAST:
						yrot = 270.0F;
						x -= 0.5D;
						break;
				}
				fakePlayer.rotationYaw = yrot;
				fakePlayer.rotationPitch = xrot;
				
				EntityArrow arrow = new EntityArrow(world, fakePlayer, 1.0F);
				
				// boost the arrow to Power I
				// makes the ranged turtle a bit closer to the melee turtle as we are also consuming items
				arrow.setDamage(arrow.getDamage() + 1);
				arrow.setIsCritical(true);
				
				// set the arrow's position, so it does not come oddly out of the top of the turtle
				arrow.setPosition(x,y,z);
				
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
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side)
	{
		IBakedModel model = TurtleUtil.getMesher().getItemModel(stack);
		Matrix4f transform = TurtleUtil.getTransforms(side);

		// rotate the matrix 90 degrees on the Y axis
		Matrix3f angle = new Matrix3f( 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F );
		transform.setRotation(angle);
		transform.m23 -= 1.0F;
		return Pair.of(model, transform);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side){}

}
