
public abstract class ChessPiece implements IChessPiece {
    private Player owner;
    public Location  location;		// added 12-29-2017 LJK

    protected ChessPiece(Player player)
    {
        this.owner  = player;
        this.location = new Location( -1, -1 );
    }

    protected ChessPiece(Player player, Location location )
    {
        this.owner  = player;
        this.location = location;
    }

    /**
     * setLocation identifies the location of the chess piece on the board.
     */
    public void setLocation( int row, int column )		// added 12-29-2017, LJK
    {
        location.set(row, column);
    }	

    public abstract String type();

    public Player player()
    {
        return owner;
    }

    /* -------------------------------------------------------------------------
     * A valid move for any chess piece, in general, must satisfy the following
     * requirements:
     *    1) Every move, in general, involves two distinctly different locations
     *       on the board;
     *    2) The "from" location must contain a chess piece;
     *    3) The "to" location is either exclusively empty or it contains a chess
     *       piece of the opposite color, which is subsequently taken, i.e.
     *       removed from play, and no longer on the board.
     * -------------------------------------------------------------------------
     */	
    public boolean isValidMove(Move move, IChessPiece[][] board)
    {	
        return	(
            move.toDifferentLocation( ) &&
            board[move.from.row][move.from.column] != null &&
            ( board[move.to.row][move.to.column] == null || moveToOpponentPiece( move, board ) )
        );
    }

    private boolean moveToOpponentPiece( Move move, IChessPiece[][] board )
    {
        return board[move.from.row][move.from.column].player() != board[move.to.row][move.to.column].player();
    }

    /* -------------------------------------------------------------------------
     * The overEmptyLocations returns :
     *    1) true - if all locations exclusively between the "from" location and the
     *              "to" location are empty.
     *    2) false - if at least one location between the "from" location and the
     *               "to" location is occupied.
     * -------------------------------------------------------------------------
     */	
    protected boolean overEmptyLocations( Move move, IChessPiece[][] board)
    {
        Location unit = move.from.unitStepLocation(move.to);
        Location temp = new Location( move.from );

        temp.plus( unit );
        while ( !temp.equals( move.to ))
        {
            if (board[temp.row][temp.column] != null)
            {
                return false;
            }
            temp.plus( unit );
        }
        return true;	
    }

}