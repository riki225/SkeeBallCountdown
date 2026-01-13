import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls implements KeyListener{
    
    boolean UpPressed;
    boolean DownPressed;
    boolean LeftPressed;
    boolean RightPressed;
    boolean APressed;
    boolean DPressed;
    boolean WPressed;
    @Override
    public void keyTyped(KeyEvent e){

    }
    @Override
    public void keyPressed(KeyEvent e){
        int epressed = e.getKeyCode();
        if(epressed == KeyEvent.VK_4){
            UpPressed = true;
            
        }
        if(epressed == KeyEvent.VK_6){
            WPressed = true;
        }
        if(epressed == KeyEvent.VK_9){
            APressed = true;
        }
        if(epressed == KeyEvent.VK_1){
            DownPressed = true;
        }
        if(epressed == KeyEvent.VK_5){
            DPressed = true;
        }
        if(epressed == KeyEvent.VK_2){
            LeftPressed = true;
        }
        if(epressed == KeyEvent.VK_3){
            RightPressed = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
        int epressed = e.getKeyCode();
        if(epressed == KeyEvent.VK_4){
            UpPressed = false;
        }
        if(epressed == KeyEvent.VK_1){
            DownPressed = false;
        }
        if(epressed == KeyEvent.VK_2){
            LeftPressed = false;
        }
        if(epressed == KeyEvent.VK_3){
            RightPressed = false;
        }
        if(epressed == KeyEvent.VK_5){
            DPressed = false;
        }
         if(epressed == KeyEvent.VK_9){
             //APressed = false;
         }
        if(epressed == KeyEvent.VK_6){
            WPressed = false;
        }
    }
}