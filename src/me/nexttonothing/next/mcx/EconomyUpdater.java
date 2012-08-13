package me.nexttonothing.next.mcx;

import org.bukkit.Bukkit;
import java.util.Timer;
import java.util.TimerTask;

import me.nexttonothing.next.configs.MainConfig;
import me.nexttonothing.next.software.ForumSoftware;
import me.nexttonothing.next.software.Software;

public class EconomyUpdater {
    public MainConfig config;
    private Timer timer = new Timer();
    private int timerstat = 0;
    
    public EconomyUpdater(MainConfig config) {
        this.config = config;
        timer.schedule(
                new update(),
                0,
                ((int) config.getInt("economy.checkRate")) * (60000)
        );
        timerstat = 1;
    }
    
    public int stop() {
        if(timerstat == 1) {
            timer.cancel();
            timer = new Timer();
            timerstat = 0;
        }
        return timerstat;
    }
    
    public int start() {
        if(timerstat == 0) {
            timer.schedule(
                    new update(),
                    0,
                    ((int) config.getInt("economy.checkRate")) * (60000)
            );
            timerstat = 1;
        }
        return timerstat;
    }
    
    class update extends TimerTask {
        @Override
        public void run() {
            if(payPosts()) {
                Bukkit.broadcastMessage(McX.lang.get("locale.player.notification.economyPaid"));
            } else {
                Bukkit.broadcastMessage(McX.lang.get("locale.player.notification.economyCompat"));
            }
            config.editValue("economy.lastCheck", (int) System.currentTimeMillis());
            config.save();
        }
    }
    
    private boolean payPosts() {
        Software postchecker;
        postchecker = ForumSoftware.getSoftwareObject(McX.ForumType, config.getString("mysql.verifyuser"), config);
        return postchecker.payPosts();
    }
}