package pgDev.bukkit.DisguiseCraft.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class handles the parsing of JSON objects
 * 
 * From http://www.json.org/:
 * An object is an unordered set of name/value pairs. An object begins with { (left brace) and ends with } (right brace). Each name is followed by : (colon) and the name/value pairs are separated by , (comma).
 * 
 * @author Devil Boy
 */
public class JSONObject extends JSONValue {

	private Map<String, JSONValue> map = new LinkedHashMap<String, JSONValue>();
	
	/**
	 * Gets the JSONValue linked to the specified key
	 * @param key The key linked to the value you want
	 * @return The JSONValue-wrapped data
	 */
	public JSONValue get(String key) {
		return map.get(key);
	}
	
	/**
	 * Returns this object back to JSON form
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append('{');
		
		for (Map.Entry<String, JSONValue> e : map.entrySet()) {
			if (sb.length() > 1) {
				sb.append(',');
			}
			
			sb.append("\"" + e.getKey() + "\":");
			
			JSONValue v = e.getValue();
			if (v == null) {
				sb.append("null");
			} else {
				sb.append(e.getValue().toString());
			}
		}
		
		sb.append('}');
		
		return sb.toString();
	}
	
	/**
	 * Gets a set of the entries in the internal map
	 * @return The entrySet() result from the internal map
	 */
	public Set<Map.Entry<String, JSONValue>> entrySet() {
		return map.entrySet();
	}
	
	/**
	 * Parses the JSON-formatted data as an object (key-value map)
	 * @param toParse The data String to parse
	 * @return A JSONObject containing all the parsed data values
	 * @throw IllegalArgumentException If a formatting error prevents parsing
	 */
	public static JSONObject parseObject(String toParse) {
		// Find and remove starting bracket
		if (toParse.startsWith("{")) {
			toParse = toParse.substring(1, toParse.length());
		} else {
			throw new IllegalArgumentException("No opening bracket found");
		}
		
		// Find and remove ending bracket
		if (toParse.endsWith("}")) {
			toParse = toParse.substring(0, toParse.length() - 1);
		} else {
			throw new IllegalArgumentException("No closing bracket found");
		}
		
		// Construct the JSONObject
		JSONObject o = new JSONObject();
		
		// Parse pairs
		boolean quoteDepth = false;
		int bracketDepth = 0;
		int lastIndex = 0;
		String lastKey = null;
		for (int i=0; i < toParse.length(); i++) {
			char c = toParse.charAt(i);
			
			// Check for escape character
			if (c == '\\') {
				i++;
			} else {
				// Check for quotations
				if (c == '"') {
					quoteDepth = !quoteDepth;
				}
				
				if (!quoteDepth) {
					// We're assuming brackets are correctly matched (no [{]}'s)
					if (c == '[' || c == '{') {
						bracketDepth++;
					} else if (c == ']' || c == '}') {
						if (bracketDepth <= 0) {
							throw new IllegalArgumentException("Close bracket found before an open bracket");
						} else {
							bracketDepth--;
						}
					}
						
					if (bracketDepth == 0) {
						if (c == ':') {
							// Save key
							lastKey = JSONString.parseString(toParse.substring(lastIndex, i).trim()).get();
							lastIndex = i + 1;
						} else if (c == ',') {
							if (lastKey == null) {
								throw new IllegalArgumentException("Pair missing a key");
							} else {
								// Add pair
								o.map.put(lastKey, JSONValue.parse(toParse.substring(lastIndex, i)));
								lastIndex = i + 1;
							}
						}
					}
				}
			}
		}
		
		// Add last pair
		o.map.put(lastKey, JSONValue.parse(toParse.substring(lastIndex, toParse.length())));
		
		return o;
	}
}
