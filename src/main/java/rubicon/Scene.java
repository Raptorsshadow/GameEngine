package rubicon;

import imgui.ImGui;
import render.Renderer;

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

    // Collection of GameObjects used to build the scene
    protected final List<GameObject> gameObjects = new ArrayList<>();
    // Camera responsible for displaying the scene
    protected       Camera           camera;
    // Renderer used to draw the scene
    protected       Renderer         renderer;
    // State variable to track if the scene is running
    private         boolean          isRunning   = false;
    //Currently Selected activeGameObject used to render specific ImGui Overlays
    protected GameObject actveGameObject = null;

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
        this.renderer = new Renderer();
    }

    /**
     * Lifecycle hook called to dispose of any resources held by the scene.
     */
    public void dispose() {
        //Optional Lifecycle hook for disposing of resources.
    }
    /**
     * Iterates all GameObjects and calls their start method and flags scene as running.
     */
    public void start() {
        this.gameObjects.forEach(go -> {
            go.start();
            this.renderer.add(go);
        });
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
            this.renderer.add(go);
        }
    }

    /**
     * Retrieve the Scene Camera
     *
     * @return camera
     */
    public Camera getCamera() {
        return this.camera;
    }

    /**
     * Render the ImGui overlay for a scene.  If we currently have an active game object
     * render those controls as well.
     */
    public void sceneImgui () {
        if(actveGameObject != null) {
            ImGui.begin("Inspector");
            actveGameObject.imgui();
            ImGui.end();
        }
        imgui();
    }

    /**
     * Hook available for scene specific ImGui controls.
     */
    public void imgui() {
        //Used to create custom scene IMGui interactions
    }
}
