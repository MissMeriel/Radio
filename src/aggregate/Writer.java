package aggregate;
/**
 * A class designed to write both playlists and songs to a file. It can either append or overwrite
 * @author Brian French
 * 
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {

	private String fileName;
	private Boolean overWrite;
	private BufferedWriter writer;
	private File file;
	
	/**
	 * 
	 * @param fileName The file name to be written to
	 * @param overwrite Whether to append to the file or not
	 * @throws IOException
	 */
	public Writer(String fileName, Boolean overwrite) throws IOException{
		this.fileName = fileName;
		this.overWrite = overwrite;
		
		File file = new File(fileName);
		this.writer = new BufferedWriter(new  FileWriter(file, overwrite));
		//System.out.println("File writer test constructor");

	}
	
	/**
	 * 
	 * @param fileName The name of the new file
	 * @param overwrite whether to append or not
	 * @param p the new playlist to be added
	 * @throws IOException
	 */
	public Writer(String fileName, Boolean overwrite, ArrayList<Broadcastable> p) throws IOException{
		this.fileName = fileName;
		this.overWrite = overwrite;
		
		File file = new File(fileName);
		this.writer = new BufferedWriter(new  FileWriter(file, overwrite));
		//System.out.println("File writer test constructor");
		writeList(p);
	}
	
	/**
	 * 
	 * @param s The broadcastable item to be written
	 * @throws IOException
	 */
	protected void writeBroadcast(Broadcastable s) throws IOException{
		//System.out.println("File writer test");
		writer.write(s.toString());
		writer.newLine();
		writer.flush();
	}
	
	/**
	 * Creates a new Buffered File Writer with a set append boolean. Not efficient
	 * @param ap Whether to append or not
	 * @throws IOException
	 */
	protected void setAppend(Boolean ap) throws IOException{
		writer.close();
		writer = new BufferedWriter(new FileWriter(new File(fileName), ap));
	}
	
	/**
	 * 
	 * @param p The Playlist to be written
	 * @throws IOException
	 */
	protected void writeList(ArrayList<Broadcastable> p) throws IOException{
		

		for(Broadcastable b: p){
			writeBroadcast(b);
		}
		
		
	}
	
	/**
	 * Closes the writer. To be called from Driver on close
	 * @throws IOException
	 */
	protected void close() throws IOException{
		writer.close();
	}
	
	public boolean deleteFile(){
		return file.delete();
	}
	
	
}
