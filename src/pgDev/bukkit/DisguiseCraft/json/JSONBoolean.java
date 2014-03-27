package pgDev.bukkit.DisguiseCraft.json;

/**
 * This class merely wraps a boolean in a JSON value
 * 
 * @author Devil Boy
 */
public class JSONBoolean extends JSONValue {
	
	private boolean parsed;
	
	/**
	 * Gets the wrapped boolean value
	 * @return The boolean value
	 */
	public boolean get() {
		return parsed;
	}
	
	/**
	 * Returns the boolean back to JSON formatting
	 */
	@Override
	public String toString() {
		return parsed ? "true" : "false";
	}
	
	/**
	 * Parses the String into a boolean value
	 * @param toParse The String to parse
	 * @return A JSONBoolean carrying the proper boolean data
	 * @throws IllegalArgumentException If the given String could not be parsed as either true or false
	 */
	public static JSONBoolean parseBoolean(String toParse) {
		JSONBoolean b = new JSONBoolean();
		
		if (toParse.equalsIgnoreCase("true")) {
			b.parsed = true;
		} else if (toParse.equalsIgnoreCase("false")) {
			b.parsed = false;
		} else {
			throw new IllegalArgumentException("Could not parse \"" + toParse + "\" as a boolean");
		}
		
		return b;
	}
}
