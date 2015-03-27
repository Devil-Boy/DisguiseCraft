package net.hexadigital.disguisecraft.disguises;

/**
 * This represents a disguise of a Minecraft entity
 * Entities are not locked to the block grid and can move about the world
 *
 * @author Devil Boy
 *
 */
public interface EntityDisguise extends Disguise {

	/**
	 * Gets the entity ID this disguise uses
	 * @return A (hopefully) unique ID
	 */
	int getEntityId();
	
	/**
	 * Gets the entity ID of the entity or disguise riding this disguise
	 * @return A unique ID
	 */
	int getRider();
	
	/**
	 * Sets the given entity ID as the rider of this disguise
	 * @param id The unique ID of the rider
	 */
	void setRider(int id);
}
