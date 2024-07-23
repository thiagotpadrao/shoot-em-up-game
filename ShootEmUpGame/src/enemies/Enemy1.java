package enemies;

import java.awt.Color;

import game.Main;
import lib.GameLib;

public class Enemy1 extends Enemy{
	private long nextShoot;                 // moment of the next shoot
	
    public Enemy1(Long currentTime){
        super(Main.ACTIVE, Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 3 * Math.PI / 2, 0.0);
        this.nextShoot = currentTime + 500;
        this.setRadius(9.0);
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
			
			GameLib.setColor(Color.CYAN);
			GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
		}
    }
    
    /* getters and setters */
    public void setNextShoot(long nextShoot) {
        this.nextShoot = nextShoot;
    }
    public long getNextShoot() {
        return nextShoot;
    }
}
