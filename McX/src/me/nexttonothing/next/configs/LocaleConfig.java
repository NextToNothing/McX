package me.nexttonothing.next.configs;

import me.nexttonothing.next.mcx.McX;
import me.nexttonothing.next.util.parseLang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class LocaleConfig {
    McX plugin;
    YamlConfiguration config;
    YamlConfigurationOptions configO;
    Boolean hasChanged = false;

    public LocaleConfig(McX instance) {
        plugin = instance;

        config = plugin.locale;

        configO = config.options();
        configO.indent(4);
    }
    
    public String getOption(String option) {
        if (config.isSet(option))
            return parseLang.parseColor(config.getString(option));
        return "";
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(plugin.localeF);
        plugin.locale = config;
        config.options().indent(4);
    }

    public void save() {
        try {
            plugin.locale = config;
            plugin.locale.save(plugin.localeF);

            hasChanged = false;

            System.out.println(plugin.getLocale().getOption("locale.conf.save") + "locale.yml");
        } catch (Exception ignored) {}
    }

    void defaultLocale() {
        configO.header("Locale File");

		config.set("locale.validate.start","[McX] Validating Configs...");
		config.set("locale.validate.teach", "[McX] Teaching McX ");
		config.set("locale.validate.confVal", "[McX] Validating configuration file ");
		config.set("locale.validate.confSuccess","[McX] Successfully validated configuration file ");
		config.set("locale.validate.unknownType","[McX] Type is unknown! Disabling Plugin");
		config.set("locale.validate.mysql.hostVal","[McX] Verifying MySQL Host");
		config.set("locale.validate.mysql.hostFail","[McX] Verification of MySQL host failed! Disabling Plugin.");
		config.set("locale.validate.mysql.userVal","[McX] Verifying MySQL User");
		config.set("locale.validate.mysql.userFail","[McX] Verification of MySQL user failed! Disabling Plugin.");
		config.set("locale.validate.mysql.databaseVal","[McX] Verifying MySQL Database");
		config.set("locale.validate.mysql.databaseFail","[McX] Verification of MySQL database failed! Disabling Plugin.");
		config.set("locale.validate.mysql.testVal","[McX] Verifying MySQL connection");
		config.set("locale.validate.mysql.testSuccess","[McX] MySql was tested by user verification on player `<player>`!");
		config.set("locale.validate.mysql.testFail","[McX] MySql testing was unsuccessful! Are the config files correct?");
		config.set("locale.plugins.economy", "[McX] Economy Support Activated!");
		config.set("locale.plugins.economyMissing", "[McX] You must enable the plugin `Vault` to use the Economy Xtra");
		config.set("locale.plugins.perms", "[McX] Permissions Support Activated!");
		config.set("locale.conf.loading","[McX] Loading configuration file ");
		config.set("locale.conf.loadSuccess","[McX] Successfully loaded configuration file ");
		config.set("locale.conf.save","[McX] Successfully saved configuration file ");
		config.set("locale.update.version","[McX] Version Info:");
		config.set("locale.update.currversion","[McX]   Current Version: ");
		config.set("locale.update.latestversion","[McX]   Latest Version: ");
		config.set("locale.player.action.seton","[McX] Activated!");
		config.set("locale.player.action.setoff","[McX] Deactivated!");
		config.set("locale.player.notification.on","[McX] Plugin is currently activated");
		config.set("locale.player.notification.off","[McX] Plugin is currently deactivated");
		config.set("locale.player.notification.registerFirst","[McX] First register on the Forum!");
		config.set("locale.player.notification.auth","[McX] Player `<player>` is authenticated!");
		config.set("locale.player.notification.kick","Server is Reloading; Please register on the Forum!");
		config.set("locale.player.notification.whitelist","Not on Whitelist! Register on the Forum;");
		config.set("locale.player.notification.newUpdate","[McX] New Update Avaliable!");
		config.set("locale.player.notification.greyActivated","[McX] Greylist activated");
		config.set("locale.player.notification.whiteActivated","[McX] Whitelist activated");
		config.set("locale.player.notification.noPerm","&2[McX] Permission error!");
		config.set("locale.player.notification.confReload", "&e[McX] Reloaded configuration file ");
		config.set("locale.player.notification.confAllReload", "&e[McX] All configuration files reloaded");
		config.set("locale.player.notification.economyPaid", "&e[McX] All forum posts have been credited!");
		config.set("locale.player.notification.economyCompat", "&e[McX] There is an economy posts <> forum compatibility issue! Check for updates or disable this feature!");
		config.set("locale.player.notification.economyUpdater.start", "&e[McX] The EconomyUpdater has been started!");
		config.set("locale.player.notification.economyUpdater.started", "&e[McX] EconomyUpdater is already running.");
		config.set("locale.player.notification.economyUpdater.stop", "&e[McX] The EconomyUpdater has been stopped!");
		config.set("locale.player.notification.economyUpdater.stopped", "&e[McX] EconomyUpdater is already stopped.");
		config.set("locale.misc.enabled","[McX] Enabled McX: Minecraft Xtra, version ");
		config.set("locale.misc.disabled","[McX] Disabled McX: Minecraft Xtra, version ");
		
		/*config.set("System.lang.teach", "[McX] Teaching McX ");
		config.set("System.Validate.start","[McX] Validating Configs");
		config.set("System.Validate.langSucess","[McX] Language Config Check sucessfull");
		config.set("System.Validate.confSucess","[McX] Configs validated");
		config.set("System.Validate.mysql.host","[McX] Creating MySQL Host");
		config.set("System.Validate.mysql.port","[McX] Creating MySQL Port");
		config.set("System.Validate.mysql.user","[McX] Creating MySQL User");
		config.set("System.Validate.mysql.password","[McX] Creating MySQL Password");
		config.set("System.Validate.mysql.database","[McX] Creating MySQL Database");
		config.set("System.Validate.mysql.prefix","[McX] Creating MySQL Prefix");
		config.set("System.Validate.mysql.testedSucess","[McX] MySql was tested by User Verifikation <player>!");
		config.set("System.Validate.mysql.testedFail","[McX] MySql testing was unsuccessful! Are the config files correct?");
		config.set("System.conf.loading","[McX] Loading Configs");
		config.set("System.conf.loadSucess","[McX] Configs loaded");
		config.set("System.lang.loading","[McX] Loading language");
		config.set("System.lang.loadSucess","[McX] Language loaded");
		config.set("System.state.seton","[McX] Activated!");
		config.set("System.state.on","[McX] Plugin is currently activated");
		config.set("System.state.off","[McX] Plugin is currently deactivated");
		config.set("System.state.setoff","[McX] Deactivated!");
		config.set("System.info.registerFirst","[McX] First register on the Forum!");
		config.set("System.info.auth","[McX] Player <player> is authenticated!");
		config.set("System.info.kick","Server is Reloading; Please register on the Forum!");
		config.set("System.info.whitelist","Not on Whitelist! Register on the Forum;");
		config.set("System.info.newUpdate","[McX] New Update!");
		config.set("System.info.greyActivated","[McX] Greylist activated");
		config.set("System.info.whiteActivated","[McX] Whitelist activated");
		config.set("System.error.unknownType","[McX] Type is unknown! Disableing Plugin");
		config.set("System.error.noPerm","&2[McX] Permission error!");
		config.set("System.update.version","[McX] Version Info:");
		config.set("System.update.currversion","[McX] Current Version: ");
		config.set("System.update.latestversion","[McX] Latest Version: ");
		config.set("System.misc.enabled","[McX] loaded ->");
		config.set("System.misc.disabled","[McX] unloaded ->");*/

        hasChanged = true;
    }

    public void load() {
		System.out.println("[McX] Validating configuration file locale.yml");
        if (!(plugin.localeF.exists()))
            defaultLocale();

		checkOption("locale.validate.start","[McX] Validating Configs...");
		checkOption("locale.validate.teach", "[McX] Teaching McX ");
		checkOption("locale.validate.confVal", "[McX] Validating configuration file ");
		checkOption("locale.validate.confSuccess","[McX] Successfully validated configuration file ");
		checkOption("locale.validate.unknownType","[McX] Type is unknown! Disabling Plugin");
		checkOption("locale.validate.mysql.hostVal","[McX] Verifying MySQL Host");
		checkOption("locale.validate.mysql.hostFail","[McX] Verification of MySQL host failed! Disabling Plugin.");
		checkOption("locale.validate.mysql.userVal","[McX] Verifying MySQL User");
		checkOption("locale.validate.mysql.userFail","[McX] Verification of MySQL user failed! Disabling Plugin.");
		checkOption("locale.validate.mysql.databaseVal","[McX] Verifying MySQL Database");
		checkOption("locale.validate.mysql.databaseFail","[McX] Verification of MySQL database failed! Disabling Plugin.");
		checkOption("locale.validate.mysql.testVal","[McX] Verifying MySQL connection");
		checkOption("locale.validate.mysql.testSuccess","[McX] MySql was tested by user verification on player `<player>`!");
		checkOption("locale.validate.mysql.testFail","[McX] MySql testing was unsuccessful! Are the config files correct?");
		checkOption("locale.plugins.economy", "[McX] Economy Support Activated!");
		checkOption("locale.plugins.economyMissing", "[McX] You must enable the plugin `Vault` to use the Economy Xtra");
		checkOption("locale.plugins.perms", "[McX] Permissions Support Activated!");
		checkOption("locale.conf.loading","[McX] Loading configuration file ");
		checkOption("locale.conf.loadSuccess","[McX] Successfully loaded configuration file ");
		checkOption("locale.conf.save","[McX] Successfully saved configuration file ");
		checkOption("locale.update.version","[McX] Version Info:");
		checkOption("locale.update.currversion","[McX]   Current Version: ");
		checkOption("locale.update.latestversion","[McX]   Latest Version: ");
		checkOption("locale.player.action.seton","[McX] Activated!");
		checkOption("locale.player.action.setoff","[McX] Deactivated!");
		checkOption("locale.player.notification.on","[McX] Plugin is currently activated");
		checkOption("locale.player.notification.off","[McX] Plugin is currently deactivated");
		checkOption("locale.player.notification.registerFirst","[McX] First register on the Forum!");
		checkOption("locale.player.notification.auth","[McX] Player `<player>` is authenticated!");
		checkOption("locale.player.notification.kick","Server is Reloading; Please register on the Forum!");
		checkOption("locale.player.notification.whitelist","Not on Whitelist! Register on the Forum;");
		checkOption("locale.player.notification.newUpdate","[McX] New Update Avaliable!");
		checkOption("locale.player.notification.greyActivated","[McX] Greylist activated");
		checkOption("locale.player.notification.whiteActivated","[McX] Whitelist activated");
		checkOption("locale.player.notification.noPerm","&2[McX] Permission error!");
		checkOption("locale.player.notification.confReload", "&e[McX] Reloaded configuration file ");
		checkOption("locale.player.notification.confAllReload", "&e[McX] All configuration files reloaded");
		checkOption("locale.player.notification.economyPaid", "&e[McX] All forum posts have been credited!");
		checkOption("locale.player.notification.economyCompat", "&e[McX] There is an economy posts <> forum compatibility issue! Check for updates or disable this feature!");
		checkOption("locale.player.notification.economyUpdater.start", "&e[McX] The EconomyUpdater has been started!");
		checkOption("locale.player.notification.economyUpdater.started", "&e[McX] EconomyUpdater is already running.");
		checkOption("locale.player.notification.economyUpdater.stop", "&e[McX] The EconomyUpdater has been stopped!");
		checkOption("locale.player.notification.economyUpdater.stopped", "&e[McX] EconomyUpdater is already stopped.");
		checkOption("locale.misc.enabled","[McX] Enabled McX: Minecraft Xtra, version ");
		checkOption("locale.misc.disabled","[McX] Disabled McX: Minecraft Xtra, version ");

        if (hasChanged) {
            configO.header("Locale File");
            save();
        }
        System.out.println(getOption("locale.validate.confSuccess") + "locale.yml");
    }

    void checkOption(String option, Object dOption) {
        if (config.get(option) == null) {
            config.set(option, dOption);
            hasChanged = true;
        }
    }
}
