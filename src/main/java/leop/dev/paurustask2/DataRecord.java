package leop.dev.paurustask2;

public class DataRecord {
	
	public Integer fileRowNo;
	public String match_id; 
	public Integer market_id; 
	public String outcome_id; 
	public String specifiers; 
	 
		
	public DataRecord() {  
		
	} 

	public DataRecord(Integer fileRowNo, String match_id, Integer market_id, String outcome_id, String specifiers) { 
		
		this.fileRowNo = fileRowNo; 
		this.match_id = match_id;
		this.market_id = market_id;
		this.outcome_id = outcome_id;
		this.specifiers = specifiers; 
	}
}
