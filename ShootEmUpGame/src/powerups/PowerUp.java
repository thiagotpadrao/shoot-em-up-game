package powerups;

import java.awt.Color;

import game.GameObject;
import game.Main;
import lib.GameLib;

public class PowerUp extends GameObject{
    private double V;					            // speed
	private double angle;				            // angle (indicates direction of movement)
	private double RV;					            // rotation speed
	private double explosion_start;		            // instant of the beginning of the explosion
	private double explosion_end;		            // instant of the end of the explosion

    public PowerUp(int state, double X, double Y, double V, double angle, double RV, double radius){
        this.setX(X);
        this.setY(Y);
        this.setState(state);
        this.setRadius(radius);
        this.V = V;
        this.angle = angle;
        this.RV = RV;
    }

/* method for drawing the powerup */
    public void draw(Long currentTime){
        
        /* if the state is EXPLODING draw the explosion */
        if(this.getState() == Main.EXPLODING){
					
			double alpha = (currentTime - this.getExplosion_start()) / (this.getExplosion_end() - this.getExplosion_start());
			GameLib.drawExplosion(this.getX(), this.getY(), alpha);
		}
		
        /* if the state is ACTIVE, draw the powerup */
		if(this.getState() == Main.ACTIVE){
			
			GameLib.setColor(Color.YELLOW);
			GameLib.drawStar(this.getX(), this.getY(), this.getRadius());
		}
    }

    /* method for handle collisions */
    public boolean handleCollisions(Long currentTime, GameObject obj){
        if(this.getState() == Main.ACTIVE){
					
            if (this.collidesWith(obj)) {
                this.setState(Main.EXPLODING);
                this.setExplosion_start(currentTime);
                this.setExplosion_end(currentTime + 300);
                return true;
            }
        }
    return false;
    }

    /* getters and setters */
    public void setV(double v) {
        V = v;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void setRV(double rV) {
        RV = rV;
    }
    public void setExplosion_start(double explosion_start) {
        this.explosion_start = explosion_start;
    }
    public void setExplosion_end(double explosion_end) {
        this.explosion_end = explosion_end;
    }
    public double getV() {
        return V;
    }
    public double getAngle() {
        return angle;
    }
    public double getRV() {
        return RV;
    }
    public double getExplosion_start() {
        return explosion_start;
    }
    public double getExplosion_end() {
        return explosion_end;
    }
}
