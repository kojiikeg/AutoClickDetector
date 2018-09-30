import java.util.List;

import org.bukkit.entity.Player;

/**
 * クリック間隔からマクロチェックするクラス
 * 設定によってはサーバーに影響が出そうなのでThreadで処理
 * @author makura
 *
 */
public class CheckClickIntervalThread extends Thread {
	private Player target;
	private List<Long> timeList;
	private long allowNanoMillis;

	public CheckClickIntervalThread(Player target, List<Long> timeList, long allowNanoMillis) {
		this.target = target;
		this.allowNanoMillis = allowNanoMillis;
		this.timeList = timeList;
	}

	@Override
	public void run() {
		long intervalDiffSum = 0;

		long beforeInterval = Math.abs(timeList.get(1) - timeList.get(0));
		for (int i = 2; i < timeList.size(); i++) {
			long nowInterval = Math.abs(timeList.get(i) - timeList.get(i - 1));
			intervalDiffSum += Math.abs(beforeInterval - nowInterval);
		}

		if (intervalDiffSum / (timeList.size() - 2) < allowNanoMillis) {
			Main.report(target, intervalDiffSum / (timeList.size() - 2));
		}
	}
}
