import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static final String TAG = "AutoClickDetector";
	public static final String CHECK_INTERVAL_TAG = "Check-Interval";
	public static final String ALLOW_NANO_MILLIS_TAG = "Allow-NanoMillis";

	@Override
	public void onEnable() {
		FileConfiguration configuration = this.getConfig();
		if (configuration.get(CHECK_INTERVAL_TAG) == null) {
			configuration.set(CHECK_INTERVAL_TAG, 30);
			this.saveConfig();
		}
		if(configuration.get(ALLOW_NANO_MILLIS_TAG) == null) {
			configuration.set(ALLOW_NANO_MILLIS_TAG, 1000000);
			this.saveConfig();
		}

		EventListener eventListener = new EventListener(this);
		Bukkit.getPluginManager().registerEvents(eventListener, this);
	}

	public static void report(Player player, long averageDiff) {
		Bukkit.broadcastMessage("マクロ使用の疑い:" + player.getName() + "(平均クリック間隔:" + averageDiff + "ns)");
	}
}
