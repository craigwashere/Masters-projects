package game.player;
import game.Animation;
import game.EventObject;
import game.GameWorld;
import game.HealthBar;
import game.Projectile;
import game.enemy.Enemy;
import game.item.Item;
import game.room.Door;
import game.room.Room;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public abstract class Player 
{
	/*
	 * The movement states a player can have
	 */
	enum MovementState 
	{
		STANDING, 
		RUNNING, 
		CLOSE_COMBAT, 
		LONG_RANGE
	};

	// how fast to move the image
	private float moveSpeed = 0.35f;
	
	private int rangedDamage = 1;
	
	// current health
	private int playerCurrentHealth;
	
	// the maximum health
	private int playerMaxHealth; 
	
	// the player type
	private PlayerType playerType;
	
	// the bound/hit box
	private Rectangle boundingBox;
	
	//gameworld reference
	private GameWorld gameWorld;

	// various animation types
	private Animation standingAnimation;
	private Animation runningAnimation;
	private Animation longRangeAttackAnimation;
	private Animation currentAnimation;
	private Animation projectileAnimation;

	// projectile launch sound
	private Sound longRangeSound;

	// the current movement state
	private MovementState currentMovementState;

	// list of player projectiles
	private ArrayList<Projectile> projectiles;
	
	// an event to determine if the player is invincible
	private EventObject notInvincibleEvent;
	
	// an event that determines if the player can fire a projectile
	private EventObject canFireRanged;

	public Player(GameWorld gameWorld) 
	{
		this.gameWorld = gameWorld;

		initPlayerType();

		initStandingAnimation();
		initRunningAnimation();
		initLongRangeAttackAnimation();
		initProjectileAnimation();

		setCurrentMovementState(MovementState.STANDING);

		projectiles = new ArrayList<Projectile>();
		
		playerMaxHealth = 6;
		playerCurrentHealth = 6;
		
		notInvincibleEvent = new EventObject(750, false);
		canFireRanged = new EventObject(200, false);
		
		System.out.println("Player created");
	}

	public abstract void initPlayerType();

	public abstract void initStandingAnimation();

	public abstract void initRunningAnimation();

	public abstract void initLongRangeAttackAnimation();

	public abstract void initProjectileAnimation();

	public void update(GameContainer container, int delta) throws SlickException 
	{
		updateInput(container, delta);
		updateLogic(container, delta);
	}

	/*
	 * Updates the player's logic including animations, projectiles
	 * and determining if a player has been hit by an enemy projectile
	 */
	private void updateLogic(GameContainer container, int delta) 
	{
		notInvincibleEvent.update(delta);
		
		//update current animation
		getCurrentAnimation().update(delta);
		
		//update player projectiles
		Iterator<Projectile> iter = projectiles.iterator();
		while(iter.hasNext()) 
		{
			Projectile p = iter.next();
			if(p.isAlive())
				p.update(delta);
			else
				iter.remove();
		}
		
		
		for(Enemy e: getGameWorld().getCurrentRoom().getEnemyList()) 
		{
			// enemy bounding box has intersected the player (melee damage)
			if(getBoundingBox().intersects(e.getBoundingBox())) 
			{
				if(notInvincibleEvent.isReady()) 
				{
					playerCurrentHealth -= e.getMeleeDamage();
					notInvincibleEvent.fireEvent();
				} 
				else {	} // player is still invincible 
				
				// player bounces back if hit
				float	player_x = getBoundingBox().getX(),
						player_y = getBoundingBox().getY(),
						new_x = player_x - (e.getBoundingBox().getX() - player_x),
						new_y = player_y - (e.getBoundingBox().getY() - player_y);
				
				Rectangle room_boundingBox = getGameWorld().getCurrentRoom().getBoundingBox();
				
				// determine if bounce back will push the player out of the playing area
				if (new_x >= 0 && new_x >= room_boundingBox.getX() - 1 && 
					new_x <= room_boundingBox.getX() + room_boundingBox.getWidth() - getCurrentAnimation().getSprite().getWidth()) 
				{	getBoundingBox().setX(new_x);	}
				
				if (new_y >= 0 && new_y >= room_boundingBox.getY() && 
					new_y <= room_boundingBox.getY() + room_boundingBox.getHeight() - getCurrentAnimation().getSprite().getHeight()) 
				{	getBoundingBox().setY(new_y);	}
			}
			
			//determine if player projectile has hit an enemy
			Rectangle projectileBoundingBox;
			iter = e.getProjectiles().iterator();
			while(iter.hasNext()) 
			{
				Projectile p = iter.next();
				projectileBoundingBox = new Rectangle(	p.getX(),	p.getY(), 
														p.getAnimation().getSprite().getWidth(), 
														p.getAnimation().getSprite().getHeight());
				if( this.getBoundingBox().intersects(projectileBoundingBox)) 
				{
					this.playerCurrentHealth -= e.getRangedDamage();
					iter.remove();
				}
			}
		}

		ArrayList<Item> item_list = getGameWorld().getCurrentRoom().getItemList(); 
		for(int i = 0; i < item_list.size(); i++) 
		{
			Item temp_item = item_list.get(i);
			if(getBoundingBox().intersects(temp_item.getBoundingBox())) 
			{
				switch (temp_item.item_type) 
				{
					/*case HALF_POTION:	getHealthBar().increase_health();
										break;
					case SPEED_2:		break;
					case SPEED_3:		break;*/
					case HEART:			playerMaxHealth += 2;
										playerCurrentHealth = playerMaxHealth;
										break;
					case FULL_POTION:	playerCurrentHealth = playerMaxHealth;
										break;
					case SPEED_1:		increaseSpeed(0.2f);
										break;
					case SWORD:			rangedDamage += 2;
										break;
//					case PORTAL:		if (((Portal) temp_item).getActiveStatus() == false)	{	break;	}
										//End game or "jump" to next level
									
					default:			break;
				}
				item_list.remove(i);
			}
		}

	}


	/*
	 * Render the player and the player's projectiles
	 */
	public void render(GameContainer container, Graphics g) throws SlickException 
	{
		// draw bounding box for testing
		if(notInvincibleEvent != null && notInvincibleEvent.isReady())
			g.setColor(Color.green);
		else
			g.setColor(Color.red);
		g.draw(getBoundingBox());
		
		//draw player sprite
		g.drawImage(getCurrentAnimation().getSprite(), getBoundingBox().getX(),	getBoundingBox().getY(), getGameWorld().getCurrentRoom().getRoomColor());
		
		//draw each projectile
		for (Projectile p : projectiles) 
			g.drawImage(p.getAnimation().getSprite(), p.getX(), p.getY());
	}

	/*
	 * Updates player input from mouse and keyboard
	 */
	private void updateInput(GameContainer container, int delta) 
	{
		canFireRanged.update(delta);
		
		Input in = container.getInput();
		float MOVE_SPEED = getmoveSpeed();
		float s = 0;

		float newX = 0;
		float newY = 0;

		/* player is moving left or right */
		if (in.isKeyDown(Input.KEY_LEFT) || in.isKeyDown(Input.KEY_RIGHT)	|| in.isKeyDown(Input.KEY_A) || in.isKeyDown(Input.KEY_D)) 
		{
			if (in.isKeyDown(Input.KEY_LEFT) || in.isKeyDown(Input.KEY_A))
				s = -MOVE_SPEED * delta;
			else if (in.isKeyDown(Input.KEY_RIGHT) || in.isKeyDown(Input.KEY_D))
				s = MOVE_SPEED * delta;
			newX = getBoundingBox().getX() + s;
		}

		/* player is moving up or down */
		if (in.isKeyDown(Input.KEY_DOWN) || in.isKeyDown(Input.KEY_UP) || in.isKeyDown(Input.KEY_W) || in.isKeyDown(Input.KEY_S)) 
		{
			if (in.isKeyDown(Input.KEY_DOWN) || in.isKeyDown(Input.KEY_S))
				s = MOVE_SPEED * delta;
			else if (in.isKeyDown(Input.KEY_UP) || in.isKeyDown(Input.KEY_W))
				s = -MOVE_SPEED * delta;

			newY = getBoundingBox().getY() + s;
		}

		/* Update player x position if within playable area */
		Rectangle boundingBox = getGameWorld().getCurrentRoom().getBoundingBox();

		if (newX >= 0 && newX >= boundingBox.getX() - 1 && newX <= boundingBox.getX() + boundingBox.getWidth() - getCurrentAnimation().getSprite().getWidth()) 
		{
			getBoundingBox().setX(newX);
			
			setCurrentMovementState(MovementState.RUNNING);
		}
		
		/* Update player y position if within playable area */
		if (newY >= 0 && newY >= boundingBox.getY() && newY <= boundingBox.getY() + boundingBox.getHeight() - getCurrentAnimation().getSprite().getHeight()) 
		{
			getBoundingBox().setY(newY);
			
			setCurrentMovementState(MovementState.RUNNING);
		}

		// determine if player is no longer moving
		if (newX <= 0 && newY <= 0)
			setCurrentMovementState(MovementState.STANDING);

		// player attempts to fire a projectile
		if (in.isMousePressed(Input.MOUSE_LEFT_BUTTON) && canFireRanged.isReady()) 
		{
			canFireRanged.fireEvent();
			setCurrentMovementState(MovementState.LONG_RANGE);


			Vector2f targetVector = new Vector2f(in.getMouseX(), in.getMouseY());

			Vector2f currLoc = new Vector2f(getBoundingBox().getCenter());
			currLoc.set(currLoc.getX() + 25, currLoc.getY());

			addProjectile(currLoc, targetVector);

			if (longRangeSound != null)
				longRangeSound.play(1.0f, 0.3f);
		}

		/* interact with environment */
		if (in.isKeyPressed(Input.KEY_SPACE)) 
		{
			Room currentRoom = getGameWorld().getCurrentRoom();

			//determine if player is near a door
			for (Door d : currentRoom.getDoors()) 
				if (d != null && d.getBoundingBox().intersects(getBoundingBox())) 
					getGameWorld().movePlayerThroughDoor(d.getDoorSide());

			in.consumeEvent();
		}

	}

	/* Adds a projectile to the list of player projectiles */
	private void addProjectile(Vector2f currLoc, Vector2f target) 
	{	projectiles.add(new Projectile(currLoc, target, .65f, 2500L, getProjectileAnimation()));	}

	/* Sets the current movement state and changes the player animation accordingly */
	public void setCurrentMovementState(MovementState newState) 
	{
		if (getCurrentAnimation() != null && !getCurrentAnimation().isLoops()) 
			if (!getCurrentAnimation().isHasPlayed()) 
				return;
			 else 
				getCurrentAnimation().setHasPlayed(false);

		if (this.currentMovementState != newState) 
		{
			if (getCurrentAnimation() != null)
				getCurrentAnimation().stop();

			this.currentMovementState = newState;

			if (newState == MovementState.STANDING) 
				setCurrentAnimation(getStandingAnimation());
			else if (newState == MovementState.RUNNING) 
				setCurrentAnimation(getRunningAnimation());
			else if (newState == MovementState.LONG_RANGE) 
				setCurrentAnimation(getLongRangeAttackAnimation());

			getCurrentAnimation().start();
			if (boundingBox == null)
				boundingBox = new Rectangle(1200 / 2, 900 / 2, getCurrentAnimation().getSprite().getWidth(), getCurrentAnimation().getSprite().getHeight());
		}
	}

	/* Returns the player's type */
	public PlayerType getPlayerType() 												{	return playerType;											}

	/* Sets the player's type */
	public void setPlayerType(PlayerType playerType) 								{	this.playerType = playerType;								}
	
	/* Returns the bounding box of the player */
	public Rectangle getBoundingBox() 												{	return boundingBox;											}

	/* sets the bounding box of hte player */
	public void setBoundingBox(Rectangle boundingBox) 								{	this.boundingBox = boundingBox;								}

	/* Returns the referenced GameWorld object */
	public GameWorld getGameWorld() 												{	return gameWorld;											}
	
	/* Sets the reference gameworld object	 */
	public void setGameWorld(GameWorld gameWorld) 									{	this.gameWorld = gameWorld;									}
	
	/* Returns the current health of the player	 */
	public int getCurrentHealth() 													{	return playerCurrentHealth;									}
	
	/* Returns the maximum health of the player	 */
	public int getMaxHealth() 														{	return playerMaxHealth;										}

	/* Gets the movespeed of the player	 */
	public float getmoveSpeed() 													{	return moveSpeed;											}
	
	/* Sets the movespeed for the player. */
	public void setMoveSpeed(float f) 												{	this.moveSpeed = f;											}

	/* gets the running animation for this player */
	public Animation getRunningAnimation() 											{	return runningAnimation;									}

	/* Sets the running animation for this player */
	public void setRunningAnimation(Animation runningAnimation) 					{	this.runningAnimation = runningAnimation;					}

	/* Returns the long range attack animation */
	public Animation getLongRangeAttackAnimation() 									{	return longRangeAttackAnimation;							}

	/* Sets the long rage attack animation */
	public void setLongRangeAttackAnimation(Animation longRangeAttackAnimation) 	{	this.longRangeAttackAnimation = longRangeAttackAnimation;	}

	/* Returns the standing animation for the player */
	public Animation getStandingAnimation() 										{	return standingAnimation;									}

	/* Setes the standing animation of the player */
	public void setStandingAnimation(Animation standingAnimation) 					{	this.standingAnimation = standingAnimation;					}

	/* Returns the current movement state of the player	 */
	public MovementState getCurrentMovementState() 									{	return currentMovementState;								}

	/* Gets the player's current animation	 */
	public Animation getCurrentAnimation() 											{	return currentAnimation;									}

	/* sets the current player animation */
	public void setCurrentAnimation(Animation currentAnimation) 					{	this.currentAnimation = currentAnimation;					}

	/* Returns the projectile animation	 */
	public Animation getProjectileAnimation() 										{	return projectileAnimation;									}

	/* Sets the projectile animation */
	public void setProjectileAnimation(Animation projectileAnimation) 				{	this.projectileAnimation = projectileAnimation;				}
	
	/* Returns a list of the player's projectiles */
	public ArrayList<Projectile> getProjectiles() 									{	return projectiles;											}

	/* Returns the sound a projectile makes	 */
	public Sound getLongRangeSound() 												{	return longRangeSound;										}

	/* The sound played when a projectile is launched */
	public void setLongRangeSound(Sound longRangeSound) 							{	this.longRangeSound = longRangeSound;						}

	/* Returns the amount of ranged damage given */
	public int getRangedDamage() 													{	return rangedDamage;										}

	private void increaseSpeed(float speed)											 {	moveSpeed += speed;											}
}
