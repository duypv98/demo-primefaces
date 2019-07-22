package fd.db_utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
	public static String DRIVER = "com.mysql.cj.jdbc.Driver";
	public static String URL = "jdbc:mysql://localhost:3306/jsf_demo";
	
	public static String USERNAME = "root";
	public static String PASSWORD = "admin1";
	
	Connection connection = null;

	public MySQLConnector() throws Exception {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void connect() throws SQLException {
		try {
			if (this.connection == null) {
				this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void close() throws SQLException {
		try {
			if (this.connection != null)
				this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
