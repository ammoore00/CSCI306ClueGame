package clueGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GUIMenu extends JMenu {
	JMenu menu;
	
	public GUIMenu() {
		menu = new JMenu("File");
	}
	
	public JMenu createFileMenu() {
		menu.add(createDetectiveNotesItem());
		menu.add(createFileExitItem());
		return menu;
	}
	
	private JMenuItem createDetectiveNotesItem() {
		JMenuItem item = new JMenuItem("Detective Notes");
		item.addActionListener(new ButtonListener());
		return item;
	}
	
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		
		return item;
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	
}
