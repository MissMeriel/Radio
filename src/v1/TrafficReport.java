package v1;

import java.text.ParseException;
import java.time.Duration;

public class TrafficReport extends Report {

	final static Duration TRAFFIC_REPORT_LENGTH = Duration.ofSeconds(2l);

	/**
	 * Constructor to read TrafficReports from a file 
	 * @param rdsMessage
	 * @throws ParseException
	 */
	public TrafficReport(String rdsMessage) throws ParseException{
		super();
		this.rdsMessage = rdsMessage;
		this.length = TRAFFIC_REPORT_LENGTH;
	}
	
	/**
	 * Normal constructor
	 * @throws ParseException
	 */
	public TrafficReport() throws ParseException {
		super();
		reportLength = TRAFFIC_REPORT_LENGTH;
	}
	
	public String displayRDS(){
		//will later be displayed in a GUI, at which pt. s.o.p. can be deleted
		System.out.println("Now Playing: Hourly news report");
		return "Hourly traffic report: " + rdsMessage;
	}

}
