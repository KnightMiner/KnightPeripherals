package knightminer.knightperipherals.init;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import knightminer.knightperipherals.reference.Reference;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

@SideOnly(Side.CLIENT)
public class ModIcons {
	
	public static IIcon turtleClawLeft, turtleClawRight, turtleBow;
	
	@SubscribeEvent
	public void register(TextureStitchEvent event)
	{
		boolean terrain = event.map.getTextureType() == 0;
		
		if(terrain)
		{
			turtleClawLeft = event.map.registerIcon(Reference.RESOURCE_LOCATION + ":turtle_claw_left");
			turtleClawRight = event.map.registerIcon(Reference.RESOURCE_LOCATION + ":turtle_claw_right");
		} else
		{
			turtleBow = event.map.registerIcon(Reference.RESOURCE_LOCATION + ":turtle_bow");
		}
	}

}
