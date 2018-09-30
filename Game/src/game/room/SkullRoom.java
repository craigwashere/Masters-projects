package game.room;
import game.GameWorld;
import game.enemy.AquamentusMonster;
import game.enemy.FlyingMonster;
import game.enemy.KeeseMonster;
//import game.item.PegasusBootsItem;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.PathFindingContext;

public class SkullRoom extends Room {
	
	private static final int versions = 3;

	public SkullRoom(GameWorld gameWorld) {
		super(gameWorld);
		setaStarMap(new int[getaStarMapHeight()][getaStarMapWidth()]);
		generateEnemies();
		generateItems();
	}

	private void generateEnemies() {
		//int numEnemies = (int) (GameWorld.RAND.nextFloat() * 10 + 1);
		//numEnemies = Math.max(4, numEnemies);
		//numEnemies = Math.min(numEnemies, 8);
		
		//for(int i = 0; i < numEnemies; ++i) {
		//	
		//}
		getEnemyList().add(new KeeseMonster(getGameWorld()));
		getEnemyList().add(new FlyingMonster(getGameWorld()));
		getEnemyList().add(new AquamentusMonster(getGameWorld()));
	//	getItemList().add(new PegasusBootsItem(getGameWorld()));
	}
	
	private void generateItems() {
		
		
	}

	protected void initRoomImage() {
		/* Set the room image */
		Random r = GameWorld.RAND;
		try {
			setRoomImage(new Image("images/skull_room_" + r.nextInt(versions)
					+ ".png"));
		} catch (SlickException e) {
			
			e.printStackTrace();
		}

	}


	@Override
	protected void initRoomColor() {
		Random r = GameWorld.RAND;
		/* Set the room color */
		setRoomColor(new Color((float) Math.max(r.nextFloat(), .6),
				(float) Math.max(r.nextFloat(), .6), (float) Math.max(
						r.nextFloat(), .6)));
	}

	@Override
	protected void initRoomType() {
		/* Set the room type */
		setRoomType(RoomType.SKULL_ROOM_NORMAL);
	}

	@Override
	protected void initRoomPlayableBoundingBox() {

		/* Set the room's playable area */
		setBoundingBox(new Rectangle(166, 123, 868, 654));
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
