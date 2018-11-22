 

public class Bishop extends ChessPiece {

	public Bishop(Player player ) {
		super(player);
	}
	
	public Bishop(Player player, Location s )
	{
		super(player, s);
	}

	public String type() {
		return "Bishop";
	}

	/* -------------------------------------------------------------------------
	 * A valid move for a Bishop on the board must satisfy the following
	 * requirements:
	 *    1) A move for any chess piece in general;
	 *    2) The move must be restricted exclusively to a diagonal;
	 *    3) The squares strictly between the "from" location and the "to" location
	 *       must be empty (unoccupied).
	 *       
	 *  method modified on 12-29-2017, LJK
	 * -------------------------------------------------------------------------
	 */	
	public boolean isValidMove( Move move, IChessPiece[][] board )
	{
		Location direction = move.from.direction(move.to);

		return 	super.isValidMove(move, board) &&
				Math.abs(direction.row) == Math.abs(direction.column) &&
				this.overEmptyLocations( move, board );	
	}
	
	public String toString()
	{
		return player() + "B " + location.toString();
	}
}

