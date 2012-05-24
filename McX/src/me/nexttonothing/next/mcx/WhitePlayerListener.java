package me.nexttonothing.next.mcx;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import me.nexttonothing.next.software.ForumSoftware;
import me.nexttonothing.next.software.Software;

public class WhitePlayerListener implements Listener {
	public McX plugin;

	public WhitePlayerListener(McX instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (plugin.ac == State.On) {
			Software d;
			d = ForumSoftware.getSoftwareObject(plugin.getMainConfig().getString("mysql.forumType"), event.getPlayer().getName(), plugin.getMainConfig());
			//d = new ForumUser(event.getPlayer().getName(), plugin.getConfig(),false);
			if(!d.getRegistrationValue(false))
				event.disallow(Result.KICK_OTHER, McX.lang.get("locale.player.notification.whitelist"));
		}
	}
}
