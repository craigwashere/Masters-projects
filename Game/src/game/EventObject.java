package game;
/*
 * Used to simulate a timer for events
 * Taken from:
 * http://gamedev.stackexchange.com/questions/75672/slick-and-timers
 */

public class EventObject {
    float timeSinceTrigger = 0;
    float triggerInterval;
    boolean fireOnce;
    boolean Fired;

    /*
     * 
     * @param	triggerInterval	the interval at which this event is ready
     * @param	fireOnce		this event can only fire once
     */
    public EventObject(float triggerInterval, boolean fireOnce) {
       this.triggerInterval = triggerInterval;
       this.fireOnce = fireOnce;
    }
    
    /*
     * Updates the total time since last trigger
     */
    public void update(float delta) {
        timeSinceTrigger += delta;
    }

    /*
     * Returns true if this trigger's interval has expired and
     * is ready to be fired
     */
    public boolean isReady() {
        if(fireOnce && Fired)
            return false;

        if(timeSinceTrigger >= triggerInterval) {
            //We're ready, if we fire only once, set our flag for that
            // otherwise, reset our time since trigger to fire again later.
            return true;
        }
        return false;
    }
    
    /*
     * resets this trigger
     * If fireOnce is set to true, Fired is set to true
     * otherwise, the timer is reset to this trigger
     * 
     */
    public void fireEvent() {
        if(fireOnce)
            Fired = true;
        else
            timeSinceTrigger = 0;
    }

}