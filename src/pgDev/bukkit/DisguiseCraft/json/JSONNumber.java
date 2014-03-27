package pgDev.bukkit.DisguiseCraft.json;

/**
 * This class merely wraps a number in a JSON value
 * 
 * From http://www.json.org/:
 * A number is very much like a C or Java number, except that the octal and hexadecimal formats are not used.
 * 
 * @author Devil Boy
 */
public class JSONNumber extends JSONValue {

	private double parsed;
	
	public double get() {
		return parsed;
	}
	
	@Override
	public String toString() {
		String output = Double.toString(parsed);
		
		if (output.endsWith(".0")) {
			output = output.substring(0, output.length() - 2);
		}
		
		return output;
	}
	
	public static JSONNumber parseNumber(String toParse) {
		JSONNumber n = new JSONNumber();
		
		n.parsed = Double.parseDouble(toParse);
		
		return n;
	}
}
