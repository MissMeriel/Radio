package v3;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;

import javax.swing.*;

public class FinalizedPlaylistPanel extends JPanel {
	
	Playlist playlist;
	private static int panelCount = 0;
	JLabel listLabel;
	Writer writer;

	public FinalizedPlaylistPanel() {
		super();
		// The second tab!
		// finalizedPlaylistsTab.setLayout(new GridLayout());
		// default GridLayout constructor does one row w/ as many columns as
		// components added
		setLayout(new GridLayout());

	}

	/**
	 * Display each playlist we have saved using a new JScrollPane for each
	 * playlist we have in some collection
	 */
	public void addFinalizedPanel(Playlist playlist) {

		this.playlist = playlist;

		FinalizedPanel finalizedPanel = new FinalizedPanel();
//		JPanel finalizedPanel = new JPanel();
//		finalizedPanel
//				.setLayout(new BoxLayout(finalizedPanel, BoxLayout.Y_AXIS));
//		finalizedPanel.setPreferredSize(new Dimension(200, 400));
//		if (playlist.getPlaylistName().equals("")) {
//			listLabel = new JLabel("Untitled: " + LocalDate.now().toString());
//		} else {
//			listLabel = new JLabel(playlist.getPlaylistName());
//		}
//		DefaultListModel finalDLM = new DefaultListModel();
//		playlist.getItems().forEach(e -> finalDLM.addElement(e));
//		JList finalList = new JList(finalDLM);
//
//		JScrollPane playlistScroller = new JScrollPane(finalList);
//		playlistScroller.setPreferredSize(new Dimension(10, 400));
//		finalizedPanel.add(listLabel);
//		finalizedPanel.add(playlistScroller);
//		JButton deleteButton = new JButton("Delete");
//		deleteButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				//e.getSource().
//				remove(finalizedPanel);
//				panelCount--;
//				
//			}
//
//		});
//		finalizedPanel.add(deleteButton);
		add(finalizedPanel);
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

			JScrollPane playlistScroller = new JScrollPane(finalList);
			playlistScroller.setPreferredSize(new Dimension(10, 400));
			add(listLabel);
			add(playlistScroller);
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					((JPanel) ((JButton) e.getSource()).getParent().getParent()).remove(((JButton) e.getSource()).getParent());
					writer.deleteFile();
					panelCount--;
					
				}

			});
			add(deleteButton);
		}
		
	}

}
