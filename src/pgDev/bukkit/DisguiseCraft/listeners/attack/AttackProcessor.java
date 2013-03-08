package pgDev.bukkit.DisguiseCraft.listeners.attack;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Sound;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;

public class AttackProcessor implements Runnable {
	DisguiseCraft plugin;
	
	public ConcurrentLinkedQueue<PlayerAttack> queue = new ConcurrentLinkedQueue<PlayerAttack>();
	private int amount = 0;
	
	public AttackProcessor(DisguiseCraft plugin) {
		this.plugin = plugin;
	}
	
	public synchronized void incrementAmount() {
		amount++;
	}
	
	private synchronized int flushAmount() {
		int output = amount;
		amount = 0;
		return output;
	}

	@Override
	public void run() {
		int polls = flushAmount();
		for (int i=0; i < polls; i++) {
			PlayerAttack attack = queue.poll();
			attack.attacker().attack(attack.victim());
			
			// Play sound
			if (plugin.disguiseDB.containsKey(attack.victim.getName())) {
				Sound sound = plugin.disguiseDB.get(attack.victim.getName()).getDamageSound();
				if (sound != null) {
					attack.victim.getWorld().playSound(attack.victim.getLocation(), sound, 1.0F, 1.0F);
				}
			}
		}
	}
}
