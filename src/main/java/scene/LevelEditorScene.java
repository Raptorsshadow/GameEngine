package scene;

import component.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import rubicon.Camera;
import rubicon.GameObject;
import rubicon.Prefabs;
import rubicon.Transform;
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
    GameObject levelEditorStuff = new GameObject("levelEditor", new Transform(new Vector2f()), 0);

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
        this.levelEditorStuff.addComponent(new MouseControls());
        this.levelEditorStuff.addComponent(new GridLines());
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        if (this.levelLoaded) {

            this.activeGameObject = this.gameObjects.getFirst();
        }
    }

    /**
     * Pre-Load necessary assets to prevent runtime allocations.
     */
    private void loadResources() {
        AssetPool.getShader("assets/shader/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png",
                                 spriteSheet);
        //Fix texture Ids to prevent race condition issues with textureIds
        gameObjects
                .stream()
                .filter(g -> g.getComponent(SpriteRenderer.class) != null)
                .map(g -> g.getComponent(SpriteRenderer.class))
                .filter(s -> s.getTexture() != null)
                .forEach(s -> s.setTexture(AssetPool.getTexture(s.getTexture()
                                                                 .getFilePath())));
    }

    /**
     * Responsible for rendering each frame of the scene at delta time.
     *
     * @param dt Delta Time since last scene
     */
    @Override
    public void update(float dt) {
        levelEditorStuff.update(dt);

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
        ImGui.getStyle()
             .getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < spriteSheet.size(); i++) {
            Sprite sprite = spriteSheet.getSprite(i);
            float sWidth = sprite.getWidth() * 4;
            float sHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, sWidth, sHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                                  texCoords[2].y)) {
                GameObject block = Prefabs.generateSpriteObject(sprite, 32, 32);
                //Attach to the mouse cursor
                levelEditorStuff.getComponent(MouseControls.class)
                                .pickupObject(block);
            }
            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + sWidth;
            if (i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
