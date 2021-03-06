package aggregate;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PlaylistMakerPanel extends JPanel {

	JLabel playlistTitle = new JLabel("");;
	JButton playlistTitleEdit = new JButton("Enter");
	JTextField playlistTitler = new JTextField("Your Playlist Title");
	JLabel timeRemaining = new JLabel("Time Remaining: 4:00:00");
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

	
	// Stuff I'm using to try to work with the rds display
	Timer rdsTimer;
	int timerCounter = 0;

	public PlaylistMakerPanel(Playlist playlist, MarqueePanel mP,
			FinalizedPlaylistPanel finalizedPanel) {
		this.playlist = playlist;
		this.mP = mP;
		this.finalizedPanel = finalizedPanel;
		setLayout(new BorderLayout());
		mP.getTextField().setVisible(false);
		setPreferredSize(new Dimension(250, 400));
		makeTitlePanel();
		makeMidPanel();
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
		JPanel top = new JPanel();

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
	} // end makeTopPanel
	
	private void makeMidPanel(){
		playlistListModel = new DefaultListModel();
		if (playlist.getItems().size() > 0) {
			for (Broadcastable item : playlist.getItems()) {
				playlistListModel.addElement(item);

			}
		} // end makeMidPanel
		
		playlistList = new JList(playlistListModel);
		playlistList.setVisibleRowCount(15);
		playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistList.addListSelectionListener(new PlaylistSelectionListener());
		playlistList.setCellRenderer(new PlaylistCellRenderer());
		playlistPane = new JScrollPane(playlistList);
		playlistPane.setOpaque(true);
		playlistPane.setPreferredSize(new Dimension(250, 400));

		// mid panel displays the playlist we're working on.
		mid = new JPanel();
		mid.add(playlistPane);
	}

	private void makeBottomPanel(){
		// rds button & listener
		rdsButton = new JButton("View in RDS");
		rdsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testing) System.out.println("inside actionlistener for View in RDS");
				playlistList.setSelectedIndex(0);
				timerCounter = 0;
				Song current = (Song) playlistList.getSelectedValue();
				mP.setMarqueeText(current.toString());
				mP.getTextField().setVisible(!mP.getTextField().isVisible());
				mP.getParent().revalidate();
				mP.getParent().repaint();
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
						finalizedPanel.addFinalizedPanel(playlist);
						
					} else {
						JOptionPane.showMessageDialog(null,
							    "Your playlist must be at least 3:58:30",
							    "Warning: Under Time",
							    JOptionPane.WARNING_MESSAGE);
					}
				} catch (OverTimeException e1) {
					OverTimeException.showPopup();
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
	} // end makeBottomPanel
	
	private class PlaylistSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			// Cool way to check is playlist is being selected by a mouse or not
			// If it is, just turn off rds display. Causes errors that are hard to deal with.
			if(e.getValueIsAdjusting()) {
				rdsTimer.stop();
				mP.getTextField().setVisible(false);
			}
			// If it's not a result of mouse click, that means we're doing our rds thing
			else if(playlistList.getSelectedValue() != null) {
			// rds button default selects first item in list
			Song current = (Song) playlistList.getSelectedValue();
			int time = (int) current.getLength().toMillis();
			rdsTimer = new Timer(1,
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// counter keeps track of where we are
					timerCounter++;
					if(timerCounter < playlist.getItems().size()) {
					mP.setMarqueeText(playlist.getItems().get(timerCounter).toString());
					playlistList.setSelectedIndex(timerCounter);
					}
					// turn off the rds if we're at the end
					if(timerCounter == playlistList.getModel().getSize())
					mP.getTextField().setVisible(false);
				}
			} );
			// Don't repeat actions
			rdsTimer.setRepeats(false);
			// This is where we "simulate" the song playing by waiting
			// rdsTimer.setInitialDelay(2000); 							// short time for testing
			rdsTimer.setInitialDelay((int) playlist.getItems().get(timerCounter).getLength().toMillis()); // want this delay to be the song length
			rdsTimer.start();
		}
		}
	}
	// This looks bad if we use a mouse listener for the playlist.
	// With a mouse listener we can scroll through items using keypad and this makes everything grey
	
	private class PlaylistCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component comp = super.getListCellRendererComponent(list, value,
					index, false, false);

			if (isSelected && cellHasFocus) {

				comp.setBackground(Color.white);
				comp.setForeground(Color.black);
				comp.setEnabled(false);
			}
			return comp;
		}
	} // end PlaylistCellRendererComponent
}

 // end PlaylistMakerPanel

