/*
* TCSS 305 – Autumn 2016
* Assignment 6 – Tetris
*/
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.MovableTetrisPiece;

/**
 * JPanel that displays the next piece of the Tetris Game.
 * @author James Roberts
 * @version 2 December 2016
 */
public class NextPiecePanel extends JPanel implements Observer {
    
    /** The Serial ID #. */
    private static final long serialVersionUID = -4025203312340203723L;
    /** Size of a block. */
    private static final int BLOCK_SIZE = 35;
    /** Number of blocks that make up the side of the square panel. */
    private static final int PANEL_WIDTH = 5;
    /** Collection of Blocks that make up the Tetris piece. */
    private final List<Rectangle2D.Double> myBlocks;
    
    /** 
     * Constructor for the NextPiecePanel. 
     */
    public NextPiecePanel() {
        super();
        myBlocks = new ArrayList<Rectangle2D.Double>();
        final Dimension panelSize = new Dimension(BLOCK_SIZE * PANEL_WIDTH, 
                                             BLOCK_SIZE * PANEL_WIDTH);
        setPreferredSize(panelSize);
        setMaximumSize(panelSize);
        setBackground(Color.GRAY);
    }
    
    /**
     * Paints the representation of the Next Piece.
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);      
        final Graphics2D g2d = (Graphics2D) theGraphics; 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);

        for (final Rectangle2D.Double r : myBlocks) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fill(r);
            g2d.setColor(Color.WHITE);
            g2d.draw(r);
        }
        
    }
    
    /**
     * Updates the piece being displayed if passed a MovableTetrisPiece.
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObject instanceof MovableTetrisPiece) {
            createPiece((MovableTetrisPiece) theObject);
            repaint();
        }
    }
    
    /**
     * Takes a MovableTetrisPiece object and creates a visual representation
     * of it out of that can be displayed.
     * @param thePiece the MovableTetrisPiece to be displayed.
     */
    private void createPiece(final MovableTetrisPiece thePiece) {

        myBlocks.clear(); //Clear previous Tetris Piece
        //Find starting coordinates based on height and width of the piece
        final double startY = 3 - thePiece.getHeight(); 
        final double startX;
        if (thePiece.getWidth() <= 2) { //Case of a width 2 piece's string formatting
            startX = 1;
        } else { //Width 3 or 4 piece's string formatting
            startX = PANEL_WIDTH - thePiece.getWidth();
        }
        
        //Create a Rectangle2D object for each of the blocks
        Rectangle2D.Double block;
        double x = startX;
        double y = startY;

        for (int i = 0; i < thePiece.toString().length(); i++) {
            
            if (thePiece.toString().charAt(i) == ' ') { //Found an empty spot
                x += 2; 
            } else if (thePiece.toString().charAt(i) == '\n') { //Finished one line
                y += 2;
                x = startX;
            } else { //Found a block
                block = new Rectangle2D.Double(x * (BLOCK_SIZE / 2), y * (BLOCK_SIZE / 2), 
                                               BLOCK_SIZE, BLOCK_SIZE);
                myBlocks.add(block);
                x += 2; 
            }
        }
        
    }


}
