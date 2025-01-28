package components;

import org.joml.Vector2f;
import render.Texture;

import java.util.Objects;

/**
 * Class: Sprite
 * Author: rapto
 * CreatedDate: 1/22/2025 : 4:20 PM
 * Project: GameEngine
 * Description: Manages an individual sprite resource
 */
public class Sprite {
    protected static final Vector2f[] DEF_VECTOR = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    //The texture containing the sprite
    private final Texture texture;
    //The bounding box of the sprite
    private final Vector2f[] texCoords;

    /**
     * Default Constructor
     */
    public Sprite() {
        this(null, null);
    }

    /**
     * Overridden constructor taking a texture with single image (no tiling)
     *
     * @param tex the Texture
     */
    public Sprite(Texture tex) {
        this(tex, null);
    }

    /**
     * Overridden taking a Texture and coordinates of the texture
     *
     * @param tex       The Texture
     * @param texCoords Coordinates of the texture to use.
     */
    public Sprite(Texture tex, Vector2f[] texCoords) {
        this.texture = tex;
        this.texCoords = Objects.requireNonNullElseGet(texCoords, DEF_VECTOR::clone);
    }

    /**
     * Return the Texture
     *
     * @return texture
     */
    public Texture getTexture() {
        return this.texture;
    }

    /**
     * Return the texture coordinates
     *
     * @return texture coordinates
     */
    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }
}
