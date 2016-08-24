package finalized;

import java.time.Duration;

public class RadioSpot extends Promotion {

	/**
	 * Normal parameterized constructor
	 * @param	Duration	duration
	 * @param	String		rdsMessage
	 */
	public RadioSpot(String rdsMessage, Duration duration) {
		super(duration, rdsMessage);
		this.length = PROMOTION_LENGTH;
	}
	
	public RadioSpot(String rdsMessage){
		this.rdsMessage = rdsMessage;
		this.length = PROMOTION_LENGTH;
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
