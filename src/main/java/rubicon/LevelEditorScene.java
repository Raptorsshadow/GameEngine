package rubicon;

import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import org.joml.Vector2f;
import util.AssetPool;

import java.util.Objects;

/**
 * Class: LevelEditorScene
 * Author: rapto
 * CreatedDate: 1/19/2025 : 3:49 AM
 * Project: GameEngine
 * Description: Editor Scene for testing capabilities.
 */
public class LevelEditorScene extends Scene {

    float lastChange = 0;
    private GameObject obj1;
    private final IMGuiLayer guiLayer;

    /**
     * Default constructor used to initialize the scene pieces
     */
    public LevelEditorScene() {
        super();
        guiLayer = new IMGuiLayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();
        loadResources();

        this.camera = new Camera(new Vector2f());
        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 2);
        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage1.png"))));

        this.addGameObjectToScene(obj1);


        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 2);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage2.png"))));

        this.addGameObjectToScene(obj2);

        this.guiLayer.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        //Dispose of IMGui Resources
        this.guiLayer.dispose();
    }
    /**
     * Pre-Load necessary assets to prevent runtime allocations.
     */
    private void loadResources() {
        AssetPool.getShader("assets/shader/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                                 new SpriteSheet(
                                         Objects.requireNonNull(AssetPool.getTexture("assets/images/spritesheet.png")),
                                         16, 16, 26, 0));
    }

    /**
     * Responsible for rendering each frame of the scene at delta time.
     *
     * @param dt Delta Time since last scene
     */
    @Override
    public void update(float dt) {
        lastChange += dt;
        obj1.transform.position.x += 10 * dt;
        guiLayer.startFrame();
        guiLayer.imgui();

        //update all gameobjects for the frame.
        this.gameObjects.forEach(go -> go.update(dt));

        //Call the renderer
        this.renderer.render();
        guiLayer.endFrame();
    }
}
