package game;
import org.newdawn.slick.Image;

// TODO: add functionality for dynamic bounding box
public class Animation {

	private int newDelta;

	private int currentFrame;

	private boolean isAnimating;

	private boolean hasPlayed;

	private Frame[] frames;

	private boolean loops;

	public Animation(Frame[] frames, boolean loops) {
		this.frames = frames;

		this.isAnimating = false;

		this.newDelta = 0;
		this.currentFrame = 0;

		this.setHasPlayed(false);

		this.setLoops(loops);
	}
	
	public int getFrameCount() {
		return frames.length;
	}

	public void start() {
		if (isAnimating) {
			return;
		}
		
		if (frames.length == 0) {
			return;
		}

		isAnimating = true;
	}

	public void stop() {
		if (frames.length == 0) {
			return;
		}

		isAnimating = false;
	}

	public void restart() {
		if (frames.length == 0) {
			return;
		}

		isAnimating = true;
		currentFrame = 0;
	}

	public void reset() {
		this.isAnimating = false;
		this.newDelta = 0;
		this.currentFrame = 0;
	}

	public Image getSprite() {
		return frames[currentFrame].getFrame();
	}


	public void update(int delta) {
		if (isAnimating) {
			newDelta += delta;
			if (newDelta > frames[currentFrame].getDuration()) {
				newDelta = 0;
				currentFrame++;
				if (currentFrame == frames.length) {
					currentFrame %= frames.length;
					setHasPlayed(true);
				}
			}
		}

	}

	public boolean isHasPlayed() {
		return hasPlayed;
	}

	public void setHasPlayed(boolean hasPlayed) {
		this.hasPlayed = hasPlayed;
	}

	public boolean isLoops() {
		return loops;
	}

	public void setLoops(boolean loops) {
		this.loops = loops;
	}

	public boolean isAnimating() {
		return isAnimating;
	}

}