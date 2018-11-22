
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class ChessModel implements IChessModel
{    
    private IChessPiece[][] board;
    private Player player;
    private IChessPiece[][] boardPattern;

    private IChessPiece bKing;
    private IChessPiece wKing;

    private ArrayList<IChessPiece> bPieces;
    private ArrayList<IChessPiece> wPieces;

    private ArrayList<Move> gameMoves;

    /** messageCode is used in ChessPanel for displaying the appropriate game message;
     *    0 = not in check,
     *    1 = moving into check,
     *    2 = currently in check,
     *    3 = invalid regardless
     */
    private int messageCode;
    public boolean trace;

    public ChessModel( String fileName )
    {

        createChessPieces();    

        board = new IChessPiece[8][8];
        player = Player.WHITE;
        createGameMoves( fileName );

        reset();
    }
    
    private void createChessPieces()
    {
        Player b = Player.BLACK;
        Player w = Player.WHITE;

        IChessPiece[][] boardPattern =
            {
                { new Rook( b ),  new Knight( b ), new Bishop( b ), new Queen( b ), new King( b ),  new Bishop( b ), new Knight( b ), new Rook( b )  },
                { new BPawn( b ), new BPawn( b ),  new BPawn( b ),  new BPawn( b ), new BPawn( b ), new BPawn( b ),  new BPawn( b ),  new BPawn( b ) },
                { null,           null,            null,            null,           null,           null,            null,            null           },
                { null,           null,            null,            null,           null,           null,            null,            null           },
                { null,           null,            null,            null,           null,           null,            null,            null           },
                { null,           null,            null,            null,           null,           null,            null,            null           },
                { new WPawn( w ), new WPawn( w ),  new WPawn( w ),  new WPawn( w ), new WPawn( w ), new WPawn( w ),  new WPawn( w ),  new WPawn( w ) },
                { new Rook( w ),  new Knight( w ), new Bishop( w ), new Queen( w ), new King( w ),  new Bishop( w ), new Knight( w ), new Rook( w )  },
            };
        this.boardPattern = boardPattern;

        bKing   = boardPattern[0][4];
        bPieces = new ArrayList<IChessPiece>();
        for (int r = 1; r >= 0; r--){
            for (int c = 0; c < 8; c++){
                boardPattern[r][c].setLocation( r, c );
                bPieces.add( boardPattern[r][c] );
            }
        }   

        wKing   = boardPattern[7][4];
        wPieces = new ArrayList<IChessPiece>();     
        for (int r = 6; r <= 7; r++){
            for (int c = 0; c < 8; c++){
                boardPattern[r][c].setLocation( r, c );
                wPieces.add( boardPattern[r][c] );
            }
        }
    }
    //given moves for AI, AI takes over after these finish
    private void createGameMoves( String fileName ){
        gameMoves = new ArrayList<Move>();
        if (fileName != null && fileName.length() > 0){
            this.read( fileName );
        }else{
            Location fromLocation = new Location( 0,6 );
            Location toLocation   = new Location( 2,5 );       
            Move move1 = new Move( fromLocation, toLocation );
            gameMoves.add( move1 );

            fromLocation = new Location( 1,6 );
            toLocation   = new Location( 2,6 );       
            move1 = new Move( fromLocation, toLocation );
            gameMoves.add( move1 );

            fromLocation = new Location( 0,5 );
            toLocation   = new Location( 1,6 );       
            move1 = new Move( fromLocation, toLocation );
            gameMoves.add( move1 );

            fromLocation = new Location( 0,7 );
            toLocation   = new Location( 0,5 );       
            move1 = new Move( fromLocation, toLocation );
            gameMoves.add( move1 );

            fromLocation = new Location( 1,4 );
            toLocation   = new Location( 3,4 );
            move1 = new Move( fromLocation, toLocation );
            gameMoves.add( move1 );

        }
    }

    public void read( String filename ) 
    {
        try
        { 
            FileInputStream fileByteStream = new FileInputStream(filename);
            Scanner scnr = new Scanner(fileByteStream);
            scnr.useDelimiter(" \r\n");

            while(scnr.hasNext())
            {
                gameMoves.add( new Move( scnr.nextLine() ) );
            }
            fileByteStream.close();
        }
        catch(IOException e)
        {
            System.out.println("Failed to read the data file: " + filename);
        }

    }

    private Move getGameMove( ArrayList<IChessPiece> fromPieces, ArrayList<IChessPiece> toPieces ) 
    {
        if (gameMoves.size( ) > 0)
        {
            Move tentativeMove = gameMoves.get( 0 );
            if (isValidMove( tentativeMove ))
            {
                move( tentativeMove );
                if (moveThreat() == null)
                {
                    undo( tentativeMove );
                    tentativeMove = gameMoves.remove(0);
                   return tentativeMove;
                }
                else
                {
                    undo( tentativeMove );
                } 
            }
        }
        return attackPieces( fromPieces, toPieces );
    }
    //resets each board place to that in the pattern
    private void reset()
    {
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                board[r][c] = boardPattern[r][c];
            }
        }
    }

    /* ---------------------------------------------------------
     *  This method promotes a pawn to a queen.
     * ---------------------------------------------------------
     */
    public boolean promote(Location s)
    {
        if (pieceAt(s).type().equals("Pawn")) 
        {
            if (pieceAt(s).player().equals(Player.WHITE) && s.row == 0)
            {
                setPiece( s, new Queen( Player.WHITE ) );

                wPieces.add( pieceAt( s ) );

                return true;
            }
            else if (pieceAt(s).player().equals(Player.BLACK) && s.row == 7)
            {
                setPiece( s, new Queen( Player.BLACK ) );

                bPieces.add( pieceAt( s ) );

                return true;
            }
        }
        return false;
    }   

    /* ---------------------------------------------------------
     *  The move method first saves the state of the chess piece
     *  involved in the move, 
     *  
     *              move.fromPiece & move.toPiece
     *  
     *  before it makes the move.
     * ---------------------------------------------------------
     */
    public void move(Move moveSpecs)
    {

        moveSpecs.fromPiece = pieceAt( moveSpecs.from );    // remembers the fromPiece
        moveSpecs.toPiece   = pieceAt( moveSpecs.to );      // remembers the toPiece

        if (moveSpecs.toPiece != null)
        {
            moveSpecs.toPiece.setLocation( -1, -1 );          // removes the move.toPiece from the board
        }

        setPiece( moveSpecs.to, moveSpecs.fromPiece );      // moves the "moveFrom" piece to the "move.to" location
        setPiece( moveSpecs.from, null );                   // the board at the "moveFrom" location no longer contains a piece.
    }

    /* ---------------------------------------------------------------
     * The undo method restores the chess board model to the previous
     * state, the state just prior to the move.
     * -------------------------------------------------------------
     */
    public void undo( Move moveSpecs) 
    {
        setPiece( moveSpecs.from, moveSpecs.fromPiece );
        setPiece( moveSpecs.to,   moveSpecs.toPiece );
    }

    /* -----------------------------------------------------------------
     * The isValidMove method identifies the chess piece and checks if.
     * the move is valid for that particular piece. The move by a player 
     * is valid means that its king, after the move, is not in check.
     * 
     * If the move is valid within the context of the particular chess
     * piece, the method tests whether or not the move is valid within
     * the context of the game over all. This is accomplished with the
     * following sequence:
     * 
     *  1) Locally save whether the king is in check before the move.
     *  2) Make the move change to the model.
     *  3) Locally save whether the king is in check after the move.
     *  4) Undo the move change to the model.
     * 
     * From the above, the method determines, as a method side effect,
     * the most appropriate message code and finally returns the result
     * for whether or not the move is valid, i.e. true or false.
     * 
     * messageCode index:
     * 
     *      0: no message, no error
     *      1: "Invalid move; the King is placed in check."
     *      2: "Invalid move; the King remains in check."
     *      3: "" + piece.type() + ":  Invalid move."
     * 
     * -----------------------------------------------------------------
     */
    public boolean isValidMove( Move m )
    {
        boolean inCheck1;
        boolean inCheck2;
        boolean valid = false;

        IChessPiece fromPiece = pieceAt( m.from );

        if (fromPiece == null)
        {
            return false;
        }

        if (fromPiece.isValidMove (m, board ) )
        {
            inCheck1 = inCheck( player );
            move(m);
            inCheck2 = inCheck( player );
            undo( m );

            if (!inCheck2)
            {
                messageCode = 0;
                valid = true;
            }       
            else if (!inCheck1 )
            {
                messageCode = 1;
            }
            else 
            {
                messageCode = 2;
            }           
        }
        else
        {
            messageCode = 3;
        }

        return valid;
    }

    /**
     * The move to m.to happened. Can we castle?
     */
    public Move castleMove( Move m )
    {
        Location s1 = new Location( 7,4);
        Location s2 = new Location( 0,4);

        if (pieceAt(m.to).type().equals("Rook")) 
        {
            Location unitLocation = m.from.unitStepLocation( m.to );
            if (unitLocation.row == 0)
            {
                Location kingLocation = new Location(m.to);
                kingLocation.plus( unitLocation );
                if (kingLocation.equals( s1 ) || kingLocation.equals( s2 ))
                {
                    if ( pieceAt(kingLocation) != null && pieceAt(kingLocation).type().equals("King")) 
                    {
                        Location destination = new Location( m.to );
                        destination.minus( unitLocation );
                        Move castle = new Move( kingLocation, destination );
                        return castle;  
                    }
                }
            }
        }
        return null;
    }

    /*--------------------------------------------------------------------------
     * The king is in check if a valid move exists for at least one chess piece,
     * on the offense, from its current location on the board to the location
     * occupied by the king.
     * -------------------------------------------------------------------------
     */
    private boolean inCheck( IChessPiece king, ArrayList<IChessPiece> pieces )
    {
        Move moveSpecs;
        Location location; 
        Location kingLocation;

        kingLocation = ((ChessPiece)king).location;
        for (IChessPiece cp: pieces){
            location = ((ChessPiece)cp).location;

            if(cp.isValidMove(moveSpecs = new Move(location,kingLocation),board)){
                return true;
            }
        }   
        return false;
    }

    /*--------------------------------------------------------------------------
     * This inCheck method identifies the king in question, and the opponent
     * chess pieces, to determine whether or not it is in check.
     * -------------------------------------------------------------------------
     */
    public boolean inCheck( Player p )
    {
        IChessPiece king;
        ArrayList<IChessPiece> pieces;

        if (p == Player.BLACK)
        {
            king   = bKing;     // the king in question
            pieces = wPieces;   // opponent chess pieces        
        }
        else
        {
            king   = wKing;     // the king in question
            pieces = bPieces;   // opponent chess pieces                        
        }

        return inCheck(king, pieces);           
    }

    /*--------------------------------------------------------------------------
     * The state of the king is check mate if no valid move on the chess board
     * model exists for any chess piece of the same color as the king in
     * question.
     * 
     * For each chess piece in the ArrayList of pieces for that color, and still
     * on the chess board model, this method does an exhaustive search to find a
     * single valid move from its current location, i.e. to any other location on
     * the board.
     * -------------------------------------------------------------------------
     */ 
    public boolean isCheckMate( )
    {
        Move tentativeMove;
        Location fromLocation = new Location( -1, -1 );
        Location toLocation   = new Location( -1, -1 );

        ArrayList<IChessPiece> pieces;
        ChessPiece piece;

        if (inCheck(player))                                    // if current player is in check
        {
            if (player == Player.BLACK)
            {
                pieces = bPieces;           
            }
            else
            {
                pieces = wPieces;                       
            }

            for (IChessPiece icp: pieces)                       // An array list of the player's pieces
            {
                piece = (ChessPiece)icp;
                fromLocation = ((ChessPiece)icp).location;
                for (int r = 0; r < 8; r++)
                {
                    for (int c = 0; c < 8; c++)
                    {
                        toLocation.set(r,c);

                        if(icp.isValidMove(tentativeMove = new Move(fromLocation,toLocation),board)){
                            move(tentativeMove);
                            if(inCheck(player)==false){
                                undo(tentativeMove);
                                return false;
                            }
                            undo(tentativeMove);
                        }
                    }
                    // 2) complete --  between 8 and 16 lines of code
                }
            }
            return true;
        }
        return false;
    }

    public IChessPiece pieceAt( Location s )
    {       
        return board[s.row][s.column];
    }
    
    public IChessPiece pieceAt( int a, int b )
    {       
        return board[a][b];
    }
    
    /* ----------------------------------------------------------
     * This method not only sets the piece on the board model,
     * but it also sets the location of that piece to its location
     * on the board.
     * ----------------------------------------------------------
     */
    public void setPiece( Location s, IChessPiece piece )
    {
        if ( 0 <= s.row && s.row <= 7 && 0 <= s.column && s.column <= 7 )
        {       
            board[s.row][s.column] = piece;
        }

        if (piece != null)
        {
            piece.setLocation(s.row, s.column);
        }
    }

    public Player currentPlayer()
    {
        return player;
    }

    public void setNextPlayer()
    {
        player = player.next();
    }

    public int getMessage()
    {
        return messageCode;
    }

    public Move AI() {

        /*
         * Write a simple AI set of rules in the following order. 
         * a. Check to see if you are in check.
         *      i. If so, get out of check by moving the king or placing a piece to block the check 
         * 
         * b. Attempt to put opponent into check (or checkmate). 
         *      i. Attempt to put opponent into check without losing your piece
         *      ii. Perhaps you have won the game. 
         *
         *c. Determine if any of your pieces are in danger, 
         *      i. Move them if you can. 
         *      ii. Attempt to protect that piece. 
         *
         *d. Move a piece (pawns first) forward toward opponent king 
         *      i. check to see if that piece is in danger of being removed, if so, move a different piece.
         */

        if (isCheckMate())
        {
            System.out.println(  "Checkmate!!");
            return null;
        }
        else
        {
            return attack();
        }
    }

    private Move attack( )
    {
        Move tentativeMove = null;

        if (!inCheck(Player.BLACK))
        {
            tentativeMove = getGameMove( bPieces, wPieces );
        }        
        if (tentativeMove != null)
        {
            return tentativeMove;
        }      

        tentativeMove = attackKing( bPieces, Player.WHITE);
        if (tentativeMove != null)
        {
            return tentativeMove;
        }

        tentativeMove = attackPieces(bPieces, wPieces);
        if (tentativeMove != null)
        {
            return tentativeMove;

        }

        tentativeMove = attackWithCaution();
        if ( tentativeMove != null)
        {
            return tentativeMove;

        }

        return attackBoard();
    }

    private Move moveThreat( )
    {
        Move nextMove;
        nextMove = attackPieces( wPieces, bPieces );
        if (nextMove != null)
        {
            return nextMove;
        }

        nextMove = attackKing( wPieces, Player.BLACK);
        if (nextMove != null)
        {
            return nextMove;
        }        
        return null;
    }

    public Move attackWithCaution() {
        Move tentativeMove;
        Location fromLocation = new Location(-1, -1);
        Location toLocation = new Location(-1, -1);

        ArrayList<IChessPiece> pieces;
        ChessPiece piece;

        if (player == Player.BLACK) {
            pieces = bPieces;
        } else {
            pieces = wPieces;
        }

        for (IChessPiece icp : pieces) // An array list of the player's pieces
        {
            piece = (ChessPiece) icp;
            if (piece.location.row != -1) // Is the chess piece still on the board?
            {
                fromLocation.set(piece.location);
            }
        }
        return null;
    }

    public Move attackBoard() {
        Move tentativeMove;
        Location fromLocation = new Location(-1, -1);
        Location toLocation = new Location(-1, -1);

        ArrayList<IChessPiece> pieces;
        ChessPiece piece;

        if (player == Player.BLACK) {
            pieces = bPieces;
        } else {
            pieces = wPieces;
        }

        for (IChessPiece icp : pieces) // An array list of the player's pieces
        {
            piece = (ChessPiece) icp;
            if (piece.location.row != -1) // Is the chess piece still on the board?
            {
                fromLocation.set(piece.location);
                for (int r = 0; r < 8; r++)
                    {
                        for (int c = 0; c < 8; c++)
                        {
                            toLocation.set( r, c );   // a possible location in the move
                            tentativeMove = new Move( fromLocation, toLocation );   // construct a temporary move 
                            if (isValidMove( tentativeMove ))
                            {
                                //priority of Pieces to move
                                if(piece.type().equals("Pawn")){
                                   return tentativeMove;  
                                }
                                if(piece.type().equals("Knight")){
                                   return tentativeMove;  
                                }
                                if(!piece.type().equals("King")){
                                   return tentativeMove;  
                                }
                                if(piece.type().equals("King")){
                                   return tentativeMove;  
                                }
                            }
                        }
                    }   
            }
        }
        return null;
    }

    private Move attackPieces( ArrayList<IChessPiece> fromPieces, ArrayList<IChessPiece> toPieces ) 
    {
        Move   tentativeMove;
        Move   counterAttack;
        Location fromLocation; 
        Location toLocation; 

        for (IChessPiece bcp: fromPieces)
        {
            if (((ChessPiece)bcp).location.row != -1)
            {
                fromLocation = ((ChessPiece)bcp).location;     

                for (IChessPiece wcp: toPieces)
                {
                    if (((ChessPiece)wcp).location.row != -1)
                    {
                        toLocation = ((ChessPiece)wcp).location;             
                        tentativeMove = new Move( fromLocation, toLocation ); 
                        if(isValidMove(tentativeMove)){
                            move(tentativeMove);
                            for (IChessPiece wap: toPieces){
                                if(wcp.isValidMove(counterAttack = new Move(((ChessPiece)wap).location,toLocation),board)){
                                    undo(tentativeMove);
                                    return tentativeMove;
                                }
                            }   
                            undo(tentativeMove);
                            if(!bcp.type().equals("Queen")&& wcp.type().equals("Queen")){
                                return tentativeMove;
                            }
                            if(bcp.type().equals("Pawn")){
                                return tentativeMove;
                            }
                            if(!bcp.type().equals("Pawn")&& !wcp.type().equals("pawn")){
                                return tentativeMove;
                            }
                            if(!bcp.type().equals("Pawn")&& wcp.type().equals("pawn")){
                                return tentativeMove;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }   

    private Move attackKing( ArrayList<IChessPiece> fromPieces,  Player opponent )
    {
        {
            Move   moveSpecs;
            Location fromLocation;
            Location toLocation = new Location( -1, -1);

            for (IChessPiece bcp: fromPieces)
            {
                if (((ChessPiece)bcp).location.row != -1)
                {                           
                    fromLocation = ((ChessPiece)bcp).location;        

                    for (int r = 0; r < 8; r++)
                    {
                        for (int c = 0; c < 8; c++)
                        {
                            toLocation.set( r, c );   // a possible location in the move
                            moveSpecs = new Move( fromLocation, toLocation );   // construct a temporary move 
                            if (isValidMove( moveSpecs ))
                            {
                                move(moveSpecs);
                                if(inCheck(opponent)){
                                    undo(moveSpecs);
                                    return moveSpecs;
                                }
                                undo(moveSpecs);
                            }
                        }
                    }   
                }
            }
            return null;
        }
    }

}