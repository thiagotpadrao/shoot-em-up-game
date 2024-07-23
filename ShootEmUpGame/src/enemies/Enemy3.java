package enemies;

import game.Main;
import lib.GameLib;
import java.awt.Color;

public class Enemy3 extends Enemy{
    private long nextShoot;                 // moment of the next shoot
    
    public Enemy3(Long currentTime){
        super(Main.ACTIVE, Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.10, 3 * Math.PI / 2, 0.0);
        this.nextShoot = currentTime + 500;
        this.setRadius(11.0);
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
			
			GameLib.setColor(Color.PINK);
			GameLib.drawSquare(this.getX(), this.getY(), this.getRadius());
		}
    }

    /* getters and setters */
    public long getNextShoot() {
        return nextShoot;
    }
    public void setNextShoot(long nextShoot) {
        this.nextShoot = nextShoot;
    }
}
