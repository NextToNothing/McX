package me.nexttonothing.next.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import me.nexttonothing.next.mcx.McX;

public class Saver {
    public McX plugin;
    public Saver(McX p)
    {    
        plugin = p;
    }
    public void save(FileConfiguration fc) {
        try {
            fc.save(plugin.getDataFolder() + "/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void save(FileConfiguration fc, String path) {
        try {
            fc.save(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void load(FileConfiguration fc,LocalisationUtility lang,File dataFolder) {
        System.out.println(lang.get("System.conf.loading"));
        try {
            fc.load(dataFolder + "/config.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(lang.get("System.conf.loadSucess"));
    }
}
