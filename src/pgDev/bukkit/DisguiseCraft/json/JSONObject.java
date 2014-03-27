package pgDev.bukkit.DisguiseCraft.json;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the parsing of JSON objects
 * 
 * From http://www.json.org/:
 * An object is an unordered set of name/value pairs. An object begins with { (left brace) and ends with } (right brace). Each name is followed by : (colon) and the name/value pairs are separated by , (comma).
 * 
 * @author Devil Boy
 */
public class JSONObject extends JSONValue {

	private Map<String, JSONValue> map = new HashMap<String, JSONValue>();
	
	private JSONObject(String unparsed) {
		super(unparsed);
	}
	
	public JSONValue get(String key) {
		return map.get(key);
	}
	
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
		JSONObject o = new JSONObject(toParse);
		
		// Parse pairs
		//TODO: Write parser
		
		return o;
	}
}
