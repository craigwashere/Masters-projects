package game.enemy;
import game.Animation;
import game.GameWorld;
import game.Projectile;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/* used to generalize an Enemy entity */
/* Extend this class with your own enemy */
/* add enemies to appropriate Room classes */
public abstract class Enemy {

	/* everything drawn should have a boundingBox */
	/* Used for hit detection */
	private Rectangle boundingBox;

	/* how fast to move the image */
	private float moveSpeed = 0.25f;

	/* determines difficulty (not currently used) */
	private float difficulty;

	/* a reference to gameworld if needed */
	private GameWorld gameWorld;
	
	/* the current animation of this enemey */
	private Animation currentAnimation;
	
	/* a list of active projectiles this enemy has launched */
	private ArrayList<Projectile> projectiles;
	
	/* The current and maximum health variables */
	private int maximumHealth, currentHealth;

	public Enemy(GameWorld gameWorld) {
		this.setGameWorld(gameWorld);
		difficulty = 1.0f;
		setProjectiles(new ArrayList<Projectile>());
	}

	/* updates enemies logic */
	public abstract void update(GameContainer container, int delta)
			throws SlickException ;

	/* renders enemy and enemy sprites to the canvas */
	public abstract void render(GameContainer container, Graphics g)
			throws SlickException ;

	/* adds a project to this enemy's list of projectiles */
	public abstract void fireProjectile();
	
	/* Getter and Setter methods follow */

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public float getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(float difficulty) {
		this.difficulty = difficulty;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(Animation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

	/*
	 * Returns the amount of melee damage done by this enemy
	 */
	public abstract float getMeleeDamage() ;

	/*
	 * Returns the amount of ranged damage done by this enemy
	 */
	public abstract int getRangedDamage();

	/*
	 * Returns the maximum amount of health this enemy has 
	 */
	public int getMaximumHealth() {
		return maximumHealth;
	}

	/*
	 * Sets the maximum health of aquamentus
	 */
	public void setMaximumHealth(int maximumHealth) {
		this.maximumHealth = maximumHealth;
	}

	/*
	 * Returns the current health this enemy has
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/*
	 * Sets the current health of aquamentus
	 */
	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

}
