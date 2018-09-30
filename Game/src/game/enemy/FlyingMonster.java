package game.enemy;
import game.Animation;
import game.AnimationFactory;
import game.GameWorld;
import game.Projectile;
import game.room.Room;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

/*
 * A flying monster that chases a player while shooting projectiles.
 * If the flying monster runs into the player, damage is given to the player.
 * 
 */
public class FlyingMonster extends Enemy {

	// amount of flying monsters that have been created
	public static int monsterNumber = 0;
	
	// this particular monster's ID
	private int monsterID;

	/* the only animation this monster has */
	private Animation movingAnimation;

	// determines if the monster can fire a projectile
	private float longRangeDelta;
	
	// the time a monster must wait to fire another projectile
	private static final long LONG_RANGE_WAIT = 700;

	// A path finder implementation that uses the AStar heuristic based algorithm to determine a path
	private AStarPathFinder pathFinder;
	
	// the path this monster is travelling to reach its destination
	private Path path;

	// the current step this monster is in within the path
	private int pathIndex;
	
	// the time that has passed since this monster last moved
	private int timeSinceLastMove;

	/*
	 * Creates a FlyingMonster object with a reference to the GameWOrld
	 * 
	 * @param	gameWorld	a reference to the GameWorld 
	 */
	public FlyingMonster(GameWorld gameWorld) {
		super(gameWorld);
		initResources();
		longRangeDelta = 0f;
		pathIndex = 0;
		timeSinceLastMove = 0;
		monsterNumber++;
		monsterID = monsterNumber;
		setMaximumHealth(2);
		setCurrentHealth(2);
	}
	

	/*
	 * Initializes any outside resources for this monster includuing
	 * animations.
	 * Also initializes this monsters bounding box.
	 */
	private void initResources() {
		movingAnimation = AnimationFactory.getFlyingMonsterMovingAnimation();
		setCurrentAnimation(movingAnimation);

		/* set the bounding box of this enemy */
		Rectangle r = new Rectangle(400, 200, movingAnimation.getSprite()
				.getWidth(), movingAnimation.getSprite().getHeight());

		setBoundingBox(r);

	}

	/* 
	 * Updates the pathfinding for this monster to track the player
	 * Updates the moving animation
	 * Updates the monster's projectiles
	 * Determines if this monster has been hit by a player projectile
	 * 
	 * (non-Javadoc)
	 * @see Enemy#update(org.newdawn.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta)
			throws SlickException {
		updatePathFinding(container, delta);

		// updates the moving animatino
		movingAnimation.update(delta);
		
		//determines if this monster can fire another projectile
		longRangeDelta += delta;
		if (longRangeDelta > LONG_RANGE_WAIT) {
			// System.out.println("Enemy firing projectile");
			longRangeDelta = 0;
			fireProjectile();
		}

		/* update FlyingMonster's projectiles */
		Iterator<Projectile> iter = getProjectiles().iterator();
		while (iter.hasNext()) {
			Projectile p = iter.next();
			if (p.isAlive())
				p.update(delta);
			else
				iter.remove();
		}
		
		// check if a player's projectile has hit this enemy
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
	 * Updates this monster's pathfinding to track the player
	 */
	private void updatePathFinding(GameContainer container, int delta) {
		Room r = getGameWorld().getCurrentRoom();

		//initialize pathFInder object
		if (pathFinder == null) {
			//max path length = 100
			pathFinder = new AStarPathFinder(getGameWorld().getCurrentRoom(),
					100, true);

			//start position of monster
			int startX = r.getColFromXCoord(getBoundingBox().getX());
			int startY = r.getRowFromYCoord(getBoundingBox().getY());


			//goal position = player position
			int goalX = r.getColFromXCoord(getGameWorld().getPlayer()
					.getBoundingBox().getX());
			int goalY = r.getRowFromYCoord(getGameWorld().getPlayer()
					.getBoundingBox().getY());

			// find path from monster to player
			path = pathFinder.findPath(new Mover(){}, startX, startY, goalX, goalY);

		}

		// if the path has been sucessfully traveled, then
		// redetermine another path from the monster to the player
		if (path == null || pathIndex == path.getLength()) {

			int startX = r.getColFromXCoord(getBoundingBox().getX());
			int startY = r.getRowFromYCoord(getBoundingBox().getY());

			int goalX = r.getColFromXCoord(getGameWorld().getPlayer()
					.getBoundingBox().getX());
			int goalY = r.getRowFromYCoord(getGameWorld().getPlayer()
					.getBoundingBox().getY());

			path = pathFinder.findPath(new Mover(){}, startX, startY, goalX, goalY);
			
			pathIndex = 0;
		}
		
		// this monster can only move so many spaces per 100 ms
		timeSinceLastMove += delta;
		if (path != null && pathIndex < path.getLength()
				&& timeSinceLastMove > 100) {
			getBoundingBox().setX(r.getXCoordFromCol(path.getX(pathIndex)));
			getBoundingBox().setY(r.getYCoordFromRow(path.getY(pathIndex)));
			pathIndex++;
			timeSinceLastMove = 0;
		}
		
	}

	/* 
	 * Render this monster to the canvas
	 * Render this monster's projectiles to the canvas
	 * (non-Javadoc)
	 * @see Enemy#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.drawImage(movingAnimation.getSprite(), getBoundingBox().getX(),
				getBoundingBox().getY());
		g.draw(getBoundingBox());

		for (Projectile p : getProjectiles()) {
			g.drawImage(p.getAnimation().getSprite(), p.getX(), p.getY());
		}
		g.drawString("H:" + getCurrentHealth() + "/" + getMaximumHealth(), getBoundingBox().getX(), getBoundingBox().getY());
		
	}

	/*
	 * Adds a projectile starting at the enemy position directed towards
	 * the player.
	 * (non-Javadoc)
	 * @see Enemy#fireProjectile()
	 */
	public void fireProjectile() {
		Vector2f targetVector = new Vector2f(getGameWorld().getPlayer()
				.getBoundingBox().getCenter());

		//monster location
		Vector2f currLoc = new Vector2f(getBoundingBox().getCenter());
		currLoc.set(currLoc.getX(), currLoc.getY());

		addProjectile(currLoc, targetVector);

	}

	/*
	 * Adds a projectile to the projectile list
	 * @param	currLoc			the starting position of the projectile
	 * @param	targetVector	the goal position of the projectile
	 */
	private void addProjectile(Vector2f currLoc, Vector2f targetVector) {
		
		Animation projectileAnimation = AnimationFactory.getGreenProjectileAnimation();
		projectileAnimation.start();
		
		getProjectiles().add(
				new Projectile(currLoc, targetVector, .45f, 2500L,
						projectileAnimation));

	}

	public String toString() {
		return "FlyingMonster" + monsterID;
	}

	/*
	 * Returns the melee damage given by this monster
	 * (non-Javadoc)
	 * @see Enemy#getMeleeDamage()
	 */
	@Override
	public float getMeleeDamage() {
		return 1;
	}

	/*
	 * Returns the ranged damage given by this monster's projectile
	 * (non-Javadoc)
	 * @see Enemy#getRangedDamage()
	 */
	@Override
	public int getRangedDamage() {
		return 1;
	}

}
