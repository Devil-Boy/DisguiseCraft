package pgDev.bukkit.DisguiseCraft.mojangauth;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import net.minecraft.server.v1_8_R2.EntityHuman;

import com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R2.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;

public class ProfileCache {
	// The database for storing profiles
	Map<String, GameProfile> cache;
	
	// This is the GameProfile field in the player entity
	Field playerProfileField;
	Field offlinePlayerProfileField;
	
	public ProfileCache() throws Exception {
		// Instantiate cache
		cache = new ConcurrentHashMap<String, GameProfile>();
		
		// Search for GameProfile field by type
		for (Field field : EntityHuman.class.getDeclaredFields()) {
			if (field.getType() == GameProfile.class && !Modifier.isStatic(field.getModifiers())) {
				playerProfileField = field;
			}
		}
		for (Field field : CraftOfflinePlayer.class.getDeclaredFields()) {
			if (field.getType() == GameProfile.class && !Modifier.isStatic(field.getModifiers())) {
				offlinePlayerProfileField = field;
			}
		}
		
		// Fall-back on hardcoded GameProfile field name
		if (playerProfileField == null) {
			playerProfileField = EntityHuman.class.getDeclaredField("i");
		}
		if (offlinePlayerProfileField == null) {
			offlinePlayerProfileField = CraftOfflinePlayer.class.getDeclaredField("profile");
		}
		
		// Set field accessible
		playerProfileField.setAccessible(true);
	}
	
	public GameProfile cache(String playerName) {
		// Check if online
		Player onlinePlayer = Bukkit.getPlayer(playerName);
		if (onlinePlayer != null) {
			GameProfile profile = extractProfile(onlinePlayer);
			cache.put(playerName, profile);
			return profile;
		}
		
		// Try fetching the GameProfile
		try {
			// Convert player name to UUID
			UUIDFetcher uFetcher = new UUIDFetcher(Arrays.asList(playerName));
			Map<String, UUID> uResponse = uFetcher.call();
			
			UUID uid = uResponse.get(playerName);
			if (uid != null) {
				// Search for GameProfile using UUID
				return extractProfile(Bukkit.getOfflinePlayer(uid));
				
				/*
				ProfileFetcher pFetcher = new ProfileFetcher(Arrays.asList(uid));
				Map<UUID, GameProfile> pResponse = pFetcher.call();
				
				GameProfile profile = pResponse.get(uid);
				cache.put(playerName, profile);
				return profile;*/
			}
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.WARNING, "Error while fetching offline player UUID", e);
		}
		
		// Try generating a fake GameProfile with an empty UUID
		try {
			GameProfile profile = new GameProfile(DisguiseCraft.emptyUUIDs.findEmptyUUID(), playerName);
			cache.put(playerName, profile);
			return profile;
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.WARNING, "Error while searching for an empty UUID", e);
		}
		
		// Fall back on a GameProfile with any UUID
		GameProfile profile = new GameProfile(unfetchedRandomUUID(), playerName);
		cache.put(playerName, profile);
		return profile;
	}
	
	public UUID retrieveUUID(String playerName) {
		GameProfile profile = cache.get(playerName);
		if (profile == null) {
			return unfetchedRandomUUID();
		} else {
			return profile.getId();
		}
	}
	
	UUID unfetchedRandomUUID() {
		UUID uid = null; // For reference: "00000000-0000-0000-0000-000000000000"
		do {
			uid = UUID.randomUUID();
		} while (cache.keySet().contains(uid));
		return uid;
	}
	
	public GameProfile extractProfile(Player player) {
		try {
			return (GameProfile) playerProfileField.get(((CraftPlayer) player).getHandle());
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.WARNING, "Could not properly extract GameProfile for player: " + player.getName());
			return new GameProfile(player.getUniqueId(), player.getName());
		}
	}
	
	public GameProfile extractProfile(OfflinePlayer player) {
		try {
			return (GameProfile) offlinePlayerProfileField.get((CraftOfflinePlayer) player);
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.WARNING, "Could not properly extract GameProfile for offline player: " + player.getName());
			return new GameProfile(player.getUniqueId(), player.getName());
		}
	}
}
