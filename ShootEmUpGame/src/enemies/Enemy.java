package enemies;

import game.GameObject;
import game.Main;

public class Enemy extends GameObject{
	private double V;					            // speed
	private double angle;				            // angle (indicates direction of movement)
	private double RV;					            // rotation speed
	private double explosion_start;		            // moment of the explosion's onset
	private double explosion_end;		            //moment of the explosion's end

    public Enemy(int state, double X, double Y, double V, double angle, double RV){
        this.setX(X);
        this.setY(Y);
        this.V = V;
        this.angle = angle;
        this.RV = RV;
        this.setState(state);
    }

    /* method to handle collisions */
    public void handleCollisions(Long currentTime, GameObject obj){
        if(this.getState() == Main.ACTIVE){
					
            if (this.collidesWith(obj)) {
                this.setState(Main.EXPLODING);
                this.setExplosion_start(currentTime);
                this.setExplosion_end(currentTime + 500);
            }
        }
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
