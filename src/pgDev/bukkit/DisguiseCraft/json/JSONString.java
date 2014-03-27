package pgDev.bukkit.DisguiseCraft.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
	
	public String get() {
		return parsed;
	}
	
	@Override
	public String toString() {
		return "\"" + parsed + "\"";
	}

	public static JSONString parseString(String toParse) {
		JSONString s = new JSONString();
		s.parsed = toParse;
		
		// Find and remove beginning quote
		if (s.parsed.startsWith("\"")) {
			s.parsed = s.parsed.substring(1, s.parsed.length());
		} else {
			throw new IllegalArgumentException("No beginning quote found");
		}
		
		// Find and remove ending quote
		if (s.parsed.endsWith("\"")) {
			s.parsed = s.parsed.substring(0, s.parsed.length() - 1);
		} else {
			throw new IllegalArgumentException("No ending quote found");
		}
		
		// Try to decode
		try {
			s.parsed = URLDecoder.decode(s.parsed, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return s;
	}
}
