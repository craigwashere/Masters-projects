package game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AnimationFactory {

	private static Frame[] greenProjectileFrames;

	private static Frame[] flyingMonsterMovingFrames;

	private static Frame[] keeseMonsterMovingFrames;

	private static Frame[] redProjectileFrames;

	public AnimationFactory() {
		initGreenProjectile();
		initRedProjectile();
		initFlyingMonsterMovingAnimation();
		initKeeseMonsterMovingAnimation();
	}

	private void initKeeseMonsterMovingAnimation() {
		try {
			keeseMonsterMovingFrames = new Frame[] {
					new Frame(new Image("images/keese_monster_0.png"), 100),
					new Frame(new Image("images/keese_monster_1.png"), 100),
					new Frame(new Image("images/keese_monster_2.png"), 100),
					new Frame(new Image("images/keese_monster_3.png"), 100)
			};
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	private void initFlyingMonsterMovingAnimation() {
		try {
			flyingMonsterMovingFrames = new Frame[] {
					new Frame(new Image("images/flying_monster_0.png"), 80),
					new Frame(new Image("images/flying_monster_1.png"), 80),
					new Frame(new Image("images/flying_monster_2.png"), 70),
					new Frame(new Image("images/flying_monster_3.png"), 60) };
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void initGreenProjectile() {
		try {
			Image greenProjectile0, greenProjectile1, greenProjectile2;
			greenProjectile0 = new Image("images/green_projectile_0.png");
			greenProjectile1 = new Image("images/green_projectile_1.png");
			greenProjectile2 = new Image("images/green_projectile_2.png");
			greenProjectileFrames = new Frame[] {
					new Frame(greenProjectile0, 100),
					new Frame(greenProjectile1, 100),
					new Frame(greenProjectile2, 100) };
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void initRedProjectile() {
		try {
			Image redProjectile0, redProjectile1, redProjectile2;
			redProjectile0 = new Image("images/red_projectile_0.png");
			redProjectile1 = new Image("images/red_projectile_1.png");
			redProjectile2 = new Image("images/red_projectile_2.png");
			redProjectileFrames = new Frame[] {
					new Frame(redProjectile0, 100),
					new Frame(redProjectile1, 100),
					new Frame(redProjectile2, 100) };
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static Animation getKeeseMonsterMovingAnimation() {
		return new Animation(keeseMonsterMovingFrames, true);
	}

	public static Animation getFlyingMonsterMovingAnimation() {
		return new Animation(flyingMonsterMovingFrames, true);
	}

	public static Animation getGreenProjectileAnimation() {
		return new Animation(greenProjectileFrames, true);
	}
	
	public static Animation getRedProjectileAnimation() {
		return new Animation(redProjectileFrames, true);
	}
}
