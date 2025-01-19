package rubicon;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: Scene
 * Author: rapto
 * CreatedDate: 1/19/2025 : 2:49 AM
 * Project: GameEngine
 * Description: Contract for managing a scene.
 */
public abstract class Scene {

    //Collection of GameObjects used to build the scene
    protected final List<GameObject> gameObjects = new ArrayList<>();
    // Camera responsible for displaying the scene
    protected Camera camera;
    //State variable to track if the scene is running
    private boolean isRunning = false;

    /**
     * Contract method responsible for updating the scene event delta time.
     *
     * @param dt Delta Time since last scene
     */
    public abstract void update(float dt);

    /**
     * Init method available to perform startup operations if necessary
     */
    public void init() {

    }

    /**
     * Iterates all GameObjects and calls their start method and flags scene as running.
     */
    public void start() {
        this.gameObjects.forEach(GameObject::start);
        this.isRunning = true;
    }

    /**
     * Registers a given GameObject in the scene and if running, starts it.
     *
     * @param go GameObjec to register
     */
    public void addGameObjectToScene(GameObject go) {
        gameObjects.add(go);
        if (isRunning) {
            go.start();
        }
    }
}
