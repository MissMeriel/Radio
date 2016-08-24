package finalized;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

import javax.swing.*;

public class FinalizedPlaylistPanel extends JPanel {
	
	Playlist playlist;
	private static int panelCount = 0;
	JLabel listLabel;
	Writer writer;
	JPanel playlistsPanel;
	JScrollPane scroller;

	public FinalizedPlaylistPanel() {
		super();
		// The second tab!
		// finalizedPlaylistsTab.setLayout(new GridLayout());
		// default GridLayout constructor does one row w/ as many columns as
		// components added
		playlistsPanel = new JPanel();
		playlistsPanel.setLayout(new BoxLayout(playlistsPanel, BoxLayout.X_AXIS));
		playlistsPanel.setPreferredSize(new Dimension(800,400));
		scroller = new JScrollPane(playlistsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setPreferredSize(new Dimension(450,570));
		add(scroller);
		
	//setLayout(new GridLayout());

	}

	/**
	 * Display each playlist we have saved using a new JScrollPane for each
	 * playlist we have in some collection
	 */
	public void addFinalizedPanel(Playlist playlist) {

		this.playlist = playlist;

		FinalizedPanel finalizedPanel = new FinalizedPanel();
		playlistsPanel.add(Box.createRigidArea(new Dimension(20,20)));
		playlistsPanel.add(finalizedPanel);
		playlistsPanel.add(Box.createRigidArea(new Dimension(20,20)));
		scroller.revalidate();
		panelCount++;
	}
	
	private class FinalizedPanel extends JPanel{
		
		private int panelID = panelCount;
		
		public FinalizedPanel(){
			
			
			String filename = "src/Manager/FinalizedPlaylist"+panelID+".txt";
			try {
				writer = new Writer(filename, true);
				writer.writeList(playlist.getItems());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setPreferredSize(new Dimension(200, 400));
			if (playlist.getPlaylistName().equals("")) {
				listLabel = new JLabel("Untitled: " + LocalDate.now().toString());
			} else {
				listLabel = new JLabel(playlist.getPlaylistName());
			}
			DefaultListModel finalDLM = new DefaultListModel();
			playlist.getItems().forEach(e -> finalDLM.addElement(e));
			JList finalList = new JList(finalDLM);
			finalList.setVisibleRowCount(15);

			JScrollPane playlistScroller = new JScrollPane(finalList);
			playlistScroller.setPreferredSize(new Dimension(10, 100));
			add(listLabel);
			add(playlistScroller);
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					// This delete file is giving me a NullPointerException
					
					//writer.deleteFile();
					RadioStationGUI.savedPlaylists.remove(playlist);
					panelCount--; 
					try (FileOutputStream fileOut = new FileOutputStream("src/Manager/savedplaylists.txt");
							ObjectOutputStream out = new ObjectOutputStream(fileOut);
									) {
						    	out.writeObject(RadioStationGUI.savedPlaylists);
						    	
						    } catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
					((JPanel) ((JButton) e.getSource()).getParent().getParent()).remove(((JButton) e.getSource()).getParent());
					playlistsPanel.revalidate();
				}

			});
			add(deleteButton);
		}
		
	}

}
