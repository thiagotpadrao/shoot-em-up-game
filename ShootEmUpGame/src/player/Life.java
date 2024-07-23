package player;

import game.GameObject;
import game.Main;
import lib.GameLib;
import java.awt.Color;

public class Life extends GameObject{
    
    public Life(int state, double X, double Y){
        this.setX(X);
        this.setY(Y);
        this.setRadius(9.0);
        this.setState(Main.ACTIVE);
    }

    /* method to draw life */
    public void draw(){
        if(this.getState() == Main.ACTIVE){
            GameLib.setColor(Color.RED);
		    GameLib.drawLife(this.getX(), this.getY(), this.getRadius());
        }
    }
}
