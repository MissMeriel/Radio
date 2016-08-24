package v1;

import javax.swing.JOptionPane;

public class OverTimeException extends Throwable {

	public OverTimeException() {
		super("length of playlist greater than max time; songs not set");
		JOptionPane.showMessageDialog(null,
			    "Your playlist has exceeded the maximum allowed time. Item not added.",
			    "Warning: Over Time",
			    JOptionPane.WARNING_MESSAGE);
	}

	public OverTimeException(String message){
		super(message);
	}
	
	public OverTimeException(Throwable cause){
		super(cause);
	}
	
	public OverTimeException(String message, Throwable cause){
		
	}

}
