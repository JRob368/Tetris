/*
* TCSS 305 – Autumn 2016
* Assignment 6 – Tetris
*/
package view;

import java.awt.EventQueue;


/**
 * Driver for the Tetris Game. 
 * @author James Roberts
 * @version 2 December 2016
 */
public final class TetrisDriver {

    /**
     * Private constructor to prevent instantiation. 
     */
    private TetrisDriver() {
        throw new IllegalStateException();
    }
    
    /**
     * Invokes the Tetris GUI. Does not use
     * the command line arguments.
     * @param theArgs Command line arguments.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI().start();
            }
        });

    }

}
