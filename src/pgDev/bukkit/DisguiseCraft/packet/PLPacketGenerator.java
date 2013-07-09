package pgDev.bukkit.DisguiseCraft.packet;

import java.util.logging.Level;

import net.minecraft.server.v1_6_R2.DataWatcher;
import net.minecraft.server.v1_6_R2.Packet20NamedEntitySpawn;
import net.minecraft.server.v1_6_R2.Packet24MobSpawn;
import net.minecraft.server.v1_6_R2.Packet29DestroyEntity;

import org.bukkit.Location;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import pgDev.bukkit.DisguiseCraft.*;
import pgDev.bukkit.DisguiseCraft.disguise.*;

public class PLPacketGenerator extends DCPacketGenerator {
	ProtocolManager pM = DisguiseCraft.protocolManager;

	public PLPacketGenerator(Disguise disguise) {
		super(disguise);
	}
	
	// Packet creation methods
	@Override
	public Packet24MobSpawn getMobSpawnPacket(Location loc, String name) {
		// Make values
		int[] locVars = getLocationVariables(loc);
		byte[] yp = getYawPitch(loc);
		int eID = d.entityID;
		int mobID = d.type.id;
		int xPos = locVars[0];
		int yPos = locVars[1];
		int zPos = locVars[2];
		
		// Make packet
		PacketContainer pC = pM.createPacket(24);
		try {
			pC.getIntegers().
				write(0, eID).
				write(1, mobID).
				write(2, xPos).
				write(3, yPos).
				write(4, zPos);
		} catch (FieldAccessException e) {
			DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the integers for a " + d.type.name() +  " disguise!", e);
		}
		try {
			pC.getBytes().
				write(0, yp[0]).
				write(1, yp[1]).
				write(2, yp[0]);
		} catch (FieldAccessException e) {
			DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the bytes for a " + d.type.name() +  " disguise!", e);
		}
		
		DataWatcher metadata = d.metadata;
		if (name != null) {
			metadata = d.mobNameData(name);
		}
		
		try {
			pC.getDataWatcherModifier().
				write(0, new WrappedDataWatcher(metadata));
		} catch (FieldAccessException e) {
			DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the metadata for a " + d.type.name() +  " disguise!", e);
		}
		return (Packet24MobSpawn) pC.getHandle();
	}
	
	@Override
	public Packet20NamedEntitySpawn getPlayerSpawnPacket(Location loc, short item) {
		// Make Values
		int[] locVars = getLocationVariables(loc);
		byte[] yp = getYawPitch(loc);
		int eID = d.entityID;
        String name = d.data.getFirst();
        int xPos = locVars[0];
        int yPos = locVars[1];
        int zPos = locVars[2];
        
        // Make Packet
        PacketContainer pC = pM.createPacket(20);
		try {
			pC.getIntegers().
				write(0, eID).
				write(1, xPos).
				write(2, yPos).
				write(3, zPos).
				write(4, (int) item);
		} catch (FieldAccessException e) {
			DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the integers for a player disguise!", e);
		}
		try {
			pC.getStrings().
				write(0, name);
		} catch (FieldAccessException e) {
			DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the name for a player disguise!", e);
		}
		try {
			pC.getBytes().
				write(0, yp[0]).
				write(1, yp[1]);
		} catch (FieldAccessException e) {
			DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the bytes for a player disguise!", e);
		}
		try {
			pC.getDataWatcherModifier().
				write(0, new WrappedDataWatcher(d.metadata));
		} catch (FieldAccessException e) {
			DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the metadata for a player disguise!", e);
		}
        return (Packet20NamedEntitySpawn) pC.getHandle();
	}
	
	@Override
	public Packet29DestroyEntity getEntityDestroyPacket() {
		PacketContainer pC = pM.createPacket(29);
		StructureModifier<int[]> intPos = pC.getIntegerArrays();
		if (intPos.size() > 0) {
			try {
				int[] intArray = {d.entityID};
				intPos.write(0, intArray);
			} catch (FieldAccessException e) {
				DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the integer array for a destroy packet!", e);
			}
		} else {
			try {
				pC.getSpecificModifier(int.class)
					.write(0, d.entityID);
			} catch (FieldAccessException e) {
				DisguiseCraft.logger.log(Level.SEVERE, "PL: Unable to modify the integer for a destroy packet!", e);
			}
		}
		return (Packet29DestroyEntity) pC.getHandle();
	}
}
