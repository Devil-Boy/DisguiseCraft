package pgDev.bukkit.DisguiseCraft.update;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;

public class DCUpdateNotifier  implements Runnable {
	final DisguiseCraft plugin;
	Player player;
	
	public DCUpdateNotifier(final DisguiseCraft plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}
	
	@Override
	public void run() {
		if (player.isOnline()) {
			String latestVersion = DCUpdateChecker.getLatestVersion();
			try {
				if (DCUpdateChecker.isUpToDate(plugin.pdfFile.getVersion(), latestVersion)) {
					player.sendMessage(ChatColor.BLUE + "There is a new update for DisguiseCraft available: " + latestVersion);
				}
			} catch (NumberFormatException e) {
				DisguiseCraft.logger.log(Level.WARNING, "Could not parse version updates.");
			}
		}
	}
}
