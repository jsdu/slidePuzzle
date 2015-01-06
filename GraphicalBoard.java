import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * The "GraphicalBoard" class. The graphical part of the board which will draw
 * out the board and the other images like the numbers and pictures for the game
 * 
 * @author Justin Kim, Joel Abraham, Jason Du
 * @version January 15, 2014
 */

public class GraphicalBoard extends Board {

	// All the program variables
	Image[] images;
	Image empty3; // The black empty piece for 3x3
	Image empty4; // The black empty piece for 4x4
	private int squareSize; // The length (in pixels) of the side of each
	private int random;
	String[] picNames = { "Up", "HTYD", "disney" };
	String[] picNames2 = { "UpV2", "HTYDV2", "disneyV2" };

	/**
	 * Constructs a GraphicalBoard object that loads the board and the pictures
	 * 
	 * @param boardSize
	 *            the size of the board depending on 3x3 or 4x4
	 * @param number
	 *            to see if the player wants number or picture mode
	 */
	public GraphicalBoard(int boardSize, boolean number) {
		super(boardSize);
		// Constructs the size of the board depending on what mode the player
		// wants
		if (boardSize == 3)
			squareSize = 200;
		else
			squareSize = 150;

		// Loads either pictures or numbers depending on what mode the player
		// wants
		if (number)
			loadNumbers();
		else
			loadPictures();

		// Initializes the empty black squares
		empty3 = new ImageIcon("3x3.gif").getImage();
		empty4 = new ImageIcon("4x4.gif").getImage();
	}

	/**
	 * Load the numbers for number mode
	 */
	private void loadNumbers() {
		images = new Image[board.length * board.length];
		for (int imageNumber = 1; imageNumber <= images.length; imageNumber++) {
			images[imageNumber - 1] = new ImageIcon("pic" + imageNumber
					+ ".png").getImage();
		}

	}

	/**
	 * Loads the pictures for picture mode
	 */
	private void loadPictures() {
		images = new Image[board.length * board.length];
		random = (int) (Math.random() * 3);
		if (board.length == 3) {
			for (int imageNumber = 0; imageNumber < images.length; imageNumber++) {
				images[imageNumber] = new ImageIcon(picNames[random]
						+ imageNumber + ".jpg").getImage();
			}
		} else {
			for (int imageNumber = 0; imageNumber < images.length; imageNumber++) {
				images[imageNumber] = new ImageIcon(picNames2[random]
						+ imageNumber + ".jpg").getImage();
			}
		}

	}

	/**
	 * Draws the graphical version of the board including the pieces and the texts
	 * @param g  The Graphics context
	 */
	public void draw(Graphics g) {
		// Draw the board with current pieces.
		for (int row = 0; row < board.length; row++)
			for (int column = 0; column < board[row].length; column++) {
				// Find the x and y positions for each row and column.
				int xPos = column * squareSize;
				int yPos = row * squareSize;
				if (board[row][column] > 0) {
					g.drawImage(images[board[row][column] - 1], xPos, yPos,
							null);
					g.setColor(Color.BLACK);
					g.drawRect(xPos, yPos, squareSize, squareSize);

				}
				// Draws the empty piece
				else {
					if (board.length == 4)
						g.drawImage(empty4, xPos, yPos, null);
					else
						g.drawImage(empty3, xPos, yPos, null);
				}

			}

	}

}
