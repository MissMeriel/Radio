package finalized;

//Tom Harker's code; must fix

import java.time.Duration;

public class Commercial extends Promotion {
	private String companyName;
	
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
