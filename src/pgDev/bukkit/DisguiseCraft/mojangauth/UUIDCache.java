package pgDev.bukkit.DisguiseCraft.mojangauth;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;

public class UUIDCache {
	static Map<String, UUID> cache = new ConcurrentHashMap<String, UUID>();
	
	public static UUID retrieve(String playerName) {
		UUID uid = cache.get(playerName);
		if (uid == null) {
			uid = uncheckedRandomUUID();
		}
		return uid;
	}
	
	public static UUID cache(String playerName) {
		// Check if online
		Player onlinePlayer = Bukkit.getPlayer(playerName);
		if (onlinePlayer != null) {
			UUID uid = onlinePlayer.getUniqueId();
			cache.put(playerName, uid);
			return uid;
		}
		
		// Try fetching the UUID
		try {
			UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(playerName));
			Map<String, UUID> response = fetcher.call();
			
			UUID uid = response.get(playerName);
			if (uid != null) return uid;
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.WARNING, "Error while fetching offline player UUID", e);
		}
		
		// Try generating an empty UUID
		try {
			return EmptyUUIDS.findEmptyUUID();
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.WARNING, "Error while searching for an empty UUID", e);
		}
		
		// Fall back on a random UUID
		return uncheckedRandomUUID();
	}
	
	static UUID uncheckedRandomUUID() {
		UUID uid = null;
		do {
			uid = UUID.randomUUID();
		} while (cache.keySet().contains(uid));
		return uid;
	}
}
