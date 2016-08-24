package finalized;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class OldSongs {

	private ArrayList<Song> oldSongs;
	private LocalDate lastUpdated;
	private final String filename = "src/OldSongs.txt";
	boolean testing = false;

	public OldSongs() {
		oldSongs = new ArrayList<Song>();
	}

	/**
	 * Add songs to oldSongs, last update is today.
	 * 
	 * @param songs
	 */
	public void add(ArrayList<Song> songs) {
		oldSongs.addAll(songs);
		lastUpdated = LocalDate.now();
	}

	/**
	 * add songs to oldSongs with option to specify lastUpdated variable
	 * 
	 * @param songs
	 * @param localDate
	 *            assigned to lastUpdated
	 */
	public void add(ArrayList<Song> songs, LocalDate localDate) {
		oldSongs.addAll(songs);
		lastUpdated = localDate;
	}

	public ArrayList<Song> getSongs() {
		return oldSongs;
	}

	public boolean contains(Song song) {
		return oldSongs.contains(song);
	}

	/**
	 * Reads new songs from a file and adds them to an ArrayList
	 * 
	 * @return an ArrayList of the outdated songs previously in NewSongs
	 */
	//@SuppressWarnings("finally")
	public ArrayList<Song> update() {
		try {
			Reader reader = new Reader(filename);
			this.oldSongs = reader.getSongs();
			lastUpdated = LocalDate.now();
			//return oldSongs;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if(testing) System.out.println("oldSongs in OldSongs: "+ oldSongs.size());
		}
		return oldSongs;
	} // end update()

	/**
	 * Return index of song if song is in OldSongs list; otherwise returns -1
	 * @param song
	 */
	public int getIndexOf(Song song) {
			if(oldSongs.contains(song))
				return oldSongs.indexOf(song);
			else return -1;
	}

}
