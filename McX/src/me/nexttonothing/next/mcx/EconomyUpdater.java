package me.nexttonothing.next.mcx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import java.util.Timer;
import java.util.TimerTask;

import me.nexttonothing.next.configs.MainConfig;

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
		if(McX.ForumType.equalsIgnoreCase("phpbb")) {
			return phpbbPosts();
		}
		if(McX.ForumType.equalsIgnoreCase("mybb")) {
			return mybbPosts();
		}
		if(McX.ForumType.equalsIgnoreCase("smf")) {
			return smfPosts();
		}
		return false;
	}

	private boolean phpbbPosts() {
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
			String query = "SELECT `poster_id` FROM "
					+ config.getString("mysql.prefix")
					+ "posts WHERE `post_postcount` = '1"
					+ "' AND `post_approved` = '1"
					+ "' AND `post_time` >= '" + currtime
					+ "'";
			ResultSet rs = stmt.executeQuery(query);
			int userId;
			String fieldName;
			while(rs.next()) {
				if(rs.getInt("poster_id") != 0) {
					userId = rs.getInt("poster_id");
					query = "SELECT `username_clean` FROM "
							+ config.getString("mysql.prefix")
							+ "users WHERE `user_id` = '" + userId
							+ "' LIMIT 1";
					rs = stmt.executeQuery(query);
					if(rs.next()) {
						if (config.getOption("general.authType").equalsIgnoreCase("username")) {
							McX.economy.depositPlayer(rs.getString("username_clean").toLowerCase(), config.getInt("economy.moneyPerPost"));
						}
						if (config.getOption("general.authType").equalsIgnoreCase("field")) {
							query = "SELECT field_name FROM "
									+ this.config.getString("mysql.prefix")
									+ "profile_fields WHERE field_id='" + config.getInt("field.id")
									+ "' LIMIT 1";
							rs = stmt.executeQuery(query);
							if(rs.next()) {
								fieldName = rs.getString("field_name");
								query = "SELECT `pf_" + rs.getString("field_name") + "` FROM "
										+ config.getString("mysql.prefix")
										+ "profile_fields_data WHERE `user_id` = '" + userId
										+ "' LIMIT 1";
								rs = stmt.executeQuery(query);
								if (rs.next()) {
									McX.economy.depositPlayer(rs.getString(fieldName).toLowerCase(), config.getInt("economy.moneyPerPost"));
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

	
	private boolean mybbPosts() {
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
			String query = "SELECT `uid` FROM "
					+ config.getString("mysql.prefix")
					+ "posts WHERE `visible` = '1"
					+ "' AND `dateline` >= '" + currtime
					+ "'";
			ResultSet rs = stmt.executeQuery(query);
			int userId;
			while(rs.next()) {
				if(rs.getInt("uid") != 0) {
					userId = rs.getInt("uid");
					query = "SELECT `username` FROM "
							+ config.getString("mysql.prefix")
							+ "users WHERE `uid` = '" + rs.getInt("uid")
							+ "' LIMIT 1";
					rs = stmt.executeQuery(query);
					if(rs.next()) {
						if (config.getOption("general.authType").equalsIgnoreCase("username")) {
							McX.economy.depositPlayer(rs.getString("username").toLowerCase(), config.getInt("economy.moneyPerPost"));
						}
						if (config.getOption("general.authType").equalsIgnoreCase("field")) {
							query = "SELECT `fid" + config.getString("field.id") + "` FROM "
									+ config.getString("mysql.prefix")
									+ "userfields WHERE `ufid` = '" + userId
									+ "' LIMIT 1";
							rs = stmt.executeQuery(query);
							if(rs.next()) {
								McX.economy.depositPlayer(rs.getString("fid" + config.getString("field.id")).toLowerCase(), config.getInt("economy.moneyPerPost"));
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
	
	private boolean smfPosts() {
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
}