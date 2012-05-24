package me.nexttonothing.next.boards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.nexttonothing.next.configs.MainConfig;

import me.nexttonothing.next.software.Software;

public class phpBB extends Software {
	public phpBB(String name, MainConfig config, boolean a) {
		super(name, config, a);
	}

	public phpBB(String name, MainConfig config) {
		super(name, config);
	}

	@Override
	public boolean getRegistrationValue(boolean o) {
		if (config.getString("general.authType").equalsIgnoreCase("username"))
			return isRegisteredOld(o);
		if (config.getString("general.authType").equalsIgnoreCase("field"))
			return isCustomFieldRegistered(o);
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
				String query = "SELECT group_id FROM "
						+ this.config.getString("mysql.prefix")
						+ "user_group WHERE user_id ='"
						+ userId
						+ "' ORDER BY group_leader DESC,user_pending ASC, group_id DESC";
				ResultSet rs = stmt.executeQuery(query);
				if (b)
					System.out.println("[McX-DEBUG] SELECT group_id FROM "
									+ this.config.getString("mysql.prefix")
									+ "user_group WHERE user_id ='"
									+ userId
									+ "' ORDER BY group_leader DESC,user_pending ASC, group_id DESC");
				if (rs.next()) {
					groupId = rs.getInt("group_id");
					query = "SELECT servergroup FROM "
							+ this.config.getString("mysql.prefix")
							+ "groups WHERE group_id ='" + groupId + "'";
					rs = stmt.executeQuery(query);
					if (b)
						System.out.println("SELECT servergroup FROM "
								+ this.config.getString("mysql.prefix")
								+ "groups WHERE group_id ='" + groupId + "'");
					if (rs.next()) {
						return rs.getString("servergroup");
					}
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
	public boolean isRegisteredOld(boolean o) {
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
			String query = "SELECT user_id,username_clean,user_type FROM "
					+ this.config.getString("mysql.prefix")
					+ "users WHERE username_clean='" + name
					+ "' LIMIT 1";
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {

				userId = rs.getInt("user_id");
				userType = rs.getInt("user_type");

				if (o)
					System.out.println("[McX] UserType: " + userType);
				return (userType != 1);
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
	public boolean isCustomFieldRegistered(boolean o) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
					+ config.getString("mysql.port") + "/"
					+ config.getString("mysql.database");
			Connection con = DriverManager.getConnection(url,
					config.getString("mysql.user"),
					config.getString("mysql.password"));
			Statement stmt = con.createStatement();
			String query = "SELECT user_id FROM "
					+ config.getString("mysql.prefix")
					+ "profile_fields_data WHERE pf_"
					+ getFieldName(Integer.parseInt(this.config.getString("field.id"))) + "='" + name
					+ "' LIMIT 1";
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				userId = rs.getInt("user_id");
				query = "SELECT username_clean,user_type FROM "
						+ this.config.getString("mysql.prefix")
						+ "users WHERE user_id='" + userId + "' LIMIT 1";
				rs = stmt.executeQuery(query);
				if (rs.next()) {
					userType = rs.getInt("user_type");
					if (o)
						System.out.println("UserType: " + userType);
					return (userType != 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getFieldName(int id) throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://" + config.getString("mysql.host") + ":"
				+ config.getString("mysql.port") + "/"
				+ config.getString("mysql.database");
		Connection con = DriverManager.getConnection(url,
				config.getString("mysql.user"),
				config.getString("mysql.password"));
		Statement stmt = con.createStatement();
		String query = "SELECT field_name FROM "
				+ this.config.getString("mysql.prefix")
				+ "profile_fields WHERE field_id='" + id
				+ "' LIMIT 1";
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()) {
			return rs.getString("field_name");
		}
		return null;
	}
}
