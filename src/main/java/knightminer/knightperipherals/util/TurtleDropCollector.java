package knightminer.knightperipherals.util;

import java.util.HashMap;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// adds turtle drops to the inventory of the turtle
// based off a class by austinv11
public class TurtleDropCollector {

	private static HashMap<Entity, ITurtleAccess> map = new HashMap<Entity, ITurtleAccess>();

	public static void addDrop(ITurtleAccess turtle, Entity ent) {
		map.put(ent, turtle);
	}

	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		if (map.containsKey(event.entity)) {
			TurtleUtil.addItemListToInv(TurtleUtil.entityItemsToItemStack(event.drops), map.get(event.entity));
			event.setCanceled(true);
			map.remove(event.entity);
		}
	}
}
