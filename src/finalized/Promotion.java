package finalized;

import java.time.Duration;

public abstract class Promotion extends Broadcastable {
	
	public static final Duration PROMOTION_LENGTH = Duration.ofSeconds(30l);
	
	public Promotion(){
		lastPlayed = Duration.ofMinutes(0l);
		this.length = PROMOTION_LENGTH;
	}
	
	public Promotion(Duration length, String rdsMessage){
		this.rdsMessage = rdsMessage;
		this.length = length;
	}
	
	public String displayRDS(){
		return getClass().getSimpleName();
	}
	
	public String play(){
		lastPlayed = Duration.ofSeconds(0l);
		return displayRDS();
	}
	
	public Duration updateLastPlayed(Duration duration){
		this.lastPlayed = duration;
		return this.lastPlayed;
	}

}
