package components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import render.Texture;
import rubicon.Component;

/**
 * Class: SpriteRenderer
 * Author: rapto
 * CreatedDate: 1/19/2025 : 12:52 AM
 * Project: GameEngine
 * Description: Component responsible for rendering Text to a Scene
 */
public class SpriteRenderer extends Component {

    // 4 bit Color variable (RGBA)
    private final Vector4f color;

    //Sprite containing texture data
    private final Sprite sprite;

    /**
     * Default Constructor taking in color Vector
     *
     * @param color Color Vector
     */
    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = null;
    }

    /**
     * Default Constructor taking in a Texture resource
     *
     * @param sprite Sprite to use
     */
    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    /**
     * {@inheritDoc}
     *
     * @param dt Delta time between calls.
     */
    @Override
    public void update(float dt) {
        //Does Nothing
    }

    /**
     * Returns color vector.
     *
     * @return color
     */
    public Vector4f getColor() {
        return color;
    }

    /**
     * Return the texture of the sprite
     *
     * @return texture
     */
    public Texture getTexture() {
        assert this.sprite != null : "Error: SpriteRenderer: Attempted to access Texture on a null Sprite";
        return this.sprite.getTexture();
    }

    /**
     * Retrieve the texCoords of the sprite
     *
     * @return TexCoords Array
     */
    public Vector2f[] getTexCoords() {
        assert this.sprite != null : "Error: SpriteRenderer: Attempted to access Texture Coordinates on a null Sprite";
        return this.sprite.getTexCoords();
    }
}
