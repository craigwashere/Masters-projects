package game.enemy;

import game.Animation;
import game.AnimationFactory;
import game.GameWorld;
import game.Projectile;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/*
 * An immobile KeeseMonster that fires projectiles towards the player
 */
public class KeeseMonster extends Enemy {
	
	public static int monsterNumber = 0;
	
	// This monster's ID
	private int monsterID ;

	/* the only animation this monster has */
	private Animation movingAnimation;
	
	// determines if this monster can fire another projectile yet
	private float longRangeDelta;
	
	// the time between projectile shots
	private static final long LONG_RANGE_WAIT = 700;

	/*
	 * Creates a KeeseMonster with a reference to the gameworld
	 * The keese monster starts with 3 health
	 * 
	 * @param	gameWorld	A gameworl reference object
	 */
	public KeeseMonster(GameWorld gameWorld) {
		super(gameWorld);
		initResources();
		monsterNumber++;
		monsterID = monsterNumber;
		setMaximumHealth(3);
		setCurrentHealth(3);
	}

	/*
	 * Initailizes any outside resources including animations.
	 * The boundingbox (hitbox) is also created
	 */
	private void initResources() {
		movingAnimation = AnimationFactory.getKeeseMonsterMovingAnimation();
		setCurrentAnimation(movingAnimation);
		movingAnimation.start();

		/* set the bounding box of this enemy */
		Rectangle r = new Rectangle(200, 200, movingAnimation.getSprite()
				.getWidth(), movingAnimation.getSprite().getHeight());
		
		setBoundingBox(r);
		
	}

	/* 
	 * The animation for this monster is updated
	 * Updates this monster's projectiles
	 * Determines if this monster has been hit by a player projectile
	 * (non-Javadoc)
	 * @see game.enemy.Enemy#update(org.newdawn.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta)
			throws SlickException {
		//update the moving animation
		movingAnimation.update(delta);
		
		// determine if this monster can fire another projectile
		longRangeDelta += delta;
		if(longRangeDelta > LONG_RANGE_WAIT) {
			//System.out.println("Enemy firing projectile");
			longRangeDelta = 0;
			fireProjectile();
		}
		
		// update all of this monster's projectiles
		Iterator<Projectile> iter = getProjectiles().iterator();
		while(iter.hasNext()) {
			Projectile p = iter.next();
			if(p.isAlive())
				p.update(delta);
			else
				iter.remove();
		}
		
		// determine if this monster has been hit by a player's projectiles
		iter = getGameWorld().getPlayer().getProjectiles().iterator();
		Projectile p;
		Rectangle playerProjectile;
		while(iter.hasNext()) {
			p = iter.next();
			playerProjectile = new Rectangle(
					p.getX(), 
					p.getY(), 
					p.getAnimation().getSprite().getWidth(), 
					p.getAnimation().getSprite().getWidth());
			if(getBoundingBox().intersects(playerProjectile)) {
				this.setCurrentHealth(this.getCurrentHealth() - getGameWorld().getPlayer().getRangedDamage());
				iter.remove();
			}
		}
	}

	/*
	 * Renders this monster and its projectiles to the canvas
	 * (non-Javadoc)
	 * @see game.enemy.Enemy#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.drawImage(movingAnimation.getSprite(), getBoundingBox().getX(), getBoundingBox().getY());
		g.draw(getBoundingBox());
		
		for(Projectile p: getProjectiles()) {
			g.drawImage(p.getAnimation().getSprite(), p.getX(), p.getY());
		}
		
		g.drawString("H:" + getCurrentHealth() + "/" + getMaximumHealth(), getBoundingBox().getX(), getBoundingBox().getY());
	}

	/*
	 * Fires a projectile from the monster's position to the player's position
	 * (non-Javadoc)
	 * @see game.enemy.Enemy#fireProjectile()
	 */
	@Override
	public void fireProjectile() {
		Vector2f targetVector = new Vector2f(getGameWorld().getPlayer().getBoundingBox().getCenter());

		Vector2f currLoc = new Vector2f(getBoundingBox().getCenter());
		currLoc.set(currLoc.getX(), currLoc.getY());

		addProjectile(currLoc, targetVector);
	}

	/*
	 * Adds a projectile to the list of this monster's projectiles
	 */
	private void addProjectile(Vector2f currLoc, Vector2f targetVector) {
		Animation redProjectile = AnimationFactory.getRedProjectileAnimation();
		redProjectile.start();
		getProjectiles().add(new Projectile(currLoc, targetVector, .45f, 2500L, redProjectile));
		
	}
	
	/*
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "KeeseMonster" + monsterID;
	}

	/*
	 * Returns the amount of melee damage given by this monster
	 * (non-Javadoc)
	 * @see game.enemy.Enemy#getMeleeDamage()
	 */
	@Override
	public float getMeleeDamage() {
		return 10;
	}
	
	/*
	 * Returns the amount of ranged damage given by this monster
	 * (non-Javadoc)
	 * @see game.enemy.Enemy#getRangedDamage()
	 */
	@Override
	public int getRangedDamage() {
		return 1;
	}

}
