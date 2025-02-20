package component;

import lombok.Data;
import org.joml.Vector2f;
import render.Texture;

import java.io.Serializable;

/**
 * Class: Sprite
 * Author: rapto
 * CreatedDate: 1/22/2025 : 4:20 PM
 * Project: GameEngine
 * Description: Manages an individual sprite resource
 */
@Data
public class Sprite implements Serializable {
    //Default Texture Coordinate vector to use if not provided.
    protected static final Vector2f[] DEF_VECTOR = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };
    //Width of the Sprite
    private                float      width;
    //Height of the Sprite
    private                float      height;
    //The texture containing the sprite
    private                Texture    texture    = null;
    //The bounding box of the sprite
    private                Vector2f[] texCoords  = DEF_VECTOR;

    /**
     * Return the TextureId
     *
     * @return textureId or -1 if texture is null.
     */
    public int getTexId() {
        return texture != null ? texture.getTextureId() : -1;
    }
}
