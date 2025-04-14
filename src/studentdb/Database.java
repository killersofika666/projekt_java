package studentdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;

public class Database {
	private Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:studentydb.db");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return conn;
	}
	
	public void initializaceDatabase() {
		try (
				Connection conn = connect();
				Statement stmt = conn.createStatement();
				BufferedReader reader = new BufferedReader(new FileReader("sql/init.sql"))
		) {
			String line;
			StringBuilder sql = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				sql.append(line);
			}
			String[] queries = sql.toString().split(",");
			
			for (String query : queries) {
				if (!query.trim().isEmpty()) {
					stmt.executeUpdate(query);
				}
			}
			System.out.println("Database initialized successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	

}
