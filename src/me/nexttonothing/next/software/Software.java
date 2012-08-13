package me.nexttonothing.next.software;
import me.nexttonothing.next.configs.MainConfig;

public class Software {
    public String name;
    public MainConfig config;
    public int userType;
    public int userId;
    public int groupId;
    public String forumUserName;
    
    public Software(String name, MainConfig config, boolean a) {
        this.name = name.toLowerCase();
        this.config = config;
    }
    
    public Software(String name, MainConfig config) {
        this.name = name.toLowerCase();
        this.config = config;
    }
    
    public boolean getRegistrationValue(boolean o) {
        if (config.getOption("general.authType").equalsIgnoreCase("username"))
            return this.isRegisteredOld(o);
        if (config.getOption("general.authType").equalsIgnoreCase("field"))
            return this.isCustomFieldRegistered(o);
        return false;
    }
    
    public boolean testMysql() {
        return this.isRegisteredOld(false);
    }
    
    public String getForumGroup(boolean b) {
        return "";
    }
    
    public String getForumGroup() {
        return this.getForumGroup(false);
    }
    
    public boolean payPosts() {
    	return false;
    }
    
    protected boolean isRegisteredOld(boolean o) {
        return false;
    }
    
    protected boolean isCustomFieldRegistered(boolean o) {
        return false;
    }
}