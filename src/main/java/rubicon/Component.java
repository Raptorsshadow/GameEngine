package rubicon;

/**
 * Class: Component
 * Author: rapto
 * CreatedDate: 1/19/2025 : 12:19 AM
 * Project: GameEngine
 * Description: Abstract class for managing the component lifecycle for the Entity Component System.
 */
public abstract class Component {
    public GameObject gameObject;

    /**
     * Responsible for performing an update operation on each "tick" to be defined by concrete classes.
     * Called after start and while the component is in use.
     *
     * @param dt Delta Time
     */
    public abstract void update(float dt);

    /**
     * Optional lifecycle hook to perform initialization and startup procedures if necessary.
     */
    public void start() {

    }
}
