package v4;

import java.time.Duration;

public class RadioSpot extends Promotion {
	
//	/**
//	 * Constructor to work with reading RadioSpots from a file
//	 * @param	String	duration
//	 * @param	String	rdsMessage
//	 */
//	public RadioSpot(String rdsMessage, String duration){
//		this.length = Duration.ofSeconds(Long.parseLong(duration));
//		this.rdsMessage = rdsMessage;
//	}
//	
	/**
	 * Normal parameterized constructor
	 * @param	Duration	duration
	 * @param	String		rdsMessage
	 */
	public RadioSpot(String rdsMessage, Duration duration) {
		super(duration, rdsMessage);
	}
	
	public RadioSpot(String rdsMessage){
		this.rdsMessage = rdsMessage;
		this.length = Duration.ofSeconds(30l);
	}
	
	public String play() {
		lastPlayed = Duration.ofSeconds(0l);
		return displayRDS();
	}
	
	public String displayRDS() {
		return this.getClass().getSimpleName() + ": " + rdsMessage;
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + ": " + rdsMessage + ", ("+length+")";
	}

}
