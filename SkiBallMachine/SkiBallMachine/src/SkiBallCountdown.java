import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;

//import arduino.Arduino;

//import arduino.Arduino;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
public class SkiBallCountdown extends JPanel implements Runnable{
    
    //map of the game above in bits.
    int hi = 3000;
    int Pastball = 10;
    int flashCount = 0;
    boolean flash = false;
    boolean flashed = false;
    int endScreenCount = 0;
    boolean endedSceen = false;
    boolean newHighScore = false;
    int pressStartFlashTime = 0;
    boolean lightStarted = true;
    Controls ballHit = new Controls();
    private static BufferedWriter databaseOutput;
    BufferedImage screenImage;
    Graphics2D g2;
    Font digital7 = null;
    Thread gameThread;
    int screenHeight;
    int screenWidth;
    boolean playEnd = true;
    Image backgroundImage = null;
    //Arduino Hi = new Arduino("COM4", 9600); //uncomment this later
    
     public void newGameThread (){
        gameThread = new Thread(this);
        gameThread.start();
     }

    public SkiBallCountdown() {
        this.setPreferredSize(new Dimension(1280, 720));
        this.setLocation(0, 0);
        this.setSize(1280, 720);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(ballHit);
        this.setFocusable(isFocusable());;
        // attempt to load Digital-7.ttf from current or parent dir
        try {
            File ff = new File("Digital-7.ttf");
            if (!ff.exists()) ff = new File(".." + File.separator + "Digital-7.ttf");
            if (ff.exists()) {
                digital7 = Font.createFont(Font.TRUETYPE_FONT, ff);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(digital7);
            }
        } catch (Exception e) {
            digital7 = null;
            System.err.println("Could not load Digital-7.ttf, using fallback font.");
        }
    }
    boolean started = false;
    @Override
    public void run() {
        //Hi.openConnection(); //uncomment this later
        try {
            Thread.sleep(2100);
            String hi = "9";
            char input = hi.charAt(0);
             //Hi.serialWrite(input); //uncomment this later
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        double tickInterval = 1000000000/60;
        double nextTick = System.nanoTime() + tickInterval;
        try {
            ScoreRead();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            backgroundImage = ImageIO.read(new File("skeeballBackgroundOff.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fullScreen();
        while(true){

            screenImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
            g2 = (Graphics2D)screenImage.getGraphics();
            drawToImageScreen();
            drawToWindowScreen();
            if(ballHit.APressed){
                if(!started){
                    started = true;
                    lightStarted = true;
                    playSound("start");
                    try {
                        if(hi > Integer.valueOf(highScoreString)){
                            highScoreString = String.valueOf(hi);
                        }
                        hi = 3000;
                        Ball = 10;

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String hi = "5";
                    char input = hi.charAt(0);
                    //Hi.serialWrite(input); //uncomment this later
                
                }else if(!lightStarted && !newHighScore){

                }
                // System.out.println(Ball);
                // System.out.println(highScoreString + "HI");
                
                Check();
            }
            if(!(Pastball == Ball)){
                flashCount = 0;
                flash = true;
            }
            Pastball = Ball;
            if(Ball == 0 && ballHit.APressed && started){
                if(hi > Integer.valueOf(highScoreString)){
                    highScoreString = String.valueOf(hi);
                    newHighScore = true;
                }else{
                    newHighScore = false;
                }
                ballHit.APressed = false;
            }
            double remainingTick = nextTick - System.nanoTime();
            remainingTick = remainingTick/1000000;
            
           // System.out.println(remainingTick);
            try {
                if(remainingTick < 0){
                    remainingTick = 0;
                }
                Thread.sleep((long) remainingTick);
                nextTick += tickInterval;
            } catch(InterruptedException e){
                e.printStackTrace();
            }
         }
    }

    String txtString = "";
    String highScoreString = "";
    File file = new File("HighScore.txt");
    public void ScoreRead() throws Exception{

        try (BufferedReader bufferedReaderStart = new BufferedReader(new FileReader(file))) {
            int lineCount=0;
            while ((txtString = bufferedReaderStart.readLine()) != null){ 
                if(!txtString.equals("")){
                    highScoreString = txtString;
            }
   }
            lineCount++;
        }
     }
        boolean isUpPressed = false;
        boolean isDownPressed = false;
        boolean isLeftPressed = false;
        boolean isRightPressed = false;
        boolean isNeutralPressed = false;
        boolean isNeutralPressedv2 = false;
        int Ball = 10;
    public void Check(){
        if(ballHit.UpPressed && !isUpPressed && Ball > 0){
            hi-= 400;
            playSound("400");
            isUpPressed = !isUpPressed;
            Ball--;
            System.out.println(Ball);
            System.out.println(highScoreString + "HI");
            if(Ball !=0){
                String hi = "3";
                char input = hi.charAt(0);
                
                //Hi.serialWrite(input); //uncomment this later
            }
        }
        if(!ballHit.UpPressed && isUpPressed){
            isUpPressed = !isUpPressed;           
        }
        if(ballHit.DownPressed && !isDownPressed && Ball > 0){
            hi-= 100;
            isDownPressed = !isDownPressed;
            Ball--;
            System.out.println(Ball);
        }
        if(!ballHit.DownPressed && isDownPressed){
            isDownPressed = !isDownPressed;
        }
        if(ballHit.LeftPressed && !isLeftPressed && Ball > 0){
            hi-= 200;
            playSound("200");
            isLeftPressed = !isLeftPressed;
            Ball--;
            if(Ball !=0){
                String hi = "1";
            char input = hi.charAt(0);
            //Hi.serialWrite(input); //uncomment this later
            }
            System.out.println(Ball);
        }
        if(!ballHit.LeftPressed && isLeftPressed){
            isLeftPressed = !isLeftPressed;
        }
        if(ballHit.RightPressed && !isRightPressed && Ball > 0){
            hi-= 300;
            playSound("300");
            isRightPressed = !isRightPressed;
            Ball--;
            if(Ball != 0){
                String hi = "2";
                char input = hi.charAt(0);
                //Hi.serialWrite(input); //uncomment this later
            }
            System.out.println(Ball);
        }
        if(!ballHit.RightPressed && isRightPressed){
            isRightPressed = !isRightPressed;
        }
        if(ballHit.DPressed && !isNeutralPressed && Ball > 0){
            hi-= 500;
            playSound("500");
            isNeutralPressed = !isNeutralPressed;
            Ball--;
            if(Ball != 0){
                String hi = "4";
                char input = hi.charAt(0);
                //Hi.serialWrite(input); //uncomment this later
            }
            System.out.println(Ball);
        }
        if(!ballHit.DPressed && isNeutralPressed){
            isNeutralPressed = !isNeutralPressed;
        }
        if(ballHit.WPressed && !isNeutralPressedv2 && Ball > 0){
            hi-= 500;
            playSound("500");
            isNeutralPressedv2 = !isNeutralPressedv2;
            Ball--;
            if(Ball !=0){
                String hi = "4";
                char input = hi.charAt(0);
                //Hi.serialWrite(input); //uncomment this later
            }
            System.out.println(Ball);
        }
        if(!ballHit.WPressed && isNeutralPressedv2){
            isNeutralPressedv2 = !isNeutralPressedv2;
        }
        if(Ball == 0 && hi > Integer.parseInt(highScoreString)){
                Path mainDatabasePath = Paths.get("HighScore.txt");
                //clears out database file so we can rewrite it
                try {
                    Files.newBufferedWriter(mainDatabasePath , StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
                if (databaseOutput == null) {
                    try {
                        databaseOutput = new BufferedWriter(new FileWriter("HighScore.txt"));
                    }
                    catch (Exception e2) {
                        System.err.println("Cannot create file for output!");
                        e2.printStackTrace();
                    }
                }
        
                try {
                        databaseOutput.write(String.valueOf(hi));
                    }
                catch (Exception e2) {
                    System.err.println("Cannot write file!");
                    e2.printStackTrace();
                }
            if (databaseOutput != null) {
                try {
                    databaseOutput.close();
                    databaseOutput = null;
                }
                catch (Exception e4) {
                    System.err.println("Cannot close output file!");
                    e4.printStackTrace();
                }
            }
            String hi = "5";
            char input = hi.charAt(0);
            //Hi.serialWrite(input); //uncomment this later
        }
    }
    public void drawToImageScreen(){
        try {
            drawScore(g2);
            g2.dispose();
        } catch (FontFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void drawToWindowScreen(){//1920/1280 = 1.5, 1080/720 = 1.5 
        Graphics g = getGraphics();
        g.drawImage(screenImage, 0, 0,(int)(1280), (int)(720), null); //use this line for local testing, use the line below for the actual skee ball machine
        //g.drawImage(screenImage, 0, 0,(int)((1280)*1.5), (int)((720)*1.5), null); //use this line for the actual skee ball machine, use the line above for local testing
        g.dispose();
    }
    public void fullScreen(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd  = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(RunGame.window);
        
        screenHeight = 720;
        screenWidth = 1280;
        System.out.println(screenHeight);
        System.out.println(screenWidth);
    }
    public void drawScore(Graphics g2) throws FontFormatException, IOException{
        Graphics2D gg = (Graphics2D) g2;
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gg.setColor(Color.BLACK);
        gg.fillRect(0, 0, 1280, 720);
        if (backgroundImage != null) {
            gg.drawImage(backgroundImage, 0, 0, 1280, 720, null);
        }
        gg.setFont(getGameFont(40f));
        gg.setColor(new Color(21,21,21));
        gg.drawString("Made by David T Yeung", 850, 695);
        // g2.setColor(Color.black);
        // g2.fillRect(0, 0, 1280, 720);
        g2.setFont(getGameFont(90f));
        g2.setColor(Color.RED);
        if(ballHit.APressed){
        AffineTransform textAffineTransform = new AffineTransform();
        textAffineTransform.rotate(Math.toRadians(-14), 0, 0);
        Font currentFont = getGameFont(60f);
        g2.setColor(Color.RED);
        Font rotatedFont = currentFont.deriveFont(textAffineTransform);
        g2.setFont(rotatedFont);
        g2.drawString("Current", 919, 215);
        g2.drawString("HighScore:"+highScoreString, 930, 260);
        g2.setFont(getGameFont(185f));
        g2.setColor(new Color(45,3,2));
        g2.drawString("18", 585, 621);
        g2.setColor(Color.RED);
        if(String.valueOf(Ball).length() < 2){
            g2.drawString(String.valueOf(Ball), 615, 621);
        }else{
            g2.drawString(String.valueOf(Ball), 585, 621);
        }
        //g2.setFont(new Font("Digital-7", 0, 100));
        //g2.drawString("Current Score", 190, 150);
        g2.setFont(getGameFont(299f));
        
        g2.setColor(new Color(45,3,2));
        g2.drawString("8888", 926-g2.getFontMetrics().stringWidth("8888"), 445);
        g2.setColor(Color.RED);
        if(hi > 1000 && hi % 1000 == 100){
            int width = g2.getFontMetrics().stringWidth(String.valueOf(hi).substring(1));
            g2.drawString(String.valueOf(hi).substring(1), 926-(width), 445);
            if(!String.valueOf(hi).substring(0,1).equals("1")){
                g2.drawString(String.valueOf(hi).substring(0,1),926-g2.getFontMetrics().stringWidth("8888"),445);
            }else{
                g2.drawString(String.valueOf(hi).substring(0,1),926-g2.getFontMetrics().stringWidth("1888"),445);
            }
        }else{
            int width = g2.getFontMetrics().stringWidth(String.valueOf(hi));
            g2.drawString(String.valueOf(hi), 926-(width), 445);
        }
        //g2.drawString(String.valueOf(hi), 926-(width), 445);
        //System.out.println(width);
        if(flash && !flashed){
            g2.setColor(new Color(45,3,2));
            if(String.valueOf(Ball).length() < 2){
                g2.drawString(String.valueOf(Ball), 615, 621);
            }else{
                g2.drawString(String.valueOf(Ball), 585, 621);
            }
            flashCount++;
            if(flashCount % 30 == 0){
                flashed = !flashed;
            }
        } else if(flash){
            flashCount++;
            if(flashCount % 30 == 0){
                flashed = !flashed;
            }
            if(flashCount > 180){
                flash = false;
                flashCount = 0;
            }
        }
        }else{
            
            
            g2.setColor(Color.RED);
            
            AffineTransform textAffineTransform = new AffineTransform();
            textAffineTransform.rotate(Math.toRadians(-14), 0, 0);
            Font currentFont = getGameFont(70f);
            Font rotatedFont = currentFont.deriveFont(textAffineTransform);
            g2.setFont(rotatedFont);
            g2.drawString("PRESS START", 30, 125);
            g2.drawString("  TO PLAY", 35, 180);
            textAffineTransform.rotate(Math.toRadians(-28), 0, 0);
            currentFont = getGameFont(60f);
            rotatedFont = currentFont.deriveFont(textAffineTransform);
            g2.setFont(rotatedFont);
            g2.drawString("Current", 919, 215);
            g2.drawString("HighScore:"+highScoreString, 930, 260);
            g2.setFont(new Font("Digital-7", 0, 299));
            g2.setColor(new Color(45,3,2));
            g2.drawString("8888", 926-g2.getFontMetrics().stringWidth("8888"), 445);
            g2.setFont(new Font("Digital-7", 0, 185));
            g2.drawString("18", 585, 621);

            if(pressStartFlashTime < 30){
                pressStartFlashTime++;
                AffineTransform saved = gg.getTransform();
                gg.rotate(Math.toRadians(14));
                gg.setColor(Color.BLACK);
                gg.fillRect(30, 55, 375, 115);
                gg.setTransform(saved);
            }else if(pressStartFlashTime >= 30 && pressStartFlashTime < 55){
                pressStartFlashTime++;
            }else{
                pressStartFlashTime = 0;
            }
            
        }
        if(Ball != 0){
            endScreenCount = 0;
            playEnd = true;
        }
        if(Ball == 0 && newHighScore && !endedSceen){
            if(playEnd) {playSound("highscore"); playEnd = false;}
            Image backgroundOn = ImageIO.read(new File("skeeballBackgroundOn.png"));
            if(endScreenCount == 359){
                String hi = "9";
                char input = hi.charAt(0);
                //Hi.serialWrite(input); //uncomment this later
            }
            if(endScreenCount < 360){
                if(endScreenCount/18 % 2 ==0 && backgroundOn != null) gg.drawImage(backgroundOn, 0, 0, 1280, 720, null);
                AffineTransform saved = gg.getTransform();
                gg.setColor(Color.BLACK);
                gg.rotate(Math.toRadians(14));
                gg.fillRect(30, 55, 375, 115);
                gg.setTransform(saved);
                gg.setFont(getGameFont(299f));
                g2.setColor(new Color(45,3,2));
                g2.drawString("8888", 926-g2.getFontMetrics().stringWidth("8888"), 445);
                g2.setColor(new Color(45,3,2));
                if(endScreenCount/18 % 2 ==0) g2.setColor(Color.RED);
                if(hi > 1000 && hi % 1000 == 100){
                    int width = g2.getFontMetrics().stringWidth(String.valueOf(hi).substring(1));
                    g2.drawString(String.valueOf(hi).substring(1), 926-(width), 445);
                    if(!String.valueOf(hi).substring(0,1).equals("1")){
                        g2.drawString(String.valueOf(hi).substring(0,1),926-g2.getFontMetrics().stringWidth("8888"),445);
                    }else{
                        g2.drawString(String.valueOf(hi).substring(0,1),926-g2.getFontMetrics().stringWidth("1888"),445);
                    }
                }else{
                    int width = g2.getFontMetrics().stringWidth(String.valueOf(hi));
                    g2.drawString(String.valueOf(hi), 926-(width), 445);
                }
                AffineTransform textAffineTransform = new AffineTransform();
                textAffineTransform.rotate(Math.toRadians(-14), 0, 0);
                Font currentFont = new Font("Digital-7", 0, 60);
                Font rotatedFont = currentFont.deriveFont(textAffineTransform);
                g2.setFont(rotatedFont);
                rotatedFont = currentFont.deriveFont(textAffineTransform);
                g2.setFont(rotatedFont);
                g2.setColor(Color.BLACK);
                g2.drawString("Current", 919, 215);
                g2.drawString("HighScore:"+highScoreString, 930, 260);
                g2.setColor(Color.BLACK);
                if(endScreenCount/18 % 2 ==0) {
                    g2.setColor(Color.RED);
                }
                g2.setFont(rotatedFont);
                g2.drawString("New", 919, 215);
                g2.drawString("HighScore:"+hi+"!!!", 930, 263);
                
                endScreenCount++;
                }else{
                started = false;
                newHighScore = false;
                hi=3000;
            }
        }
        if(Ball == 0 && !newHighScore && !endedSceen){
            if(playEnd){ playSound("end"); playEnd = false;};
            if(endScreenCount == 359){
                String hi = "9";
                char input = hi.charAt(0);
                //Hi.serialWrite(input); //uncomment this later
            }
            if(endScreenCount < 360){
                g2.setColor(Color.BLACK);
                ((Graphics2D)g2).rotate(Math.toRadians(14));
                g2.fillRect(30, 55, 375, 115);
                ((Graphics2D)g2).rotate(Math.toRadians(-14));
                g2.setFont(new Font("Digital-7", 0, 299));
                g2.setColor(new Color(45,3,2));
                g2.drawString("8888", 926-g2.getFontMetrics().stringWidth("8888"), 445);
                g2.setColor(Color.RED);
                if(hi > 1000 && hi % 1000 == 100){
                    int width = g2.getFontMetrics().stringWidth(String.valueOf(hi).substring(1));
                    g2.drawString(String.valueOf(hi).substring(1), 926-(width), 445);
                    if(!String.valueOf(hi).substring(0,1).equals("1")){
                        g2.drawString(String.valueOf(hi).substring(0,1),926-g2.getFontMetrics().stringWidth("8888"),445);
                    }else{
                        g2.drawString(String.valueOf(hi).substring(0,1),926-g2.getFontMetrics().stringWidth("1888"),445);
                    }
                }else{
                    int width = g2.getFontMetrics().stringWidth(String.valueOf(hi));
                    g2.drawString(String.valueOf(hi), 926-(width), 445);
                }
                AffineTransform textAffineTransform = new AffineTransform();
                textAffineTransform.rotate(Math.toRadians(-14), 0, 0);
                Font currentFont = new Font("Digital-7", 0, 60);
                Font rotatedFont = currentFont.deriveFont(textAffineTransform);
                g2.setFont(rotatedFont);
                rotatedFont = currentFont.deriveFont(textAffineTransform);
                g2.setFont(rotatedFont);
                g2.setColor(Color.BLACK);
                g2.drawString("Current", 919, 215);
                g2.drawString("HighScore:"+highScoreString, 930, 260);
                g2.setColor(Color.RED);
                if(endScreenCount/54 % 2 ==0)g2.setColor(Color.BLACK);
                g2.setFont(rotatedFont);
                g2.drawString("Your", 919, 215);
                g2.drawString("Score: "+hi+"!", 930, 260);
                endScreenCount++;
            }else{
                started = false;
                hi=3000;
            }
        }
        g2.dispose();

    }
    // public void endScreen(Graphics g2)throws FontFormatException, IOException{
    //     g2.setColor(Color.black);
    //     g2.fillRect(0, 0, 1280, 720);
    //     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //     Font test= Font.createFont(Font.TRUETYPE_FONT, new File("Digital-7.ttf"));
    //     ge.registerFont(test);
    //     g2.setFont(new Font("Digital-7", 0, 100));
    //     g2.setColor(Color.RED);
    //     g2.drawString("Your Score: "+ hi, 50, 100);
    // }
    public void playSound(String audioName){
                File audioFile = new File(audioName+".wav");
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Font getGameFont(float size) {
        if (digital7 != null) {
            return digital7.deriveFont(size);
        }
        return new Font("SansSerif", Font.PLAIN, Math.max(10, Math.round(size)));
    }
}