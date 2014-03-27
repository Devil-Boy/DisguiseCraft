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
	
	/**
	 * Gets the wrapped double value
	 * @return The internal double value
	 */
	public double get() {
		return parsed;
	}
	
	/**
	 * Returns this number back to JSON form
	 */
	@Override
	public String toString() {
		String output = Double.toString(parsed);
		
		if (output.endsWith(".0")) {
			output = output.substring(0, output.length() - 2);
		}
		
		return output;
	}
	
	/**
	 * Parses this number as a double
	 * @param toParse The String containing the number
	 * @return The JSONNumber object containing the numerical data
	 * @throws IllegalArgumentException If parsing of the number failed
	 */
	public static JSONNumber parseNumber(String toParse) {
		JSONNumber n = new JSONNumber();
		
		try {
			n.parsed = Double.parseDouble(toParse);
		} catch (Exception e) { // NumberFormatException | NullPointerException
			throw new IllegalArgumentException("Number could not be parsed", e);
		}
		
		return n;
	}
}
