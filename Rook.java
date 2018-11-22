 


public class Rook extends ChessPiece {

	public Rook(Player player)
	{
		super(player);
	}

	public Rook(Player player, Location Location )
	{
		super( player, Location );
	}

	public String type() {

		return "Rook";	
	}

	/* -------------------------------------------------------------------------
	 * A valid move for a Rook must satisfy the following requirements:
	 *    1) A move for any chess piece in general;
	 *    2) The move must be restricted exclusively to either a row or a column;
	 *    3) The Locations strictly between the "from" Location and the "to" Location
	 *       must be empty (unoccupied).
	 * -------------------------------------------------------------------------
	 */	
	public boolean isValidMove(Move move, IChessPiece[][] board)
	{		
		return 
				super.isValidMove(move, board) && 
				(move.to.row == move.from.row || move.to.column == move.from.column) &&
				overEmptyLocations( move, board );	
	}
	
	public String toString()
	{
		return player( ) + "R " + location.toString( );
	}
}
