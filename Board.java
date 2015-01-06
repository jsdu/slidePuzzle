import java.util.ArrayList;

/**
 * The "Board" class. The logic part of the board
 * which will determine its position and its previous
 * and future positions and generate all the possible 
 * boards that could be a potential move
 * 
 * @author Justin Kim, Joel Abraham, Jason Du
 * @version January 15, 2014
 */

public class Board implements Comparable<Board> {

	//All the program variables
	
	// The constants 
	final static int EMPTY = 0; // Represents an empty space on the board

	//Initialized as protected so that GraphicalBoard can use them too
	protected int emptyRow; // The board row and column of the empty piece
	protected int emptyColumn;
	protected boolean mixing; // For mixing up the board
	protected int noOfMoves;
	protected int hashCode;
	protected int selectedRow; // The board row and column of the selected piece
	protected int selectedColumn;
	protected int selectedPiece; // Keeps track of the currently selected piece (each piece has a unique number)
	protected int[][] board; //The main board
	protected ArrayList<Integer> moves; //Keeps track of the moves of the solution

	private boolean outOfBound;

	/**
	 * Constructs a Board object with pieces mixed up
	 * @param boardSize to see if its 3x3 or 4x4
	 */
	public Board(int boardSize) {
		board = new int[boardSize][boardSize];
		setPieces(board);
		hashCode = setHashCode();
		noOfMoves = 0;
		moves = new ArrayList<Integer>();
	}

	public Board(Board originalBoard, int selectedRow, int selectedColumn) {
		outOfBound = false;
		if (selectedRow >= originalBoard.board.length || selectedRow < 0
				|| selectedColumn >= originalBoard.board[0].length
				|| selectedColumn < 0) {
			outOfBound = true;
			return;
		}
		board = new int[originalBoard.board.length][originalBoard.board[0].length];
		// up
		for (int row = 0; row < this.board.length; row++)
			for (int col = 0; col < this.board[row].length; col++) {
				this.board[row][col] = originalBoard.board[row][col];
			}
		moves = new ArrayList<Integer>(originalBoard.moves);
		moves.add(this.board[selectedRow][selectedColumn]);
		this.board[originalBoard.emptyRow][originalBoard.emptyColumn] = this.board[selectedRow][selectedColumn];
		this.emptyRow = selectedRow;
		this.emptyColumn = selectedColumn;
		this.board[selectedRow][selectedColumn] = EMPTY;
		hashCode = setHashCode();
		noOfMoves = originalBoard.noOfMoves + 1;

	}

	/**
	 * Starts or restarts a game
	 */
	public void newGame() {
		setPieces(board);
		noOfMoves = 0;
	}

	/**
	 * Randomly sets all the pieces on the board at the beginning of a game.
	 * @param board The game board.
	 */
	public void setPieces(int[][] board) {
		int pieceCounter = 1;
		
		//Sets the pieces
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				board[row][col] = pieceCounter;
				pieceCounter++;
			}
		}
		this.emptyRow = board.length - 1;
		this.emptyColumn = board.length - 1;
		//Randomly generates the pieces so that they will be mixed up
		int randomSelect;
		while (noOfMoves < 50) {
			randomSelect = (int) (Math.random() * 4);
			// up
			if (randomSelect == 0 && this.emptyRow != 0) {
				selectedRow = this.emptyRow - 1;
				selectedColumn = this.emptyColumn;
			}
			// down
			else if (randomSelect == 1 && this.emptyRow != board.length - 1) {
				selectedRow = this.emptyRow + 1;
				selectedColumn = this.emptyColumn;
			}
			// left
			else if (randomSelect == 2 && emptyColumn != 0) {
				selectedColumn = this.emptyColumn - 1;
				selectedRow = this.emptyRow;

			}
			// right
			else if (randomSelect == 3 && emptyColumn != board.length - 1) {
				selectedColumn = this.emptyColumn + 1;
				selectedRow = this.emptyRow;
			}

			makeMove(); //Checks to see if the move on the board is possible

		}

	}

	/**
	 * Makes a move on the board (if possible).
	 * @param emptyRowThe row the player wants to move the piece to.
	 * @param emptyColumn The column the player wants to move the piece to.
	 */
	public void makeMove() {
		if (board[selectedRow][selectedColumn] > EMPTY) {
			// Move the piece to the target location
			
			//Checks to see if the selected piece is adjacent to the empty piece
			if ((this.emptyRow == selectedRow + 1 && this.emptyColumn == selectedColumn)
					|| (this.emptyRow == selectedRow - 1 && this.emptyColumn == selectedColumn)
					|| (this.emptyColumn == selectedColumn + 1 && this.emptyRow == selectedRow)
					|| (this.emptyColumn == selectedColumn - 1 && this.emptyRow == selectedRow)) {

				//Switches the pieces
				this.board[this.emptyRow][this.emptyColumn] = this.board[selectedRow][selectedColumn];
				this.emptyRow = selectedRow;
				this.emptyColumn = selectedColumn;
				this.board[selectedRow][selectedColumn] = EMPTY;

				noOfMoves++;

			}
			selectedPiece = -1;
		}
	}

	/**
	 * Checks to see if the player wins the game
	 * @return true if the player wins
	 * @return false if the player doesn't win yet
	 */
	public boolean isGameWon() {
		int pieceCounter = 1;
		//Checks the current board with a board that has the pieces in the correct order (the solution)
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] != EMPTY && board[row][col] != pieceCounter)
					return false;
				pieceCounter++;
			}
		}
		return true;
	}

	/**
	 * Checks to see if the player clicking is valid or not
	 * @param rowClicked the row which the player clicked
	 * @param columnClicked the column which the player clicked
	 * @return true if it is a valid click
	 * @return false if it is not a valid click
	 */
	public boolean validPress(int rowClicked, int columnClicked) {
		//Checks to see if the player's click is out of bounds
		if (rowClicked < 0 || rowClicked >= board.length || columnClicked < 0
				|| columnClicked >= board[0].length)
			return false;
		if (board[rowClicked][columnClicked] >= 0)
			return true;
		return false;
	}

	/**
	 * Selects the piece the player wants to switch, and checks to see if it is a valid move
	 * @param rowClicked the row the player clicked
	 * @param columnClicked the column the player clicked
	 */
	public void switchThePiece(int rowClicked, int columnClicked) {
		if (!isGameWon()) {
			selectedRow = rowClicked;
			selectedColumn = columnClicked;
			selectedPiece = board[rowClicked][columnClicked];
			makeMove();
		}

	}

	/**
	 * Selects the piece the player wants to switch using numbers, and checks to see if it is a valid move
	 * @param pieceNumber the number the player clicked
	 */
	public void switchThePiece(int pieceNumber) {
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[0].length; column++) {
				if (board[row][column] == pieceNumber) {
					selectedRow = row;
					selectedColumn = column;
					selectedPiece = board[row][column];
					makeMove();
					return;
				}
			}

		}

	}

	/**
	 * Generates all the possible boards from all moves possible
	 * @return an ArrayList of all the possible boards
	 */
	public ArrayList<Board> generateNewBoards() {
		ArrayList<Board> possibleBoards = new ArrayList<Board>();
        possibleBoards.add(new Board(this, this.emptyRow +1 , this.emptyColumn));
        possibleBoards.add(new Board(this, this.emptyRow -1 , this.emptyColumn));
        possibleBoards.add(new Board(this, this.emptyRow  , this.emptyColumn + 1));
        possibleBoards.add(new Board(this, this.emptyRow  , this.emptyColumn - 1));
        for (int size = 0; size < possibleBoards.size (); size ++){
                if (possibleBoards.get(size).outOfBound )
                {
                        possibleBoards.remove (size);
                        size --;
                }
        }

		return possibleBoards;
	}

	/**
	 * Displays a String representation of the board information in proper format
	 */
	public String toString() {
		String display = " ";
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[0].length; column++) {
				display = display + " " + board[row][column];
			}
			display = display + "\n";
		}
		display += "(" + this.emptyRow + ", " + this.emptyColumn + ")\n";
		return display;
	}

	/**
	 * Returns an integer that tells if the current board is close to the solution or not
	 * @return positive integer if its is closer to the solution
	 * @return negative integer if its further away from the solution
	 * @return zero if it is same as the solution
	 */
	public int compareTo(Board other) {
		return this.findManhattanDistance() + this.noOfMoves
				- (other.findManhattanDistance() + other.noOfMoves);
	}

	/**
	 * Creates a hash code for the board
	 * @return an integer of all the elements of the board
	 */
	public int setHashCode() {
		hashCode = 0;

		for (int row = 0; row < this.board.length; row++)
			for (int col = 0; col < this.board[row].length; col++) {
				int number = this.board[row][col];
				hashCode = hashCode * 15 + number;
			}
		return hashCode;
	}

	/**
	 * Checks to see if the two objects are equal
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		for (int row = 0; row < this.board.length; row++)
			for (int col = 0; col < this.board[row].length; col++)
				if (this.board[row][col] != other.board[row][col])
					return false;

		return true;
	}

	/**
	 * Generates the Manhattan distance of the board
	 * @return the Manhattan distance of the board
	 */
	public int findManhattanDistance() {
		int distance = 0;
		int length = this.board.length;
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				int number = this.board[row][col] - 1;
				distance += Math.abs(row - number / length)
						+ Math.abs(col - number % length);
			}
		}
		return distance;
	}

	/**
	 * Generates the next possible move of the solution
	 * @return the next possible move of the solution
	 */
	public int getMoves() {
		if (moves.size() == 0)
			return 0;
		return moves.remove(0);
	}

}
