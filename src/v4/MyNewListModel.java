package v4;

import javax.swing.DefaultListModel;

public class MyNewListModel extends DefaultListModel {
	public void update() {
		fireContentsChanged(this, 0, this.size());
	}

}
