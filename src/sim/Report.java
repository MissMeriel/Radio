package sim;

import java.text.ParseException;
import java.time.Duration;

public abstract class Report extends Broadcastable{

	public final Duration TIME_INTERVAL = Duration.ofHours(1l).plus(Duration.ofMinutes(58l));
	protected boolean played;
	protected Duration lastPlayed;
	protected Duration reportLength;
	protected String rdsMessage;

	
	public Report(String rdsMessage) throws ParseException{
		this.rdsMessage = rdsMessage;
		played = false;
		lastPlayed = Duration.ofMinutes((long) 0);
	}
	
	public String play(){
		lastPlayed = Duration.ofHours((long) 0);
		return displayRDS();
	}
	
	public abstract String displayRDS();

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	public Duration getLastPlayed() {
		return lastPlayed;
	}

	public void setLastPlayed(Duration lastPlayed) {
		this.lastPlayed = lastPlayed;
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName()+": " + rdsMessage + " ";
	}
	
}
