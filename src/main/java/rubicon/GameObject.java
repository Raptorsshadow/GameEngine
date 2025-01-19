package rubicon;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: GameObject
 * Author: rapto
 * CreatedDate: 1/19/2025 : 2:01 AM
 * Project: GameEngine
 * Description: Part of the Entity Component System.  This object is used to group related components of a scene into a
 * single collection and manages the lifecycle of those components in a single place.
 */
public class GameObject {

    //Name of component used for logging and management purposes
    private final String name;

    //All components managed by this object
    private final List<Component> components = new ArrayList<>();

    /**
     * Constructor that names the object.
     *
     * @param name GameObject name
     */
    public GameObject(String name) {
        this.name = name;
    }

    /**
     * Finds a component of the given type and returns it, otherwise returns null.
     *
     * @param cClass Type of component we want to return
     * @param <T>    Generic type extending Component
     * @return Matched Component or null
     */
    public <T extends Component> T getComponent(Class<T> cClass) {
        return components
                .stream()
                .filter(c -> cClass.isAssignableFrom(c.getClass()))
                .findFirst()
                .map(cClass::cast)
                .orElse(null);
    }

    /**
     * Attempts to remove a component that matched the given class.
     *
     * @param cClass Type of component we want to remove
     * @param <T>    Generic type extending Component
     */
    public <T extends Component> void removeComponent(Class<T> cClass) {
        for (Component c : this.components) {
            if (cClass.isAssignableFrom(c.getClass())) {
                this.components.remove(c);
                return;
            }
        }
    }

    /**
     * Register a component.
     *
     * @param c the component to register
     */
    public void addComponent(Component c) {
        this.components.add(c);
        c.gameObject = this;
    }

    /**
     * Iterate all the registered components and run their update method.
     *
     * @param dt The time delta
     */
    public void update(float dt) {
        System.out.println("Updating Components for " + this.name);
        this.components.forEach(c -> c.update(dt));
    }

    /**
     * Iterate all the registered components and run their start method.
     */
    public void start() {
        System.out.println("Starting Components for " + this.name);
        this.components.forEach(Component::start);
    }
}
