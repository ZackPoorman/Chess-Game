
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class ChessPanel extends JPanel {

    private JButton[][] board;    
    private ChessModel model;

    ImageIcon[ ] bIcons;
    ImageIcon[ ] wIcons;

    private JButton reset;
    private JButton undo;
    private JButton automatic;
    private JButton computer;
    private JButton btnTrace;

    private boolean selfPlay;
    private int messageCode;
    private int turn;
    private boolean trace;

    private ArrayList<Move> moveHistory;
    public ChessPanel() {
        GeneralListener  generalListener  = new GeneralListener();
        GameMoveListener gameMoveListener = new GameMoveListener();

        JPanel boardpanel = new JPanel();
        boardpanel.setPreferredSize(new Dimension(600, 600));
        boardpanel.setLayout(new GridLayout(9, 9, 1, 1));
        createBoard( boardpanel, gameMoveListener );
        add(boardpanel, BorderLayout.WEST);

        JPanel buttonpanel = new JPanel();
        add(buttonpanel);

        reset = new JButton("Reset Game");
        reset.addActionListener(generalListener);
        buttonpanel.add(reset);

        undo = new JButton("Undo");
        undo.addActionListener(generalListener);  
        buttonpanel.add(undo);

        automatic = new JButton("Auto play");
        automatic.addActionListener(generalListener);
        buttonpanel.add(automatic);

        btnTrace = new JButton("No Trace");
        btnTrace.addActionListener(generalListener);
        buttonpanel.add(btnTrace);

        selfPlay = true;
        trace = true;
        turn = 0;

        createChessIcons(); 
        reset( );       
    }

    private void createBoard( JPanel panel, GameMoveListener listener )
    {
        Color[ ] colors = {Color.WHITE, Color.CYAN };
        JButton b;

        //        JLabel label;
        this.board = new JButton[9][9];
        for (int r = 0; r < 8; r++ )
        {
            for (int c = 0; c <= 8; c++ )
            {
                if ( c < 8)
                {
                    b = new JButton( );
                    b.setBackground( colors[ (r + c) % 2] );
                    b.addActionListener( listener );
                    panel.add( b );             
                    this.board[r][c] = b;
                }
                else
                {
                    panel.add( new JLabel("  " + r));
                }
            }   
        }   
        JLabel label;

        int aa = (int)'a';
        for (int c = 0; c < 8; c++)
        {
            label = new JLabel( "         " + (char)(aa + c ) );
            label.setAlignmentX( Component.RIGHT_ALIGNMENT );
              panel.add(label);
            
        }

    }

    public JButton buttonAt( Location s )
    {
        return board[s.row][s.column];
    }

    private void createChessIcons()
    {
        // Sets the ImageIcon for the Black Player Pieces
        ImageIcon bRook = new ImageIcon("bRook.png");
        ImageIcon bBishop = new ImageIcon("bBishop.png");
        ImageIcon bQueen = new ImageIcon("bQueen.png");
        ImageIcon bKing = new ImageIcon("bKing.png");
        ImageIcon bPawn = new ImageIcon("bPawn.png");
        ImageIcon bKnight = new ImageIcon("bKnight.png");

        // Sets the Image for white player pieces 
        ImageIcon wRook = new ImageIcon("wRook.png");
        ImageIcon wBishop = new ImageIcon("wBishop.png");
        ImageIcon wQueen = new ImageIcon("wQueen.png");
        ImageIcon wKing = new ImageIcon("wKing.png");
        ImageIcon wPawn = new ImageIcon("wPawn.png");
        ImageIcon wKnight = new ImageIcon("wKnight.png");

        ImageIcon[ ] bIcons = { bRook, bKnight, bBishop, bQueen, bKing, bBishop, bKnight, bRook, bPawn };
        this.bIcons = bIcons;
        ImageIcon[ ] wIcons = { wRook, wKnight, wBishop, wQueen, wKing, wBishop, wKnight, wRook, wPawn };
        this.wIcons = wIcons;
    }

    private void reset()
    {
        model = new ChessModel( "GameMoves.txt" );
        moveHistory = new ArrayList<Move>();

        turn = 0;       
        model.trace = true;

        for (int c = 0; c < 8; c++ )
        {
            this.board[0][c].setIcon( bIcons[ c ] );
        }   

        for (int c = 0; c < 8; c++ )
        {
            this.board[1][c].setIcon( bIcons[ 8 ] );
        }   

        for (int r = 2; r < 6; r++ )
        {
            for (int c = 0; c < 8; c++ )
            {
                this.board[r][c].setIcon( null );
            }   
        }   

        for (int c = 0; c < 8; c++ )
        {
            this.board[6][c].setIcon( wIcons[ 8 ] );
        }   
        for (int c = 0; c < 8; c++ )
        {
            this.board[7][c].setIcon( wIcons[ c ] );
        }       
    }

    private void promote(Location s) {

        if (model.pieceAt(s).player().equals(Player.WHITE) )
        {
            buttonAt(s).setIcon(wIcons[ 3 ]);
            if (trace) { System.out.print( ", white pawn promoted" ); }
        }
        else if (model.pieceAt(s).player().equals(Player.BLACK))
        {
            buttonAt(s).setIcon(bIcons[ 3 ]);
            if (trace) { System.out.print( ", black pawn promoted" ); }
        }
    }

    private void move( Move m )
    {
        if (trace)
        {
            System.out.print( " " + turn + " " + model.pieceAt(m.from).toString( ) + m.to.toString( ));
            if (model.pieceAt(m.to) != null)
            {
                System.out.print( " take " + model.pieceAt(m.to).toString( ) );                
            }
        }

        m.fromPieceIcon = (ImageIcon)buttonAt( m.from ).getIcon( );
        m.toPieceIcon   = (ImageIcon)buttonAt( m.to   ).getIcon( );

        model.move(m);

        if (model.promote( m.to ) )
        {
            this.promote( m.to );
        }
        else
        {
            buttonAt( m.to ).setIcon( m.fromPieceIcon );
        }

        buttonAt( m.from ).setIcon( null );

        moveHistory.add( m );
    }

    private void undoMove( )
    {
        if (moveHistory.size() > 0)
        {

            int n = moveHistory.size()-1;
            Move m = moveHistory.remove( n );
            model.undo(m);

            //      promotion( m.to.row, m.to.column );

            buttonAt( m.from ).setIcon( m.fromPieceIcon );
            buttonAt( m.to   ).setIcon( m.toPieceIcon   );

            turn--;
            model.setNextPlayer();
        }
    }

    private void displayMessage(IChessPiece piece) {

        if (messageCode == 0) {
            //            JOptionPane.showMessageDialog(null, "Turn done. It is your turn.");
        }
        else if (messageCode == 1) {
            JOptionPane.showMessageDialog(null, "It is not your turn.");
        }
        else if (messageCode == 2) {
            if (model.currentPlayer() == Player.WHITE){
                JOptionPane.showMessageDialog(null, "White is in check.");
            }
            else if (model.currentPlayer() == Player.BLACK){
                JOptionPane.showMessageDialog(null, "Black is in check.");
            }
        }
        else if (messageCode == 3) {
            if (model.currentPlayer() == Player.WHITE){
                JOptionPane.showMessageDialog(null, "Checkmate;  Black wins.");

            }
            else if (model.currentPlayer() == Player.BLACK){
                JOptionPane.showMessageDialog(null, "Checkmate;  White wins.");
            }
            gameOverDialog();
        }
        else if (model.getMessage() == 1) {
            JOptionPane.showMessageDialog(null, "Invalid move; the King is placed in check.");
        }
        else if (model.getMessage() == 2) {
            JOptionPane.showMessageDialog(null, "Invalid move; the King remains in check.");
        }
        else if (model.getMessage() == 3) {
            if (piece != null) {

                JOptionPane.showMessageDialog(null, "" + piece.type() + ":  Invalid move.");
            }
        }
        messageCode = 0;
    }

    private void provideFeedback( Location s, String message )
    {
        if (model.inCheck( model.currentPlayer( ) ))
        {
            messageCode = 2;
            if (model.isCheckMate( ))
            {
                messageCode = 3;
                if (trace) { System.out.println( message ); }
            }
        }
        displayMessage( model.pieceAt( s ) );
    }

    private void gameOverDialog()
    {
        int confirm = JOptionPane.showOptionDialog(null,
                "Would you like to play again?",
                "Game Over", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (confirm == JOptionPane.YES_OPTION) {
            model = new ChessModel( "" );
            moveHistory = new ArrayList<Move>();
            turn = 0;
        }
        if (confirm == JOptionPane.NO_OPTION || confirm == JOptionPane.CLOSED_OPTION) {
            System.exit(1);
        }
    }

    private Location getEventLocation( ActionEvent event )
    {
        for(int r = 0; r < 8; r++)
        {
            for(int c = 0; c < 8; c++)
            {
                if (board[r][c] == event.getSource())
                {
                    return new Location( r, c );
                }
            }
        }
        return null;
    }

    private class GeneralListener implements ActionListener
    {
        Location eventLocation;
        Location fromLocation;
        Color backgroundColor;
        boolean fromTo;

        public void actionPerformed(ActionEvent event)
        {
            Move  thisMove;
            Location toLocation;

            if (reset == event.getSource())
            {   
                if (fromTo == true)
                {
                    fromTo = false;
                    buttonAt( eventLocation ).setBackground(backgroundColor);
                }

                int confirm = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to start a new game?",
                        "Reset Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION)
                {               
                    reset( );
                }
            }
            else if (undo == event.getSource())
            {
                if (fromTo == true)
                {
                    fromTo = false;
                    buttonAt( eventLocation ).setBackground(backgroundColor);
                }
                undoMove();
            }           
            else if (automatic == event.getSource())
            {
                selfPlay = !selfPlay;
                if (selfPlay)
                {
                    automatic.setText( "Auto play" );
                }
                else
                {
                    automatic.setText( "Manual play" );
                }
            }           
            else if (btnTrace == event.getSource())
            {
                trace = !trace;
                if (trace)
                {
                    btnTrace.setText( "No Trace" );
                }
                else
                {
                    btnTrace.setText( "Trace" );
                }
            }           
        }
    }

    private class GameMoveListener implements ActionListener
    {
        Location eventLocation;
        Location fromLocation;
        Color backgroundColor;
        boolean fromTo;

        public GameMoveListener()
        {
            fromTo = false; 
        }

        public void actionPerformed(ActionEvent event)
        {
            Move  thisMove;
            Location toLocation;

            eventLocation = getEventLocation( event );
            if (eventLocation != null)
            {
                if (!fromTo)
                {
                    fromLocation = eventLocation;

                    if (model.pieceAt(fromLocation) != null)
                    {
                        if (model.pieceAt(fromLocation).player() == model.currentPlayer())
                        {
                            backgroundColor = buttonAt( fromLocation ).getBackground();
                            buttonAt( fromLocation ).setBackground(Color.CYAN);
                            messageCode = 0;
                            fromTo = true;
                        }
                        else
                        {
                            messageCode = 1;
                            displayMessage( model.pieceAt(fromLocation) );
                        }
                    }
                }
                else if (fromTo)
                {               
                    buttonAt( fromLocation ).setBackground(backgroundColor);
                    toLocation = eventLocation;

                    thisMove = new Move(fromLocation, toLocation );
                    if (model.isValidMove( thisMove ))
                    {
                        move( thisMove );
                        Move castle = model.castleMove( thisMove );
                        if (castle != null)
                        {                 
                            move( castle );
                            if (trace) { System.out.println( ", castled" ); }
                        }
                        else
                        {
                            if (trace) { System.out.println( ); }
                        }
                        model.setNextPlayer();
                        provideFeedback( fromLocation, "Black in check" );                            
                    }
                    fromTo = false;
                }

                if (selfPlay)
                {
                    if (model.currentPlayer() == Player.BLACK)
                    {
                        Move m = model.AI();
                        if ( m != null)
                        {
                            fromLocation = m.from;
                            move( m );
                            Move castle = model.castleMove( m );
                            if (castle != null)
                            {                 
                                move( castle );
                                if (trace) { System.out.println( ", castled" ); }
                            }
                            else
                            {
                                if (trace) { System.out.println( ); }
                            }
                        }
                        model.setNextPlayer();
                        provideFeedback( fromLocation, "White in check" );                            
                    }
                    turn++;
                }
            }
        }
    }
}