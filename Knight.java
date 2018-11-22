 

public class Knight extends ChessPiece {

	public Knight(Player player)
	{
		super(player);
	}

	public String type()
	{
		return "Knight";
	}

	/* -------------------------------------------------------------------------
	 * A valid move for a Knight must satisfy the following requirements:
	 *    1) A move for any chess piece in general;
	 *    2) The move must be restricted exclusively to either 
	 *    		A) two rows and one column, or
	 *    		B) one row and two columns.
	 * -------------------------------------------------------------------------
	 */	
	public boolean isValidMove(Move move, IChessPiece[][] board)
	{
		Location distance = move.from.direction( move.to );
		
		return 	super.isValidMove(move, board) &&
				(Math.abs(distance.row ) == 2 && Math.abs(distance.column) == 1 ||
				 Math.abs(distance.row ) == 1 && Math.abs(distance.column) == 2 );
	}
	
	public String toString()
	{
		return player() + "N " + location.toString();
	}

}
