package pgDev.bukkit.DisguiseCraft.json;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the parsing of JSON arrays
 * 
 * From http://www.json.org/:
 * An array is an ordered collection of values. An array begins with [ (left bracket) and ends with ] (right bracket). Values are separated by , (comma).
 * 
 * @author Devil Boy
 */
public class JSONArray extends JSONValue {

	private List<JSONValue> list = new ArrayList<JSONValue>();
	
	private JSONArray(String unparsed) {
		super(unparsed);
	}
	
	public JSONValue get(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
	
	public static JSONArray parseList(String toParse) {
		// Find and remove starting bracket
		if (toParse.startsWith("[")) {
			toParse = toParse.substring(1, toParse.length());
		} else {
			throw new IllegalArgumentException("No opening bracket found");
		}
		
		// Find and remove ending bracket
		if (toParse.endsWith("]")) {
			toParse = toParse.substring(0, toParse.length() - 1);
		} else {
			throw new IllegalArgumentException("No closing bracket found");
		}
		
		// Construct the JSONArray
		JSONArray a = new JSONArray(toParse);
		
		// Parse values
		boolean quoteDepth = false;
		int bracketDepth = 0;
		int lastIndex = 0;
		for (int i=0; i < toParse.length(); i++) {
			// Check for escape character
			if (toParse.charAt(i) == '\\') {
				i++;
			} else {
				// Check for quotations
				if (toParse.charAt(i) == '"') {
					quoteDepth = !quoteDepth;
				}
				
				if (!quoteDepth) {
					// We're assuming brackets are correctly matched (no [{]}'s)
					if (toParse.charAt(i) == '[' || toParse.charAt(i) == '{') {
						bracketDepth++;
					} else if (toParse.charAt(i) == ']' || toParse.charAt(i) == '}') {
						if (bracketDepth <= 0) {
							throw new IllegalArgumentException("Close bracket found before an open bracket");
						} else {
							bracketDepth--;
						}
					} else if (toParse.charAt(i) == ',') {
						// Add value
						a.list.add(JSONValue.parse(toParse.substring(lastIndex, i)));
					}
				}
			}
		}
		
		// Add last value
		a.list.add(JSONValue.parse(toParse.substring(lastIndex, toParse.length())));
		
		return a;
	}
}
