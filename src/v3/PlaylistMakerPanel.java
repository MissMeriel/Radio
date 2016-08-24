package v3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PlaylistMakerPanel extends JPanel {

	private JLabel playlistTitle = new JLabel("");;
	private JButton playlistTitleEdit = new JButton("Enter");
	private JTextField playlistTitler = new JTextField("Your Playlist Title");
	private JLabel timeRemaining = new JLabel("Time Remaining: 4:00:00");
	private JPanel topPanel = new JPanel(new GridLayout(2, 1));
	private JPanel top, mid, midPanel, bot, botPanel;
	public DefaultListModel playlistListModel;
	public JList playlistList = new JList();
	public JScrollPane playlistPane;
	public Playlist playlist;
	boolean testing = true;
	private JButton finalizeButton, rdsButton, clearButton;
	private MarqueePanel mP;
	private FinalizedPlaylistPanel finalizedPanel;

	public PlaylistMakerPanel(Playlist playlist, MarqueePanel mP,
			FinalizedPlaylistPanel finalizedPanel) {
		this.playlist = playlist;
		this.mP = mP;
		this.finalizedPanel = finalizedPanel;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250, 400));

		makeTitlePanel();
		makeMiddlePanel();
		makeBottomPanel();

		// Add the panels to their respective places.
		add(topPanel, BorderLayout.NORTH);
		add(mid, BorderLayout.CENTER);
		add(bot, BorderLayout.SOUTH);
	}

	public void updateTimeRemaining(String str) {
		timeRemaining.setText(str);
	}

	public JList getList() {
		return playlistList;
	}

	private void makeTitlePanel() {

		// top holds playlist title and timeRemaining
		top = new JPanel();

		// grid layout of 2 rows and 1 column
		top.setLayout(new GridLayout(1, 2));

		playlistTitler.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				playlistTitler.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (playlistTitler.getText().equals("")) {
					playlistTitler.setText("Your Playlist Title");
				}
			}
		});

		// when edit/enter button clicked, playlist title is entered/edited
		playlistTitleEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if the title is editable
				if (playlistTitleEdit.getText().equals("Enter")) {
					playlistTitle.setText(playlistTitler.getText());
					playlistTitleEdit.setText("Edit");
					// top.remove(playlistTitler);
					top.removeAll();
					top.add(playlistTitle);
					top.add(playlistTitleEdit);
					playlist.setPlaylistName(playlistTitle.getText());

					// if the user wants to edit the title
				} else {
					top.removeAll();
					top.add(playlistTitler);
					playlistTitleEdit.setText("Enter");
					top.add(playlistTitleEdit);
				}

			}
		});
		top.add(playlistTitler);
		top.add(playlistTitleEdit);
		// playlistTitleEdit.setAlignmentY(RIGHT_ALIGNMENT);
		topPanel.add(top);
		topPanel.add(timeRemaining);
	}

	private void makeMiddlePanel() {
		playlistListModel = new DefaultListModel();
		if (playlist.getItems().size() > 0) {
			for (Broadcastable item : playlist.getItems()) {
				playlistListModel.addElement(item);
			}
		}
		playlistList = new JList(playlistListModel);
		playlistList.setVisibleRowCount(15);
		playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistList.setCellRenderer(new PlaylistCellRenderer());
		playlistPane = new JScrollPane(playlistList);
		playlistPane.setOpaque(true);
		playlistPane.setPreferredSize(new Dimension(250, 300));

		// mid panel displays the playlist we're working on.
		mid = new JPanel();
		mid.add(playlistPane);
	}

	private void makeBottomPanel() {
		// rds button & listener
		rdsButton = new JButton("View RDS");
		rdsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("inside actionlistener for View in RDS");
				// mP.setMarqueeText(playlist.getItems().get(0).toString());
				for (int j = 0; j < playlist.getItems().size(); j++) {
					Broadcastable item = playlist.getItem(j);
					Timer textTimer = new Timer((int) item.getLength()
							.getSeconds(), new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							mP.setMarqueeText(item.displayRDS());
						}
					});
				}
			}
		});

		finalizeButton = new JButton("Finalize");

		// finalize button & listener -- finish actionlistener for Finalize
		// button
		// adds playlist to finalizedPlaylistsTab if its length is sufficient
		finalizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (playlist.timeLeft().getSeconds() < Playlist.TIME_REMAINING_EPSILON
							.getSeconds()) {
						System.out.println("finalized if");
						finalizedPanel.addFinalizedPanel(playlist);

					} else {
						System.out.println("finalized else");
						JOptionPane
								.showMessageDialog(
										null,
										"Your playlist is under time. Length must be at least 3:58:30",
										"Warning: Under Time",
										JOptionPane.WARNING_MESSAGE);
					}
				} catch (OverTimeException e1) {
					System.out.println("finalized catch");
					OverTimeException.showPopup();
					e1.printStackTrace();
				}
			}
		});

		// clearButton clears out playlist and playlistPane and playlistList
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playlist.reset();
				playlistList.setListData(playlist.getItems().toArray());
				
				// if the title is editable
				if (playlistTitleEdit.getText().equals("Enter")) {
					playlistTitler.setText("Your playlist name");

					// if the user wants to edit the title
				} else {
					top.removeAll();
					top.add(playlistTitler);
					playlistTitler.setText("Your playlist name");
					playlistTitleEdit.setText("Enter");
					top.add(playlistTitleEdit);
				}
				updateTimeRemaining("Time remaining: 4:00:00");
			}
		});

		bot = new JPanel();
		bot.setLayout(new FlowLayout());
		bot.setPreferredSize(new Dimension(250, 100));
		bot.add(rdsButton);
		bot.add(finalizeButton);
		bot.add(clearButton);

	}

	private class PlaylistCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component comp = super.getListCellRendererComponent(list, value,
					index, false, false);

			if (isSelected && cellHasFocus) {

				comp.setForeground(Color.gray);
				comp.setEnabled(false);
			}
			return comp;
		}
	} // end PlaylistCellRendererComponent

} // end PlaylistMakerPanel

