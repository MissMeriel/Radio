package v4;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 *  Program which scrolls desired text in a JTextField.
 *
 */
public class MarqueePanel extends JPanel{
	protected static Timer marqueeTimer;
	private String marqueeText;
	private JTextField textOutput = new JTextField();

	
	/**
	 *  Constructor for the Marquee object
	 *  @param  marquee  String passed is the desired marquee message.
	 */
	public MarqueePanel(String marquee) {
		//setLayout(new GridLayout(1,1));
		setPreferredSize(new Dimension(100,50));
		textOutput.setEditable(false);
		// Hex code: CCFFE5 (light green background)
		//156 was yellow
		textOutput.setBackground(Color.getHSBColor(3000, 34, 300));
		//textOutput.setBorder();
		//setPreferredSize(new Dimension(250, 100));
		marqueeText = marquee;
		marqueeTimer = new Timer(200,
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					marqueeText = marqueeText.substring(1, marqueeText.length()) + marqueeText.charAt(0);
					textOutput.setText(marqueeText);
				}
			} );
		add(textOutput);
		marqueeTimer.start();
		textOutput.setVisible(true);
		
	}

	/**
	 *  Class extends toString.
	 *
	 *  @return    Returns a string format of the marquee object.
	 */
	public String toString() {
		return marqueeText;
	}
	
	public JTextField getTextField() {
		return textOutput;
	}
	
	/**
	 * Sets the text of the marquee
	 * @param text
	 */
	public void setMarqueeText(String text) {
		
		System.out.println("inside setMarqueeText");
		marqueeText = text;
		textOutput.setText(marqueeText);
		System.out.println(textOutput.getText());
		System.out.println("GetParent() is null: " +(getParent() == null));

	}
	
	public void stop() {
		marqueeTimer.stop();
	}
	
	public void start() {
		marqueeTimer.start();
	}
}

