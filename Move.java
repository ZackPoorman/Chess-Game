import javax.swing.ImageIcon;

import java.util.Scanner;

/*****************************************************************
 * A Move is a repositioning of a chess piece from one board
 * location to some other board location.
 * 
 *****************************************************************/
public class Move
{
    public Location from;         // the "from" location in a move
    public Location to;           // the "to" location in a move

    public IChessPiece fromPiece;   // storage to remember the "from" chess piece in the move
    public IChessPiece toPiece;     // storage to remember the "to" chess piece, if one exists, in the move

    public ImageIcon   fromPieceIcon; // storage to remember the "from" chess piece icon in the view
    public ImageIcon   toPieceIcon;   // storage to remember the "to" chess piece icon, if one exists, in the view

    /* ---------------------------------------------------------------------------------------------------
     * The Move constructor initializes the two locations in a move, the "from" location and the "to" location.
     * ---------------------------------------------------------------------------------------------------
     */
    public Move( Location from, Location to )
    {
        this.from = new Location( from );  // Important to make a new Location
        this.to = new Location( to );
    }

    public Move( String str )
    {
        this.from = new Location( str.substring(0,2) );  // Important to make a new Location
        this.to = new Location( str.substring(2,4) );     
    }
    
    /* --------------------------------------------------------------------
     * A valid Move consists of a move from any location, denoted by "from",
     * to some other location, denoted by "to";  i.e. the locations must be
     * two distinctly different locations.
     * --------------------------------------------------------------------
     */
    public boolean toDifferentLocation( )
    {
        
        return from.row != -1 && from.column != -1 && to.row != -1 && to.column != -1 && ! from.equals( to );
    }

    public String toString( )
    {
        return " " + from.toString() +  " " + to.toString( );
    }

}
