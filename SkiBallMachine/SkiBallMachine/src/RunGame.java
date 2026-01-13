//import arduino.*;
import java.awt.Color;
import java.util.Scanner;

import javax.swing.JFrame;

public class RunGame {
    
    public static JFrame window;
    public static void main(String[] args) throws Exception{
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Test");
        window.setSize(1280, 720);
        window.setBackground(Color.RED);
        window.setUndecorated(true);
        SkiBall game = new SkiBall();
        window.add(game);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        game.newGameThread();
        // Scanner ob = new Scanner(System.in);
        
        // char test = 5;
        // char input = ob.nextLine().charAt(0);
		// while(input != 'n'){
		// 	//Hi.serialWrite(input);
		// 	input = ob.nextLine().charAt(0);
		// }
        
    }
}
