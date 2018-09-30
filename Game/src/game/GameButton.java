package game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class GameButton {
	private Image depressedImage, hoverImage, pressedImage;
	private Image currentImage;

	private String buttonText;

	private Rectangle boundingBox;
	private PauseScreen pauseScreen;
	
	private boolean pressed;

	// TODO: change to a general Screen parameter
	public GameButton(PauseScreen pauseScreen, String text) {
		this.buttonText = text;
		this.pauseScreen = pauseScreen;
		setPressed(false);
		initResources();
	}

	private void initResources() {
		try {
			depressedImage = new Image("images/button_depressed.png");
			hoverImage = new Image("images/button_hover.png");
			pressedImage = new Image("images/button_pressed.png");
			boundingBox = new Rectangle(pauseScreen.getBoundingBox().getX(),
					pauseScreen.getBoundingBox().getY(),
					depressedImage.getWidth(), depressedImage.getHeight());
			boundingBox.setCenterX(pauseScreen.getBoundingBox().getCenterX());

			currentImage = depressedImage;
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		int x = input.getMouseX();
		int y = input.getMouseY();

		if (x >= boundingBox.getMinX() && x <= boundingBox.getMaxX()
				&& y >= boundingBox.getMinY() && y <= boundingBox.getMaxY()) {
			currentImage = hoverImage;
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
				currentImage = pressedImage;
				setPressed(true);
			}
		} else {
			 currentImage = depressedImage;
		}
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {

		g.drawImage(currentImage, boundingBox.getX(), boundingBox.getY());
		int textWidth = g.getFont().getWidth(buttonText);
		int textHeight = g.getFont().getHeight(buttonText);

		g.drawString(buttonText, boundingBox.getX()
				+ (boundingBox.getWidth() - textWidth) / 2, boundingBox.getY()
				+ (boundingBox.getHeight() - textHeight) / 2);
		g.draw(boundingBox);

	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public void setX(float x) {
		boundingBox.setX(x);
	}

	public void setY(float y) {
		boundingBox.setY(y);
	}
	
	public void consumeEvent() {
		setPressed(false);
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
}
