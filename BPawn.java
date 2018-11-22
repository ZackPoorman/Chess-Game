 

public class BPawn extends Pawn
{
	public BPawn(Player player)
	{
		super(player);
	}

	//	public boolean isValidMove(Move move, IChessPiece[][] board)
	//	{
	//		Square direction = move.from.direction( move.to );

	//		if (super.isValidMove(move, board))
	//		{
	//			if (direction.row == 1 && Math.abs(direction.column) == 1 && board[move.to.row][move.to.column] != null)  // diagonal move
	//			{
	//				return true;
	//			}
	//
	//			if (move.from.row == 1 && direction.column == 0 && direction.row == 2)  //  move forward 2 squares
	//			{
	//				if ((board[2][move.from.column]) == null && (board[3][move.from.column]) == null)
	//				{
	//					return true;
	//				}		
	//			}				
	//
	//			if (direction.column == 0 && direction.row == 1)  // move one square
	//			{		
	//				if ((board[move.to.row][move.to.column]) == null )
	//				{
	//					return true;	
	//				}
	//			}	
	//	{

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
					move.to.row - move.from.row == 1 &&
					Math.abs(move.to.column - move.from.column) == 1 &&
					board[move.to.row][move.to.column] != null)
			{
				return true;
			}

			if (  // to move forward one square
					move.to.row - move.from.row == 1 &&
					move.to.column == move.from.column &&
					board[move.to.row][move.to.column] == null )
			{
				return true;	
			}	
			
			if (  // to move forward 2 squares
					move.from.row == 1 &&
					move.to.row - move.from.row == 2 &&
					move.to.column == move.from.column  &&
					(board[2][move.from.column]) == null &&
					(board[3][move.from.column]) == null)
			{
				return true;
			}		


		}
		return false;
	}

}
