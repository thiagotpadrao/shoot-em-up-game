package enemies;

import java.awt.Color;

import game.Main;
import lib.GameLib;

public class Enemy2 extends Enemy{

    public Enemy2(double X){
        super(Main.ACTIVE, X, -10.0, 0.42, (3 * Math.PI) / 2, 0.0);
        this.setRadius(12.0);
    }

    /* method to draw the enemy */
    public void draw(Long currentTime){
        
        /* if the enemy state is EXPLODING, draw the explosion */
        if(this.getState() == Main.EXPLODING){
					
            double alpha = (currentTime - this.getExplosion_start()) / (this.getExplosion_end() - this.getExplosion_start());
            GameLib.drawExplosion(this.getX(), this.getY(), alpha);
        }
        
        /* if the enemy state is ACTIVE, draw the enemy */
        if(this.getState() == Main.ACTIVE){
    
            GameLib.setColor(Color.MAGENTA);
            GameLib.drawDiamond(this.getX(), this.getY(), this.getRadius());
        }
    }
}
