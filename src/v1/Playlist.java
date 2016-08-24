package v1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Playlist {

	private String playlistName;
	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

	private int playlistCount = 0;
	private final Duration TIME_REMAINING_EPSILON = Duration.ofSeconds(150l);
	private Duration currentLength = Duration.ofSeconds(0l);
	static final int SONG_NOT_ADDED = Integer.MIN_VALUE;
	static final int SONG_NOT_FOUND = Integer.MIN_VALUE;
	final int REPORT_INTERVAL = 0;
	// Is this measured in milliseconds/seconds/minutes etc?
	public static final Duration MAX_TIME = Duration.ofSeconds(4 * 3600l);
	private ArrayList<Song> songs = new ArrayList<>();

	/**
	 * Constructor for playlist
	 * 
	 * @param playlistName
	 */
	public Playlist(String playlistName) {
		this.playlistName = playlistName;
		playlistCount++;
		songs = new ArrayList<Song>();
	}

	/**
	 * Adds song to the end of songs ArrayList
	 * 
	 * @param s
	 * @return int corresponding to the added song's index in the ArrayList if
	 *         it was added, or Interger.MIN_VALUE if it was not added
	 */
	public int addSong(Song song) {
		int i = 0;
		while (songs.contains(i)) {
			i++;
		}
		if (songs.add(song))
			return i;
		else
			return SONG_NOT_ADDED;
	}

	/**
	 * @return ArrayList<Song> ArrayList of songs in the playlist
	 */
	public ArrayList<Song> getSongs() {
		return songs;
	}

	/**
	 * set Playlist's songs to an ArrayList parameter
	 * 
	 * @param songs
	 * @throws Exception
	 */
	public void setSongs(ArrayList<Song> songs) throws OverTimeException {
		for (Song song : songs) {
			currentLength.plus(song.getLength());
			if (currentLength.compareTo(MAX_TIME) > 0) {
				this.songs = null;

				// make custom exception for this: "PlaylistException" or
				// something
				throw new OverTimeException();
			}
		}
		this.songs = songs;
	}

	/**
	 * Delete all copies of this song in the ArrayList
	 * @param song
	 */
	public void deleteSong(Song song) {
		Iterator<Song> iter = songs.iterator();
		while(iter.hasNext()){
			Song current = iter.next();
			if (song.equals(current))
				songs.remove(current);
		}
	} // end deleteSong()
	
	/**
	 * Remove song by its index in the ArrayList
	 * @param index
	 */
	public void deleteSong(int index){
		System.out.println("deleteSong index: " + index);
		songs.remove(index);
	}
	
	public Song getSong(int index){
		return songs.get(index);
	}

	/**
	 * Time left in the playlist
	 * 
	 * @return Duration object of MAX_TIME minus current length
	 * @throws Exception
	 *             with message "length of playlist greater than max time"
	 */
	public Duration timeLeft() throws OverTimeException {
		if (currentLength.compareTo(MAX_TIME) > 0)
			return MAX_TIME.minus(currentLength);

		// make custom exception: PlaylistException/PlaylistLengthException or
		// whatever
		else
			throw new OverTimeException();
	}

	/**
	 * Get remaining time and return a radiospot with that amount of time with
	 * rds "Signoff"
	 */
	// checking whether the amount of time remaining is small
	// enough to warrant this method before method call in
	// the GUI
	public RadioSpot fillRemainingtime() {
		return new RadioSpot("End of Set: DJ Signoff", MAX_TIME.minus(currentLength)
				);

	}

	/**
	 * Included this from one of the projects we did that used a playlist class,
	 * thought it could be usable here
	 */
	public int search(String song) {
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i).getTitle().contains(song)) {
				return (i + 1);
			}
		}
		return SONG_NOT_FOUND;
	}

	@Override
	public String toString() {
		String str = playlistName;
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i) != null) {
				str += ((i + 1) + ". " + songs.get(i));
			}
		}
		return str;
	} // end printPlaylist method

	public boolean find(Song song) {
		// TODO Auto-generated method stub
		if(song == null) return false;
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i).toString().contains(song.toString())) {
				return (true);
			}
		}
		return false;
	}

	public String timeRemaining() throws OverTimeException {
		String str = "Time remaining: ";
		Duration tr = timeLeft();
		long hrs = tr.toHours();
		str += tr.toHours() + ":";
		long min = tr.minusHours(tr.toHours()).toMinutes();
		if(min < 10)	str += 0;
		str += min + ":";
		long sec = tr.minusHours(hrs).minusMinutes(min).getSeconds();
		if (sec < 10) str += 0;
		str += sec;
		return str;
	}

} // end Playlist class
