package game;
import game.player.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class HealthBar {
	private int maxHealth;
	private Image emptyHeart, halfHeart, wholeHeart;

	private int startX = 150, startY = 60, widthX = 37;
	private Player player;

	public HealthBar() {
	}

	public HealthBar(Player player, int health) {
		this.player = player;
		this.maxHealth = health;
		try {
			emptyHeart = new Image("images/heartempty.png");
			halfHeart = new Image("images/hearthalf.png");
			wholeHeart = new Image("images/heartfull.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		int currentX, i;

		int drawHalfHeart = (player.getCurrentHealth() % 2);

		for (i = 1; i < maxHealth; i += 2) {
			currentX = widthX * i + startX;
			if (i < player.getCurrentHealth())
				g.drawImage(wholeHeart, currentX, startY);
			else if (player.getCurrentHealth() > 0 && (i >= player.getCurrentHealth()) && (drawHalfHeart != 0)) {
				g.drawImage(halfHeart, currentX, startY);
				drawHalfHeart = 0;
			} else
				g.drawImage(emptyHeart, currentX, startY);
		}
	}



}
