package pgDev.bukkit.DisguiseCraft.listeners.optional;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;

public class EntityDamageListener implements Listener {
	final DisguiseCraft plugin;
	
	public EntityDamageListener(final DisguiseCraft plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!event.isCancelled()) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				if (plugin.disguiseDB.containsKey(player.getName())) {
					Disguise disguise = plugin.disguiseDB.get(player.getName());
					
					// Send the damage animation
					plugin.sendPacketToWorld(player.getWorld(), disguise.packetGenerator.getAnimationPacket(2));
					
					// Play sound
					Sound sound = disguise.getDamageSound();
					if (sound != null) {
						player.getWorld().playSound(player.getLocation(), sound, 1.0F, 1.0F);
					}
				}
			}
		}
	}
}
