package fd.services;

import java.sql.ResultSet;
import java.sql.Statement;

import fd.models.User;
import fd.db_utils.MySQLConnector;

public class UserServices {
	
	public UserServices() {
		
	}
	
	private static boolean isExUser(String username) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		boolean f = false;
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT uname FROM users;";
		ResultSet rs = st.executeQuery(sql);
		if (rs.first() == false) {
			f = false;
		} else {
			rs.first();
			if (rs.getString("uname").equalsIgnoreCase(username)) {
				f = true;
			} else {
				f = false;
			}
		}
		connectionManager.close();
		return f;
	}

	/* Business Methods */
	public boolean register(String username, String password) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		boolean f = false;
		if (isExUser(username)) {
			f = false;
		} else {
			Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			String sql = "INSERT INTO users (uname, pwd) VALUES ('" + username + "','" + password + "');";
			st.executeUpdate(sql);
			f = true;
		}
		connectionManager.close();
		return f;
	}

	public User login(String username, String password) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		User user = new User();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT * FROM users WHERE uname = '" + username + "';";
		ResultSet rs = st.executeQuery(sql);
		if (rs.first() == false) {
			user = null;
		} else {
			rs.first();
			if (username.equalsIgnoreCase(rs.getString("uname")) && password.equalsIgnoreCase(rs.getString("pwd"))) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("uname"));
			} else {
				user = null;
			}
		}
		return user;
	}

	public String getUsernameById(int id) throws Exception {
		MySQLConnector connectionManager = new MySQLConnector();
		connectionManager.connect();
		Statement st = connectionManager.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		String sql = "SELECT uname FROM users WHERE id = " + id + ";";
		ResultSet rs = st.executeQuery(sql);
		rs.first();
		String username = rs.getString("uname");
		return username;
	}
}
