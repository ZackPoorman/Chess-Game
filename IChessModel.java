 

public interface IChessModel {

	/**
	 * Returns whether the game is over.
	 *
	 * @return {@code true} if complete, {@code false} otherwise.
	 */
	boolean isCheckMate( );

	/**
	 * Returns whether the piece at location {@code [move.fromRow, move.fromColumn]} is allowed to move to location
	 * {@code [move.fromRow, move.fromColumn]}.
	 *
	 * @param move a {@link ChessProject3.chess.Move} object describing the move to be made.
	 * @return {@code true} if the proposed move is valid, {@code false} otherwise.
	 * @throws IndexOutOfBoundsException if either {@code [move.fromRow, move.fromColumn]} or {@code [move.toRow,
	 *                                   move.toColumn]} don't represent valid locations on the board.
	 */
	boolean isValidMove(Move move);

	/**
	 * Moves the piece from location {@code [move.fromRow, move.fromColumn]} to location {@code [move.fromRow,
	 * move.fromColumn]}.
	 *
	 * @param move a {@link ChessProject3.chess.Move} object describing the move to be made.
	 * @throws IndexOutOfBoundsException if either {@code [move.fromRow, move.fromColumn]} or {@code [move.toRow,
	 *                                   move.toColumn]} don't represent valid locations on the board.
	 */
	void move(Move move);

	/**
	 * Report whether the current player p is in check.
	 * @param  p {@link ChessProject3.chess.Move} the Player being checked
	 * @return {@code true} if the current player is in check, {@code false} otherwise.
	 */
	boolean inCheck(Player p);

	/**
	 * Return the current player.
	 *
	 * @return the current player
	 */
	Player currentPlayer();

}
