package me.nexttonothing.next.mcx;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.nexttonothing.next.software.ForumSoftware;
import me.nexttonothing.next.software.Software;

    public class RegisteredPlayerListener implements Listener {
        public McX plugin;
        
        public RegisteredPlayerListener(McX instance) {
            plugin = instance;
        }

        @EventHandler(priority = EventPriority.NORMAL)
        public void onPlayerJoin(PlayerJoinEvent event) {
            Software u;
            u = ForumSoftware.getSoftwareObject(plugin.getMainConfig().getString("mysql.forumtype"), event.getPlayer().getName().toLowerCase(), plugin.getMainConfig());
            if(u.getRegistrationValue(true)){
                if(plugin.ac == State.On)
                    if(McX.permission != null) {
                        if(plugin.getMainConfig().getString("general.syncGroups").equalsIgnoreCase("true")) {
                            if(!(McX.permission.playerInGroup((String)null, event.getPlayer().getName().toLowerCase(), u.getForumGroup()))){
                                System.out.println("[McX] Starting SQL-GroupSync: for `" + event.getPlayer().getName() + "` to  `" + u.getForumGroup() + "`");
                                p(event.getPlayer(),u.getForumGroup());
                                event.getPlayer().sendMessage("[McX] Your Group was set to: " +  u.getForumGroup());
                            }
                        }
                    }
                    //if(McX.economy != null)
                    //    McX.economy.depositPlayer(event.getPlayer().getName().toLowerCase(), (u.getNewPostCount() * plugin.getMainConfig().getInt("economy.moneyPerPost")));
                info(event.getPlayer());
            }
            else
                plugin.grey.add(event.getPlayer());
            if(!(event.getPlayer().hasPermission("mcx.user.join") || event.getPlayer().hasPermission("mcx.vip.override"))){
                event.getPlayer().kickPlayer(McX.lang.get("locale.player.notification.noPerm"));
            }
        }
        public  void info(Player p){
            if (p.hasPermission("mcx.maintainer.update"))
                if(plugin.Version.isOutdated())
                    p.sendMessage(McX.lang.get("locale.player.notification.newUpdate"));
        }
        public void p(Player p,String o)
        {
            McX.permission.playerAddGroup((String)null,p.getName(),o);
            for(String s : McX.permission.getGroups())
            {
                if(!o.equals(s))
                    if(McX.permission.playerInGroup((String)null,p.getName(), s)){
                        System.out.println("|" + s + "|");
                        McX.permission.playerRemoveGroup((String)null,p.getName(), s);
                    }
                    
            }
        }
}
