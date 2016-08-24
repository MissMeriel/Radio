package v1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TestDriver extends JFrame {

	NewSongs newSongs = new NewSongs();
	OldSongs oldSongs = new OldSongs();
	Playlist playlist = new Playlist("UM");
	SongInputPanel sIP = new SongInputPanel();
	PlaylistPanel pP = new PlaylistPanel();

	public TestDriver() {
		// setup Jframe
		super("TestDriver Playlist");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 400));
		setLayout(new GridLayout(1, 2));

		oldSongs.add(newSongs.update());
		playlist.addSong(newSongs.getSongs().get(5));
		System.out.println("playlist size: " + playlist.getSongs().size());
		JPanel sIP = new SongInputPanel();
		System.out.println("song input panel made");
		JPanel pP = new PlaylistPanel();
		System.out.println("playlist panel made");
		add(sIP, 0);
		add(pP, 1);
		pack();
		setVisible(true);

	}

	public class SongInputPanel extends JPanel {

		DefaultListModel dlm;
		JList songList = new JList();

		public SongInputPanel() {

			setPreferredSize(new Dimension(200, 400));
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			// set up panel to go inside of JScrollPane
			dlm = new DefaultListModel();

			newSongs.getSongs().forEach(e -> dlm.addElement(e));

			System.out.println("components in newSongPanel: " + dlm.size());

			songList = new JList(dlm);
			songList.addListSelectionListener(new SongSelectionListener());
			JScrollPane newSongPane = new JScrollPane(songList);

			add(newSongPane);
			// System.out.println("SongInputPanel constructed");
			// System.out.println("newSongs null: " + (newSongs == null));

		} // end constructor
	} // end SongInputPanel

	public class PlaylistPanel extends JPanel {

		public JList playlistList;
		JScrollPane playlistPane;
		DefaultListModel playlistListModel;

		public PlaylistPanel() {

			setPreferredSize(new Dimension(200, 400));
			System.out.println("in playlist panel constructor");
			System.out.println("playlist size: " + playlist.getSongs().size());

			playlistListModel = new DefaultListModel();
			if (playlist.getSongs().size() > 0) {
				for (Song song : playlist.getSongs()) {
					playlistListModel.addElement(song);
				}
				playlistList = new JList(playlistListModel);
				playlistList
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				// playlistList.setCellRenderer(new PlaylistCellRenderer());
				playlistList
						.addListSelectionListener(new PlaylistSelectionListener());

				playlistPane = new JScrollPane(playlistList);
				playlistPane.setPreferredSize(new Dimension(200, 350));

				playlistPane.setOpaque(true);
				playlistPane.setVisible(true);
				setOpaque(true);
				setVisible(true);
				System.out.println(playlistList.isShowing() + " "
						+ playlistList.isVisible());
				System.out.println(playlistPane.isVisible() + " "
						+ playlistPane.isShowing());
				this.add(playlistPane);
				// problem might be with container JPanel -- might have a layout
				// that doesn't easily accept new components?
				// adding components to a JLabel that's already displayed?

				playlistList.revalidate();
				playlistList.repaint();
				// pack();
			}
		}
	} // end PlaylistPanel

	public class SongSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Song current = (Song) ((JList) e.getSource()).getSelectedValue();
			if (!e.getValueIsAdjusting()) {
				System.out.println("List event, ");
				System.out.println("source is pP: "
						+ e.getSource().equals(pP.playlistList));
				System.out.println("source is sIP: "
						+ (e.getSource()).equals(sIP.songList));
				System.out.println(e.getSource().getClass().getSimpleName());
				System.out.println(current);

				// if event source is the playlist
				// if (e.getSource().equals(pP.playlistList)
				// && playlist.getSongs().size() > 0
				// && pP.playlistList.getSelectedIndex() > -1) {
				// System.out.println("pP");
				// int index = pP.playlistList.getSelectedIndex();
				//
				// // remove song from playlist and jlist - right now only
				// // removes when you add another song
				// try {
				// System.out.println("selected index: " + index);
				// playlist.deleteSong(index);
				// pP.playlistListModel.remove(index);
				// pP.playlistList.clearSelection();
				// // update GUI to reflect changes
				// // pP.playlistList.removeAll();
				// // pP.playlistList = new
				// // JList(pP.playlistListModel);
				// setVisible(true);
				// pP.playlistPane.revalidate();
				// pP.playlistPane.repaint();
				//
				// int size = pP.playlistListModel.getSize();
				// System.out.println("playlistListModel size: "
				// + size);
				// } catch (ArrayIndexOutOfBoundsException excep) {
				// }
				//
				// // return song to oldSongsList if it was from OldSongs
				// // if (oldSongs.contains(current)) {
				// // // Song song = ((Song)
				// // System.out.println(songInputPanel.getOldSongsList()
				// // .getComponents());
				// // int index1 = oldSongs.getIndexOf(current);
				// // pP.playlistListModel
				// }
				//
				// // pP.playlistPane.
				// revalidate();
				// // pP.playlistPane.
				// repaint();

				// if event source is the JList newSongs
				// if (e.getSource().equals(sIP.songList)) {
				System.out.println("newSongs");
				// add selected song to playlist
				playlist.addSong(current);
				// System.out.println("playlist size: "
				// + playlist.getSongs().size());
				// System.out.println(playlist);
				pP.playlistList.setListData(playlist.getSongs().toArray());
				// System.out.println(pP.playlistList.getModel().equals(playlist
				// .getSongs().toArray()));
				System.out.println(pP.playlistList.getModel().getSize());
				pP.// playlistPane.
				revalidate();
				pP.// playlistPane.
				repaint();
			}
		}
	} // end valueChanged()
	
	public class PlaylistSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			JList jl = pP.playlistList;
			DefaultListModel dlm = pP.playlistListModel;
			int size = dlm.getSize();
			int index = jl.getSelectedIndex();
			if (!e.getValueIsAdjusting() && playlist.getSongs().size() > 0
					&& jl.getSelectedIndex() > -1) {
					System.out.print("Playlist event, selected index: " + index);

				// remove song from playlist and jlist - right now only
				// removes when you add another song
				try {
					
					// remove song from playlist
					playlist.deleteSong(((JList) e.getSource()).getSelectedIndex());
					((JList) e.getSource()).clearSelection();
					
					System.out.println("playlistListModel size: " + dlm.getSize());
				} catch (ArrayIndexOutOfBoundsException excep) {
				}

				pP.playlistPane.revalidate();
				pP.playlistPane.repaint();
			} // end getValueIsAdjusting
		} // end valueChanged
	}// end PlaylistSelectionListener
	
	public static void main(String[] args) {
		new TestDriver();
	}
}