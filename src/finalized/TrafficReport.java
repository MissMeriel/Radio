package finalized;

import java.text.ParseException;
import java.time.Duration;

public class TrafficReport extends Report {

	final static Duration TRAFFIC_REPORT_LENGTH = Duration.ofSeconds(2l);

	/**
	 * Constructor 
	 * @param rdsMessage
	 * @throws ParseException
	 */
	public TrafficReport(String rdsMessage) throws ParseException{
		super(rdsMessage);
		this.rdsMessage = rdsMessage;
		this.length = TRAFFIC_REPORT_LENGTH;
	}
	
	public String displayRDS(){
		return "Hourly traffic report: " + rdsMessage;
	}
}
