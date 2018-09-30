package game;
import game.enemy.Enemy;
import game.item.Item;
import game.player.Player;
import game.player.SorloSuper;
import game.room.Door;
import game.room.Room;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class GameWorld {

	public static Random RAND = new Random(2501);
	private AnimationFactory animationFactory = new AnimationFactory();

	private MapLevel mapLevel;
	private Player player;
	private Room currentRoom;
	private PauseScreen pauseScreen;

	private int currentRow, currentColumn;

	private Music currentSong;
	
	private HealthBar playerHealthBar;

	private boolean paused;

	public GameWorld(int numRows, int numCols) {
		currentRow = RAND.nextInt(numRows);
		currentColumn = RAND.nextInt(numCols);
		
		mapLevel = new MapLevel(numRows, numCols, this);

		
		player = new SorloSuper(this);
				
		
		setCurrentRoom(currentRow, currentColumn);
		getCurrentRoom().getEnemyList().clear();
		getCurrentRoom().getItemList().clear();
		
		playerHealthBar = new HealthBar(player, 6);

		pauseScreen = new PauseScreen(this);
		initResources();
		paused = false;

		
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		updateInput(container, delta);

		if (!paused) {
			player.update(container, delta);
			currentRoom.update(container, delta);
			
		} else {
			pauseScreen.update(container, delta);
		}

	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {

		
		if (paused) {
			pauseScreen.render(container, g);
		} else {
			currentRoom.render(container, g);
			player.render(container, g);
			
			playerHealthBar.render(container, g);
			
			g.setColor(Color.red);
			g.drawString("Health: " + player.getCurrentHealth() + "/" + player.getMaxHealth(), 75, 50);
			mapLevel.render(container, g);
		}

	}

	private void initResources() {
	/*	try {
			currentSong = new Music("music/LegendOfZeldaGanon.ogg");

			System.out.println("Music should be playing");
			 currentSong.loop(1f, .1f);
		} catch (SlickException e) {

			e.printStackTrace();
		}*/
	}

	private void updateInput(GameContainer container, int delta) {
		Input in = container.getInput();

		/* paused game */
		if (in.isKeyPressed(Input.KEY_ESCAPE)) {
			setPaused(!isPaused());

		}
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(int row, int col) {
		System.out.println("Room changed to : (" + row + ", " + col + ")");
		if (currentRoom != null) {
			for (Enemy e : currentRoom.getEnemyList()) {
				e.getCurrentAnimation().stop();
			}
			for (Item i : currentRoom.getItemList()) {
				i.getCurrentAnimation().stop();
			}
		}
		currentRoom = mapLevel.getRoom(row, col);
		if(player.getProjectiles() != null)
			player.getProjectiles().clear();
		for (Enemy e : currentRoom.getEnemyList()) {
			e.getCurrentAnimation().start();
		}
		for (Item i : currentRoom.getItemList()) {
			i.getCurrentAnimation().start();
		}
	}

	public void movePlayerThroughDoor(int doorSide) {
		Rectangle player = getPlayer().getBoundingBox();

		switch (doorSide) {
		case Door.TOP_DOOR:
			currentRow--;
			setCurrentRoom(currentRow, currentColumn);

			player.setLocation(getCurrentRoom().getDoors()[Door.BOTTOM_DOOR]
					.getBoundingBox().getCenterX() ,
					getCurrentRoom().getDoors()[Door.BOTTOM_DOOR]
							.getBoundingBox().getMinY() - player.getHeight() - 20);
			break;
		case Door.BOTTOM_DOOR:
			currentRow++;
			setCurrentRoom(currentRow, currentColumn);
			player.setLocation(getCurrentRoom().getDoors()[Door.TOP_DOOR]
					.getBoundingBox().getCenterX() ,
					getCurrentRoom().getDoors()[Door.TOP_DOOR]
							.getBoundingBox().getMaxY() + 20);
			break;
		case Door.RIGHT_DOOR:
			currentColumn++;
			setCurrentRoom(currentRow, currentColumn);
			player.setLocation(getCurrentRoom().getDoors()[Door.LEFT_DOOR]
					.getBoundingBox().getMaxX() + 20,
					getCurrentRoom().getDoors()[Door.LEFT_DOOR]
							.getBoundingBox().getCenterY());
			break;
		case Door.LEFT_DOOR:
			currentColumn--;
			setCurrentRoom(currentRow, currentColumn);
			player.setLocation(getCurrentRoom().getDoors()[Door.RIGHT_DOOR]
					.getBoundingBox().getMinX() - player.getWidth() - 20,
					getCurrentRoom().getDoors()[Door.RIGHT_DOOR]
							.getBoundingBox().getCenterY());
			break;
		}

	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {

		this.paused = paused;
		System.out.println("paused set: " + paused);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getCurrentRow() {
		return currentRow;
	}
	
	public int getCurrentColumn() {
		return currentColumn;
	}
}
