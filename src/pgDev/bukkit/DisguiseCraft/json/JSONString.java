package pgDev.bukkit.DisguiseCraft.json;

/**
 * This class merely wraps a String in a JSON value
 * 
 * From http://www.json.org/:
 * A string is a sequence of zero or more Unicode characters, wrapped in double quotes, using backslash escapes. A character is represented as a single character string.
 * 
 * @author Devil Boy
 */
public class JSONString extends JSONValue {
	
	private String parsed;
	
	/**
	 * Gets the wrapped String
	 * @return The String contained in this wrapper
	 */
	public String get() {
		return parsed;
	}
	
	/**
	 * Returns this String back to JSON form
	 * Only '\', '"', and control characters are escaped
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(parsed);
		
		for (int i=0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			switch (c) {
			case '"':
			case '\\':
			case '/':
				sb.insert(i, '\\');
				i++;
				break;
			case '\b':
				sb.replace(i, i+1, "\\b");
				i++;
				break;
			case '\f':
				sb.replace(i, i+1, "\\f");
				i++;
				break;
			case '\n':
				sb.replace(i, i+1, "\\n");
				i++;
				break;
			case '\r':
				sb.replace(i, i+1, "\\r");
				i++;
				break;
			case '\t':
				sb.replace(i, i+1, "\\t");
				i++;
				break;
			}
		}
		
		return "\"" + sb.toString() + "\"";
	}

	/**
	 * Parses this JSON String by converting all escapes into Java chars
	 * @param toParse The JSON String data to parse
	 * @return A JSONString object with an internal String containing the parsed data
	 * @throws If formatting issues prevent proper parsing
	 */
	public static JSONString parseString(String toParse) {
		// Find and remove beginning quote
		if (toParse.startsWith("\"")) {
			toParse = toParse.substring(1, toParse.length());
		} else {
			throw new IllegalArgumentException("No beginning quote found");
		}
		
		// Find and remove ending quote
		if (toParse.endsWith("\"")) {
			toParse = toParse.substring(0, toParse.length() - 1);
		} else {
			throw new IllegalArgumentException("No ending quote found");
		}
		
		// Try to decode
		StringBuilder sb = new StringBuilder(toParse);
		for (int i=0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			if (c == '\\') {
				switch (sb.charAt(i + 1)) {
				case '"':
				case '\\':
				case '/':
					sb.deleteCharAt(i);
					break;
				case 'b':
					sb.replace(i, i+2, "\b");
					break;
				case 'f':
					sb.replace(i, i+2, "\f");
					break;
				case 'n':
					sb.replace(i, i+2, "\n");
					break;
				case 'r':
					sb.replace(i, i+2, "\r");
					break;
				case 't':
					sb.replace(i, i+2, "\t");
					break;
				case 'u':
					char decoded = (char) Integer.parseInt(sb.substring(i+2, i+6), 16);
					sb.delete(i, i+6);
					sb.insert(i, decoded);
				}
			}
		}
		
		JSONString s = new JSONString();
		s.parsed = sb.toString();
		return s;
	}
}
