package pgDev.bukkit.DisguiseCraft.json;

/**
 * This class merely wraps a boolean in a JSON value
 * 
 * @author Devil Boy
 */
public class JSONBoolean extends JSONValue {
	
	private boolean parsed;
	
	public boolean get() {
		return parsed;
	}
	
	@Override
	public String toString() {
		return parsed ? "true" : "false";
	}
	
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
