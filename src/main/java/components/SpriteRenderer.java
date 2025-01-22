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
    private final Vector4f   color;

    //Texture coordinates in [BR, BL, TR, TL] order
    private Vector2f[] texCoords = null;

    private final Texture texture;

    /**
     * Default Constructor taking in color Vector
     * @param color Color Vector
     */
    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.texture = null;
    }

    /**
     * Default Constructor taking in a Texture resource
     * @param texture Texture to use
     */
    public SpriteRenderer(Texture texture) {
        this.texture = texture;
        this.color = new Vector4f(1,1,1,1);
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
     * @return color
     */
    public Vector4f getColor() {
        return color;
    }

    /**
     * Return the texture
     * @return texture
     */
    public Texture getTexture() {
        return this.texture;
    }

    /**
     * Retrieve the texCoords for the Texture from a sprite sheet.
     * @return TexCoords Array
     */
    public Vector2f[] getTexCoords() {
        if(this.texCoords == null) {
            texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
            };
        }
        return this.texCoords;
    }
}
