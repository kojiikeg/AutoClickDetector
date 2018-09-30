import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {
	private Map<Player, List<Long>> timeListMap = new HashMap<>();
	private JavaPlugin plugin;
	
	private int checkInterval;
	private long allowNanoMillis;

	public EventListener(JavaPlugin instance) {
		plugin = instance;
		checkInterval = getCheckInterval();
		allowNanoMillis = getAllowNanoMillis();
	}

	private int getCheckInterval() {
		return plugin.getConfig().getInt(Main.CHECK_INTERVAL_TAG);
	}

	private long getAllowNanoMillis() {
		return plugin.getConfig().getInt(Main.ALLOW_NANO_MILLIS_TAG);
	}

	@EventHandler
	public void click(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action type = event.getAction();

		//とりあえず空中クリック時のみ検知
		if (type == Action.LEFT_CLICK_AIR) {
			List<Long> timeList;
			long time = System.nanoTime();

			if (!timeListMap.containsKey(player)) {
				timeList = new ArrayList<>();
				timeList.add(time);

				timeListMap.put(player, timeList);
			} else {
				timeList = timeListMap.get(player);
				timeList.add(time);

				if (timeList.size() == checkInterval) {
					new CheckClickIntervalThread(player, timeList, allowNanoMillis).start();
					timeListMap.remove(player);
				} else {
					timeListMap.put(player, timeList);
				}
			}
		}
	}
}
