package me.nexttonothing.next.boards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.nexttonothing.next.configs.MainConfig;
import me.nexttonothing.next.software.Software;

public class vbulletin extends Software {
		public vbulletin(String name, MainConfig config, boolean a) {
			super(name,config,a);
		}
		
		public vbulletin(String name, MainConfig config) {
			super(name,config);
		}
		
		@Override
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
							+ "usergroup WHERE usergroupid='" + userType + "'");
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
		
		@Override
		public String getForumGroup() {
			return this.getForumGroup(false);
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
						+ "user LIMIT 1");
				rs = stmt.executeQuery(query);
				
				if (rs.next()) {
					return true;
				}
			} catch (SQLException e) {
				
			} catch (ClassNotFoundException e) {
				
			}
			return false;
		}
		
		@Override
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
				String query = ("SELECT userid,username,usergroupid FROM "
						+ this.config.getString("mysql.prefix")
						+ "user WHERE username="
						+ "lower('" + name + "')"
						+ " LIMIT 1");
				rs = stmt.executeQuery(query);
				
				if (rs.next()) {
					userId = rs.getInt("userid");
					userType = rs.getInt("usergroupid");
					
					if (o)
						System.out.println("[McX] UserGroup: " + userType);
					return (userType != 8);
				}
			} catch (SQLException e) {
				System.out.println("Qwertzy2");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Qwertzy");
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
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
				ResultSet rs = stmt.executeQuery("SELECT userid FROM "
						+ this.config.getString("mysql.prefix")
						+ "userfield WHERE field" + this.config.getString("field.id") + "'"
						+ name + "' LIMIT 1");
				if (rs.next()) {
					userId = rs.getInt("userid");
					rs = stmt.executeQuery("SELECT username,usergroupid FROM "
							+ this.config.getString("mysql.prefix")
							+ "user WHERE userid='" + userId + "' LIMIT 1");
					if (rs.next()) {
						userType = rs.getInt("usergroupid");
						if (o)
							System.out.println("UserGroup: " + userType);
						return (userType != 8);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
}