package game;
import org.newdawn.slick.Image;

/*
 * A frame class holds an image and a duration for that image to be shown
 */
public class Frame {

    private Image frame;
    private int duration;

    /*
     * @param	frame		the image to be drawn
     * @param	duration	the duration for the image to be shown
     */
    public Frame(Image frame, int duration) {
        this.frame = frame;
        this.duration = duration;
    }

    /*
     * Returns the frame's image
     */
    public Image getFrame() {
        return frame;
    }

    /*
     * Sets the frame's image
     */
    public void setFrame(Image frame) {
        this.frame = frame;
    }

    /*
     * Returns the duration that this image will be shown for
     */
    public int getDuration() {
        return duration;
    }

    /*
     * Sets the duration that this image will be shown for
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

}