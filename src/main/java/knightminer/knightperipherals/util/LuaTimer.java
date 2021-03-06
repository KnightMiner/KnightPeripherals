package knightminer.knightperipherals.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadLocalRandom;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class LuaTimer {
	private static final String TIMER_EVENT = "computer_timer";
	private static final String TIMER_TOKEN = "timer_token_";
	private static WeakHashMap<IComputerAccess, Integer[]> timers = new WeakHashMap<IComputerAccess, Integer[]>();

	public static void delay(ILuaContext context, IComputerAccess computer, int time) throws InterruptedException {
		int validator = ThreadLocalRandom.current().nextInt();
		timers.put(computer, new Integer[] { time, validator });
		while (true) {
			Object[] event = context.pullEventRaw(TIMER_EVENT);
			if (event.length > 1 && event[1].equals(TIMER_TOKEN + validator)) {
				break;
			}
		}
	}

	@SubscribeEvent
	public void incrementTimers(ServerTickEvent event) {
		if (event.phase == Phase.START) {
			Iterator<Entry<IComputerAccess, Integer[]>> iterator = timers.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<IComputerAccess, Integer[]> timer = iterator.next();
				Integer[] data = timer.getValue();
				data[0] -= 1;
				if (data[0] < 1) { // check less than rather than equals just in
				                   // case
					timer.getKey().queueEvent(TIMER_EVENT, new Object[] { TIMER_TOKEN + data[1] });
					iterator.remove();
				}
				else {
					timer.setValue(data);
				}
			}
		}
	}
}
