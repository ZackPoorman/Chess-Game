 

public class WPawn extends Pawn {

	public WPawn(Player player)
	{
		super(player);
	}

	//	public boolean isValidMove(Move move, IChessPiece[][] board)
	//	{
	//		Square difference = move.from.direction( move.to );

	//		if (super.isValidMove(move, board))
	//		{
	//			if (difference.row == -1 && Math.abs(difference.column) == 1 && board[move.to.row][move.to.column] != null)  // move one square on the diagonal
	//			{
	//				return true;
	//			}
	//
	//			if (difference.column == 0 && difference.row == -1)  // move one square forward
	//			{		
	//				if ((board[move.to.row][move.to.column]) == null )
	//				{
	//					return true;	
	//				}
	//
	//			if (move.from.row == 6 && difference.column == 0 && difference.row == -2)  //  move 2 squares forward
	//			{
	//				if ((board[5][move.from.column]) == null && (board[4][move.from.column]) == null)
	//				{
	//					return true;
	//				}		
	//			}				
	//
	//			}	
	//	}

	/* -------------------------------------------------------------------------
	 * A valid move for a White Pawn must satisfy the following requirements:
	 *    1) A move for any chess piece in general;
	 *    2) The move may be one or two squares forward, two for its first move.
	 *    3) The move may be one square forward along the diagonal to take an
	 *       opposing piece.
	 * -------------------------------------------------------------------------
	 */	
	public boolean isValidMove(Move move, IChessPiece[][] board)
	{
		if (super.isValidMove(move, board))
		{
			if (  // to move forward on the diagonal one square
					move.to.row - move.from.row == -1 &&
					Math.abs(move.to.column - move.from.column) == 1 &&
					board[move.to.row][move.to.column] != null)
			{
				return true;
			}

			if ( // to move forward one square
					move.to.row - move.from.row == -1 &&
					move.to.column - move.from.column == 0 &&
					board[move.to.row][move.to.column] == null )
			{
				return true;	
			}
			
			if ( // to move forward 2 squares
					move.from.row == 6 &&
					move.to.row - move.from.row == -2 &&
					move.to.column - move.from.column == 0 &&
					board[5][move.from.column] == null &&
					board[4][move.from.column] == null)
			{
				return true;
			}		

		}
		return false;
	}

}
