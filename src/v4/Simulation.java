package v4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import javax.net.ssl.HostnameVerifier;


public class Simulation {

	private static final String NEWLIST = "./HotListSim.txt";
	private static final String OLDLIST = "./OldListSim.txt";
	private static final String SONGLIST = "./SongList.txt";
	private final int listSize = 4;
	private boolean testing = true;
	
	private static Random rng = new Random(System.currentTimeMillis());
	
	//Keep track to simulate the passage of time
	//Once a week the size of the old list increases by 1
	private int numWeeks =0;
	private int numDays = 0;
	
	
	private ArrayList<Song> songList;
	private ArrayList<Song> oldList = new ArrayList<Song>();
	private ArrayList<Song> newList = new ArrayList<Song>();
	
	public Simulation() throws FileNotFoundException, ParseException{
		songList = new Reader(SONGLIST).getSongs();
		
		//popOldList();
		newList = popNewList();
		
		
		
	}
	
	
	

	/**
	 * Is called once a simulated week when the new list of popular songs would be collected
	 * Creates a new Hot List, adds all songs that were in the Hot List previously and adds them to the Old List
	 * @return a new Hot List of songs randomly selected from the list of all songs that does not overlap with the oldlist
	 */
	private ArrayList<Song> popNewList(){
		
		ArrayList<Song> holder = new ArrayList<Song>();
		
		while(holder.size()<listSize){
			int rand = rng.nextInt(songList.size());
			Song s = songList.get(rand);
			if(!holder.contains(s)  && !oldList.contains(s) ){
				holder.add(songList.get(rand));
			}
		}
		
		for(Song s : newList){
			if(!holder.contains(s) ){
				oldList.add(s);
			}
		}
		return holder;
		
		
		
		
		
	}
	
	/**
	 * Is called during simulation to create a random list of old songs that cannot be played again for 30 days
	 * Cannot contain the same songs as the New List. 
	 */
	private void popOldList(){
		
		//oldList = new ArrayList<Song>();
		
		while(oldList.size()<listSize ){
			int rand = rng.nextInt(songList.size()-1);
			Song s = songList.get(rand);
			if(!oldList.contains(s)  && !newList.contains(s) ){
				oldList.add(songList.get(rand));
				songList.remove(s);
			}
		}
		
				
	}
	
	
	
	
	private void refresh() throws IOException{
		Writer writer = new Writer(OLDLIST, true);
		
		for(Song s : oldList){
			writer.writeBroadcast(s);
		}
		writer.close();
		writer = new Writer(NEWLIST, true);
		
		for(Song s : newList){
			writer.writeBroadcast(s);
		}
		
	}
	
	
	/**
	 * Simulates a week of time passing. Creates a new newList,
	 * adds the leftovers to the oldList and adds 7 days to the duration of each song in oldList
	 */
	protected void simulateWeek(){
		Duration d = Duration.ofDays(7);
		for(Song s : oldList){
			//s.setLength( s.getLength().plus(d) );
			s.subtractDate(7);
			if(testing) System.out.println(s + "  days last played  " + s.getDaysLastPlayed());
		}
		newList = popNewList();
	}
	
	
	
	protected ArrayList<Song> getSongList() {
		return songList;
	}


	protected void setSongList(ArrayList<Song> songList) {
		this.songList = songList;
	}


	public ArrayList<Song> getOldList() {
		return oldList;
	}


	protected void setOldList(ArrayList<Song> oldList) {
		this.oldList = oldList;
	}


	public ArrayList<Song> getNewList() {
		return newList;
	}


	protected void setNewList(ArrayList<Song> newList) {
		this.newList = newList;
	}
	
	
}
