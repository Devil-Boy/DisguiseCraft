package pgDev.bukkit.DisguiseCraft.update;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.json.JSONArray;
import pgDev.bukkit.DisguiseCraft.json.JSONObject;
import pgDev.bukkit.DisguiseCraft.json.JSONString;
import pgDev.bukkit.DisguiseCraft.json.JSONValue;


public class DCUpdateChecker {
	
	public static String dcInfoQuery = "https://api.curseforge.com/servermods/files?projectIds=37008";
	
	public static String getLatestVersion() {
		try {
			URL devPage = new URL(dcInfoQuery);
			BufferedReader in = new BufferedReader(new InputStreamReader(devPage.openStream()));
			
			// Just in case the response has newlines
			StringBuilder sb = new StringBuilder();
			
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			
			in.close();
			
			// Pull latest version out of JSON response
			JSONArray fileList = (JSONArray) JSONValue.parse(sb.toString());
			JSONObject latestFile = (JSONObject) fileList.get(fileList.size() - 1);
			JSONString fileName = (JSONString) latestFile.get("name");
			
			return fileName.get();
		} catch (Exception e) {
			DisguiseCraft.logger.log(Level.WARNING , "Error checking for updates", e);
		}
		return "Error during check!";
	}
	
	// Compare version Strings
	public static boolean isUpToDate(String current, String latest) {
		// Strip letters and whitespace
		current = current.replace(" ", "").replaceAll("[^\\d.]", "");
		latest = latest.replace(" ", "").replaceAll("[^\\d.]", "");
		
		// Quick match compare
		if (current.equals(latest)) {
			return true;
		}
		
		// Split into comparable segments
		String[] cSegments = current.split("\\.");
		String[] lSegments = latest.split("\\.");
		
		// Compare the versions
		try {
			for (int i=0; i < cSegments.length || i < lSegments.length; i++) {
				if (i >= cSegments.length) {
					if (i >= lSegments.length) {
						return true;
					} else {
						if (Integer.decode(lSegments[i]) > 0) {
							return false;
						} else {
							return true;
						}
					}
				} else if (i >= lSegments.length) {
					return true;
				} else {
					if (Integer.decode(cSegments[i]) > Integer.decode(lSegments[i])) {
						return true;
					} else if (Integer.decode(lSegments[i]) > Integer.decode(cSegments[i])) {
						return false;
					}
				}
			}
		} catch (NumberFormatException e) {
			DisguiseCraft.logger.log(Level.WARNING, "Version numbers could not be parsed", e);
		}
		
		// Shouldn't get here
		DisguiseCraft.logger.log(Level.WARNING, "Version numbers were not correctly parsed");
		return true;
	}
}
