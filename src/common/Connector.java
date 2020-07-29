package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connector {

	private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";
	private static final String USER = "TEST";
	private static final String PASSWORD = "test";
	private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	static {
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			conn.setAutoCommit(false);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) { 
				e.printStackTrace();
			}
		}
	}
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Connection conn, PreparedStatement ps) {
		close(conn);
		close(ps);
	}

	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		close(conn);
		close(ps);
		close(rs);
		
	}
}
