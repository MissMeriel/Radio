package v4;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class RadioStationGUI extends JFrame {
	private JTabbedPane tp;
	private JPanel playlistMakerTab, finalizedPlaylistsPanel;
	private FinalizedPlaylistPanel finalizedPlaylistsTab;
	private static PlaylistMakerPanel playlistMakerPanel;
	private static SongInputPanel songInputPanel;
	protected Playlist playlist = new Playlist("");
	protected static NewSongs newSongs = new NewSongs();
	protected static OldSongs oldSongs = new OldSongs();
	boolean testing = false;
	protected MarqueePanel mPanel;
	protected static Simulation sim;

	public RadioStationGUI() {

		// setup Jframe
		super("WACM DJ Set Playlist");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 650));
		setPreferredSize(new Dimension(500, 650));

		
		try {
			sim = new Simulation();
		} catch (FileNotFoundException | ParseException e) {
			System.out.println("error in simulation");
		}
		
		

		// native method constructs tabbed pane
		makeTabbedPane();
	}

	private void makeTabbedPane() {
		tp = new JTabbedPane();
		playlistMakerTab = new JPanel();
		playlistMakerTab.setLayout(new BorderLayout());
		finalizedPlaylistsTab = new FinalizedPlaylistPanel();
		mPanel = new MarqueePanel("RDS Emulator ");

		// Add this panel that has our songInputPanel to our big panel.
		// songInputPanel will occupy the left cell.
		songInputPanel = new SongInputPanel(playlist, newSongs, oldSongs);

		// Add right panel to second cell in GridLayout.
		playlistMakerPanel = new PlaylistMakerPanel(playlist, mPanel,
				finalizedPlaylistsTab);
		playlistMakerTab.add(playlistMakerPanel, BorderLayout.EAST);
		playlistMakerTab.add(songInputPanel, BorderLayout.WEST);
		// JPanel rdsPnl = new JPanel(new FlowLayout());
		// rdsPnl.add(Marqu);
		playlistMakerTab.add(mPanel, BorderLayout.SOUTH);
		finalizedPlaylistsPanel = new FinalizedPlaylistPanel();
		finalizedPlaylistsTab.add(finalizedPlaylistsPanel);

		tp.addTab("Playlist Maker", playlistMakerTab);
		tp.addTab("Finalized Playlists", finalizedPlaylistsTab);

		// Need to add that SSListener to the pMP, or else we can't do what we
		// wrote
		playlistMakerPanel.playlistList
				.addMouseListener(new PlaylistSongSelectionListener());
		songInputPanel.newSongList
				.addListSelectionListener(new SongSelectionListener());
		songInputPanel.oldSongList
				.addMouseListener(new OldSongSelectionListener());

		add(tp);
		pack();
		setVisible(true);
	}

	/**
	 * populate NewSongs (the hot list) and update when a week has passed
	 */
	public static void updateSongs() {
		newSongs.newList(sim.getNewList());
		
		oldSongs.setList((sim.getOldList()));
		songInputPanel.oldSongList.setListData(oldSongs.getValidSongs().toArray());
		songInputPanel.newSongList.setListData(newSongs.getSongs().toArray());
	} // end updateSongs()

	private class SongSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Song current = (Song) ((JList) e.getSource()).getSelectedValue();
			System.out.println(current);
			if (!e.getValueIsAdjusting()) {
				if (testing)	System.out.print("Mouse pressed, ");
				// if event source is the JList newSongs
				if (e.getSource().equals(songInputPanel.newSongList)) {
					if(testing) System.out.println("newSongs");
					// add selected song to playlist
					try {
						playlist.addItem(current);
					} catch (ParseException e1) {
						e1.printStackTrace();
					} catch(OverTimeException e1) {
						OverTimeException.showPopup();
					}
					playlistMakerPanel.playlistList.setListData(playlist
							.getItems().toArray());
					try {
						playlistMakerPanel.updateTimeRemaining(playlist
								.timeRemaining());
					} catch (OverTimeException e1) {
					}
					// ((JList) e.getSource()).getParent().revalidate();
				}
			}
		}
	}
	public class PlaylistSongSelectionListener implements MouseListener {
		
		public void mouseClicked(MouseEvent e) {
		// if event source is the playlist
		// NEEDS to be playlistList, NOT playlistListModel
		if (playlist.getItems().size() > 0
				&& playlistMakerPanel.playlistList.getSelectedIndex() > -1) {
			System.out.println("playlistMakerPanel");
			int index = playlistMakerPanel.playlistList
					.getSelectedIndex();

			try {
				System.out.println("selected index: " + index);
				playlist.deleteSong(index);
				
				//     These messed up my stuff for some reason -tom
				//playlistMakerPanel.playlistListModel.remove(index);
				//playlistMakerPanel.playlistList.clearSelection();

				// This is what "repaints" the JList
				playlistMakerPanel.playlistList.setListData(playlist
						.getItems().toArray());
				((MyNewListModel) songInputPanel.oldSongListModel).update();
				songInputPanel.oldSongList.setSelectedIndex(0);
				songInputPanel.oldSongList.setSelectedIndex(10);

				int size = playlistMakerPanel.playlistListModel
						.getSize();
				System.out.println("playlistListModel size: " + size);

			} catch (IndexOutOfBoundsException excep) {
				playlistMakerPanel.playlistList.setListData(playlist
						.getItems().toArray());
			}
			try {
				playlistMakerPanel.updateTimeRemaining(playlist
						.timeRemaining());
			} catch (OverTimeException e1) {
			}
		}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub		
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub		
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub		
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub	
		}
	}
	
	// Using a mouse listener for old songs
	public class OldSongSelectionListener implements MouseListener {
		// Using MouseListener lets me select items in the list to update visuals,
		// without actually adding those items to the playlist
		@Override
		public void mouseClicked(MouseEvent e) {
			Song current = (Song) ((JList) e.getSource()).getSelectedValue();
				if(!playlist.find(current)) {
					try {
						playlist.addItem(current);
					} catch (ParseException | OverTimeException e1) {
						OverTimeException.showPopup();
					} 
				}
				playlistMakerPanel.playlistList.setListData(playlist
							.getItems().toArray());
				try {
					playlistMakerPanel.updateTimeRemaining(playlist
							.timeRemaining());
				} catch (OverTimeException e1) {
				}
			}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub		
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub		
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub		
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub	
		}
		}
}