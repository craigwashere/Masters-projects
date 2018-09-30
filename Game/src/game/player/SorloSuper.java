package game.player;
import game.Animation;
import game.Frame;
import game.GameWorld;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

public class SorloSuper extends Player {

	
	public SorloSuper(GameWorld gameWorld) {
		super(gameWorld);
		try {
			setLongRangeSound(new Sound("sounds/electric_zap2.wav"));
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void initPlayerType() {
		setPlayerType(PlayerType.SORLO_SUPER);
	}
	

	@Override
	public void initRunningAnimation() {
		SpriteSheet sheet = null;
		try {
			sheet = new SpriteSheet(new Image("images/sorlo_super_running.png"), 38 ,64);
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
		Frame[] frames = new Frame[]{
				new Frame(sheet.getSprite(0, 0), 70),
				new Frame(sheet.getSprite(1, 0), 60),
				new Frame(sheet.getSprite(2, 0), 70),
				new Frame(sheet.getSprite(3, 0), 70)
		};
		Animation runningAnimation = new Animation(frames, true);
		setRunningAnimation(runningAnimation);
	}

	@Override
	public void initLongRangeAttackAnimation() {
		try {
			Frame[] frames = new Frame[]{
					new Frame(new Image("images/sorlo_super_long_range_0.png"), 20),
					new Frame(new Image("images/sorlo_super_long_range_1.png"), 30),
					new Frame(new Image("images/sorlo_super_long_range_2.png"), 60),
					new Frame(new Image("images/sorlo_super_long_range_3.png"), 60),
			};
			Animation longRangeAnimation = new Animation(frames, false);
			setLongRangeAttackAnimation(longRangeAnimation);
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void initStandingAnimation() {
		try {
			Frame[] frames = new Frame[]{
					new Frame(new Image("images/sorlo_super_standing.png"), 50),
					
			};
			Animation standingAnimation = new Animation(frames, true);
			setStandingAnimation(standingAnimation);
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void initProjectileAnimation() {
		try {
			Frame[] frames = new Frame[]{
					new Frame(new Image("images/sorlo_super_projectile_0.png"), 120),
					new Frame(new Image("images/sorlo_super_projectile_1.png"), 120),
					new Frame(new Image("images/sorlo_super_projectile_2.png"), 120),
					new Frame(new Image("images/sorlo_super_projectile_3.png"), 120),
			};
			Animation projectileAnimation = new Animation(frames, true);
			setProjectileAnimation(projectileAnimation);
		} catch (SlickException e) {
			
			e.printStackTrace();
		}
	}
}
