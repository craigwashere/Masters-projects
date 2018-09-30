package game.enemy;
import game.Animation;
import game.Frame;
import game.GameWorld;
import game.Projectile;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/*
 * The boss for the zelda dungeon level
 */
public class AquamentusMonster extends Enemy {

	/* the only animation this monster has */
	private Animation movingAnimation;

	/*
	 * creates AquamentusMonster with initial health of 15 and
	 * a maximum health of 15.
	 * 
	 * @param	gameWorld	the reference to the GameWorld object
	 */
	public AquamentusMonster(GameWorld gameWorld) {
		super(gameWorld);
		initResources();
		setMaximumHealth(15);
		setCurrentHealth(15);
	}

	/*
	 * - creates the animation for aquamentus
	 * - defines the bounding box(hitbox) for aquamentus
	 */
	private void initResources() {
		// get the images and create the animation
		try {
			Frame[] frames = new Frame[] {
					new Frame(new Image("images/aquamentus_monster_0.png"), 80),
					new Frame(new Image("images/aquamentus_monster_1.png"), 80),
					new Frame(new Image("images/aquamentus_monster_2.png"), 80),
					new Frame(new Image("images/aquamentus_monster_3.png"), 80),

			};
			movingAnimation = new Animation(frames, true);
			setCurrentAnimation(movingAnimation);

		} catch (SlickException e) {
			
			e.printStackTrace();
		}

		/* set the bounding box of this enemy */
		Rectangle r = new Rectangle(400, 400, movingAnimation.getSprite()
				.getWidth(), movingAnimation.getSprite().getHeight());
		
		setBoundingBox(r);
		
	}

	/* 
	 * - Updates the animation for this monster
	 * - Updates the enemy projectile's animation and position
	 * (non-Javadoc)
	 * @see Enemy#update(org.newdawn.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta)
			throws SlickException {
		movingAnimation.update(delta);
		
		Iterator<Projectile> iter = getGameWorld().getPlayer().getProjectiles().iterator();
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
		
		//TODO: move aquamentus around the screen
	}

	/*
	 * Renders aquamentus to the screen 
	 *
	 * (non-Javadoc)
	 * @see Enemy#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.drawImage(movingAnimation.getSprite(), getBoundingBox().getX(), getBoundingBox().getY());
		g.draw(getBoundingBox());
		g.drawString("H:" + getCurrentHealth() + "/" + getMaximumHealth(), getBoundingBox().getX(), getBoundingBox().getY());
		
	}

	@Override
	public void fireProjectile() {
		
	}

	/*
	 * Returns the amount of melee damage done by this enemy
	 * (non-Javadoc)
	 * @see Enemy#getMeleeDamage()
	 */
	@Override
	public float getMeleeDamage() {
		return 2;
	}
	
	/*
	 * Returns the amount of ranged damage done by this enemy(non-Javadoc)
	 * @see Enemy#getRangedDamage()
	 */
	@Override
	public int getRangedDamage() {
		return 1;
	}

}
