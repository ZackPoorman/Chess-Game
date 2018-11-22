 

public class Pawn extends ChessPiece {

	public Pawn(Player player)
	{
		super(player);
	}

	public String type()
	{
		return "Pawn";
	}
	
	public String toString( )
	{
		return player( ).toString() + "P " + location.toString( );
	}
}