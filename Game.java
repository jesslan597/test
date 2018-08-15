import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;


public class Game implements Runnable {
    /**
     * Game Main class that specifies the frame and widgets of the GUI
     */
    
        final static JLabel score = new JLabel("");
        static GameLogic court;
        static JFrame frame;
    
        public void run() {
            // Top-level frame in which game components live
            frame = new JFrame("TOP LEVEL FRAME");
            frame.setLocation(300, 300);

            // Status panel
            final JPanel status_panel = new JPanel();
            frame.add(status_panel, BorderLayout.SOUTH);
            final JLabel status = new JLabel("Running...");
            status_panel.add(status);

            // Main playing area
            court = new GameLogic(status);
            frame.add(court, BorderLayout.CENTER);
            score.setText("Score: " + court.getScore());
            frame.add(score, BorderLayout.NORTH);
            

            // Put the frame on the screen
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // Start game
            court.reset();
            
        }

        /*
         * Main method run to start and run the game Initializes the GUI elements
         * specified in Game and runs it 
         */
        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Game());
            
        }
        
        public static void setScoreText(int i) {
            score.setText("Score:" + i);
        }
}
