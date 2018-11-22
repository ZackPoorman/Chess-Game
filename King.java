 

public class King extends ChessPiece {

	public King(Player player) {
		super(player );
	}

	public String type() {
		return "King";
	}
	
	/* -----------------------------------------------------------------------------
	 * A valid move for a King must satisfy the following requirements:
	 *    1) A move for any chess piece in general;
	 *    2) The move must be restricted exclusively to one location in any direction
	 * -----------------------------------------------------------------------------
	 */	
	public boolean isValidMove(Move move, IChessPiece[][] board)
	{
		return super.isValidMove(move, board) && 
			
			( Math.abs(move.to.row - move.from.row) == 1       && move.to.column == move.from.column ||
			  Math.abs(move.to.column - move.from.column) == 1 && move.to.row == move.from.row ||
			  Math.abs(move.to.row - move.from.row) == 1       && Math.abs(move.to.column - move.from.column) == 1 );
	}	
	
	public String toString()
	{
		return player( ).toString( ) + "K " + location.toString( );
	}
}