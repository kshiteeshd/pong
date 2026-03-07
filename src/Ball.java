import java.awt.*;

public class Ball {
    //Ball Settings
    int x = 390;
    int y = 290;
    int velocityX = 3;
    int velocityY = 2;
    int size = 10;
    //Wall SETTINGS
    int leftWall = 780-size*2;
    int rightWall = 0;
    int topWall = 0;
    int bottomWall = 560-size;

    public void update(){
        x += velocityX;
        y += velocityY;
        if(x <= leftWall){
            velocityX = -velocityX;
        }

        if(x >= rightWall){
            velocityX = -velocityX;
        }

        if(y <= 10)
        {
            velocityY = -velocityY;
            y = 10;
        }

        if(y + size >= 550)
        {
            velocityY = -velocityY;
            y = 540 - size;
        }


    }

    public void draw(Graphics g){
        g.setColor(Color.cyan);
        g.fillOval(x, y, size, size);

    }
}
