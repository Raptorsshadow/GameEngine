package scene;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import component.Component;
import component.ComponentDeserializer;
import imgui.ImGui;
import lombok.Getter;
import render.Renderer;
import rubicon.Camera;
import rubicon.GameObject;
import rubicon.GameObjectDeserializer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Getter
    protected Camera camera;
    // Renderer used to draw the scene
    protected       Renderer         renderer = new Renderer();
    //Currently Selected activeGameObject used to render specific ImGui Overlays
    protected       GameObject       activeGameObject = null;
    // State variable to track if the scene is running
    private         boolean          isRunning = false;

    //Status bit for determine if level was loaded from file.
    protected boolean levelLoaded = false;
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
     * Render the ImGui overlay for a scene.  If we currently have an active game object
     * render those controls as well.
     */
    public void sceneImgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
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

    /**
     * Serialize the Scenes GameObjects into a level json file.
     */
    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try(FileWriter writer = new FileWriter("level.json")) {
            writer.write(gson.toJson(this.gameObjects));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a Scenes GameObjets from a level json file.
     */
    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.isEmpty()) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for(GameObject go : objs) {
                addGameObjectToScene(go);
                for(Component c : go.getComponents()) {
                    maxCompId = Math.max(c.getUid(), maxCompId);
                }
                maxGoId = Math.max(go.getUid(), maxGoId);
            }

            GameObject.init(++maxGoId);
            Component.init(++maxCompId);
            System.out.println("MGO : " + maxGoId);
            System.out.println("MComp : " + maxCompId);

            this.levelLoaded = true;
        }
    }
}
