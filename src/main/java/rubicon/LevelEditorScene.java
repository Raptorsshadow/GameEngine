package rubicon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
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

    private SpriteRenderer spriteRenderer;
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
        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);
        spriteRenderer = new SpriteRenderer();
        spriteRenderer.setColor(new Vector4f(1.0f, 0f, 0f, 1f));
        obj1.addComponent(spriteRenderer);

        this.addGameObjectToScene(obj1);
        this.actveGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 2);
        SpriteRenderer textureRenderer = new SpriteRenderer();
        Sprite sprite = new Sprite();
        sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        textureRenderer.setSprite(sprite);
        obj2.addComponent(textureRenderer);

        this.addGameObjectToScene(obj2);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String ser = gson.toJson(obj1);
        System.out.println(ser);
        GameObject vec = gson.fromJson(ser, GameObject.class);
        System.out.println(vec);

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

    @Override
    public void imgui() {
        ImGui.begin("LES Window");
        if (ImGui.button("LevelEditorScene")) {
            ImGui.text("It works");
        }
        ImGui.end();
    }
}
