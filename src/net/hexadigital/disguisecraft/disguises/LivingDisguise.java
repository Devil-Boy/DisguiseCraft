package net.hexadigital.disguisecraft.disguises;


/**
 * This represents a disguise of a living entity
 * Living entities can have potion effects applied to them
 * 
 * NOTE:  EntityEquipment methods were added here instead of
 * creating another interaface.
 *
 * @author Devil Boy
 *
 */
public interface LivingDisguise extends EntityDisguise {

	// TODO: Add methods for potion effects
	
	// TODO: All methods derived from EntityEquipment had ItemStack replaced with int
	
	/**
	 * Clears the disguise of all armor and held items
	 */
	void clear();
	
	/**
	 * Gets a copy of all worn armor
	 */
	int[] getArmorContents();
	
	/**
	 * Gets a copy of the boots currently being worn by teh entity
	 */
	int getBoots();
	
	/**
	 * Gets the chance of the boots being dropped upon this creatures death
	 */
	float getBootsDropChance();
	
	/**
	 * Gets a copy of the chestplate currently being worn by the entity
	 */
	int getChestPlate();
	
	/**
	 * Gets the chance of the chest plate being dropped upon this creatures death
	 */
	float getChestPlateDropChance();
	
	/**
	 * Gets a copy of the helmet currently being worn by the entity
	 */
	int getHelmet();
	
	/**
	 * Gets the chance of the helmet being dropped upon this creatures death
	 */
	float getHelmetDropChance();
	
	/**
	 * Get the entity that this inventory belongs to
	 * 
	 * in the java docs, this is Entity, not EntityDisguise.  And its found in EntityEquipment
	 * we decided to put the EE methods into our LivingDisguise interface
	 */
	EntityDisguise getHolder();
	
	/**
	 * Gets a copy of the item the entity is currently holding
	 */
	int getItemInHand();
	
	/**
	 * Gets the chance of the currently held item being dropped upon this creatures death
	 */
	float getItemInHandDropChance();
	
	/**
	 *Gets a copy of the leggings currently being worn by the entity
	 */
	int getLeggings();
	
	/**
	 * Gets the chance of the currently worn leggings being dropped upon this creatures death
	 */
	float getLeggingsDropChance();
	
	/**
	 * Sets the entities armor to the provided array of ints
	 */
	void setArmorContents(int[] items);
	
	/**
	 * Sets the boots worn by the entity
	 */
	void setBoots(int boots);
	
	/**
	 * Sets the chance of the boots being dropped upon this creatures death
	 */
	void setBootsDropChance(float chance);
	
	/**
	 * Sets the chest plate worn by the entity
	 */
	void setChestPlate(int chestplate);
	
	/**
	 * Sets the chance of the chest plate being dropped upon this creatures death
	 */
	void setChestPlateDropChance(float chance);
	
	/**
	 * Sets the helmet worn by the entity
	 */
	void setHelmet(int helmet);
	
	/**
	 * Sets the chance of the helmet being dropped upon this creatures death
	 */
	void setHelmetDropChance(float chance);
	
	/**
	 * Sets the item the entity is holding
	 */
	void setItemInHand(int stack);
	
	/**
	 * Sets the chance of the item this creature is currently holding being dropped upon this creatures death
	 */
	void setItemInHandDropChance(float chance);
	
	/**
	 * Sets the leggings worn by the entity
	 */
	void setLeggings(int leggings);
	
	/**
	 * Sets the chance of the leggings being dropped upon this creatures death
	 */
	void setLeggingsDropChance(float chance);
	
}
