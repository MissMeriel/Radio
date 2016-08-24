package v1;

import java.text.ParseException;
import java.time.Duration;

public class NewsReport extends Report {

	final static Duration NEWS_REPORT_LENGTH = Duration.ofMinutes((long) 3);

	public NewsReport() throws ParseException {
		super();
		reportLength = NEWS_REPORT_LENGTH;

	}

	public Duration length() {
		return reportLength;
	}

	public String displayRDS() {
		// this will later be displayed in a GUI, at which pt. s.o.p. can be deleted
		System.out.println("Now Playing: Hourly news report");
		return "Now Playing: Hourly news report";
	}

}
