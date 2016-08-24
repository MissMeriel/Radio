package v3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDate;

import javax.swing.*;

public class SongInputPanel extends JPanel {

	public SongInputSubPanel songInputSubPanel;

	protected Playlist playlist;
	private boolean testing = true;
	public DefaultListModel newSongListModel = new DefaultListModel();
	public DefaultListModel oldSongListModel = new DefaultListModel();
	public JList newSongList, oldSongList;
	private NewSongs newSongs;
	private OldSongs oldSongs;

	public SongInputPanel(Playlist p, NewSongs ns, OldSongs os) {

		this.playlist = p;
		this.newSongs = ns;
		this.oldSongs = os;
		setPreferredSize(new Dimension(200, 400));
		// Add songInputSubPanel to another panel, SongInputPanel, so that the
		// size of songInputSubPanel can be the size of setPreferredSize() and doesn't
		// have to match size of GridLayout cell.
		JPanel songInputSubPanel = new SongInputSubPanel();
		add(songInputSubPanel);
	}

	public SongInputSubPanel getSongInputSubPanel() {
		return songInputSubPanel;
	}

	public JList getNewSongsList() {
		return newSongList;
	}

	public JList getOldSongsList() {
		return oldSongList;
	}

	public class SongInputSubPanel extends JPanel {

		public SongInputSubPanel() {

			//when I comment this out, left sliver of songInputPanel gets cut off
			setPreferredSize(new Dimension(200, 400));

			// This panel has a vertical box layout to organize new songs and
			// old songs.
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			newSongs.getSongs().forEach(e -> newSongListModel.addElement(e));
			oldSongs.getSongs().forEach(e -> oldSongListModel.addElement(e));
			newSongList = new JList(newSongListModel);
			oldSongList = new JList(oldSongListModel);
			newSongList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			oldSongList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			oldSongList.setCellRenderer(new DisabledItemListCellRenderer());

			JScrollPane newSongPane = new JScrollPane(newSongList);
			JScrollPane oldSongPane = new JScrollPane(oldSongList);

			JLabel oldLabel = new JLabel("Old Songs");
			JLabel newLabel = new JLabel("New Songs");

			// The order of addition and the rigid areas make it look nice.
			add(Box.createRigidArea(new Dimension(30, 0)));
			add(Box.createRigidArea(new Dimension(0, 20)));

			add(newLabel);
			add(newSongPane);
			add(Box.createRigidArea(new Dimension(0, 20)));
			add(oldLabel);
			add(oldSongPane);
			add(Box.createRigidArea(new Dimension(0, 20)));
		} // end constructor

	}

	public class DisabledItemListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			//method called after song is added to playlist
			Component comp = super.getListCellRendererComponent(list, value,
					index, false, false);
			JComponent jc = (JComponent) comp;
			if (isSelected && cellHasFocus && playlist.find((Song) list.getSelectedValue())) {
				
				comp.setForeground(Color.gray);
				comp.setEnabled(false);
				//System.out.println("cellRenderer if, repainted song in playlist? "+ playlist.find((Song) list.getSelectedValue()));
			} else { //if (!playlist.find((Song) list.getSelectedValue())){
				comp.setForeground(Color.black);
				comp.setEnabled(true);
				//System.out.println("cellRenderer elif, repainted song in playlist? "+ playlist.find((Song) list.getSelectedValue()));

			}
			// if (!isSelected) {
			// if ((value.toString()).trim().equals("yellow")) {
			// comp.setForeground(Color.orange);
			// comp.setBackground(Color.magenta);
			// }
			// }
			// return comp;
			// }

			return comp;
		} // end getListCellRendererComponent
	}

}