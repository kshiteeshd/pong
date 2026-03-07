import javax.swing.*;
import java.awt.*;

public class Paddle extends JPanel {

    //Start Location Variables
    int x;
    int y;

    //Size of paddle variables
    int width = 14;
    int height = 120;

    //Speed of paddle multiplier
    int speed = 16;

    //Boundary Condition
    int topWall = 0;
    int bottomWall = 560;

    //Velocity
    int velocityY = 0;

    public Paddle(int startX,int startY){
        x = startX;
        y = startY;
    }

    public void moveUp(){
        velocityY = -speed;
        y -=speed;
        if(y<topWall){
            y=topWall;
        }
    }

    public void moveDown(){
        velocityY = speed;
        y += speed;
        if(y > bottomWall-height){
            y = bottomWall-height;
        }
    }

    public void stop(){
        velocityY = 0;
    }

    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillRect(x,y,width,height);
    }
}
