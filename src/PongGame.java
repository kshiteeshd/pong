import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.*;
import java.awt.event.KeyListener;


public class PongGame extends JFrame {
    public PongGame() {
        setTitle("Pong Game");
        setSize(800, 600);
        add(new GamePanel());
        setVisible(true);

    }


    public static void main(String args[]) {
        new PongGame();
    }
}

class GamePanel extends JPanel implements Runnable, KeyListener {

    Thread gameThread;
    Ball ball;
    Paddle paddleLeft;
    Paddle paddleRight;
    SoundManager sound = new SoundManager();
    int gameState = 0; //0-Menu 1-Playing 2-gameOver
    int menuOption = 0; //For menu selection



    boolean wPressed = false;
    boolean sPressed = false;

    boolean upPressed = false;
    boolean downPressed = false;

    //SCORE
    int leftScore = 0;
    int rightScore = 0;
    int maxScore = 10;

    //Wall
    int wallThickness = 10;

    //Spawn Variables
    boolean ballSpawning = false;
    int spawnTimer = 0;
    int spawnDuration = 60; //60 frames = 1 second

    //

    public GamePanel() {

        //Setting background colour
        setBackground(Color.BLACK);

        //Sound Opening
        //sound.playSound("data/Sounds/PongGame Intro.wav");//Opening Path


        //intitlize game objects
        ball = new Ball();
        paddleLeft = new Paddle(40,220);
        paddleRight = new Paddle(740,220);

        addKeyListener(this);
        setFocusable(true);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run()
    {
        final double FPS = 60.0;
        final double frameTime = 1000000000.0 / FPS;

        double delta = 0;
        long lastTime = System.nanoTime();

        while(true)
        {
            long now = System.nanoTime();

            delta += (now - lastTime) / frameTime;
            lastTime = now;

            if(delta >= 1)
            {
                updateGame();
                repaint();
                delta--;
            }

            Thread.yield();
        }
    }

    public void updateGame() {

            if(gameState == 1){
                if(ballSpawning){

                    spawnTimer+=1;

                    if(spawnTimer > spawnDuration){
                        ballSpawning = false;

                        if(Math.random() < 0.5)
                            ball.velocityX = 3;
                        else
                            ball.velocityX = -3;

                        if(Math.random() < 0.5)
                            ball.velocityY = 2;
                        else
                            ball.velocityY = -2;


                    }
                }
                else{
                    ball.update();

                    if(checkCollision(ball, paddleLeft) && ball.velocityX < 0)
                    {
                        ball.velocityX = -ball.velocityX;
                        ball.velocityY += paddleLeft.velocityY;
                        int maxY = 6;

                        if(ball.velocityY > maxY)
                            ball.velocityY = maxY;

                        if(ball.velocityY < -maxY)
                            ball.velocityY = -maxY;
                        ball.x = paddleLeft.x + paddleLeft.width;
                        //sound.playSound("data/Sounds/Paddle Hit.wav");//Paddle Hit Sound
                    }

                    if(checkCollision(ball, paddleRight) && ball.velocityX > 0)
                    {
                        ball.velocityX = -ball.velocityX;
                        ball.velocityY += paddleRight.velocityY;
                        int maxY = 6;

                        if(ball.velocityY > maxY)
                            ball.velocityY = maxY;

                        if(ball.velocityY < -maxY)
                            ball.velocityY = -maxY;
                        ball.x = paddleRight.x - ball.size;
                        //sound.playSound("data/Sounds/Paddle Hit.wav");//Paddle Hit sound
                    }



                    if(wPressed)
                        paddleLeft.moveUp();

                    if(sPressed)
                        paddleLeft.moveDown();

                    if(upPressed)
                        paddleRight.moveUp();

                    if(downPressed)
                        paddleRight.moveDown();

                    checkGoal();
                }

            }






    }

    public void checkGoal(){

        if(ball.x < 10){
            rightScore++;
            if(leftScore >= maxScore || rightScore >= maxScore)
            {
                gameState = 2;
            }

            //sound.playSound("data/Sounds/Score.wav");//Goal Sound
            resetBall();
        }
        if(ball.x > 760){
            leftScore++;
            if(leftScore >= maxScore || rightScore >= maxScore)
            {
                gameState = 2;
            }
            //sound.playSound("data/Sounds/Score.wav");//Goal Sound
            resetBall();
        }

    }

    public void restartGame()
    {
        leftScore = 0;
        rightScore = 0;

        ball.x = 390;
        ball.y = 290;

        resetBall();

        gameState = 1;
    }

    public void resetBall(){
        ball.x = 390;
        ball.y = 290;

        ball.velocityX = 0;
        ball.velocityY = 0;

        ballSpawning = true;
        spawnTimer = 0;

        if(Math.random() < 0.5){
            ball.velocityX = 3;
        }
        else {
            ball.velocityX = -3;
        }

        if(Math.random() < 0.5){
            ball.velocityY = 3;
        }
        else {
            ball.velocityY = -3;
        }
        //sound.playSound("data/Sounds/Respanw.wav");//ResetBall


    }

    public boolean checkCollision(Ball ball, Paddle paddle)
    {
        int ballLeft = ball.x;
        int ballRight = ball.x + ball.size;
        int ballTop = ball.y;
        int ballBottom = ball.y + ball.size;

        int paddleLeft = paddle.x;
        int paddleRight = paddle.x + paddle.width;
        int paddleTop = paddle.y;
        int paddleBottom = paddle.y + paddle.height;

        return (ballRight >= paddleLeft &&
                ballLeft <= paddleRight &&
                ballBottom >= paddleTop &&
                ballTop <= paddleBottom);
    }

    public void drawMenu(Graphics g){
        String start ;
        String end ;

        g.setColor(Color.white);
        Font titleFont = new Font("Arial",Font.BOLD,60);
        g.setFont(titleFont);
        FontMetrics metrics = g.getFontMetrics();
        String title = "PONG";
        int x1 = (getWidth() - metrics.stringWidth(title));
        g.drawString(title, x1/2,200);

        g.setFont(new Font("Arial",Font.BOLD,30));
        FontMetrics metrics1 = g.getFontMetrics();
        if(menuOption == 0)
            start = "> Start Game";
        else
            start = "  Start Game";

        int x2 = (getWidth()- metrics1.stringWidth(start));
        g.drawString(start, x2/2,300);

        g.setFont(new Font("Arial",Font.BOLD,30));
        FontMetrics metrics2 = g.getFontMetrics();
        if(menuOption == 0)
            end = " Exit Game";
        else
            end = ">  Exit Game";

        int x3 = (getWidth() - metrics2.stringWidth(end));
        g.drawString(end, x3/2,340);


    }

    public void drawGameOver(Graphics g)
    {
        g.setColor(Color.WHITE);

        Font titleFont = new Font("Arial", Font.BOLD, 60);
        g.setFont(titleFont);

        FontMetrics metrics = g.getFontMetrics();

        String text = "GAME OVER";
        int x = (getWidth() - metrics.stringWidth(text)) / 2;

        g.drawString(text, x, 250);


        g.setFont(new Font("Arial", Font.BOLD, 30));
        metrics = g.getFontMetrics();

        String option;

        if(menuOption == 0)
            option = "> Restart";
        else
            option = "  Restart";

        int x2 = (getWidth() - metrics.stringWidth(option)) / 2;

        g.drawString(option, x2, 350);


        if(menuOption == 1)
            option = "> Menu";
        else
            option = "  Menu";

        int x3 = (getWidth() - metrics.stringWidth(option)) / 2;

        g.drawString(option, x3, 400);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(gameState == 0){
            drawMenu(g);

        }
        if(gameState == 1){

            //Drawing Wall
            g.setColor(Color.YELLOW);
            g.fillRect(0, 0, getWidth(), wallThickness);// top wall
            g.fillRect(0, 552, getWidth(), wallThickness); // bottom wall

            //Drawing Ball
            if(ballSpawning){
                if(spawnTimer % 10 < 1){
                    ball.draw(g);
                }
            }
            else{
                ball.draw(g);
            }

            //Drawing Paddle
            paddleLeft.draw(g);
            paddleRight.draw(g);

            //Drawing ScoreCard
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString(String.valueOf(leftScore), 10, 40);
            g.drawString(String.valueOf(rightScore), 760, 40);
        }
        if(gameState == 2)
        {
            drawGameOver(g);
        }

    }
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        //Game State 0(MENU)
        if(gameState == 0){
            if(key == KeyEvent.VK_UP) {
                menuOption--;

                if(menuOption < 0)
                    menuOption = 1;

                //sound.playSound("data/Sounds/Menu Sound.wav");
            }

            if(key == KeyEvent.VK_DOWN) {
                menuOption++;

                if(menuOption > 1)
                    menuOption = 0;

                //sound.playSound("data/Sounds/Menu Sound.wav");
            }
            if(key == KeyEvent.VK_ENTER) {

                if(menuOption == 0) {
                    gameState = 1;
                }

                if(menuOption == 1) {
                    System.exit(0);
                }
            }
        }




        //Game State = 1(PLAYING)
        if(gameState == 1) {

            if (key == KeyEvent.VK_ESCAPE) {
                gameState = 0;
                //sound.playSound("data/Sounds/PongGame Intro.wav");
            }

            if(key == KeyEvent.VK_W)
                wPressed = true;

            if(key == KeyEvent.VK_S)
                sPressed = true;

            if(key == KeyEvent.VK_UP)
                upPressed = true;

            if(key == KeyEvent.VK_DOWN)
                downPressed = true;
        }

        if(gameState == 2)
        {
            if(key == KeyEvent.VK_UP)
            {
                menuOption--;
                if(menuOption < 0) menuOption = 1;
            }

            if(key == KeyEvent.VK_DOWN)
            {
                menuOption++;
                if(menuOption > 1) menuOption = 0;
            }

            if(key == KeyEvent.VK_ENTER)
            {
                if(menuOption == 0)
                {
                    restartGame();
                }

                if(menuOption == 1)
                {
                    gameState = 0;
                }
            }
        }


        repaint();

    }

    public void keyReleased(KeyEvent e) {
        if(gameState == 1){
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_W)
                wPressed = false;

            if(key == KeyEvent.VK_S)
                sPressed = false;

            if(key == KeyEvent.VK_UP)
                upPressed = false;

            if(key == KeyEvent.VK_DOWN)
                downPressed = false;

            if(key == KeyEvent.VK_W || key == KeyEvent.VK_S)
                paddleLeft.stop();

            if(key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
                paddleRight.stop();
        }
    }

    public void keyTyped(KeyEvent e) {}
}
