package pgDev.bukkit.DisguiseCraft.packet;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.logging.Level;

import net.minecraft.server.v1_4_R1.DataWatcher;
import net.minecraft.server.v1_4_R1.MathHelper;
import net.minecraft.server.v1_4_R1.Packet;
import net.minecraft.server.v1_4_R1.Packet18ArmAnimation;
import net.minecraft.server.v1_4_R1.Packet201PlayerInfo;
import net.minecraft.server.v1_4_R1.Packet20NamedEntitySpawn;
import net.minecraft.server.v1_4_R1.Packet22Collect;
import net.minecraft.server.v1_4_R1.Packet23VehicleSpawn;
import net.minecraft.server.v1_4_R1.Packet24MobSpawn;
import net.minecraft.server.v1_4_R1.Packet28EntityVelocity;
import net.minecraft.server.v1_4_R1.Packet29DestroyEntity;
import net.minecraft.server.v1_4_R1.Packet32EntityLook;
import net.minecraft.server.v1_4_R1.Packet33RelEntityMoveLook;
import net.minecraft.server.v1_4_R1.Packet34EntityTeleport;
import net.minecraft.server.v1_4_R1.Packet35EntityHeadRotation;
import net.minecraft.server.v1_4_R1.Packet38EntityStatus;
import net.minecraft.server.v1_4_R1.Packet40EntityMetadata;
import net.minecraft.server.v1_4_R1.Packet5EntityEquipment;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;

import pgDev.bukkit.DisguiseCraft.*;
import pgDev.bukkit.DisguiseCraft.disguise.*;

public class DCPacketGenerator {
	final Disguise d;
	
	protected int encposX;
	protected int encposY;
	protected int encposZ;
	protected boolean firstpos = true;
	
	public DCPacketGenerator(final Disguise disguise) {
		d = disguise;
	}
	
	// Vital packet methods
	public Packet getSpawnPacket(Player disguisee) {
		if (d.type.isMob()) {
			return getMobSpawnPacket(disguisee.getLocation());
		} else if (d.type.isPlayer()) {
			return getPlayerSpawnPacket(disguisee.getLocation(), (short) disguisee.getItemInHand().getTypeId());
		} else {
			return getObjectSpawnPacket(disguisee.getLocation());
		}
	}
	
	public Packet getSpawnPacket(Location loc) {
		if (d.type.isMob()) {
			return getMobSpawnPacket(loc);
		} else if (d.type.isPlayer()) {
			return getPlayerSpawnPacket(loc, (short) 0);
		} else {
			return getObjectSpawnPacket(loc);
		}
	}
	
	public LinkedList<Packet> getArmorPackets(Player player) {
		LinkedList<Packet> packets = new LinkedList<Packet>();
		ItemStack[] armor;
		if (player == null) {
			armor = new  ItemStack[] {new ItemStack(0, 0), new ItemStack(0, 0), new ItemStack(0, 0), new ItemStack(0, 0)};
		} else {
			armor = player.getInventory().getArmorContents();
		}
		for (byte i=0; i < armor.length; i++) {
			packets.add(getEquipmentChangePacket((short) (i + 1), armor[i]));
		}
		return packets;
	}
	
	// Individual packet generation methods
	public int[] getLocationVariables(Location loc) {
		int x = MathHelper.floor(loc.getX() *32D);
		int y = MathHelper.floor(loc.getY() *32D);
		int z = MathHelper.floor(loc.getZ() *32D);
		if(firstpos) {
			encposX = x;
			encposY = y;
			encposZ = z;
			firstpos = false;
		}
		return new int[] {x, y, z};
	}
	
	public Packet24MobSpawn getMobSpawnPacket(Location loc) {
		int[] locVars = getLocationVariables(loc);
		
		byte yaw = DisguiseCraft.degreeToByte(loc.getYaw());
		byte pitch = DisguiseCraft.degreeToByte(loc.getPitch());
		if (d.type == DisguiseType.EnderDragon) { // Ender Dragon fix
			yaw = (byte) (yaw - 128);
		} else if (d.type == DisguiseType.Chicken) { // Chicken fix
			pitch = (byte) (pitch * -1);
		}
		
		Packet24MobSpawn packet = new Packet24MobSpawn();
		packet.a = d.entityID;
		packet.b = d.type.id;
		packet.c = locVars[0];
		packet.d = locVars[1];
		packet.e = locVars[2];
		packet.i = yaw;
		packet.j = pitch;
		packet.k = yaw;
		
		try {
			Field metadataField = packet.getClass().getDeclaredField("s");
			metadataField.setAccessible(true);
			metadataField.set(packet, d.metadata);
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.SEVERE, "Unable to set the metadata for a " + d.type.name() +  " disguise!", e);
		}
		return packet;
	}
	
	public Packet20NamedEntitySpawn getPlayerSpawnPacket(Location loc, short item) {
		int[] locVars = getLocationVariables(loc);
		
		Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
		packet.a = d.entityID;
		packet.b = d.data.getFirst();
		packet.c = locVars[0];
		packet.d = locVars[1];
		packet.e = locVars[2];
		packet.f = DisguiseCraft.degreeToByte(loc.getYaw());
		packet.g = DisguiseCraft.degreeToByte(loc.getPitch());
		packet.h = item;
		
		try {
			Field metadataField = packet.getClass().getDeclaredField("i");
			metadataField.setAccessible(true);
			metadataField.set(packet, d.metadata);
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.SEVERE, "Unable to set the metadata for a player disguise!", e);
		}
		return packet;
	}
	
	public Packet23VehicleSpawn getObjectSpawnPacket(Location loc) {
		Packet23VehicleSpawn packet = new Packet23VehicleSpawn();
		int data = 1;
		
		// Block specific
    	if (d.type.isBlock()) {
    		loc.setY(loc.getY() + 0.5);
    		
    		Integer blockID = d.getBlockID();
    		if (blockID != null) {
    			data = blockID.intValue();
    			
    			Byte blockData = d.getBlockData();
    			if (blockData != null) {
    				data = data | (((int) blockData) << 0xC);
    			}
    		}
    	}
    	
    	int[] locVars = getLocationVariables(loc);
    	byte[] yp = getYawPitch(loc);
		
		packet.a = d.entityID;
		packet.b = locVars[0];
		packet.c = locVars[1];
		packet.d = locVars[2];
		packet.e = 0;
		packet.f = 0;
		packet.g = 0;
		packet.h = yp[1];
		packet.i = yp[0];
		packet.j = d.type.id;
		packet.k = data;
		
		return packet;
	}
	
	public Packet29DestroyEntity getEntityDestroyPacket() {
		return new Packet29DestroyEntity(new int[] {d.entityID});
	}
	
	public Packet5EntityEquipment getEquipmentChangePacket(short slot, ItemStack item) {
		return new Packet5EntityEquipment(d.entityID, slot, (item == null) ? null : CraftItemStack.asNMSCopy(item));
	}
	
	public byte[] getYawPitch(Location loc) {
		byte yaw = DisguiseCraft.degreeToByte(loc.getYaw());
		byte pitch = DisguiseCraft.degreeToByte(loc.getPitch());
		if (d.type == DisguiseType.EnderDragon) { // EnderDragon specific
			yaw = (byte) (yaw - 128);
		} else if (d.type == DisguiseType.Chicken) { // Chicken fix
			pitch = (byte) (pitch * -1);
		} else if (d.type.isVehicle()) { // Vehicle fix
			yaw = (byte) (yaw - 64);
		}
		return new byte[] {yaw, pitch};
	}
	
	public Packet32EntityLook getEntityLookPacket(Location loc) {
		byte[] yp = getYawPitch(loc);
		return new Packet32EntityLook(d.entityID, yp[0], yp[1]);
	}
	
	public Packet33RelEntityMoveLook getEntityMoveLookPacket(Location loc) {
		byte[] yp = getYawPitch(loc);
		
		MovementValues movement = getMovement(loc);
		encposX += movement.x;
		encposY += movement.y;
		encposZ += movement.z;
		
		return new Packet33RelEntityMoveLook(d.entityID,
				(byte) movement.x, (byte) movement.y, (byte) movement.z,
				yp[0], yp[1]);
	}
	
	public Packet34EntityTeleport getEntityTeleportPacket(Location loc) {
		byte[] yp = getYawPitch(loc);
		
		int x = (int) MathHelper.floor(32D * loc.getX());
		int y = (int) MathHelper.floor(32D * loc.getY());
		int z = (int) MathHelper.floor(32D * loc.getZ());
		
		encposX = x;
		encposY = y;
		encposZ = z;
		
		return new Packet34EntityTeleport(d.entityID,
				x, y, z,
				yp[0], yp[1]);
	}
	
	public Packet40EntityMetadata getEntityMetadataPacket() {
		return new Packet40EntityMetadata(d.entityID, (DataWatcher) d.metadata, true); // 1.4.2 update: true-same method as 1.3.2
	}
	
	public Packet201PlayerInfo getPlayerInfoPacket(Player player, boolean show) {
		if (d.type.isPlayer()) {
			int ping;
			if (show) {
				ping = ((CraftPlayer) player).getHandle().ping;
			} else {
				ping = 9999;
			}
			
			return new Packet201PlayerInfo(d.data.getFirst(), show, ping);
		} else {
			return null;
		}
	}
	
	public MovementValues getMovement(Location to) {
		int x = MathHelper.floor(to.getX() *32D);
		int y = MathHelper.floor(to.getY() *32D);
		int z = MathHelper.floor(to.getZ() *32D);
		int diffx = x - encposX;
		int diffy = y - encposY;
		int diffz = z - encposZ;
		return new MovementValues(diffx, diffy, diffz, DisguiseCraft.degreeToByte(to.getYaw()), DisguiseCraft.degreeToByte(to.getPitch()));
	}
	
	public Packet35EntityHeadRotation getHeadRotatePacket(Location loc) {
		return new Packet35EntityHeadRotation(d.entityID, DisguiseCraft.degreeToByte(loc.getYaw()));
	}
	
	public Packet18ArmAnimation getAnimationPacket(int animation) {
		// 1 - Swing arm
		// 2 Damage animation
		// 5 Eat food
		Packet18ArmAnimation packet = new Packet18ArmAnimation();
		packet.a = d.entityID;
		packet.b = animation;
		return packet;
	}
	
	public Packet38EntityStatus getStatusPacket(int status) {
		// 2 - entity hurt
		// 3 - entity dead
		// 6 - wolf taming
		// 7 - wolf tamed
		// 8 - wolf shaking water
		// 10 - sheep eating grass
		return new Packet38EntityStatus(d.entityID, (byte) status);
	}

	public Packet22Collect getPickupPacket(int item) {
		return new Packet22Collect(item, d.entityID);
	}
	
	public Packet28EntityVelocity getVelocityPacket(int x, int y, int z) {
		Packet28EntityVelocity packet = new Packet28EntityVelocity();
		packet.a = d.entityID;
		packet.b = x;
		packet.c = y;
		packet.d = z;
		return packet;
	}
}
