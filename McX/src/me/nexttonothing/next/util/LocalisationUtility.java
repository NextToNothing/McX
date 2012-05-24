package me.nexttonothing.next.util;

import me.nexttonothing.next.mcx.McX;
import me.nexttonothing.next.configs.LocaleConfig;

public class LocalisationUtility {
	public LocaleConfig config;
	public LocalisationUtility(McX p){
		config = p.getLocale();
	}
	
	public String get(String node){
		return config.getOption(node);
	}
}
