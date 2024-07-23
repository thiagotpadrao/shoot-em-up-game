package game;

import java.util.Iterator;
import java.util.LinkedList;

import enemies.Enemy1;
import enemies.Enemy2;
import enemies.Enemy3;
import lib.GameLib;
import player.Player;
import powerups.PowerUp;
import projectiles.EnemyProjectile;
import projectiles.PlayerProjectile;


public class Control {
    /* control attributes used by enemy 1 */
	private Long nextEnemy1;					// instant at which a new enemy 1 should appear
	
	/* control attributes used by enemy 2 */
	private Long nextEnemy2;					// instant at which a new enemy 2 should appear
	private double spawnX;						// x-coordinate of the next type 2 enemy to appear
	private int count;							// type 2 enemy count (used in "flight formation")

	/* control attributes used by enemy 3 */
	private Long nextEnemy3;					// instant at which a new enemy 3 should appear

	/* control attributes used by power up */
	private Long nextPowerUp;					// instant at which a new power up should appear

	public Control(Long currentTime){
		this.nextEnemy1 = currentTime + 2000;   // defines the initial appearance time of the first enemy 1
		this.nextEnemy2 = currentTime + 7000;	// defines the initial appearance time of the first enemy 2
		this.nextEnemy3 = currentTime + 5000; 	// defines the initial appearance time of the first enemy 3
		this.nextPowerUp = currentTime + 4000; 	// defines the initial appearance time of the first power up
		this.spawnX = GameLib.WIDTH * 0.20;		// defines the first spawn location of the first enemy 2
		this.count = 0;							// initializes enemy2 count to 0
	}
    
	/*************************************************************************/
	/* methods for updating the states of game elements that use arraylists: */
    /*************************************************************************/

	/* method to update the states of the player's projectiles */
	public void statusProjeteisP(Long delta, LinkedList<PlayerProjectile> PProjectilesList) {
		Iterator<PlayerProjectile> iterator = PProjectilesList.iterator();
		while (iterator.hasNext()) {
			PlayerProjectile p = iterator.next();
	
			if (p.getState() == Main.ACTIVE) {
				// Checking if the projectile left the screen
				if (p.getY() < 0) {
					iterator.remove(); // Remove projectile from the list using Iterator
				} else {
					// Updates projectile positions
					p.setX(p.getX() + p.getVX() * delta);
					p.setY(p.getY() + p.getVY() * delta);
				}
			}
		}
	}
	
	/* method to update enemy projectile states */
    public void statusProjeteisE(Long delta, LinkedList<EnemyProjectile> EProjectilesList) {
		Iterator<EnemyProjectile> iterator = EProjectilesList.iterator();
		while (iterator.hasNext()) {
			EnemyProjectile ep = iterator.next();
	
			if (ep.getState() == Main.ACTIVE) {
				// Checking if the projectile left the screen
				if (ep.getY() > GameLib.HEIGHT) {
					iterator.remove(); // Remove projectile from the list using Iterator
				} else {
					// Updates projectile positions
					ep.setX(ep.getX() + ep.getVX() * delta);
					ep.setY(ep.getY() + ep.getVY() * delta);
				}
			}
		}
	}
	
	/* method to update the states of type 1 enemies */
	public void statusInimigos1(Long currentTime, Long delta, LinkedList<Enemy1> Enemy1List, LinkedList<EnemyProjectile> EProjectileList, Player player) {
		Iterator<Enemy1> iterator = Enemy1List.iterator();
		while (iterator.hasNext()) {
			Enemy1 e1 = iterator.next();
	
			if (e1.getState() == Main.EXPLODING) {
				// If the enemy's status is EXPLODING, check when the explosion has ended
				if (currentTime > e1.getExplosion_end()) {
					iterator.remove(); // Remove enemy from list using Iterator
				}
			}
	
			if (e1.getState() == Main.ACTIVE) {
				// Checking if the enemy has left the screen
				if (e1.getY() > GameLib.HEIGHT + 10) {
					iterator.remove(); // Remove enemy from list using Iterator
				} else {
					// Updates enemy positions
					e1.setX(e1.getX() + e1.getV() * Math.cos(e1.getAngle()) * delta);
					e1.setY(e1.getY() + e1.getV() * Math.sin(e1.getAngle()) * delta * (-1.0));
					e1.setAngle(e1.getAngle() + e1.getRV() * delta);
	
					// If the time to shoot has come, create a new projectile
					if (currentTime > e1.getNextShoot() && e1.getY() < player.getY()) {
						EnemyProjectile newEProjectile = new EnemyProjectile(e1.getX(), e1.getY(), Math.cos(e1.getAngle()) * 0.45, Math.sin(e1.getAngle()) * 0.45 * (-1.0));
						EProjectileList.add(newEProjectile);
						e1.setNextShoot((long) (currentTime + 200 + Math.random() * 500));
					}
				}
			}
		}
	}
	
	/* method to update the states of type 2 enemies */
	public void statusInimigos2(Long currentTime, Long delta, LinkedList<Enemy2> Enemy2List, LinkedList<EnemyProjectile> EProjectileList) {
		Iterator<Enemy2> iterator = Enemy2List.iterator();
		while (iterator.hasNext()) {
			Enemy2 e2 = iterator.next();
	
			if (e2.getState() == Main.EXPLODING) {
				// If the enemy's status is EXPLODING, check when the explosion has ended
				if (currentTime > e2.getExplosion_end()) {
					iterator.remove(); // Remove enemy from list using Iterator
				}
			}
	
			if (e2.getState() == Main.ACTIVE) {
				// Checking if the enemy has left the screen
				if (e2.getX() < -10 || e2.getX() > GameLib.WIDTH + 10) {
					iterator.remove(); // Remove enemy from list using Iterator
				} else {
					boolean shootNow = false;
					double previousY = e2.getY();
	
					// Updates enemy positions
					e2.setX(e2.getX() + e2.getV() * Math.cos(e2.getAngle()) * delta);
					e2.setY(e2.getY() + e2.getV() * Math.sin(e2.getAngle()) * delta * (-1.0));
					e2.setAngle(e2.getAngle() + e2.getRV() * delta);
	
					double threshold = GameLib.HEIGHT * 0.30;
					if (previousY < threshold && e2.getY() >= threshold) {
						if (e2.getX() < GameLib.WIDTH / 2) e2.setRV(0.003);
						else e2.setRV(-0.003);
					}
	
					if (e2.getRV() > 0 && Math.abs(e2.getAngle() - 3 * Math.PI) < 0.05) {
						e2.setRV(0.0);
						e2.setAngle(3 * Math.PI);
						shootNow = true;
					}
	
					if (e2.getRV() < 0 && Math.abs(e2.getAngle()) < 0.05) {
						e2.setRV(0.0);
						e2.setAngle(0.0);
						shootNow = true;
					}
	
					/* If the time to shoot has come, create new projectiles */
					if (shootNow) {
						double[] angles = {Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8};
						for (int k = 0; k < 3; k++) {
							double a = angles[k] + Math.random() * Math.PI / 6 - Math.PI / 12;
							double vx = Math.cos(a);
							double vy = Math.sin(a);
							EnemyProjectile newEProjectile = new EnemyProjectile(e2.getX(), e2.getY(), vx * 0.30, vy * 0.30);
							EProjectileList.add(newEProjectile);
						}
					}
				}
			}
		}
	}
	
	/* method to update the states of type 3 enemies */
	public void statusInimigos3(Long currentTime, Long delta, LinkedList<Enemy3> Enemy3List, LinkedList<EnemyProjectile> EProjectileList, Player player) {
		Iterator<Enemy3> iterator = Enemy3List.iterator();
		while (iterator.hasNext()) {
			Enemy3 e3 = iterator.next();
	
			if (e3.getState() == Main.EXPLODING) {
				// If the enemy's status is EXPLODING, check when the explosion has ended
				if (currentTime > e3.getExplosion_end()) {
					iterator.remove(); // Remove enemy from list using Iterator
				}
			}
	
			if (e3.getState() == Main.ACTIVE) {
				// Checking if the enemy has left the screen
				if (e3.getY() > GameLib.HEIGHT + 10) {
					iterator.remove(); // Remove enemy from list using Iterator
				} else {
					// Updates enemy positions
					e3.setX(e3.getX() + e3.getV() * Math.cos(e3.getAngle()) * delta);
					e3.setY(e3.getY() + e3.getV() * Math.sin(e3.getAngle()) * delta * (-1.0));
					e3.setAngle(e3.getAngle() + e3.getRV() * delta);
	
					// If the time to shoot has come, create new projectiles
					if (currentTime > e3.getNextShoot() && e3.getY() < player.getY()) {
						
						/* defining the shooting angles */
						double angles [] = new double [16];
						for(int i = 0; i < angles.length; i++){
							angles[i] = i*(Math.PI / 8);
						}
						
						/* shooting */
						for (int k = 0; k < angles.length; k++) {
							double a = angles[k];
							double vx = Math.cos(a);
							double vy = Math.sin(a);
							EnemyProjectile newEProjectile = new EnemyProjectile(e3.getX(), e3.getY(), vx * 0.20, vy * 0.20);
							EProjectileList.add(newEProjectile);
						}
						e3.setNextShoot((long) (currentTime + 2000));
					}
				}
			}
		}
	}

	/* method for updating power up states */
	public void statusPowerUp(Long currentTime, Long delta, LinkedList<PowerUp> PowerUpList) {
		Iterator<PowerUp> iterator = PowerUpList.iterator();
		while (iterator.hasNext()) {
			PowerUp pu = iterator.next();
	
			if (pu.getState() == Main.EXPLODING) {
				// If the powerup status is "exploding", check when the explosion is over
				if (currentTime > pu.getExplosion_end()) {
					iterator.remove(); // Remove powerup from the list using Iterator
				}
			}
	
			if (pu.getState() == Main.ACTIVE) {
				// Checking if the powerup has left the screen
				if (pu.getY() > GameLib.HEIGHT + 10) {
					iterator.remove(); // Remove powerup from the list using Iterator
				} else {
					// Updates powerup positions
					pu.setX(pu.getX() + pu.getV() * Math.cos(pu.getAngle()) * delta);
					pu.setY(pu.getY() + pu.getV() * Math.sin(pu.getAngle()) * delta * (-1.0));
					pu.setAngle(pu.getAngle() + pu.getRV() * delta);
				}
			}
		}
	}

	/* method to spawn type 1 enemies */
	public void throwNewEnemy1(Long currentTime, LinkedList<Enemy1> Enemy1List){
		
		if(currentTime > nextEnemy1){
				
			Enemy1 newEnemy1 = new Enemy1(currentTime + 500);
			Enemy1List.add(newEnemy1);
				
			nextEnemy1 = currentTime + 500;
		}
	}

	/* method to spawn type 2 enemies */
	public void throwNewEnemy2(Long currentTime, LinkedList<Enemy2> Enemy2List){
		if(currentTime > nextEnemy2){
				
			Enemy2 newEnemy2 = new Enemy2(spawnX);					
			Enemy2List.add(newEnemy2);
			count++;
				
			if(count < 10){
				nextEnemy2 = currentTime + 120;
			}
			else {
				count = 0;
				spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
				nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
			}	
		}
	}

	/* method to spawn type 3 enemies */
	public void throwNewEnemy3(Long currentTime, LinkedList<Enemy3> Enemy3List){
		
		if(currentTime > nextEnemy3){
				
			Enemy3 newEnemy3 = new Enemy3(currentTime + 500);
			Enemy3List.add(newEnemy3);
				
			nextEnemy3 = (long) (currentTime + 5000 + Math.random() * 3000);
		}
	}

	/* method to spawn power ups */
	public void throwNewPowerUp(Long currentTime, LinkedList<PowerUp> PowerUpList){
		
		if(currentTime > nextPowerUp){
				
			PowerUp newPowerUp = new PowerUp(Main.ACTIVE, Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.15, 3 * Math.PI / 2, 0.0, 10.0);
			PowerUpList.add(newPowerUp);
				
			nextPowerUp = (long) (currentTime + 25000 + Math.random() * 5000);
		}
	}
}
