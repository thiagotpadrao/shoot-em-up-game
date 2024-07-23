package projectiles;

import java.awt.Color;

import game.Main;
import lib.GameLib;

public class EnemyProjectile extends Projectile{

    public EnemyProjectile(double X, double Y, double VX, double VY){
        super(X, Y, VX, VY);
        this.setRadius(2.0);
    }

    /* method for drawing the enemy's projectile */
    public void draw(){
        if(this.getState() == Main.ACTIVE){
	
			GameLib.setColor(Color.RED);
			GameLib.drawCircle(this.getX(), this.getY(), this.getRadius());
		}
    }
}
