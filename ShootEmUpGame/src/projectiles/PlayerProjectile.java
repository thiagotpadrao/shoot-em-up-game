package projectiles;

import java.awt.Color;

import game.Main;
import lib.GameLib;

public class PlayerProjectile extends Projectile{

    public PlayerProjectile(double X, double Y){
        super(X, Y, 0.0, -1.0);
    }

    /* method to draw the player's projectile */
    public void draw(){
        if(this.getState() == Main.ACTIVE){
					
			GameLib.setColor(Color.GREEN);
			GameLib.drawLine(this.getX(), this.getY() - 5, this.getX(), this.getY() + 5);
			GameLib.drawLine(this.getX() - 1, this.getY() - 3, this.getX() - 1, this.getY() + 3);
			GameLib.drawLine(this.getX() + 1, this.getY() - 3, this.getX() + 1, this.getY() + 3);
		}
    }
}
