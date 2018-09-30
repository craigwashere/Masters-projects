package game;
import game.room.Door;
import game.room.Room;
import game.room.RoomType;
import game.room.ZeldaBossRoom;
import game.room.ZeldaDungeonRoom;
import game.room.ZeldaPortalRoom;
import game.room.ZeldaTreasureRoom;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class MapLevel {
	private int maxRows, maxColumns;
	private Room[][] rooms;
	private GameWorld gameWorld;

	private int maxDistanceFromSpawn = 0;

	public MapLevel(int rows, int cols, GameWorld gameWorld) {
		this.setGameWorld(gameWorld);
		this.maxRows = rows;
		this.maxColumns = cols;
		rooms = new Room[rows][cols];

		initRooms();
		System.out.println("All rooms created");
	}

	private void initRooms() {

		generateRandomRooms();
		generateRandomDoors();
		generateSpecialRoom(RoomType.ZELDA_ROOM_PORTAL,
				getMaxDistanceFromSpawn(), getMaxDistanceFromSpawn() + 1);
		generateSpecialRoom(RoomType.ZELDA_ROOM_BOSS,
				getMaxDistanceFromSpawn()-1, getMaxDistanceFromSpawn() );
		generateSpecialRoom(RoomType.ZELDA_ROOM_TREASURE,
				getMaxDistanceFromSpawn()/2, getMaxDistanceFromSpawn() - 2);
	}

	private void generateSpecialRoom(RoomType roomType, int minDistance,
			int maxDistance) {
		// the rooms with the specified distance from the spawn
		ArrayList<Room> rooms = getRoomsWithRangedDistance(
				minDistance, maxDistance);
		getGameWorld();
		Room r = rooms.get(GameWorld.RAND.nextInt(rooms.size()));
		int row = r.getRow();
		int col = r.getCol();

		r.setRow(row);
		r.setCol(col);

		switch (roomType) {
		case SKULL_ROOM_BOSS:
			break;
		case SKULL_ROOM_NORMAL:
			break;
		case SKULL_ROOM_TREASURE:
			break;
		case ZELDA_ROOM_BOSS: this.rooms[row][col] = new ZeldaBossRoom(gameWorld, row, col);
			break;
		case ZELDA_ROOM_NORMAL:
			break;
		case ZELDA_ROOM_PORTAL:
			this.rooms[row][col] = new ZeldaPortalRoom(gameWorld, row, col);
			break;
		case ZELDA_ROOM_TREASURE:
			this.rooms[row][col] = new ZeldaTreasureRoom(gameWorld, row, col);
			break;
		default:
			break;
		
		}
		
		//copy doors from old room
		this.rooms[row][col].setDoors(r.getDoors());
		
		
		System.out.println("Setting: " + row + "," + col + " to treasure room");
	}

	/*private void generatePortalRoom() {

		// the rooms with the specified distance from the spawn
		ArrayList<Room> rooms = getRoomsWithRangedDistance(
				getMaxDistanceFromSpawn(), getMaxDistanceFromSpawn() + 1);
		getGameWorld();
		Room r = rooms.get(GameWorld.RAND.nextInt(rooms.size()));
		int row = r.getRow();
		int col = r.getCol();

		r.setRow(row);
		r.setCol(col);

		this.rooms[row][col] = new ZeldaPortalRoom(gameWorld);
		System.out.println("Setting: " + row + "," + col + " to portal room");
	}

	private void generateBossRoom() {
		// the rooms with the specified distance from the spawn
		ArrayList<Room> rooms = getRoomsWithRangedDistance(
				getMaxDistanceFromSpawn() - 1, getMaxDistanceFromSpawn());
		getGameWorld();
		Room r = rooms.get(GameWorld.RAND.nextInt(rooms.size()));
		int row = r.getRow();
		int col = r.getCol();

		r.setRow(row);
		r.setCol(col);

		this.rooms[row][col] = new ZeldaBossRoom(gameWorld);
		System.out.println("Setting: " + row + "," + col + " to boss room");
	}

	private void generateTreasureRoom() {
		// the rooms with the specified distance from the spawn
		ArrayList<Room> rooms = getRoomsWithRangedDistance(
				getMaxDistanceFromSpawn() - 1, getMaxDistanceFromSpawn());
		getGameWorld();
		Room r = rooms.get(GameWorld.RAND.nextInt(rooms.size()));
		int row = r.getRow();
		int col = r.getCol();

		r.setRow(row);
		r.setCol(col);

		this.rooms[row][col] = new ZeldaTreasureRoom(gameWorld);
		System.out.println("Setting: " + row + "," + col + " to treasure room");
	}*/

	private ArrayList<Room> getRoomsWithRangedDistance(int minDistance,
			int maxDistance) {

		ArrayList<Room> ret = new ArrayList<Room>();
		for (Room[] roomRow : rooms)
			for (Room r : roomRow)
				if (r.getDistanceFromSpawn() >= minDistance
						&& r.getDistanceFromSpawn() < maxDistance)
					ret.add(r);
		return ret;
	}

	public Room getRoom(int row, int col) {
		return rooms[row][col];
	}

	private void generateRandomRooms() {
		double randValue = -1;
		for (int i = 0; i < rooms.length; ++i) {
			for (int j = 0; j < rooms[i].length; ++j) {
				randValue = GameWorld.RAND.nextFloat();
				if (randValue >= 0) {
					rooms[i][j] = new ZeldaDungeonRoom(getGameWorld());
					rooms[i][j].setRow(i);
					rooms[i][j].setCol(j);

				}

			}
		}
	}

	/*
	 * Generates doors for each room Uses a recursive backtracker algorithm from
	 * Wikipedia http://en.wikipedia.org/wiki/Maze_generation_algorithm
	 */
	private void generateRandomDoors() {
		Random rand = GameWorld.RAND;
		Stack<Room> stack = new Stack<Room>();
		int numCellsVisited = 0;

		// current distance from player spawn
		int distance = 0;
		int totalCellsToVisit = maxRows * maxColumns;

		boolean[][] visited = new boolean[maxRows][maxColumns];

		int currentRoomRow = gameWorld.getCurrentRow(), currentRoomCol = gameWorld
				.getCurrentColumn();
		Room currentRoom = getRoom(currentRoomRow, currentRoomCol);
		currentRoom.setDistanceFromSpawn(distance);
		visited[currentRoomRow][currentRoomCol] = true;
		numCellsVisited++;

		ArrayList<Room> neighbors;
		while (numCellsVisited < totalCellsToVisit) {
			neighbors = getUnvisitedNeighbors(currentRoom, visited);
			distance++;
			if (neighbors.size() > 0) {
				stack.push(currentRoom);
				Room oldRoom = currentRoom;
				currentRoom = neighbors.get(rand.nextInt(neighbors.size()));

				currentRoom.setDistanceFromSpawn(distance);
				maxDistanceFromSpawn = Math.max(maxDistanceFromSpawn, distance);

				// System.out.println("Visited room: " + currentRoom);
				visited[currentRoom.getRow()][currentRoom.getCol()] = true;
				numCellsVisited++;
				linkRooms(oldRoom, currentRoom);
			} else if (stack.size() > 0) {
				distance -= 2;
				currentRoom = stack.pop();
			} else { // pick a random unvisited cell and mark it as visited
				// i think this counts for unconnected graphs
			}
		}
	}

	private void linkRooms(Room oldRoom, Room newRoom) {
		int rowDelta = newRoom.getRow() - oldRoom.getRow();
		int colDelta = newRoom.getCol() - oldRoom.getCol();
		if (rowDelta == -1 && colDelta == 0) {
			oldRoom.createDoor(Door.TOP_DOOR);
			newRoom.createDoor(Door.BOTTOM_DOOR);
		} else if (rowDelta == 1 && colDelta == 0) {
			oldRoom.createDoor(Door.BOTTOM_DOOR);
			newRoom.createDoor(Door.TOP_DOOR);
		} else if (rowDelta == 0 && colDelta == -1) {
			oldRoom.createDoor(Door.LEFT_DOOR);
			newRoom.createDoor(Door.RIGHT_DOOR);
		} else if (rowDelta == 0 && colDelta == 1) {
			oldRoom.createDoor(Door.RIGHT_DOOR);
			newRoom.createDoor(Door.LEFT_DOOR);
		}
	}

	private ArrayList<Room> getUnvisitedNeighbors(Room currentRoom,
			boolean[][] visited) {
		ArrayList<Room> neighbors = new ArrayList<Room>();
		int row = currentRoom.getRow();
		int col = currentRoom.getCol();

		if (col - 1 >= 0) // west neighbor
			if (!visited[row][col - 1])
				neighbors.add(rooms[row][col - 1]);
		if (col + 1 < rooms[row].length) // east neighbor
			if (!visited[row][col + 1])
				neighbors.add(rooms[row][col + 1]);
		if (row - 1 >= 0)
			if (!visited[row - 1][col]) // north neighbor
				neighbors.add(rooms[row - 1][col]);
		if (row + 1 < rooms.length)
			if (!visited[row + 1][col]) // south neighbor
				neighbors.add(rooms[row + 1][col]);

		return neighbors;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.setColor(org.newdawn.slick.Color.black);
		g.fillRect(Main.WIDTH - 150, 0, 150, 150);

		// room width in map
		int width = 140 / maxColumns;

		// room height in map
		int height = 140 / maxRows;

		// draw the map
		for (int row = 0; row < rooms.length; ++row) {
			for (int col = 0; col < rooms[0].length; ++col) {
				Room currentRoom = getRoom(row, col);
				Rectangle roomRect = new Rectangle(Main.WIDTH - 145 + col
						* width, row * height, width, height);
				g.setColor(org.newdawn.slick.Color.white);
				g.draw(roomRect);

				Rectangle smallerRoomRect = new Rectangle(roomRect.getX() + 2,
						roomRect.getY() + 2, roomRect.getWidth() - 4,
						roomRect.getHeight() - 4);
				switch (currentRoom.getRoomType()) {
				case SKULL_ROOM_BOSS:
					break;
				case SKULL_ROOM_NORMAL:
					break;
				case SKULL_ROOM_TREASURE:
					break;
				case ZELDA_ROOM_BOSS:
					g.setColor(Color.red);
					break;
				case ZELDA_ROOM_NORMAL:
					g.setColor(Color.gray);
					break;
				case ZELDA_ROOM_PORTAL:
					g.setColor(Color.yellow);
					break;
				case ZELDA_ROOM_TREASURE:
					g.setColor(Color.green);
					break;
				default:
					g.setColor(Color.gray);
					break;

				}

				g.fill(smallerRoomRect);

				float centerX = roomRect.getCenterX();
				float centerY = roomRect.getCenterY();

				g.setColor(org.newdawn.slick.Color.green);
				if (currentRoom.hasDoor(Door.TOP_DOOR))
					g.drawRect(centerX, centerY - height / 2, 4, 1);
				if (currentRoom.hasDoor(Door.BOTTOM_DOOR))
					g.drawRect(centerX, centerY + height / 2, 4, 1);
				if (currentRoom.hasDoor(Door.LEFT_DOOR))
					g.drawRect(centerX - width / 2, centerY, 1, 4);
				if (currentRoom.hasDoor(Door.RIGHT_DOOR))
					g.drawRect(centerX + width / 2, centerY, 1, 4);

				// g.drawString("" + row + "," + col,Main.WIDTH - 140 + col *
				// width, row * height);
			}
		}

		Room currentRoom = getGameWorld().getCurrentRoom();
		g.fillOval(Main.WIDTH - 145 + currentRoom.getCol() * width + width / 2,
				currentRoom.getRow() * height + height / 2, 5, 5);
	}

	public int getMaxRows() {
		return maxRows;
	}

	public int getMaxColumns() {
		return maxColumns;
	}

	public int getMaxDistanceFromSpawn() {
		return maxDistanceFromSpawn;
	}

}
