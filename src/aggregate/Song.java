package aggregate;

/**
 * A Broadcastable Song class. It contains basic information about the song and when it was last played.
 * @author Brian French
 */
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Song extends Broadcastable {

	private String title;
	private String artist;
	private Duration length;
	private String lengthString;
	private boolean testing = false;
	private String rds = "";
	private LocalDate lastPlayed;

	/**
	 * Constructor that works with reading Songs from a file
	 * 
	 * @param title
	 * @param artist
	 * @param length
	 * @param lastDate
	 * @throws ParseException
	 */
	public Song(String title, String artist, String seconds, String lastDate)
			throws ParseException {
		this.title = title;
		this.artist = artist;
		this.length = Duration.ofSeconds(Long.parseLong(seconds.trim()));

		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		lastPlayed = LocalDate.parse(lastDate.trim(), format);
		lengthString = ""+length.toMinutes() + ":"
		+ length.minusMinutes(length.toMinutes()).getSeconds();
		if (testing)
			System.out.println("New Song: " + toString());
		if (testing)
			System.out.println("Time: " + lengthString);
		if (testing)
			System.out.println("Date: " + lastDate);
	}

	/**
	 * 
	 * @return The Length of the song in seconds
	 */
	protected Duration getSongLength() {

		return length;
	}

	// /**
	// * @return The file formated String of the Song
	// */
	// public String toString(){
	// return "Title: " + title + ",  Artist: " + artist
	// + "Song Length: " + length + "  Last Played:  " + lastDate.getTime();
	// //return "Title: " + name + ",  Artist: " + artist;
	// }

	@Override
	public String toString() {
		return this.getArtist() + " - " + this.getTitle();
	}

	/**
	 * 
	 * @return The last date the Song was played
	 */
	protected LocalDate getDate() {
		return lastPlayed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Duration getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = Duration.ofSeconds((long) length);
	}

	/**
	 * equals()
	 * 
	 * @return true if the Object is a Song and the title and artist are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (o.getClass().getSimpleName().equals("Song")) {
			if (((Song) o).getTitle().equals(title)) {
				if (((Song) o).getClass().equals(artist)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String play() {
		lastPlayed = LocalDate.now();
		return displayRDS();
	}

	@Override
	public String displayRDS() {
		rds = this.toString() + ", " + length.toMinutes() + ":"
				+ length.minusMinutes(length.toMinutes()).getSeconds();
		return rds;
	}

}
