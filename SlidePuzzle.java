import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * The "SlidePuzzle" class. The main program of this game. Extends JFrame as
 * this class makes the main frame of this game. Also has mouse handling and the
 * DrawPanel class in it which extends JPanel as the DrawingPanel class draws
 * the main panel of this game. DrawingPanel class includes mousePressed and
 * mouseMoved and paintComponent. The SlidePuzzle class implements
 * ActionListener for the actionPerformed method for selecting the menu from the
 * bar on the frame. Some methods used are declareBoard and solve which allows
 * the hint button to work in the game. This main class makes the frame and
 * everything, except for the board and images, needed for the game.
 * 
 * @author Justin Kim, Joel Abraham, Jason Du
 * @version January 15, 2014
 */

public class SlidePuzzle extends JFrame implements ActionListener {
	// All the program variables
	private GraphicalBoard board; // The main board for our game that can be
									// used to use methods in the GraphicalBoard
									// class

	JMenuItem newOption, exitOption, instructionOption, aboutOption; // Menu options for the main frame bar

	Boolean gameStarted = false;// Checks to see if the player clicked New Game in
								// the main menu
	Image easyBoard; // For 3x3 board
	Image hardBoard; // For 4x4 board

	// Different screens which the player will encounter before playing the game
	Image mainMenu;
	Image instructions;
	Image levelSelect;
	Image gameOptions;

	DrawingPanel drawingArea; // For DrawingPanel class
	private int solvedPiece; //
	private boolean gameCompleted;
	private boolean number;
	private int boardSize;
	private int stage;
	private int squareSize;

	/** 
	 * Constructs a new SlidePuzzle object with a simple SlidePuzzle Frame Application
	 */
	public SlidePuzzle() {

		stage = 0; // For main menu
		
		//Initializes the different screens the player will encounter in the game
		mainMenu = new ImageIcon("MainMenu.png").getImage();
		instructions = new ImageIcon("Instructions.png").getImage();
		levelSelect = new ImageIcon("LevelSelect.png").getImage();
		easyBoard = new ImageIcon("Board.png").getImage();
		hardBoard = new ImageIcon("Board2.png").getImage();
		gameOptions = new ImageIcon("GameOptions.png").getImage();

		// Sets up the screen including the title, icon, and the options for the menu bar.
		setTitle("Slide Puzzle");
		setLocation(75, 50);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("earth.gif"));
		addMenus();
		
		// Creates and adds the drawing panel using drawingArea and having a fixed size of 1000 by 600.
		Dimension size = new Dimension(1000, 600);
		drawingArea = new DrawingPanel(size);
		add(drawingArea, BorderLayout.CENTER);
	}

	/** 
	 * Declares the board and its graphics and logic
	 */
	public void declareBoard() {
		board = new GraphicalBoard(boardSize, number); // Initializes the board
		
		//Determines the square size if its 3x3, it will be 200 or 4x4, it will be 150
		if (boardSize == 3)
			squareSize = 200;
		else
			squareSize = 150;
	}

	/** 
	 * Adds the different options for the menu bar on the main frame of this game
	 */
	private void addMenus() {
		// Sets up the Game Menu Items which includes New and Exit.
		newOption = new JMenuItem("New");
		newOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		newOption.addActionListener(this);

		exitOption = new JMenuItem("Exit");
		exitOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.CTRL_MASK));
		exitOption.addActionListener(this);

		// Sets up the Help Menu Items which includes Instructions and About.
		instructionOption = new JMenuItem("Instructions");
		instructionOption.setMnemonic('I');
		instructionOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				InputEvent.CTRL_MASK));
		instructionOption.addActionListener(this);

		aboutOption = new JMenuItem("About");
		aboutOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK));
		aboutOption.setMnemonic('I');
		aboutOption.addActionListener(this);

		// Sets up the Game and Help Menus.
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		// Add each MenuItem to the Game Menu (with a separator).
		gameMenu.add(newOption);
		gameMenu.addSeparator();
		gameMenu.add(exitOption);

		// Add each MenuItem to the Help Menu (with a separator).
		helpMenu.add(instructionOption);
		helpMenu.addSeparator();
		helpMenu.add(aboutOption);

		// Adds the GameMenu and HelpMenu to the JMenuBar.
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(gameMenu);
		mainMenu.add(helpMenu);

		// Displays the menus.
		setJMenuBar(mainMenu);
	}

	/**
	 * Finds the fastest solution to solve the puzzle
	 * @return solution in type Board
	 */
	public Board solve() {
		// Sets up the queue of boards and the usedBoards set
		Queue<Board> queueOfBoards = new PriorityQueue<Board>();
		queueOfBoards.add(board);
		Set<Board> usedBoards = new HashSet<Board>();
		
		//Initializes solution
		Board solution = null;
		
        // Keeps looking until it finds a solution or not if the queue is empty
		while (solution == null && !queueOfBoards.isEmpty()) {
			
			//Gets the next board from the queue
			Board currentBoard = queueOfBoards.remove();
			
			// Checks to see if the board is the solution
			if (currentBoard.isGameWon()) {
				solution = currentBoard;
			} 
			
			// Checks to see if this board has been used before, or else it will 
			// add it to the usedBoards set and generates boards of all possible 
			// moves that are one move away from the empty square
			else if (!usedBoards.contains(currentBoard)) {
				usedBoards.add(currentBoard);
				queueOfBoards.addAll(currentBoard.generateNewBoards());
			}
		}
		//Returns the solution
		return solution;
	}
	
    /** Allows the player to click on the various options in the menu bar
     * @param event Finds which option the player clicked
     */
	public void actionPerformed(ActionEvent event) {
		// If the new option is selected, the board is reset and a new game
		// begins.
		if (event.getSource() == newOption) {
			board.newGame();
			setCursor(Cursor.getDefaultCursor());
			repaint(); // paint the drawing panel when restarting a new game
		}
		// Closes the game screen if the exit option is selected.
		else if (event.getSource() == exitOption) {
			dispose();
			System.exit(0);
		}
		// Displays the instructions if the instruction option is selected.
		else if (event.getSource() == instructionOption) {
			JOptionPane
					.showMessageDialog(
							this,
							"Press the mouse on the piece you wish to move"
									+ "\n(the square of the selected piece is highlighted)."
									+ "\n\nThen press the mouse on the square to which"
									+ "\nyou wish to move the selected piece.",
							"Instructions ", JOptionPane.INFORMATION_MESSAGE);
		}
		// Displays copyright information if the about option is selected.
		else if (event.getSource() == aboutOption) {
			JOptionPane.showMessageDialog(this,
					"\u00a9 2010 By Justin Kim, Joel Abraham, Jason Du",
					"About Slide Puzzle Game", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Creates and draws the main program panel that the player sees
	private class DrawingPanel extends JPanel {
		/**
		 * Constructs a new DrawingPanel object
		 */
		public DrawingPanel(Dimension size) {
			setPreferredSize(size); // set size of this panel
			// Add mouse listeners to this drawing panel
			this.addMouseListener(new MouseHandler());
		}

		/**
		 * Repaint the drawing panel.
		 * @param g The Graphics context.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Checks to see if the player wants to play the 3x3 board or 4x4 board
			if (gameStarted && boardSize == 3) {
				g.drawImage(easyBoard, 0, 0, this);
				board.draw(g);
			} else if (gameStarted && boardSize == 4) {
				g.drawImage(hardBoard, 0, 0, this);
				board.draw(g);
			}

			// Chooses the screen the player clicks such as Main Menu, Level Select,
			// Game Options and Instructions
			else if (stage == 1)
				g.drawImage(instructions, 0, 0, this);
			else if (stage == 2)
				g.drawImage(gameOptions, 0, 0, this);
			else if (stage == 3)
				g.drawImage(levelSelect, 0, 0, this);
			else if (stage == 0)
				g.drawImage(mainMenu, 0, 0, this);
			
			if (stage > 3)
			{
				//Draws the text on the side of the board
				g.setColor(Color.CYAN);
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString("No of Moves: " + board.noOfMoves, 618, 125);
			}
			
			// Shows that the game is completed and the player won
			if (gameCompleted) {
				g.setColor(Color.GREEN);
				g.setFont(new Font("Arial", Font.BOLD, 40));
				g.drawString("Game Completed!", 638, 175);
			} 
			
			// Continues the game
			else if (!gameCompleted && stage > 3) {

				g.setFont(new Font("Arial", Font.BOLD, 40));
				g.drawString("Game in Progress", 638, 175);
			}

		} // paintComponent Method

		// Inner class to handle event when mouse is pressed while mouse is over
		// the DrawingPanel.
		private class MouseHandler extends MouseAdapter {
			/**
			 * Responds to a mousePressed event
			 * @param event Information about the mouse pressed event.
			 */
			public void mousePressed(MouseEvent event) {
				Point pressedOnPoint = event.getPoint();

				// Main Menu
				if (stage == 0) {
					//New Game Button
					if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 176 && pressedOnPoint.y <= 257)) {
						stage+= 2;
						repaint();
						return;
					}

					//Exit Game Button
					else if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 370 && pressedOnPoint.y <= 452)) {
						System.exit(0);
					}

					//Instructions Button
					else if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 276 && pressedOnPoint.y <= 355)) {
						stage ++;
						repaint();
						return;
					} 
					
					else {
						repaint();
						return;
					}
				}
				
				//Instructions
				else if (stage == 1) {
					
					//Main Menu Button
					if ((pressedOnPoint.x >= 14 && pressedOnPoint.x <= 171)
							&& (pressedOnPoint.y >= 11 && pressedOnPoint.y <= 69)) {
						stage = 0;
						repaint();
						return;
					}
					else
					{
						repaint();
						return;
					}
				}

				//Game Options
				else if (stage == 2) {
					
					//Number mode Button
					if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 176 && pressedOnPoint.y <= 257)) {
						stage++;
						number = true;
						repaint();
						return;
					}

					//Picture mode Button
					else if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 276 && pressedOnPoint.y <= 355)) {

						stage++;
						number = false;
						repaint();
						return;
					}

					//Main Menu
					else if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 370 && pressedOnPoint.y <= 452)) {
						stage = 0;
						repaint();
						return;
					} 
					
					else {
						repaint();
						return;
					}
				}

				//Level Select
				else if (stage == 3) {
					
					//3 x 3 Button
					if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 176 && pressedOnPoint.y <= 257)) {
						gameStarted = true;
						boardSize = 3;
						stage++;
						declareBoard();
						repaint();
						return;
					}

					//4 x 4 Button
					else if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 276 && pressedOnPoint.y <= 355)) {
						gameStarted = true;
						boardSize = 4;
						stage++;
						declareBoard();
						repaint();
						return;
					}

					//Main Menu Button
					else if ((pressedOnPoint.x >= 380 && pressedOnPoint.x <= 650)
							&& (pressedOnPoint.y >= 370 && pressedOnPoint.y <= 452)) {
						gameStarted = false;
						stage = 0;
						repaint();
						return;
					}

					else {
						repaint();
						return;
					}
				} 

				//New Game Button
				if ((pressedOnPoint.x >= 675 && pressedOnPoint.x <= 925)
						&& (pressedOnPoint.y >= 300 && pressedOnPoint.y <= 380)) {
					board.newGame();
					gameCompleted = false;
				}

				//Quit Button
				else if ((pressedOnPoint.x >= 675 && pressedOnPoint.x <= 925)
						&& (pressedOnPoint.y >= 395 && pressedOnPoint.y <= 475)) {
					System.exit(0);
				}

				//Solve Button
				else if ((pressedOnPoint.x >= 675 && pressedOnPoint.x <= 925)
						&& (pressedOnPoint.y >= 490 && pressedOnPoint.y <= 570)) {
					repaint();
					Board solveBoard = solve();
					solvedPiece = solveBoard.getMoves();
					board.switchThePiece(solvedPiece);
					if (board.isGameWon())
						gameCompleted = true;
					repaint();
					return;
				} 
				
				//Main Menu Button
				else if ((pressedOnPoint.x >= 675 && pressedOnPoint.x <= 925)
						&& (pressedOnPoint.y >= 205 && pressedOnPoint.y <= 285)) {
					gameStarted = false;
					gameCompleted = false;
					stage = 0;
					repaint();
					return;
				}
				
				//Allows player to click on the pieces on the board
				if (!gameCompleted) {
					// Convert mouse-pressed location to board row and column 200 or 150.
					int pressedOnColumn = event.getX() / squareSize;
					int pressedOnRow = event.getY() / squareSize;
					// Check if the selected square is empty or has a player piece
					if (board.validPress(pressedOnRow, pressedOnColumn)) 
						board.switchThePiece(pressedOnRow, pressedOnColumn);
					// Check if the player wins the game
					if (board.isGameWon())
						gameCompleted = true;
				}

				repaint();
			}
		}

	} // DrawingPanel

	// Program starts here and constructs the game.
	public static void main(String[] args) {
		SlidePuzzle frame = new SlidePuzzle();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	} // Main method
}
