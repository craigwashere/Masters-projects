package game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Main extends BasicGame {

	public static final int WIDTH = 1200, HEIGHT = 900;

	public static void main(String[] args) throws SlickException {
		// creates a new "app" i.e. native window for desktop
		new AppGameContainer(new Main(), WIDTH, HEIGHT, false).start();
	}

	public Main() {
		super("My Game");
	}

	private GameWorld gameWorld ;

	@Override
	public void init(GameContainer container) throws SlickException {
		// ///// here we set up our container. this stuff is all "optional"
		// ///// but I would recommend looking into each.

		// show/hide FPS counter:
		container.setShowFPS(true);

		// cap the frame rate (recommended)
		container.setTargetFrameRate(60);

		// "cap" the delta value to some value you define, so that large
		// spikes will not affect simple collision checking
		container.setMaximumLogicUpdateInterval(10);

		// enable VSync (optional; on some computers it will perform better)
		container.setVSync(true);

		gameWorld = new GameWorld(4,4);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		gameWorld.render(container, g);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		gameWorld.update(container, delta);
		

	}
}