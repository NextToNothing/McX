package me.nexttonothing.next.configs;

import me.nexttonothing.next.mcx.McX;
import me.nexttonothing.next.util.parseLang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class MainConfig {
    McX plugin;
    YamlConfiguration config;
    YamlConfigurationOptions configO;
    Boolean hasChanged = false;

    public MainConfig(McX instance) {
        plugin = instance;

        config = plugin.config;

        configO = config.options();
        configO.indent(4);
    }
    
    public String getOption(String option) {
        if (config.isSet(option))
            return parseLang.parseColor(config.getString(option));
        return "";
    }
    
    public String getString(String option) {
        if (config.isSet(option))
            return parseLang.parseColor(config.getString(option));
        return "";
    }
    
    public int getInt(String option) {
        if (config.isSet(option))
            return config.getInt(option);
        return 0;
    }
    
    public boolean getBoolean(String option) {
        if (config.isSet(option))
            return config.getBoolean(option);
        return false;
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(plugin.configF);
        plugin.config = config;
        config.options().indent(4);
    }

    public void save() {
        try {
            plugin.config = config;
            plugin.config.save(plugin.configF);

            hasChanged = false;

            System.out.println(plugin.getLocale().getOption("locale.conf.save") + "config.yml");
        } catch (Exception ignored) {}
    }

    void defaultConfig() {
        configO.header("Configuration File");

		config.set("mysql.forumtype", "phpbb");
		config.set("mysql.verifyuser", "anonymous");
		config.set("mysql.host", "localhost");
		config.set("mysql.port", "3306");
		config.set("mysql.user", "root");
		config.set("mysql.password", "test");
		config.set("mysql.database", "mc");
		config.set("mysql.prefix", "phpbb3_");
		config.set("general.type", "Greylist"); //Greylist , Whitelist
		config.set("general.authType", "Field"); //Field , Username 
		config.set("field.id","1");
		config.set("general.syncGroups","false");
		config.set("general.groupOverride","false");
		config.set("general.greylist.protection.damageEntities", "true"); //true , false
		config.set("general.greylist.protection.lootItems", "true"); //true , false
		config.set("general.greylist.protection.dropItems", "true"); //true , false
		config.set("general.greylist.protection.chat", "true"); //true , false
		config.set("general.greylist.protection.interact", "true"); //true , false
		config.set("general.greylist.protection.command", "true"); //true , false
		config.set("economy.enabled", "false");
		config.set("economy.moneyPerPost", 0);
		config.set("economy.checkRate", 8);
		config.set("economy.lastCheck", 0);

        hasChanged = true;
    }

    public void load() {
    	System.out.println(plugin.getLocale().getOption("locale.validate.confVal") + "config.yml");
        if (!(plugin.configF.exists()))
            defaultConfig();

		checkOption("mysql.forumtype", "phpbb");
		checkOption("mysql.verifyuser", "anonymous");
		checkOption("mysql.host", "localhost");
		checkOption("mysql.port", "3306");
		checkOption("mysql.user", "root");
		checkOption("mysql.password", "test");
		checkOption("mysql.database", "mc");
		checkOption("mysql.prefix", "phpbb3_");
		checkOption("general.type", "Greylist"); //Greylist , Whitelist
		checkOption("general.authType", "Field"); //Field , Username 
		checkOption("field.id","1");
		checkOption("general.syncGroups","false");
		checkOption("general.groupOverride","false");
		checkOption("general.greylist.protection.damageEntities", "true"); //true , false
		checkOption("general.greylist.protection.lootItems", "true"); //true , false
		checkOption("general.greylist.protection.dropItems", "true"); //true , false
		checkOption("general.greylist.protection.chat", "true"); //true , false
		checkOption("general.greylist.protection.interact", "true"); //true , false
		checkOption("general.greylist.protection.command", "true"); //true , false
		checkOption("economy.enabled", "false");
		checkOption("economy.moneyPerPost", 0);
		checkOption("economy.checkRate", 8);
		checkOption("economy.lastCheck", 0);

        if (hasChanged) {
            configO.header("Configuration File");
            save();
        }
    	System.out.println(plugin.getLocale().getOption("locale.validate.confSuccess") + "config.yml");
    }

    void checkOption(String option, Object dOption) {
        if (config.get(option) == null) {
            config.set(option, dOption);
            hasChanged = true;
        }
    }

    /*void editOption(YamlConfiguration config, String oldOption, String newOption) {
        if (config.isSet(oldOption)) {
            config.set(newOption, config.get(oldOption));
            config.set(oldOption, null);
            hasChanged = true;
        }
    }

    void removeOption(YamlConfiguration config, String option) {
        if (config.isSet(option)) {
            config.set(option, null);
            hasChanged = true;
        }
    }*/

    public void editValue(String option, Object newValue) {
        if (config.isSet(option))
            if (config.get(option) != newValue) {
                config.set(option, newValue);
                hasChanged = true;
            }
    }
}
