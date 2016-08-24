package v3;

import java.time.Duration;

/**
 * Abstract superclass for objects playable over the radio
 * 
 * @author Meriel Stein
 *
 */
public abstract class Broadcastable {

	/**
	 * the length of the broadcastable item in seconds; initial value is 0
	 * seconds
	 */
	protected Duration length;// = Duration.ofSeconds(0l);

	/**
	 * the message to be displayed in the Radio Display System when the
	 * Broadcastable object is played
	 */
	protected String rdsMessage = "";

	/**
	 * how long ago the broadcastable object was played in seconds; initial
	 * value is 0 seconds
	 */
	protected Duration lastPlayed = Duration.ofSeconds(0l);

	/**
	 * Plays the object it is called upon
	 * 
	 * @return String returns String from displayRDS() via internal call
	 */
	abstract public String play();

	/**
	 * Displays Radio Display System message
	 * 
	 * @return String rdsMessage
	 */
	abstract public String displayRDS();

	public Duration getLength() {
		return length;
	}

	public void setLength(Duration duration) {
		this.length = duration;
	}

	public String getRDSMessage() {
		return rdsMessage;
	}

	public void setRDSMessage(String rdsMessage) {
		this.rdsMessage = rdsMessage;
	}
}
