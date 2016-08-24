package v3;

//Tom Harker's code; must fix

import java.time.Duration;

public class Commercial extends Promotion {
	private String companyName;

//	/**
//	 * Constructor for reading Commercials from a file
//	 * @param rdsMessage
//	 * @param duration
//	 */
//	public Commercial(String rdsMessage, String duration) {
//		this.length = Duration.ofSeconds(Long.parseLong(duration.trim()));
//		this.rdsMessage = rdsMessage;
//	}
//
//	public Commercial(Duration duration, String rdsMessage) {
//		super(duration, rdsMessage);
//	}
	
	public Commercial(String rdsMessage){
		this.length = Duration.ofSeconds(30l);
		this.rdsMessage = rdsMessage;
	}

	public String play() {
		lastPlayed = Duration.ofSeconds(0l);
		return displayRDS();
	}

	public String displayRDS() {
		return this.toString();
	}

	public String toString() {
		return this.getClass().getSimpleName() + ": " + rdsMessage;
	}

}
