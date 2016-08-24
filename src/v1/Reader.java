package v1;

/**
 * A class that reads in Broadcastable objects from a file
 * @author Brian French
 * 
 * File Paths ./ + 
 * HotList
 * ColdList
 * TrafficReportList
 * NewsReportList
 * CommercialList
 * SongList
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Reader {
	// private File file;
	// private Scanner reader;
	private Scanner lineReader;
	private String line;
	private boolean testing = false;
	private Scanner reader;
	private String fileName = null;

	public Reader(String fileName) throws FileNotFoundException {
		File f = new File(fileName);
		reader = new Scanner(f);
		this.fileName = fileName;
	}

	/**
	 * Reads a series of songs from a file and adds to an array
	 * 
	 * @return ArrayList of file
	 * @throws ParseException
	 */
	protected ArrayList<Song> getSongs() throws ParseException {
		ArrayList<Song> songs = new ArrayList<Song>();
		while (reader.hasNextLine()) {
			if (testing)
				System.out.println("getSongs");
			line = reader.nextLine();
			lineReader = new Scanner(line);

			Song s = new Song(getNextVar(), getNextVar(), getNextVar(),
					getNextVar());
			if (testing)
				System.out.println(s);
			songs.add(s);
		}

		return songs;
	}

	/**
	 * 
	 * @return An ArrayList of Commercials
	 */
	protected ArrayList<Commercial> getCommercials() {
		ArrayList<Commercial> coms = new ArrayList<Commercial>();
		while (reader.hasNextLine()) {
			line = reader.nextLine();
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			Commercial c = new Commercial(getNextVar(), getNextVar());
			coms.add(c);
		}

		return coms;
	}

	/**
	 * 
	 * @return An ArrayList of Spots
	 */
	protected ArrayList<RadioSpot> getSpots() {
		ArrayList<RadioSpot> spots = new ArrayList<RadioSpot>();
		while (reader.hasNextLine()) {
			line = reader.nextLine();
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			RadioSpot s = new RadioSpot(getNextVar(), getNextVar());
			spots.add(s);
		}
		return spots;
	}

	/**
	 * 
	 * @param traffic
	 *            True if reading a traffic report. False if a News Report
	 * @return An ArrayList of Reports
	 */
	protected ArrayList<Report> getReports(boolean traffic) {
		ArrayList<Report> reports = new ArrayList<Report>();
		Report s = null;

		while (reader.hasNextLine()) {
			line = reader.nextLine();
			// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			if (traffic) {
				try {
					s = new TrafficReport(getNextVar());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					s = new NewsReport();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			reports.add(s);
		}

		return reports;
	}

	/**
	 * 
	 * @return returns the next variable in the line as String
	 */
	private String getNextVar() {
		String s = "";
		String holder = null;

		do {
			if (lineReader.hasNext()) {

				holder = lineReader.next();
				// if (testing)
				// System.out.print("Line Reader has Next     " + holder
				// + "\n");
				if (holder.equals("+")) {
					// if(testing) System.out.println("Holder = +");
					break;
				}
				s += " " + holder;

			} else
				break;
		} while (!holder.equals("+") && lineReader.hasNext());

		// if (testing)
		// System.out.println("S = " + s);

		return s;
	}

}
