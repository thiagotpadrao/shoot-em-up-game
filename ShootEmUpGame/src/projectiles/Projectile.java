package projectiles;

import game.GameObject;
import game.Main;

public class Projectile extends GameObject{
	private double VX;				                // speed on x axis
	private double VY;				                // speed on y axis

    public Projectile(double X, double Y, double VX, double VY){
        this.setX(X);
        this.setY(Y);
        this.VX = VX;
        this.VY = VY;
        this.setState(Main.ACTIVE);
    }

     /* getters and setters */
    public void setVX(double vX) {
        VX = vX;
    }
    public void setVY(double vY) {
        VY = vY;
    }
    public double getVX() {
        return VX;
    }
    public double getVY() {
        return VY;
    }
}

