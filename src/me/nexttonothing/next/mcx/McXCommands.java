package me.nexttonothing.next.mcx;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import me.nexttonothing.next.software.ForumSoftware;
import me.nexttonothing.next.software.Software;
import me.nexttonothing.next.util.parseLang;

public class McXCommands implements CommandExecutor {

    private McX plugin;

    public McXCommands(McX plugin) {
        this.plugin = plugin;
    }
    
    private String parseColor(String data) {
        new parseLang();
        return me.nexttonothing.next.util.parseLang.parseColor(data);
    }

    //@Override
    public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
        if (args.length == 0)
            return sendHelp(sender);
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("on"))
                if (sender.hasPermission("mcx.maintainer.on")) {
                    return plugin.setOn();
                } else
                    return permError(sender);
            if (args[0].equalsIgnoreCase("off"))
                if (sender.hasPermission("mcx.maintainer.off")) {
                    return plugin.setOff();
                } else
                    return permError(sender);
            if (args[0].equalsIgnoreCase("status"))
                if (sender.hasPermission("mcx.maintainer.status")) {
                    return plugin.status(sender);
                } else
                    return permError(sender);
            if(args.length == 1 && args[0].equalsIgnoreCase("reload"))
                if (sender.hasPermission("mcx.maintainer.reload")) {
                    return sendReloadHelp(sender);
                } else
                    return permError(sender);
            if(args.length == 1 && args[0].equalsIgnoreCase("economy"))
                if (sender.hasPermission("mcx.maintainer.economyupdater")) {
                    return sendEconomyUpdaterHelp(sender);
                } else
                    return permError(sender);
            if (plugin.ac == State.On) {
                if (args.length == 2) {
                    if (sender.hasPermission("mcx.user.lookup")) {
                        if (args[0].equalsIgnoreCase("lookup")) {
                            Software x;
                            x = ForumSoftware.getSoftwareObject(plugin.getMainConfig().getString("mysql.forumtype"),args[1], plugin.getMainConfig());
                            //x = new ForumUser(args[1], plugin.getConfig());
                            // sender.sendMessage("Player Account Status: ");
                            if (x.getRegistrationValue(true)) {
                                sender.sendMessage(ChatColor.YELLOW + "[McX] Player `" + args[1] +"` is " + ChatColor.GREEN + "Active!");
                                return true;
                            }
                            sender.sendMessage(ChatColor.YELLOW + "[McX] Player `" + args[1] +"` is " + ChatColor.RED + "Inactive!");
                            return true;
                        }
                    } else
                        return permError(sender);
                    if(sender.hasPermission("mcx.maintainer.economyupdater")) {
                        if(args[0].equalsIgnoreCase("economy"))
                            if (args[1].equalsIgnoreCase("on")) {
                                if(plugin.EconomyUpdater.start() == 1) {
                                    sender.sendMessage(ChatColor.YELLOW + plugin.getLocale().getOption("locale.player.notification.economyUpdater.start"));
                                } else {
                                    sender.sendMessage(ChatColor.YELLOW + plugin.getLocale().getOption("locale.player.notification.economyUpdater.started"));
                                }
                                return true;
                            } else if(args[1].equalsIgnoreCase("off")) {
                                if(plugin.EconomyUpdater.stop() == 0) {
                                    sender.sendMessage(ChatColor.YELLOW + plugin.getLocale().getOption("locale.player.notification.economyUpdater.stop"));
                                } else {
                                    sender.sendMessage(ChatColor.YELLOW + plugin.getLocale().getOption("locale.player.notification.economyUpdater.stopped"));
                                }
                                return true;
                            } else if(args[1].equalsIgnoreCase("forcepay")) {
                                if(plugin.EconomyUpdater.stop() == 0) {
                                    plugin.EconomyUpdater.start();
                                }
                                return true;
                            } else 
                                return sendEconomyUpdaterHelp(sender);
                    } else
                        return permError(sender);
                    if(args[0].equalsIgnoreCase("reload")) {
                        if(args[1].equalsIgnoreCase("all")) {
                            if(sender.hasPermission("mcx.maintainer.reload.all")) {
                                plugin.getMainConfig().reload();
                                plugin.getLocale().reload();
                                sender.sendMessage(parseColor(plugin.getLocale().getOption("lang.player.notification.confAllReload")));
                                return true;
                            } else
                                return permError(sender);
                        } else if(args[1].equalsIgnoreCase("config"))
                            if(sender.hasPermission("mcx.maintainer.reload.config")) {
                                plugin.getMainConfig().reload();
                                sender.sendMessage(parseColor(plugin.getLocale().getOption("lang.player.notification.confReload")) + "config.yml");
                                return true;
                            } else 
                                return permError(sender);
                        else if(args[1].equalsIgnoreCase("locale")) 
                            if(sender.hasPermission("mcx.maintainer.reload.locale")) {
                                plugin.getLocale().reload();
                                sender.sendMessage(parseColor(plugin.getLocale().getOption("lang.player.notification.confReload")) + "locale.yml");
                                return true;
                            } else 
                                return permError(sender);
                        else 
                            return sendReloadHelp(sender);
                    }
                }
                sender.sendMessage("Unknown Command");
            }
        }
        return sendHelp(sender);
    }
    
    public boolean sendHelp(CommandSender sender) {
        if (plugin.ac == State.On) {
            sender.sendMessage("This could be a Help!");
            sender.sendMessage("mcx on -> Enable Plugin");
            sender.sendMessage("mcx off -> Disable Plugin");
            sender.sendMessage("mcx status -> Display Status");
            sender.sendMessage("mcx reload -> List reload commands");
            sender.sendMessage("mcx economy -> List economyupdater commands");
            sender.sendMessage("mcx lookup <username> -> Display whether player has a forum account or not");
            return true;
        }
        return false;
    }
    
    public boolean sendReloadHelp(CommandSender sender) {
        if (plugin.ac == State.On) {
            sender.sendMessage("This could be a Help!");
            sender.sendMessage("mcx reload all -> Reload all config files");
            sender.sendMessage("mcx reload config -> Reload config.yml file");
            sender.sendMessage("mcx reload locale -> Reload locale.yml file");
            return true;
        }
        return false;
    }
    
    public boolean sendEconomyUpdaterHelp(CommandSender sender) {
        if (plugin.ac == State.On) {
            sender.sendMessage("This could be a Help!");
            sender.sendMessage("mcx economy on -> Enable economy updater");
            sender.sendMessage("mcx economy off -> Disable economy updater");
            sender.sendMessage("mcx economy forcepay -> Forces a pay out for forum posts");
            return true;
        }
        return false;
    }

    public boolean permError(CommandSender sender) {
        sender.sendMessage(McX.lang.get("locale.player.notification.noPerm"));
        return true;
    }
}