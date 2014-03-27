package pgDev.bukkit.DisguiseCraft.json;

/**
 * This class handles the parsing of JSON values
 * 
 * From http://www.json.org/:
 * A value can be a string in double quotes, or a number, or true or false or null, or an object or an array. These structures can be nested.
 * 
 * @author Devil Boy
 */
public abstract class JSONValue {
	String unparsed;
	
	protected JSONValue(String unparsed) {
		this.unparsed = unparsed;
	}
	
	public static JSONValue parse(String toParse) {
		//TODO: Actually write parser
		if (toParse.startsWith("\"")) {
			return JSONString.parseString(toParse);
		}
		
		return null;
	}
}
