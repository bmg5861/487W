package Application;

import java.sql.*;

public class DatabaseConnection {
	public Connection databaseLink;
	
	public Connection getConnection() {
		String databaseName = "";
		String databaseUser = "";
		String databasePassword = "";
		String url = "jdbc:mysql://localhost/" + databaseName;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
		} catch (SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return databaseLink;
		
	}
	
	
}
