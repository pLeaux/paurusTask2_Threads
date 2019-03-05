package leop.dev.paurustask2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class Config, upon its creation, loads settings from resource file 'config.properties' into 'params' object.
 * Usage: String dbDriver = ConfigParams.dbDriver, dbConnection, ...  (class Config is created automatically) 
 * For explanation of particular params, see 'config.properties'
 *  
 * @author LeoP
 *
 */
public class Config {  
	
	private static ConfigParams_ params = null;  
	
	private Config() throws IOException {
		if (params == null)
		params = new  ConfigParams_();  
	}
	
	public static ConfigParams_ getParams() throws IOException {
		if (params == null) {
			params = new ConfigParams_();  
		}
		return params;
	}  
 
	static class ConfigParams_ {
		
		public String dbDriver; 
		public String dbConnection; 
		public String dbUserName; 
		public String dbPassword;  	
		public String srcFilePath; 
		public Integer dbInsertBatchCount;  
		public Integer dbDbWriterThreadCount; 
		public Integer fileThread_SleepMicroseconds_AfterEach1000Records;  
		public Integer consoleLog_showDebugInfo; 
		public String dbCreateTableSql;  
		public String dbDropTableSql;
		
		private static Integer strToIntDef(String s, boolean allow0, Integer defaultValue) {
		    if ((s == null) || (s == "") || ((! allow0) && (s == "0"))) {
		    	return defaultValue; 
		    } else {
		    	return Integer.valueOf(s);
		    }
		}
		
		public ConfigParams_() throws IOException  { 
			
			Properties prop = new Properties();
		    ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		    InputStream stream = loader.getResourceAsStream("config.properties");
		    prop.load(stream);
		    stream.close();		
		    dbDriver = prop.getProperty("dbDriver");
		    dbConnection = prop.getProperty("dbConnection");
		    dbDriver = prop.getProperty("dbDriver");
		    dbUserName = prop.getProperty("dbUserName");
		    dbPassword = prop.getProperty("dbPassword");
		    srcFilePath = prop.getProperty("srcFilePath");		    
		    dbInsertBatchCount = strToIntDef(prop.getProperty("dbInsertBatchCount"), false, 1);  
			dbDbWriterThreadCount = strToIntDef(prop.getProperty("dbDbWriterThreadCount"), false, 20); 
			fileThread_SleepMicroseconds_AfterEach1000Records = strToIntDef(prop.getProperty("fileThread_SleepMicroseconds_AfterEach1000Records"), true, 1000); 
			consoleLog_showDebugInfo = strToIntDef(prop.getProperty("consoleLog_showDebugInfo"), true, 1); 
			dbCreateTableSql = prop.getProperty("dbCreateTableSql");	
			dbDropTableSql = prop.getProperty("dbDropTableSql");	
		} 
		
	} 
 
 
 
  
 
}
 
 
 