package game;
import org.newdawn.slick.geom.Vector2f;

/*
 * Creates a projectile that can be fired from an enemy or a player
 */
public class Projectile {

	// the x and y position of this projectile
	private float x, y;

	// the direction this projectile is traveling
	private Vector2f direction;
	
	//the speed this projectile is moving
	private float speed;

	// the animation of this projectile
	private Animation animation;
	
	// the time at which this projectile expires
	private long dieTime;
	
	// the time this projectile has been alive
	private long aliveTime;
	
	// state of projectile
	private boolean isAlive;

	/*
	 * Creates a projectile with an specified speed, expiration time, and animation
	 */
	public Projectile(Vector2f start, Vector2f end, float speed, long dieTime, Animation animation) {
		x = start.getX();
		y = start.getY();
		direction = end.sub(start);
		direction.normalise();
		this.speed = speed;
		this.animation = animation;
		this.setDieTime(dieTime);
		this.aliveTime = 0;
		isAlive = true;
		animation.start();
		
	}

	/*
	 * Update this projectiles alive time and coordinates
	 * Also update the animation for this projectile
	 */
	public void update(int delta) {
		aliveTime += delta;
		if(aliveTime >= getDieTime()) {
			animation.stop();
			isAlive = false;
		}
		
		x += delta * speed * direction.getX();
		y += delta * speed * direction.getY();
		
		animation.update(delta);
		
		
	}

	/*
	 * Return the x coordinate of this projectile
	 */
	public float getX() {
		return x;
	}

	/*
	 * Sets the x coordinate of this projectile
	 */
	public void setX(float x) {
		this.x = x;
	}

	/*
	 * Return the y coordinate of this projectile
	 */
	public float getY() {
		return y;
	}

	/*
	 * Sets the y coordinate of this projectile
	 */
	public void setY(float y) {
		this.y = y;
	}

	/*
	 * Returns the speed of this projectile
	 */
	public float getSpeed() {
		return speed;
	}

	/*
	 * Sets the speed of this projectile
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/*
	 * Returns the animation of this projectile
	 */
	public Animation getAnimation() {
		return animation;
	}

	/*
	 * Sets the animation of this projectile
	 */
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	/*
	 * Returns the time at which this projectile expires
	 */
	public long getDieTime() {
		return dieTime;
	}

	/*
	 * Set the time at which this projectile expires
	 */
	public void setDieTime(long dieTime) {
		this.dieTime = dieTime;
	}

	/*
	 * Returns true if this projectile has not expired
	 */
	public boolean isAlive() {
		return isAlive;
	}

	/*
	 * sets this projectile as alive or not
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

}
