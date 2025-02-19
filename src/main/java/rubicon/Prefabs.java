package rubicon;

import component.Sprite;
import component.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {
    private Prefabs() {
        //Hidden constructor for static class.
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = new GameObject("Sprite Object Gen", new Transform(new
                                                                                     Vector2f(),
                                                                             new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setSprite(sprite);
        block.addComponent(spriteRenderer);
        return block;
    }
}
