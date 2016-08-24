package v3;

import javax.swing.*;
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
	private PlaylistMakerPanel playlistMakerPanel;
	private SongInputPanel songInputPanel;
	protected Playlist playlist = new Playlist("");
	protected NewSongs newSongs = new NewSongs();
	protected OldSongs oldSongs = new OldSongs();
	boolean testing = false;
	protected MarqueePanel mPanel;

	public RadioStationGUI() {

		// setup Jframe
		super("WACM DJ Set Playlist");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 650));
		setPreferredSize(new Dimension(500, 650));

		// update newSongs and oldSongs
		updateSongs();

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
				.addListSelectionListener(new SongSelectionListener());
		songInputPanel.newSongList
				.addListSelectionListener(new SongSelectionListener());
		songInputPanel.oldSongList
				.addListSelectionListener(new SongSelectionListener());

		add(tp);
		pack();
		setVisible(true);
	}

	/**
	 * populate NewSongs (the hot list) and update when a week has passed
	 */
	public void updateSongs() {
		if (testing)
			System.out.println("updateNewSongs()");
		if (newSongs.getLastUpdated() != null) {

			if (LocalDate.now().minusWeeks(1)
					.compareTo(newSongs.getLastUpdated()) < 0) {
				oldSongs.add(newSongs.update());
			}
		} else {
			oldSongs.update();
			oldSongs.add(newSongs.update());
		}
	} // end updateSongs()

	private class SongSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Song current = (Song) ((JList) e.getSource()).getSelectedValue();
			System.out.println(current);
			if (!e.getValueIsAdjusting()) {
				if (testing)	System.out.print("Mouse pressed, ");
				// if event source is the playlist
				// NEEDS to be playlistList, NOT playlistListModel
				if (e.getSource().equals(playlistMakerPanel.playlistList)
						&& playlist.getItems().size() > 0
						&& playlistMakerPanel.playlistList.getSelectedIndex() > -1) {
					if(testing)System.out.println("playlistMakerPanel");
					int index = playlistMakerPanel.playlistList
							.getSelectedIndex();
					try {
						if(testing)System.out.println("selected index: " + index);
						playlist.deleteSong(index);
						playlistMakerPanel.playlistListModel.remove(index);
						playlistMakerPanel.playlistList.clearSelection();

						// This is what "repaints" the JList
						playlistMakerPanel.playlistList.setListData(playlist
								.getItems().toArray());

						int size = playlistMakerPanel.playlistListModel
								.getSize();
						if(testing) System.out.println("playlistListModel size: " + size);

					} catch (IndexOutOfBoundsException excep) {
						playlistMakerPanel.playlistList.setListData(playlist
								.getItems().toArray());
					}
				}

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
					((JList) e.getSource()).getParent().revalidate();
				}

				// if event source is the JList oldSongs
				if (e.getSource().equals(songInputPanel.getOldSongsList())) {
					if(testing)System.out.println("oldSongs");

					// add selected song to playlist
					try {
						playlist.addItem(current);
					} catch (ParseException e1) {
						e1.printStackTrace();
					} catch (OverTimeException e1) {
						OverTimeException.showPopup();
					}
					playlistMakerPanel.playlistList.setListData(playlist
							.getItems().toArray());
					if(testing) System.out.println("playlistListModel size: "
							+ playlistMakerPanel.playlistList.getModel()
									.getSize());
				}
				try {
					playlistMakerPanel.updateTimeRemaining(playlist
							.timeRemaining());
				} catch (OverTimeException e1) {
					OverTimeException.showPopup();
				}
			}
		} // end valueChanged()
	}// end SongSelectionListener

}
