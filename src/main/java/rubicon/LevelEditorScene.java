package rubicon;

import components.RigidBody;
import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import imgui.ImGui;
import imgui.ImVec2;
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

    private final SpriteSheet spriteSheet = new SpriteSheet(
            Objects.requireNonNull(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png")),
            16, 16, 81, 0);
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
        this.camera = new Camera(new Vector2f(-250, 0));

        if(this.levelLoaded) {
            this.activeGameObject = this.gameObjects.getFirst();
            System.out.println(this.activeGameObject.getName());
            return;
        }
        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setColor(new Vector4f(1.0f, 0f, 0f, 1f));
        obj1.addComponent(spriteRenderer);
        obj1.addComponent(new RigidBody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 2);
        SpriteRenderer textureRenderer = new SpriteRenderer();
        Sprite sprite = new Sprite();
        sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        textureRenderer.setSprite(sprite);
        obj2.addComponent(textureRenderer);

        this.addGameObjectToScene(obj2);
    }

    /**
     * Pre-Load necessary assets to prevent runtime allocations.
     */
    private void loadResources() {
        AssetPool.getShader("assets/shader/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png",
                spriteSheet);
        AssetPool.getTexture("assets/images/blendImage2.png");
    }

    /**
     * Responsible for rendering each frame of the scene at delta time.
     *
     * @param dt Delta Time since last scene
     */
    @Override
    public void update(float dt) {

        System.out.println(MouseListener.getOrthoY());
        //update all gameobjects for the frame.
        this.gameObjects.forEach(go -> go.update(dt));

        //Call the renderer
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("LES Window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i = 0; i < spriteSheet.size(); i++) {
            Sprite sprite = spriteSheet.getSprite(i);
            float sWidth = sprite.getWidth() * 4;
            float sHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, sWidth, sHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                System.out.println("Button " + i + " clicked");
            }
            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + sWidth;
            if(i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
