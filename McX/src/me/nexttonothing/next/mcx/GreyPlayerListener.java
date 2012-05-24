package me.nexttonothing.next.mcx;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.Listener;

import me.nexttonothing.next.software.ForumSoftware;
import me.nexttonothing.next.software.Software;

public class GreyPlayerListener implements Listener{
	public McX plugin;

	public GreyPlayerListener(McX instance){
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event){
			if (plugin.ac == State.On) {
				Software player;
				System.out.println(plugin.getMainConfig().getString("mysql.forumtype"));
				player = ForumSoftware.getSoftwareObject(plugin.getMainConfig().getString("mysql.forumtype"), event.getPlayer().getName().toLowerCase(), plugin.getMainConfig());
				if(player != null){
					if(player.getRegistrationValue(false)){
						System.out.println((McX.lang.get("locale.player.notification.auth")).replaceAll("<player>", event.getPlayer().getName()));
						plugin.grey.remove(event.getPlayer());
					}
				}
				else {
					plugin.grey.add(event.getPlayer());
				}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(plugin.getMainConfig().getString("general.greylist.protection.lootItems").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcx.vip.override")))
				event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(plugin.getMainConfig().getString("general.greylist.protection.dropItems").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcx.vip.override"))){
				event.getPlayer().sendMessage(McX.lang.get("locale.player.notification.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		if(plugin.getMainConfig().getString("general.greylist.protection.chat").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcx.vip.override"))){
				event.getPlayer().sendMessage(McX.lang.get("locale.player.notification.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(plugin.getMainConfig().getString("general.greylist.protection.interact").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On&& !(event.getPlayer().hasPermission("mcx.vip.override"))) {
				event.getPlayer().sendMessage(McX.lang.get("locale.player.notification.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(plugin.getMainConfig().getString("general.greylist.protection.command").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcx.vip.override"))) {
				event.getPlayer().sendMessage(McX.lang.get("locale.player.notification.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event){
		if(plugin.getMainConfig().getString("general.greylist.protection.damageEntities").equals("true"))
			if(plugin.ac == State.On) {
				if(!(plugin.getMainConfig().getBoolean("general.greylist.protection.damageEntities")))
					if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
						EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) event;
						Player p;
						if (ed.getDamager() instanceof Player) {
							p = (Player) ed.getDamager();
							if(plugin.grey.contains(p) && !(p.hasPermission("mcx.vip.override"))){
								p.sendMessage(McX.lang.get("locale.player.notification.registerFirst"));
								event.setCancelled(true);
							}
						}
					}
			}
	}
}