package game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;



//TODO: make a general screen class

public class PauseScreen {

	private Image backgroundImage;


	private Rectangle boundingBox;
	
	private GameWorld gameWorld;
	
	private GameButton continueButton, saveButton, quitButton;
	
	public PauseScreen(GameWorld gameWorld) {
		initResources();
		this.gameWorld= gameWorld;
		initButtons();
	}


	private void initButtons() {
		continueButton = new GameButton(this, "Continue");
		continueButton.setY(continueButton.getBoundingBox().getY() + 10);
		
		saveButton = new GameButton(this, "Save");
		saveButton.setY(continueButton.getBoundingBox().getY() + continueButton.getBoundingBox().getHeight() + 10);
		
		quitButton = new GameButton(this, "Quit");
		quitButton.setY(saveButton.getBoundingBox().getY() + saveButton.getBoundingBox().getHeight() + 10);
	}


	private void initResources() {
		try {
			backgroundImage = new Image("images/pause_menu_background.png");
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
		
		boundingBox = new Rectangle(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
		boundingBox.setCenterX(Main.WIDTH/2);
		boundingBox.setCenterY(Main.HEIGHT/2);
	}
	
	public void update(GameContainer container, int delta)
			throws SlickException {
		
		continueButton.update(container, delta);
		saveButton.update(container, delta);
		quitButton.update(container, delta);
		
		if(continueButton.isPressed()) {
			gameWorld.setPaused(false);
			continueButton.consumeEvent();
		} else if (saveButton.isPressed()) {
			
		} else if (quitButton.isPressed()) {
			
		}
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {

		g.drawImage(backgroundImage, boundingBox.getX(), boundingBox.getY());
		continueButton.render(container,  g);
		saveButton.render(container, g);
		quitButton.render(container, g);
		
	}
	
	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}
}
