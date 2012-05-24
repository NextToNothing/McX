package me.nexttonothing.next.boards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.nexttonothing.next.configs.MainConfig;
import me.nexttonothing.next.software.Software;

public class myBB extends Software{
	
	public myBB(String name, MainConfig config, boolean a) {
		super(name,config,a);
	}
	
	public myBB(String name, MainConfig config) {
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
					+ "users LIMIT 1");
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
				ResultSet rs = stmt.executeQuery("SELECT servergroup FROM "
						+ this.config.getString("mysql.prefix")
						+ "usergroups WHERE gid ='" + userType + "'");
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
			String query = ("SELECT uid,username,usergroup FROM "
					+ this.config.getString("mysql.prefix")
					+ "users WHERE username="
					+ "lower('" + name + "')"
					+ " LIMIT 1");
			rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				userId = rs.getInt("uid");
				userType = rs.getInt("usergroup");
				if (o)
					System.out.println("[McX] UserGroup: " + userType);
				return (userType != 5);
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
			String query = "SELECT ufid FROM "
					+ config.getString("mysql.prefix")
					+ "userfields WHERE fid"
					+ config.getString("field.id") + "='" + name
					+ "' LIMIT 1";
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				userId = rs.getInt("ufid");
				query = "SELECT uid,usergroup FROM "
						+ config.getString("mysql.prefix")
						+ "users WHERE uid='" + userId
						+ "' LIMIT 1";
				rs = stmt.executeQuery(query);
				if (rs.next()) {
					userType = rs.getInt("usergroup");
					if (o)
						System.out.println("UserGroup: " + userType);
					return (userType != 5);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}