package game;

import java.awt.Color;
import java.util.LinkedList;

import background.Star;
import enemies.Enemy1;
import enemies.Enemy2;
import enemies.Enemy3;
import lib.GameLib;
import player.Life;
import player.Player;
import powerups.PowerUp;
import projectiles.EnemyProjectile;
import projectiles.PlayerProjectile;

public class Main {
	
	/* Constants related to the states that game elements 
	(player, projectiles, or enemies) can assume. */

	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	public static final int FLASHING = 3;
	
	/* Method that waits, doing nothing, until the current time is */
	/* greater than or equal to the time specified in the "time" parameter. */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
	/* Main method */
	
	public static void main(String [] args){

		/* Indicates that the game is running */
		boolean running = true;

		/* variables used for time control in the main loop */
		long delta;
		long currentTime = System.currentTimeMillis();

		/* variable that stores the number of lives */
		int lifes = 3;

		/*******************/
		/* initializations */
		/*******************/

		/* instance of a class used for functions in main */
		Control control = new Control(currentTime);         
		
		/* LinkedList to store the player's projectiles */
		LinkedList<PlayerProjectile> PProjectileList = new LinkedList<PlayerProjectile>();

		/* LinkedList to store enemy projectiles */
		LinkedList<EnemyProjectile> EProjectileList = new LinkedList<EnemyProjectile>();
		
		/* LinkedList to store type 1 enemies */
		LinkedList<Enemy1> Enemy1List = new LinkedList<Enemy1>();

		/* LinkedList to store type 2 enemies */
		LinkedList<Enemy2> Enemy2List = new LinkedList<Enemy2>();

		/* LinkedList to store type 3 enemies */
		LinkedList<Enemy3> Enemy3List = new LinkedList<Enemy3>();

		/* LinkedList to store powerups */
		LinkedList<PowerUp> PowerUpList = new LinkedList<PowerUp>();

		/* LinkedList to store lives */
		LinkedList<Life> LifeList = new LinkedList<Life>();
		
		/* player */
		Player player = new Player(currentTime, lifes, LifeList);

		/* stars that form the foreground background */
		Star star1 = new Star(20, 20, 0.070, Color.GRAY, 3);

		/* stars that form the background background */
		Star star2 = new Star(50, 50, 0.045, Color.DARK_GRAY, 2);
						
		/* starting graphical interface: */
		
		GameLib.initGraphics();
		
		/*************************************************************************************************/
		/*                                                                                               */
		/* game main loop                                                                                */
		/*                                                                                               */
		/* The game's main loop performs the following operations:                                       */
		/*                                                                                               */
		/* 1) Checks for collisions and updates element states as necessary.           					 */
		/*                                                                                               */
		/* 2) Updates element states based on the time since the last update 							 */
		/* and in the current timestamp: position and orientation, projectile firing execution, etc.     */
		/*                                                                                               */
		/* 3) Processes user input (keyboard) and updates player states as needed. 						 */
		/*                                                                                               */
		/* 4) Draw the scene, based on the states of the elements.                                       */
		/*                                                                                               */
		/* 5) Wait a period of time (so that delta is approximately always constant).      				 */
		/*                                                                                               */
		/*************************************************************************************************/
		
		while(running){
		
			/* Used to update the state of game elements */
			/* (player, projectiles and enemies) "delta" indicates how many */
			/* ms have passed since the last update. */
			delta = System.currentTimeMillis() - currentTime;
			
			/* The variable "currentTime" gives us the current timestamp. */
			currentTime = System.currentTimeMillis();
			
			/*******************/
			/* Collision Check */
			/*******************/
			
			/* player collision with enemy projectiles */
			for (EnemyProjectile ep : EProjectileList) {
				player.handleCollisions(currentTime, ep);
			}

			/* player collision with enemies 1 */
			for (Enemy1 e1 : Enemy1List) {
				player.handleCollisions(currentTime, e1);
			}

			/* player collision with enemies 2 */
			for (Enemy2 e2 : Enemy2List) {
				player.handleCollisions(currentTime, e2);
			}

			/* player collision with enemies 3 */
			for (Enemy3 e3 : Enemy3List) {
				player.handleCollisions(currentTime, e3);
			}

			/* collision of the powerup with the player */
			for(PowerUp pu : PowerUpList){					
				if (pu.handleCollisions(currentTime, player)){
					player.setPowerUpOn(true);
					player.setPowerUpTime(currentTime + 15000);
				} 
			}
			
			/* enemy collisions with the player's projectiles */
			
			for(PlayerProjectile p : PProjectileList){
				
				for(Enemy1 e1 : Enemy1List){					
					e1.handleCollisions(currentTime, p);
				}
				
				for(Enemy2 e2 : Enemy2List){
					e2.handleCollisions(currentTime, p);
				}

				for(Enemy3 e3 : Enemy3List){
					e3.handleCollisions(currentTime, p);
				}
			}
				
			/******************/
			/* Status updates */
			/******************/
			
			/* Player projectiles */
			control.statusProjeteisP(delta, PProjectileList);

			/* Enemy projectiles */
			control.statusProjeteisE(delta, EProjectileList);
			
			/* enemies 1 */
			control.statusInimigos1(currentTime, delta, Enemy1List, EProjectileList, player);
			
			/* enemies 2 */
			control.statusInimigos2(currentTime, delta, Enemy2List, EProjectileList);
			
			/* enemies 3 */
			control.statusInimigos3(currentTime, delta, Enemy3List, EProjectileList, player);

			/* power up */
			control.statusPowerUp(currentTime, delta, PowerUpList);
			
			/* player */
			if(player.status(currentTime, delta, PProjectileList) == false) running = false; //if the lives are over, end the game

			/* checking if new enemies (type 1) should be "released" */
			control.throwNewEnemy1(currentTime, Enemy1List);

			/* checking if new enemies (type 2) should be "released" */
			control.throwNewEnemy2(currentTime, Enemy2List);

			/* checking if new enemies (type 3) should be "released" */
			control.throwNewEnemy3(currentTime, Enemy3List);

			/* checking if new power up should be "released" */
			control.throwNewPowerUp(currentTime, PowerUpList);

			/****************/
			/* Scene design */
			/****************/
			
			/* drawing background nearby */
			star1.draw(delta);

			/* drawing distant background */
			star2.draw(delta);
						
			/* drawing player */
			player.draw(currentTime);
			
			/* drawing player projectiles */
			for(PlayerProjectile p : PProjectileList){
				p.draw();
			}
			
			/* drawing enemy projectiles */
			for(EnemyProjectile ep : EProjectileList){
				ep.draw();
			}
			
			/* drawing enemies (type 1) */
			for(Enemy1 e1 : Enemy1List){
				e1.draw(currentTime);
			}
			
			/* drawing enemies (type 2) */
			for(Enemy2 e2 : Enemy2List){
				e2.draw(currentTime);
			}

			/* drawing enemies (type 3) */
			for(Enemy3 e3 : Enemy3List){
				e3.draw(currentTime);
			}

			/* drawing power up */
			for(PowerUp pu : PowerUpList){
				pu.draw(currentTime);
			}

			/* drawing lives */
			for(Life l : LifeList){
				l.draw();
			}
			
			/* calling display() from the GameLib class updates the design displayed by the game interface. */
			GameLib.display();
			
			/* if the ESC key is used, the game ends */
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;

			/* pauses so that each execution of the main loop takes approximately 5 ms. */
			busyWait(currentTime + 5);
		}
		
		System.exit(0);
	}
}
