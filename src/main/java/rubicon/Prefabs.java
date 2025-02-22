package rubicon;

import component.Sprite;
import component.SpriteRenderer;
import org.joml.Vector2f;

/**
 * Class: Prefabs
 * Author: rapto
 * CreatedDate: 2/22/2025 : 4:10 AM
 * Project: GameEngine
 * Description: Generates prefabricated or pre-configured objects.
 */
public class Prefabs {
    private Prefabs() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = new GameObject("Sprite Object Gen",
                                          new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setSprite(sprite);
        block.addComponent(spriteRenderer);
        return block;
    }
}
