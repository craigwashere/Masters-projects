package game.room;
import game.GameWorld;
import game.enemy.FlyingMonster;
import game.enemy.KeeseMonster;
import game.item.HeartContainerItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.PathFindingContext;


public class ZeldaDungeonRoom extends TiledImageRoom {
	
	private static Image WALL_TEST, DUNGEON_FLOOR;

	public ZeldaDungeonRoom(GameWorld gameWorld) {
		super(gameWorld);
		setRoomType(RoomType.ZELDA_ROOM_NORMAL);
		loadMapFromFile(System.getProperty("user.dir") + "/src/rooms/test_room_1.txt");
		
		try {
			WALL_TEST = new Image("images/bg_tile_60x60_2.png");
			DUNGEON_FLOOR = new Image("images/dungeonfloor1.png");
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
		
		getEnemyList().add(new KeeseMonster(getGameWorld()));
		
	}

	private void loadMapFromFile(String filePath) {
		try {
			int[][] tiles = super.getTiles();
			Scanner s = new Scanner(new File(filePath));
			int currentRow = 0;
			while(s.hasNextLine()) {
				tiles[currentRow++] = convertToIntArray(s.nextLine());
				
			}
			s.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

	private int[] convertToIntArray(String line) {
		//System.out.println(line);
		//System.out.println("\t" + java.util.Arrays.toString(line.split(",")));
		String[] strings = line.split(",");
		int[] ret = new int[strings.length];
		for(int i = 0; i < strings.length; ++i)
			ret[i] = Integer.parseInt(strings[i]);
		return ret;
	}

	@Override
	public void drawBackground(GameContainer container, Graphics g) {
		
		int[][] tiles = super.getTiles();
		for(int i = 0; i < tiles.length; ++i) {
			for(int j = 0; j < tiles[i].length; ++j) {
				g.drawImage(getTileFromValue(tiles[i][j]), j * 60, i * 60, getRoomColor());
			}
		}
		//System.out.println("Columns: " + tiles[0].length);
	}	
	
	public Color getRoomColor() {
		return new Color(255, 255,255, 255);
	}

	private Image getTileFromValue(int value) {
		
		switch(value) {
		case (0) : return WALL_TEST;
		case (1) : return DUNGEON_FLOOR;
		default: return null;
		}
	}

	@Override
	public int getWidthInTiles() {
		
		return getaStarMapWidth();
	}

	@Override
	public int getHeightInTiles() {
		
		return getaStarMapHeight();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		
		
	}

	@Override
	public boolean blocked(PathFindingContext context, int tx, int ty) {
		return getaStarMap()[ty][tx] != 0;
	}

	@Override
	public float getCost(PathFindingContext context, int tx, int ty) {
		return 1.0f;
	}
	
	

}
