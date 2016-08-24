package v4;

import java.text.ParseException;
import java.time.Duration;

public class NewsReport extends Report {

	final static Duration NEWS_REPORT_LENGTH = Duration.ofMinutes((long) 3);

	public NewsReport(String rdsMessage) throws ParseException {
		super(rdsMessage);
		reportLength = NEWS_REPORT_LENGTH;

	}

	public Duration length() {
		return reportLength;
	}

	public String displayRDS() {
		return "Hourly news report: "+rdsMessage;
	}

}
