package leop.dev.paurustask2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * 
 * DbWriter class accepts data and inserts it, chunk by chunk, into the database.
 * Usage: set DB connection params in file "config.properties". 
 * 
 * @author  
 *
 */

public class DbWriterThread implements AutoCloseable, Runnable {
 
    private SharedQueue sharedQueue; 
    private final int batchSizeLimit;
    private int batchSize = 0;
    private DataRecord dataRecord = new DataRecord(); 
    private DbUtils dbUtils;  

    public DbWriterThread(SharedQueue sharedQueue, int batchSizeLimit) {
    	this.dbUtils = new DbUtils();  
    	this.sharedQueue = sharedQueue;  
        this.batchSizeLimit = batchSizeLimit; 
    }
    
    public void insertRecord(String match_id, String market_id, Integer outcome_id, String specifiers) {
        if (++batchSize >= batchSizeLimit) {
            sendBatch();
        }
    }
    // 2DO: save data 
    public void sendBatch() {
        System.out.format(">>>>> Send batch with %d records%n", batchSize);
        batchSize = 0;
    }
    
    public void close() throws Exception{ 
        if (batchSize != 0) {
            sendBatch();
        }
    }

	public void run() { 
		
		System.out.println("DbWriterThread started..."); 
		   		
		String batchSQL = ""; 
		int batchSize = 0;
		int batchRowNoStart = 0;  
		  
 		try {
		 
			DbUtils dbUtils = new DbUtils();
			Connection conn = dbUtils.getDbConnection();
			PreparedStatement insertSql = conn.prepareStatement("insert into foo_random (fileRowNo, match_id, market_id, outcome_id, specifiers) values (?, ?, ?, ?, ?); \n"); 
			
		    while ((! sharedQueue.isWritingCompleted()) ||  (! sharedQueue.isEmpty())) { 
		    	
		    	if (sharedQueue.getNextRecord(dataRecord)) { 
		    		if (batchRowNoStart == 0) 
		    			batchRowNoStart = dataRecord.fileRowNo;  
		    		if (++batchSize <= batchSizeLimit) { 
		    			insertSql.setInt(1, dataRecord.fileRowNo);
		    			insertSql.setString(2, dataRecord.match_id);
		    			insertSql.setInt(3, dataRecord.market_id);
		    			insertSql.setString(4, dataRecord.outcome_id);
		    			insertSql.setString(5, dataRecord.specifiers);
		    			insertSql.addBatch(); 
		    		} else {   
		    			System.out.println(String.format(">>>>>>>>>>>>>>>>> %s BATCH EXECUTE (%d records %d-%d)", Thread.currentThread().getName(), batchSize, batchRowNoStart, dataRecord.fileRowNo)); 
		    			insertSql.executeBatch(); 
		    			batchSize = 0; 
		    			batchRowNoStart = 0;
		    			// insertSql.clearBatch();
		    			if (! conn.getAutoCommit())
		    				conn.commit();
		    		} 
		    	};
		    } 
		    
		} catch (Exception e) { 
			e.printStackTrace();
		}  finally {
			try {
				dbUtils.closeDbConnection();
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
		 
		
		
		
	}
}