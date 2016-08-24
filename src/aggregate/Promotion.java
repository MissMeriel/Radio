package aggregate;

import java.time.Duration;

public abstract class Promotion extends Broadcastable {
	
	public static final Duration PROMOTION_LENGTH = Duration.ofSeconds(30l);
	
	public Promotion(){
		lastPlayed = Duration.ofMinutes(0l);
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

//package v1;
//
//import java.time.Duration;
//
//public class RadioSpot extends Promotions {
//	private String radioStation;
//	
//	public RadioSpot(Duration duration, String filename) {
//		super(duration, filename);
//	}
//	
//	public void play() {
//		displayRDS();
//	}
//	
//	public void displayRDS() {
//		
//	}
//	
//	public String toString() {
//		return radioStation + ": " + getDuration();
//	}
//
//}
