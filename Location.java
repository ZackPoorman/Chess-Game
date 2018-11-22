import java.util.Scanner;

/**
 * -----------------------------------------------------
 * A Location encapsulates a location on the board,
 * consisting of the row and the column.
 * -----------------------------------------------------
 */
public class Location
{
    public int row;
    public int column;

    private static char[] letter = { 'a','b','c','d','e','f','g','h'};

    public Location( int row, int column )
    {
        this.row    = row;
        this.column = column;
    }

    public Location( Location square )
    {
        this.row    = square.row;
        this.column = square.column;
    }

    public Location( String str )
    {
        this.row = Integer.parseInt( str.substring(0,1) );      
        
        char ch = str.substring(1,2).charAt( 0 );
        this.column = (int)ch - (int)'a';
     }

    public void set( int row, int column )
    {
        this.row    = row;
        this.column = column;
    }

    public void set( Location s )
    {
        this.row    = s.row;
        this.column = s.column;
    }

    public boolean equals( Location s )
    {
        return this.row == s.row && this.column == s.column;
    }

    /**
     * ---------------------------------------------------------------------
     * The unitStepLocation method returns a Location that represents a unit
     * "step in the right direction" for a piece to move from this Location
     * to the target Location.
     * 
     * @param target
     * @return a unit direction from this Location to the target Location
     * ---------------------------------------------------------------------
     */
    public Location unitStepLocation( Location target )
    {
        Location step = this.direction( target );

        if (step.row != 0)
        {
            step.row = step.row / Math.abs( step.row );
        }
        if (step.column != 0)
        {
            step.column = step.column / Math.abs( step.column );
        }
        return step;
    }

    /**
     * --------------------------------------------------------
     * The direction method returns a Location that consists of
     * the row and column differences between two locations, i.e.
     * the direction from this location to the square location.
     * 
     * @param s
     * @return a direction from this Location to the square
     * --------------------------------------------------------
     */	
    public Location direction( Location square )
    {
        Location vector = new Location( 0, 0 );

        vector.row    = square.row    - this.row;
        vector.column = square.column - this.column;

        return vector;
    }

    /**
     * --------------------------------------------------------
     * The plus method modifies this Location so that it may
     * identify some other location on the board, such as an
     * adjacent Location if increment would denote a 
     * unitLocationStep.
     * 
     * @param increment
     * --------------------------------------------------------
     */	
    public void plus( Location increment )
    {	
        this.row    = this.row    + increment.row;
        this.column = this.column + increment.column;
    }

    public void minus( Location decrement )
    {	
        this.row    = this.row    - decrement.row;
        this.column = this.column - decrement.column;
    }

    public String toString( )
    {
        return  "" + row + letter[column];
    }

}