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
	
	public static JSONValue parse(String toParse) {
		// Strip whitespace
		toParse = toParse.trim();
		
		// Parse based on first character
		char c = toParse.charAt(0);
		if (c == '"') {
			return JSONString.parseString(toParse);
		} else if (Character.isDigit(c) || c == '-') {
			return JSONNumber.parseNumber(toParse);
		} else if (c == '{') {
			return JSONObject.parseObject(toParse);
		} else if (c == '[') {
			return JSONArray.parseArray(toParse);
		} else if (c == 't' || c == 'f') {
			return JSONBoolean.parseBoolean(toParse);
		} else if (c == 'n') {
			if (toParse.equalsIgnoreCase("null")) {
				return null;
			}
		}
		
		throw new IllegalArgumentException("Could not identify value type");
	}
}
