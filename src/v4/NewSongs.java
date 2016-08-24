 package v4;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class NewSongs {

	private ArrayList<Song> newSongs;
	private LocalDate lastUpdated;
	private static int updateNumber = 0;
	private final String filename = "src/NewSongs";
	private final String filenameSuffix = ".txt";
	
	public NewSongs(){
		newSongs = new ArrayList<Song>();
		
	}
	
	public void newList(ArrayList<Song> songs){
		newSongs = songs;
	}

	//
	
	/**
	 * Reads new songs from a file and adds them to an ArrayList
	 * @return an ArrayList of the outdated songs previously in NewSongs
	 */
	@SuppressWarnings("finally")
	public ArrayList<Song> update(){
		
		ArrayList<Song> oldSongs = null;
		try{
			Reader reader = new Reader(filename + updateNumber + filenameSuffix);
			oldSongs = newSongs;
			newSongs = reader.getSongs();
			lastUpdated = LocalDate.now();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(ParseException e){
			e.printStackTrace();
		}finally{
			updateNumber ++;
			return oldSongs;
		}
	} // end update()
	
	public LocalDate getLastUpdated(){
		return lastUpdated;
	}
	
	public ArrayList<Song> getSongs(){
		return newSongs;
	}
	
	public boolean contains(Song song){
		return newSongs.contains(song);
	}
	
}
