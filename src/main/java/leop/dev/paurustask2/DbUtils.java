package leop.dev.paurustask2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;  
 
public class DbUtils {
	
	private Connection dbConnection; 
 
	public Connection getDbConnection() throws ClassNotFoundException, SQLException, IOException {
		if (dbConnection == null) { 
			Class.forName(Config.getParams().dbDriver);
			System.out.println("JDBC driver loaded...");
			dbConnection = DriverManager.getConnection(Config.getParams().dbConnection, Config.getParams().dbUserName, Config.getParams().dbPassword); 
			System.out.println("Connection established...");	 
		};  
		return dbConnection;
	}
	
	public void closeDbConnection() throws SQLException {
		if (dbConnection != null) {
			System.out.println(String.format("------------------> %s dbConnection.close(); !", Thread.currentThread().getName())); 
			dbConnection.close();
		}
		System.out.println("Connection closed.");
	}
	
	
	
	@Override
	protected void finalize() throws Throwable { 
		// closeDbConnection();
		super.finalize();
	}

	public void createTables() throws Exception {
		
		 PreparedStatement sql; 
		 boolean wasConnected = dbConnection != null; 
		
		 Connection conn = getDbConnection();  
		 sql = conn.prepareStatement(Config.getParams().dbDropTableSql); 
		 sql.addBatch(); 
		 sql.executeBatch(); 
		 sql = conn.prepareStatement(Config.getParams().dbCreateTableSql); 
		 sql.addBatch(); 
		 sql.executeBatch();  
		 sql.clearBatch();
		 if (! conn.getAutoCommit())
			conn.commit();
		 if (wasConnected) 
			 closeDbConnection();
	} 
}
		 
		 
				  
 
 