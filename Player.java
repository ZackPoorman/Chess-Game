 


public enum Player
{
	BLACK, WHITE;

	/**
	 * Return the {@code Player} whose turn is next.
	 *
	 * @return the {@code Player} whose turn is next
	 */
	public Player next()
	{
		return this == BLACK ? WHITE : BLACK;
	}
	
	public String toString( )
	{
	    return this == BLACK ? "B" : "W";
	}
}