package pgDev.bukkit.DisguiseCraft.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class handles the parsing of JSON arrays
 * 
 * From http://www.json.org/:
 * An array is an ordered collection of values. An array begins with [ (left bracket) and ends with ] (right bracket). Values are separated by , (comma).
 * 
 * @author Devil Boy
 */
public class JSONArray extends JSONValue implements Iterable<JSONValue> {

	private List<JSONValue> list = new ArrayList<JSONValue>();
	
	/**
	 * Gets the value at the specified index
	 * @param index The index from which you want data from
	 * @return The JSONValue at the specified index
	 */
	public JSONValue get(int index) {
		return list.get(index);
	}
	
	/**
	 * Gets the number of stored values
	 * @return The size of the internal list
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * Returns this back to a JSON-formatted array
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		
		for (int i=0; i < list.size(); i++) {
			if (i != 0) {
				sb.append(',');
			}
			
			JSONValue v = list.get(i);
			if (v == null) {
				sb.append("null");
			} else {
				sb.append(list.get(i).toString());
			}
		}
		
		sb.append(']');
		
		return sb.toString();
	}
	
	/**
	 * Gets an iterator from the internal list
	 */
	@Override
	public Iterator<JSONValue> iterator() {
		return list.iterator();
	}
	
	/**
	 * Parses the array into an internal JSONValue list
	 * @param toParse The JSON-formatted String to parse
	 * @return A JSONArray containing all the data values
	 * @throws IllegalArgumentException If formatting errors prevent parsing
	 */
	public static JSONArray parseArray(String toParse) {
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
		JSONArray a = new JSONArray();
		
		// Parse values
		boolean quoteDepth = false;
		int bracketDepth = 0;
		int lastIndex = 0;
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
						if( c == ',') {
							// Add value
							a.list.add(JSONValue.parse(toParse.substring(lastIndex, i)));
							lastIndex = i + 1;
						}
					}
				}
			}
		}
		
		// Add last value
		a.list.add(JSONValue.parse(toParse.substring(lastIndex, toParse.length())));
		
		return a;
	}
}
