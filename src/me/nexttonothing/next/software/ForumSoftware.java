package me.nexttonothing.next.software;

import me.nexttonothing.next.configs.MainConfig;
import me.nexttonothing.next.boards.myBB;
import me.nexttonothing.next.boards.phpBB;
import me.nexttonothing.next.boards.smf;
import me.nexttonothing.next.boards.xenforo;
import me.nexttonothing.next.boards.vbulletin;

public class ForumSoftware {
    public static Software getSoftwareObject(String software,String playername, MainConfig config){
        if(software != null){
            if(software.equalsIgnoreCase("phpbb")){
                return new phpBB(playername, config);
            }
            if(software.equalsIgnoreCase("mybb")){
                return new myBB(playername, config);
            }
            if(software.equalsIgnoreCase("smf")){
                return new smf(playername, config);
            }
            if(software.equalsIgnoreCase("xenforo")){
                return new xenforo(playername,config);
            }
            if(software.equalsIgnoreCase("vbulletin")){
                return new vbulletin(playername,config);
            }
        }
        System.out.println("ForumSoftware not Found!");
        return null;
    } 
}
