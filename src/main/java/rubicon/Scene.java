package rubicon;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected final List<GameObject> gameObjects = new ArrayList<>();
    public Scene() {

    }

    public abstract void update(float dt);

    public void init() {

    }

    public void start() {
        this.gameObjects.forEach(GameObject::start);
        this.isRunning = true;
    }
    public void addGameObjectToScene(GameObject go) {
        gameObjects.add(go);
        if (isRunning) {
            go.start();
        }
    }
}
