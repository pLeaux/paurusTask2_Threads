package leop.dev.paurustask2;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;

/**
*  
* glej: https://stackoverflow.com/questions/49811474/fastest-way-to-process-a-file-db-insert-java-multi-threading
* 
*/  

public class FileToDbImporter { 
  
	
	public static void main(String[] args) throws Exception { 
		
		// init 
		String srcFilePath = Config.getParams().srcFilePath;  
		Integer dbInsertBatchCount = Config.getParams().dbInsertBatchCount;  
		Integer fileThread_SleepMicroseconds_AfterEach1000Records = Config.getParams().fileThread_SleepMicroseconds_AfterEach1000Records;
		Integer consoleLog_showDebugInfo = Config.getParams().consoleLog_showDebugInfo;
		Integer dbDbWriterThreadCount  = Config.getParams().dbDbWriterThreadCount;
		
		System.out.println("Config.params.srcFilePath = " + srcFilePath); 
		
		// re/create table 
		DbUtils dbUtils = new DbUtils();  
		dbUtils.createTables(); 
		
		// create a queue, shared among FileReaderThread as producer and DbWriterThread/s as consumer/s
		SharedQueue sharedQueue = new SharedQueue();   
		
		// start FileReader and DbWriter threads  
	    FileReaderThread myFileReader = new FileReaderThread(sharedQueue, srcFilePath, fileThread_SleepMicroseconds_AfterEach1000Records, consoleLog_showDebugInfo); 
	    Thread frt = new Thread(myFileReader,"FileReaderThread"); 
	    frt.start();
 
		// start multiple DbWriterThreads
		for (int i = 1; i <=  dbDbWriterThreadCount ; i++) {
			new Thread(new DbWriterThread(sharedQueue, dbInsertBatchCount), "DbWriterThread_" + String.valueOf(i)).start();
		}
		
		System.out.println("Main thread after DbWriterThread start....");  
		System.out.println("Main thread ended.");  
	 

	}

}
