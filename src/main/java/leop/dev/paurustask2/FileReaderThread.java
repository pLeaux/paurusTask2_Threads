package leop.dev.paurustask2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileReaderThread implements Runnable {
	
	private BufferedReader myReader; 
	private String filePath;  
	private Integer fileThread_SleepMicroseconds_AfterEach1000Records; 
	private Integer consoleLog_showDebugInfo;
	private SharedQueue sharedQueue; 
	
	public FileReaderThread(SharedQueue sharedQueue, String filePath, Integer fileThread_SleepMicroseconds_AfterEach1000Records, Integer consoleLog_showDebugInfo)   {
		this.filePath = filePath;   
		this.fileThread_SleepMicroseconds_AfterEach1000Records = fileThread_SleepMicroseconds_AfterEach1000Records;
		this.sharedQueue = sharedQueue;
		this.consoleLog_showDebugInfo = consoleLog_showDebugInfo; 
	} 
	
	private boolean parseOneRecord (String fileLine, DataRecord dataRecord) {
		dataRecord.match_id = ""; 
		dataRecord.market_id = null;
		dataRecord.outcome_id = "";
		dataRecord.specifiers = "";
		String s = "";
		String[] sa = fileLine.split("\\|");
		if (sa.length >= 1) 
			dataRecord.match_id = sa[0].replace("'", ""); 
		if (sa.length >= 2) 
			dataRecord.market_id = Integer.valueOf(sa[1]); 
		if (sa.length >= 3) { 
			dataRecord.outcome_id = sa[2].replace("'", ""); 	 
		};
		for (int i = 3; i < sa.length; i++) { 
			dataRecord.specifiers += sa[i].replace("'", ""); 
			if (i != (sa.length-1)) 
				dataRecord.specifiers +=  "\\|";
		};
		return true; 
	}
	 
	public void run() {
		
		System.out.println("FileReaderThread started...");  
		
		String fileLine; 
		DataRecord dataRecord = new DataRecord(); 
		int lineNo = 0;  
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS"); 
		
		try {
			File srcFile = new File(filePath);
			myReader = new BufferedReader(new FileReader(srcFile));  
			Date startDate = new Date(); 
			System.out.println(">>>>>>>>>>> IMPORT START at " + dateFormat.format(startDate)); 
			while ((fileLine = myReader.readLine()) != null) {
				lineNo++;   
				if (lineNo == 1) // 1st record = field names
					continue;
				// batchRecNo++;  
				if (parseOneRecord (fileLine, dataRecord)) {
					dataRecord.fileRowNo = lineNo; 
					sharedQueue.addRecord(dataRecord.fileRowNo, dataRecord.match_id, dataRecord.market_id, dataRecord.outcome_id, dataRecord.specifiers);
				}; 
				// slow down execution, for debugging purposes 
				if (lineNo % 1000 == 0) {
					Thread.sleep(fileThread_SleepMicroseconds_AfterEach1000Records);
					if (consoleLog_showDebugInfo == 1) 
						System.out.println(String.format("FileReader batch point ....... line #%d", lineNo)); 
				}; 
				
				// if (lineNo > 100)  break; 
			}
			myReader.close();
			Date endDate = new Date();
			System.out.println(">>>>>>>>>>> FileImport StartDate " + dateFormat.format(startDate)); 
			System.out.println(">>>>>>>>>>> FileImport EndDate " + dateFormat.format(endDate)); 
			sharedQueue.setWritingCompleted(true);
			
		} catch (FileNotFoundException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		  
		} finally {
			System.out.println("FileReaderThread finished");
		}
		
		
	}


 
	
 

}
