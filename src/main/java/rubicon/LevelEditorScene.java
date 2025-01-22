package rubicon;

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

    /**
     * Default constructor used to initialize the scene pieces
     */
    public LevelEditorScene() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();
        loadResources();

        this.camera = new Camera(new Vector2f());
        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));

        this.addGameObjectToScene(obj1);


        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 400), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(25)));

        this.addGameObjectToScene(obj2);
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
        //update all gameobjects for the frame.
        this.gameObjects.forEach(go -> go.update(dt));

        //Call the renderer
        this.renderer.render();
    }
}
