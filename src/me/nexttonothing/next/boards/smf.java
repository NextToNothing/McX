package me.nexttonothing.next.boards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.nexttonothing.next.configs.MainConfig;
import me.nexttonothing.next.mcx.McX;
import me.nexttonothing.next.software.Software;

public class smf extends Software{
    
    public smf(String name, MainConfig config, boolean a) {
        super(name,config,a);
    }
    
    public smf(String name, MainConfig config) {
        super(name,config);
    }
    
    public boolean getRegistrationValue(boolean o) {
        if (config.getString("general.authType").equals("Username"))
            return this.isRegisteredOld(o);
        if (config.getString("general.authType").equals("Field"))
            return this.isCustomFieldRegistered(o);
        return false;
    }

    @Override
    public boolean testMysql() {
        String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
            + config.getString("mysql.port") + "/"
            + config.getString("mysql.database");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con;
            con = DriverManager.getConnection(url,
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            Statement stmt;
            stmt = con.createStatement();
            ResultSet rs;
            String query = ("SELECT * FROM "
                    + this.config.getString("mysql.prefix")
                    + "members LIMIT 1");
            rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            
        } catch (ClassNotFoundException e) {
            
        }
        return false;
    }
    
    public String getForumGroup(boolean b) {
        try {
            if (userId != 0) {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://" + config.getString("mysql.host")
                        + ":" + config.getString("mysql.port") + "/"
                        + config.getString("mysql.database");
                Connection con = DriverManager.getConnection(url,
                        config.getString("mysql.user"),
                        config.getString("mysql.password"));
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id_group,servergroup FROM "
                        + this.config.getString("mysql.prefix")
                        + "membergroups WHERE id_group='" + groupId + "'");
                if (rs.next()) {
                    return rs.getString("servergroup");
                }
            
            } else {
                System.out.println("[McX] Sorry... Theres a fail in there!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ForumUserError: " + e.toString());
        } catch (SQLException e) {
            System.out.println("ForumUserError: " + e.toString());
        }

        System.out.println("User Forum Group not recognised!");
        return null;
    }
    
    public String getForumGroup() {
        return this.getForumGroup(false);
    }
    
    @Override
    public boolean payPosts() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
                    + config.getString("mysql.port") + "/"
                    + config.getString("mysql.database");
            Connection con = DriverManager.getConnection(url,
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            Statement stmt = con.createStatement();
            int currtime = (int) (config.getInt("economy.lastCheck") * 1000);
            String query = "SELECT `id_member` FROM "
                    + config.getString("mysql.prefix")
                    + "messages WHERE `approved` = '1"
                    + "' AND `poster_time` >= '" + currtime
                    + "'";
            ResultSet rs = stmt.executeQuery(query);
            int userId;
            while(rs.next()) {
                if(rs.getInt("id_member") != 0) {
                    userId = rs.getInt("id_member");
                    query = "SELECT `member_name` FROM "
                            + config.getString("mysql.prefix")
                            + "members WHERE `id_member` = '" + rs.getInt("uid")
                            + "' LIMIT 1";
                    rs = stmt.executeQuery(query);
                    if(rs.next()) {
                        if (config.getOption("general.authType").equalsIgnoreCase("username")) {
                            McX.economy.depositPlayer(rs.getString("member_name").toLowerCase(), config.getInt("economy.moneyPerPost"));
                        }
                        if (config.getOption("general.authType").equalsIgnoreCase("field")) {
                            query ="SELECT id_field,col_name FROM "
                                    + config.getString("mysql.prefix")
                                    + "custom_fields WHERE id_field"
                                    + "='" +  config.getString("field.id")
                                    + "' LIMIT 1";
                            rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                query = "SELECT `value` FROM "
                                        + config.getString("mysql.prefix")
                                        + "themes WHERE `variable` = "
                                        + "'cust_" + rs.getString("col_name")
                                        + "' AND `id_member` = '" + userId
                                        + "' LIMIT 1";
                                rs = stmt.executeQuery(query);
                                if(rs.next()) {
                                    McX.economy.depositPlayer(rs.getString("value").toLowerCase(), config.getInt("economy.moneyPerPost"));
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    protected boolean isRegisteredOld(boolean o) {
        String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
        + config.getString("mysql.port") + "/"
        + config.getString("mysql.database");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con;
            con = DriverManager.getConnection(url,
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            Statement stmt;
            stmt = con.createStatement();
            ResultSet rs;
            String query = ("SELECT id_member,member_name,id_group,is_activated FROM "
                    + this.config.getString("mysql.prefix")
                    + "members WHERE member_name="
                    + "lower('" + name + "')"
                    + " LIMIT 1");
            rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                userId = rs.getInt("id_member");
                userType = rs.getInt("is_activated");
                groupId = rs.getInt("id_group");
                if (o)
                    System.out.println("[McX] UserType: " + userType);
                return (userType == 1);
            }
        } catch (SQLException e) {
            //System.out.println("Qwertzy2");
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //System.out.println("Qwertzy");
            //e.printStackTrace();
        }
        return false;
    }
    
    protected boolean isCustomFieldRegistered(boolean o) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
                    + config.getString("mysql.port") + "/"
                    + config.getString("mysql.database");
            Connection con = DriverManager.getConnection(url,
                    config.getString("mysql.user"),
                    config.getString("mysql.password"));
            Statement stmt = con.createStatement();

            String query ="SELECT id_field,col_name FROM "
                    + this.config.getString("mysql.prefix")
                    + "custom_fields WHERE id_field"
                    + "='" +  this.config.getString("field.id")
                    + "' LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String field = rs.getString("col_name");
                query = "SELECT id_member,variable,value FROM "
                        + config.getString("mysql.prefix")
                        + "themes WHERE variable ="
                        + "'cust_" + field + "'"
                        + "AND value='"
                        + name + "' "
                        + "LIMIT 1";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    userId = rs.getInt("id_member");
                    query = ("SELECT id_member,member_name,id_group,is_activated FROM "
                            + this.config.getString("mysql.prefix")
                            + "members WHERE id_member="
                            + "'" + userId + "')"
                            + " LIMIT 1");
                    rs = stmt.executeQuery(query);
                    
                    if (rs.next()) {
                        userId = rs.getInt("id_member");
                        userType = rs.getInt("is_activated");
                        groupId = rs.getInt("id_group");
                        if (o)
                            System.out.println("UserType: " + userType);
                        return (userType == 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}