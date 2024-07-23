package player;

import java.awt.Color;
import java.util.LinkedList;

import game.GameObject;
import game.Main;
import lib.GameLib;
import projectiles.PlayerProjectile;

public class Player extends GameObject{
	private double VX = 0.25;						// speed on x axis
	private double VY = 0.25;						// speed on y axis
	private double explosion_start = 0;				// moment of the beginning of the explosion
	private double explosion_end = 0;				// instant of the end of the explosion
	private long nextShot;				            // time for the next shot
    private boolean PowerUpOn = false;              // variable that says whether the powerup is active in the player
    private double PowerUpTime;                     // time that the powerup is active in the player
    private int lives;                              // number of player lives
    private LinkedList<Life> LifeList;              // lives LinkedList
    private double flash_end = 0;                   // moment of the end of the flash
    private boolean isBlue = false;                 // Boolean variable to tell player color during flash
    private Long lastToggleTime = (long) 0.0;       // time control during flash

    public Player(Long currentTime, int lives, LinkedList<Life> LifeList){
        this.setX(GameLib.WIDTH / 2);
        this.setY(GameLib.HEIGHT * 0.90);
        this.setRadius(12.0);
        this.nextShot = currentTime;
        this.setState(Main.ACTIVE);
        this.lives = lives;
        this.LifeList = LifeList;

        /* lives initialization */
		initializeLives();
    }

    /* method that initializes lives in the LinkedList of lives */
    public void initializeLives(){
        int space = 30;

		for(int l = this.lives; l > 0; l--){
			Life newLife = new Life(Main.ACTIVE, GameLib.WIDTH - space, GameLib.HEIGHT * 0.90);
			this.LifeList.add(newLife);
			space += 30;
		}
    }

    /* method for handle collisions */
    public void handleCollisions(Long currentTime, GameObject obj) {
        if (this.getState() == Main.ACTIVE) {
            
            if (this.collidesWith(obj)) {

                if(LifeList.size() > 0){ //if there are still lives, flash it
                    this.LifeList.removeLast();
                    this.setState(Main.FLASHING);
                    this.setFlash_end(currentTime + 2500);
                    this.PowerUpOn = false;
                }
                else{ //if there are no more lives it explodes
                    this.setState(Main.EXPLODING);
                    this.setExplosion_start(currentTime);
                    this.setExplosion_end(currentTime + 2000);
                    this.PowerUpOn = false;
                }   
            }   
        }
    }

    /* method that updates the player's states, including their movement */
    public boolean status(Long currentTime, Long delta, LinkedList<PlayerProjectile> PProjectileList){
        
         /* updating powerup state with powerupTime */
         if (currentTime > PowerUpTime) this.PowerUpOn = false;

        /* Checking if the player's explosion has already ended. */
        if(this.getState() == Main.EXPLODING){
				
			if(currentTime > this.getExplosion_end()){
				this.setState(Main.INACTIVE);
				return false; // when the player's explosion ends, it returns false to end the game
			}
		}

        /* Checking if the player's flash has finished. */
        if(this.getState() == Main.FLASHING){
        
            if(currentTime >= this.getFlash_end()){
                this.setState(Main.ACTIVE); // At the end of the flash, the player becomes active again
            }

            /* checking user input to define the player's position */
            /* movement */
			if(GameLib.iskeyPressed(GameLib.KEY_UP)) this.setY(this.getY() - delta * this.getVY());
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.setY(this.getY() + delta * this.getVY());
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.setX(this.getX() - delta * this.getVX());
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.setX(this.getX() + delta * this.getVY());

            /* Checking if the player's coordinates are still in */
			/* from the game screen after processing user input.       */
			if(this.getX() < 0.0) this.setX(0.0);
			if(this.getX() >= GameLib.WIDTH) this.setX(GameLib.WIDTH - 1);
			if(this.getY() < 25.0) this.setY(25.0);
			if(this.getY() >= GameLib.HEIGHT) this.setY(GameLib.HEIGHT - 1);
        }

        /* checking if player is active */
        if(this.getState() == Main.ACTIVE){
			
            /* checking user input to define the player's position */
            /* movement */
			if(GameLib.iskeyPressed(GameLib.KEY_UP)) this.setY(this.getY() - delta * this.getVY());
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.setY(this.getY() + delta * this.getVY());
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.setX(this.getX() - delta * this.getVX());
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.setX(this.getX() + delta * this.getVY());
			
            /* checks if the Control key is active */
            if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
				/* if true, and it's time for the next shot, create a player projectile */
                if(currentTime > this.getNextShot()){
						
					PlayerProjectile newPProjectile = new PlayerProjectile(this.getX(), this.getY() - 2 * this.getRadius());
					PProjectileList.add(newPProjectile);
                    
                    if (PowerUpOn){ //if the powerup is active, creates two more projectiles
                        PlayerProjectile newPProjectile2 = new PlayerProjectile(this.getX() + 10, this.getY() - 2 * this.getRadius());
					    PProjectileList.add(newPProjectile2);
                        PlayerProjectile newPProjectile3 = new PlayerProjectile(this.getX() - 10, this.getY() - 2 * this.getRadius());
					    PProjectileList.add(newPProjectile3);

                    }
					
					this.setNextShot(currentTime + 100);	
				}	
			}

            /* Checking if the player's coordinates are still in */
			/* from the game screen after processing user input.       */
			if(this.getX() < 0.0) this.setX(0.0);
			if(this.getX() >= GameLib.WIDTH) this.setX(GameLib.WIDTH - 1);
			if(this.getY() < 25.0) this.setY(25.0);
			if(this.getY() >= GameLib.HEIGHT) this.setY(GameLib.HEIGHT - 1);
		}
    return true;
    }

    /* method for drawing the player */
    public void draw(Long currentTime){
        
        /* draws the explosion if the state is EXPLODING */
        if(this.getState() == Main.EXPLODING){
            double alpha = (currentTime - this.explosion_start) / (this.explosion_end - this.explosion_start);
            GameLib.drawExplosion(this.getX(), this.getY(), alpha);
            return;
        }
    
        /* draws the flash effect if the state is FLASHING */
        if (this.getState() == Main.FLASHING) {
           
            if(currentTime - lastToggleTime > 80){
                isBlue = !isBlue;
                lastToggleTime = currentTime;
            }
            
            if(isBlue) GameLib.setColor(Color.BLUE);
            else GameLib.setColor(Color.WHITE);
            GameLib.drawPlayer(this.getX(), this.getY(), this.getRadius());
            return;
        }
        
        /* draws the player if the state is ACTIVE */
        if(this.getState() == Main.ACTIVE){
            if(this.PowerUpOn) GameLib.setColor(Color.YELLOW); //yellow if powerup is active
            else GameLib.setColor(Color.BLUE);
            GameLib.drawPlayer(this.getX(), this.getY(), this.getRadius());
        }
    }
    
    /* getters and setters */
    public double getVX() {
        return VX;
    }
    public double getVY() {
        return VY;
    }
    public double getExplosion_start() {
        return explosion_start;
    }
    public double getExplosion_end() {
        return explosion_end;
    }
    public long getNextShot() {
        return nextShot;
    }
    public boolean getPowerUpOn() {
        return PowerUpOn;
    }
    public double getPowerUpTime() {
        return PowerUpTime;
    }
    public int getLives() {
        return lives;
    }
    public LinkedList<Life> getLifeList() {
        return LifeList;
    }
    public double getFlash_end() {
        return flash_end;
    }
    public void setVX(double vX) {
        VX = vX;
    }
    public void setVY(double vY) {
        VY = vY;
    }
    public void setExplosion_start(double explosion_start) {
        this.explosion_start = explosion_start;
    }
    public void setExplosion_end(double explosion_end) {
        this.explosion_end = explosion_end;
    }
    public void setNextShot(long nextShot) {
        this.nextShot = nextShot;
    }
    public void setPowerUpOn(boolean powerUpOn) {
        PowerUpOn = powerUpOn;
    }
    public void setPowerUpTime(double powerUpTime) {
        PowerUpTime = powerUpTime;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }
    public void setVidas(LinkedList<Life> LifeList) {
        this.LifeList = LifeList;
    }
    public void setFlash_end(double flash_end) {
        this.flash_end = flash_end;
    }
}
