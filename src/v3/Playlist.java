package v3;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Playlist {

	boolean testing = true;

	private String playlistName;
	private int playlistCount = 0;
	public static final Duration TIME_REMAINING_EPSILON = Duration
			.ofSeconds(150l);
	private Duration currentLength = Duration.ofSeconds(0l);
	static final int SONG_NOT_ADDED = Integer.MIN_VALUE;
	static final int SONG_NOT_FOUND = Integer.MIN_VALUE;
	final int REPORT_INTERVAL = 0;
	// Is this measured in milliseconds/seconds/minutes etc?
	public static final Duration MAX_TIME = Duration.ofSeconds(4 * 3600l);
	private ArrayList<Broadcastable> playlist = new ArrayList<Broadcastable>();
	// could have a getLastAd that returns last time an ad was added
	private int promotionTime = 1800;
	private int reportTime = 3600;
	Reader adRdr = new Reader();
	Reader spotRdr = new Reader();
	Reader newsRdr = new Reader();
	Reader trafficRdr = new Reader();
	ArrayList<Commercial> adList;
	ArrayList<Report> trafficReportList;
	ArrayList<Report> newsReportList;
	ArrayList<RadioSpot> radioSpotList;
	Random rand = new Random();

	/**
	 * Constructor for playlist
	 * 
	 * @param playlistName
	 */
	public Playlist(String playlistName) {
		this.playlistName = playlistName;
		playlistCount++;
		playlist = new ArrayList<Broadcastable>();
		try {
			adRdr = new Reader("src/AdList.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		adList = adRdr.getCommercials();
		try {
			spotRdr = new Reader("src/RadioSpotList.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		radioSpotList = spotRdr.getSpots();
		try {
			newsRdr = new Reader("src/NewsReportList.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		newsReportList = newsRdr.getReports(false);
		try {
			trafficRdr = new Reader("src/TrafficReportList.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		trafficReportList = trafficRdr.getReports(true);
	}

	/**
	 * Adds song to the end of playlist ArrayList
	 * 
	 * @param s
	 * @return int corresponding to the added song's index in the ArrayList if
	 *         it was added, or Interger.MIN_VALUE if it was not added
	 * @throws OverTimeException
	 * @throws ParseException
	 */
	public void addItem(Broadcastable s) throws OverTimeException,
			ParseException {
		if (testing)
			System.out
					.println("currentLength.plus(s.getLength()).compareTo(MAX_TIME)= "
							+ currentLength.plus(s.getLength()).compareTo(
									MAX_TIME));
		// add if currentLength + s.length is not over 4 hours long
		// if (currentLength.plus(s.getLength()).compareTo(MAX_TIME) <= 0) {
		if (s.getLength().compareTo(timeLeft()) <= 0) {
			playlist.add(s);
			currentLength = currentLength.plus(s.getLength());
			if (testing)
				System.out
						.println("currentLength.getSeconds() >= promotionTime? "
								+ (currentLength.getSeconds() >= promotionTime));
			long time = currentLength.getSeconds();
			// if it's been a half hour since adding Promotions & there's room
			// for them
			if (time >= promotionTime && time <= (4 * 3600 - 600)) {
				for (int j = 0; j < 8; j++) {
					playlist.add(adList.get(rand.nextInt(adList.size())));
					currentLength = currentLength
							.plus(Commercial.PROMOTION_LENGTH);
				}
				for (int k = 0; k < 2; k++) {
					playlist.add(radioSpotList.get(rand.nextInt(radioSpotList
							.size())));
					currentLength = currentLength
							.plus(RadioSpot.PROMOTION_LENGTH);
				}
				promotionTime += 1800;
				if (testing)
					System.out.println("promotionTime= " + promotionTime);
			}
			time = currentLength.getSeconds();
			// if it's been an hour since adding Reports & there's room for them
			if (time >= reportTime && time <= (4 * 3600 - 300)) {

				playlist.add(newsReportList.get(rand.nextInt(newsReportList
						.size())));
				currentLength = currentLength
						.plus(NewsReport.NEWS_REPORT_LENGTH);
				playlist.add(trafficReportList.get(rand
						.nextInt(trafficReportList.size())));
				currentLength = currentLength
						.plus(TrafficReport.TRAFFIC_REPORT_LENGTH);
				reportTime += 3600;
				if (testing)
					System.out.println("reportTime=" + reportTime);
			}
		} else
			throw new OverTimeException();
	}

	/**
	 * @return Duration object of MAX_TIME minus current length
	 * @throws OverTimeException
	 */
	public Duration timeLeft() throws OverTimeException {
		// currentLength needs to be LESS THAN max time.
		if (currentLength.compareTo(MAX_TIME) <= 0) {
			if (testing)
				System.out.println("timeLeft if, currentLength: "
						+ currentLength);
			return MAX_TIME.minus(currentLength);
		} else if (testing)
			System.out
					.println("timeLeft else, currentLength: " + currentLength);
		throw new OverTimeException();
	}

	/**
	 * Delete all copies of this song in playlist
	 * 
	 * @param song
	 */
	public void deleteSong(Song song) {
		Iterator<Broadcastable> iter = playlist.iterator();
		while (iter.hasNext()) {
			Broadcastable current = iter.next();
			if (song.equals(current))
				playlist.remove(current);
		}
		adjustAdsAndReports();
	} // end deleteSong()

	/**
	 * Remove song by its index in playlist
	 * 
	 * @param index
	 */
	public void deleteSong(int index) {
		int size = playlist.size();
		if (testing)
			System.out.println("deleteSong index: " + index);
		// Need to set our currentLength
		currentLength = currentLength.minus(playlist.get(index).getLength());
		playlist.remove(index);
		if (index < size - 1) {
			adjustAdsAndReports();
		}
	}

	/**
	 * Called when a song is deleted somewhere other than the end of the list.
	 */
	private void adjustAdsAndReports() {
		Iterator<Broadcastable> iter = playlist.iterator();
		while (iter.hasNext()) {
			Broadcastable b = iter.next();
			if (b instanceof Report || b instanceof Promotion) {
				iter.remove();
				currentLength = currentLength.minus(b.getLength());
			}
		}
		if (testing)
			System.out
					.println("in adjustAdsAndReports, deleted all reports & promotions");
		ArrayList<Broadcastable> al = new ArrayList<Broadcastable>();
		int promoTime = 1800;
		int repTime = 3600;
		Duration alLength = Duration.ofSeconds(0);
		Iterator iter2 = playlist.iterator();
		while (iter2.hasNext()) {
			Broadcastable b = (Broadcastable) iter2.next();
			al.add(b);
			alLength = alLength.plus(b.getLength());
			if (testing) {
				System.out.println("b: " + b);
				System.out.println("b.length: " + b.length);
				System.out.println("alLength.getSeconds(): "
						+ alLength.getSeconds());
				System.out.println("alLength.getSeconds() >= promoTime: "
						+ (alLength.getSeconds() >= promoTime));
				System.out.println();
			}
			if (alLength.getSeconds() >= promoTime
					&& alLength.getSeconds() < (4 * 3600 - 300)) {
				for (int j = 0; j < 8; j++) {
					al.add(adList.get(rand.nextInt(adList.size())));
					alLength = alLength
							.plus(Commercial.PROMOTION_LENGTH);
				}
				for (int k = 0; k < 2; k++) {
					al.add(radioSpotList.get(rand.nextInt(radioSpotList.size())));
					alLength = alLength
							.plus(RadioSpot.PROMOTION_LENGTH);
				}
				promoTime += 1800;
				if (testing) {
					System.out.print("promoTime= ");
					System.out.println(promoTime);
				}
				// if it's been an hour since adding Reports & there's room for them
				if (alLength.getSeconds() >= reportTime
						&& alLength.getSeconds() <= (4 * 3600 - 300)) {

					al.add(newsReportList.get(rand.nextInt(newsReportList.size())));
					alLength = alLength.plus(NewsReport.NEWS_REPORT_LENGTH);
					al.add(trafficReportList.get(rand.nextInt(trafficReportList
							.size())));
					alLength = alLength
							.plus(TrafficReport.TRAFFIC_REPORT_LENGTH);
					repTime += 3600;
					if (testing)
						System.out.println("repTime=" + repTime);
				}
			}
		}
		playlist = al;
		currentLength = alLength;
		promotionTime = promoTime;
		reportTime = repTime;
	}

	/**
	 * Return a "DJ Signoff" radiospot with the remaining amount of time if the
	 * remaining amount of time is <= MAX_TIME.minus(TIME_REMAINING_EPSILON)
	 * 
	 * @throws OverTimeException
	 */
	public RadioSpot fillRemainingtime() throws OverTimeException {
		if (timeLeft().compareTo(MAX_TIME.minus(TIME_REMAINING_EPSILON)) <= 0) {
			return new RadioSpot("End of Set: DJ Signoff",
					MAX_TIME.minus(currentLength));
		}
		return null;
	}

	/**
	 * @return a String representation of the remaining time in "0:00:00" format
	 * @throws OverTimeException
	 */
	public String timeRemaining() throws OverTimeException {
		String str = "Time Remaining: ";
		Duration tr = timeLeft();
		long hrs = tr.toHours();
		str += tr.toHours() + ":";
		long min = tr.minusHours(tr.toHours()).toMinutes();
		if (min < 10)
			str += 0;
		str += min + ":";
		long sec = tr.minusHours(hrs).minusMinutes(min).getSeconds();
		if (sec < 10)
			str += 0;
		str += sec;
		return str;
	}

	@Override
	public String toString() {
		String str = playlistName;
		for (int i = 0; i < playlist.size(); i++) {
			if (playlist.get(i) != null) {
				str += ((i + 1) + ". " + playlist.get(i));
			}
		}
		return str;
	}

	public Duration getLength() {
		return currentLength;
	}

	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

	public Broadcastable getItem(int index) {
		return playlist.get(index);
	}

	/**
	 * set Playlist's playlist ArrayList to the ArrayList parameter
	 * 
	 * @param playlist
	 * @throws Exception
	 */
	public void setPlaylist(ArrayList<Broadcastable> playlist)
			throws OverTimeException {
		for (Broadcastable item : playlist) {
			currentLength.plus(item.getLength());
			if (currentLength.compareTo(MAX_TIME) > 0) {
				this.playlist = null;
				throw new OverTimeException();
			}
		}
		this.playlist = playlist;
	}

	/**
	 * @return ArrayList<Song> ArrayList of playlist in the playlist
	 */
	public ArrayList<Broadcastable> getItems() {
		return playlist;
	}

	/**
	 * 
	 * @param song
	 * @return true if at least one instance of song is in playlist
	 */
	public boolean find(Song song) {
		if (song == null)
			return false;
		for (int i = 0; i < playlist.size(); i++) {
			if (playlist.get(i).toString().contains(song.toString())) {
				return (true);
			}
		}
		return false;
	}

	/**
	 * Returns index of first instance of song if it is in playlist; if not,
	 * returns Integer.MIN_VALUE
	 */
	public int search(String song) {
		for (int i = 0; i < playlist.size(); i++) {
			if (playlist.get(i) instanceof Song) {
				if (((Song) playlist.get(i)).getTitle().contains(song))
					return (i + 1);
			}
		}
		return SONG_NOT_FOUND;
	}
	
	public void reset(){
		playlist = new ArrayList<Broadcastable>();
		currentLength = Duration.ofSeconds(0l);
	}

} // end Playlist class
