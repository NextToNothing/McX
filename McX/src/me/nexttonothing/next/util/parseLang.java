package me.nexttonothing.next.util;

/*
 * Thanks to Bone008
 * http://forums.bukkit.org/threads/solved-parse-for-color-codes.25965/#post-467467
 */

public class parseLang {
	public static String parseColor(String p){
	        if(p == null) return null;
	        return p.replaceAll("&([0-9a-f])", "\u00A7$1");		
	}
}
