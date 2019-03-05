package leop.dev.paurustask2;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * SharedQueue maintains a queue of DataRecods, shared by FileReader as a producer and DbWriter as a consumer
 * When last record is added to the queue, FileReader should set writingCompleted property to true. 
 * After pulling the data from the queue with getNextRecord, DbWriter should check if there is data to pull (completed = (! writingCompleted) || (! isEmpty()))
 * 
 * @author LeoP
 *
 */
public class SharedQueue  { 
	 
	private volatile LinkedBlockingQueue<DataRecord> dataQueue; // private volatile LinkedList<DataRecord> dataQueue;
	private volatile boolean writingCompleted = false;  
	private volatile int recordCountIn = 0;    
	
	public SharedQueue() {		
		// this.dataQueue = new LinkedList<DataRecord>(); 
		this.dataQueue = new LinkedBlockingQueue<DataRecord>();  
	} 

	public synchronized boolean isWritingCompleted() {	 
		return writingCompleted;
	} 
	
	public synchronized void setWritingCompleted(boolean writingCompleted) {	
		System.out.println(String.format("------------------> %s setWritingCompleted !", Thread.currentThread().getName())); 
		this.writingCompleted = writingCompleted;
	}
	
    public synchronized boolean isEmpty() {
		return dataQueue.isEmpty();
	}
    
    public synchronized int getRecordCountIn() {
		return recordCountIn;
	}

	public synchronized void setRecordCountIn(int recordCountIn) {
		this.recordCountIn = recordCountIn;
	}

	public synchronized void addRecord (Integer fileRowNo, String match_id, Integer market_id, String outcome_id, String specifiers) {	 
		dataQueue.add(new DataRecord(fileRowNo, match_id, market_id, outcome_id, specifiers));		 
	}
	 
	public synchronized boolean getNextRecord (DataRecord dataRecord) {
		if (! dataQueue.isEmpty()) {
			DataRecord qRecord = dataQueue.poll();
			dataRecord.market_id = qRecord.market_id; 
			dataRecord.match_id = qRecord.match_id; 
			dataRecord.outcome_id = qRecord.outcome_id; 
			dataRecord.specifiers = qRecord.specifiers;  
			dataRecord.fileRowNo = qRecord.fileRowNo; 
			return true; 
		} else {
			return false; 
		}
	}
	

}
