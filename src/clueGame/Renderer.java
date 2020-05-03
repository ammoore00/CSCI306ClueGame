/*
 * Team Members:
 * Rebecca Cowgill
 * Abigail Moore
 * 
 */

package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Renderer {
	private static Renderer instance = new Renderer();
	
	public static final int DEFAULT_X_RESOLUTION = 1000;
	public static final int DEFAULT_Y_RESOLUTION = 1000;
	
	private JFrame frame;
	private JMenuBar menuBar;
	private Board board = Board.getInstance();

	private GUIBoard guiBoard;
	private GUIControlPanel guiControlPanel;
	
	//Singleton pattern
	private Renderer() {
		
	}
	
	public static Renderer getInstance() {
		return instance;
	}
	
	public void initialize() {
		//Create new frame to hold panels
		frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Clue Game");
		frame.setResizable(false);
		frame.setSize(DEFAULT_X_RESOLUTION, DEFAULT_Y_RESOLUTION);
		
		//add menuBar to JFrame
		initializeMenuBar();
		initializeControlGUI();
		initializeBoardGUI();
		
		frame.setVisible(true);
	}
	
	private void initializeMenuBar() {
		//create menu object
		GUIMenu menu = new GUIMenu();
		//create menuBar
		menuBar = new JMenuBar();
		//add menu object to menuBar
		menuBar.add(menu.createFileMenu());
		frame.setJMenuBar(menuBar);
	}
	
	private void initializeControlGUI() {
		guiControlPanel = new GUIControlPanel();
		frame.add(guiControlPanel, BorderLayout.PAGE_END);
	}
	
	private void initializeBoardGUI() {
		guiBoard = new GUIBoard();
		guiBoard.addMouseListener(guiBoard);
		frame.add(guiBoard);
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
