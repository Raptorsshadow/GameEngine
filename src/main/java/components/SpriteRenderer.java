package components;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import render.Texture;
import rubicon.Component;
import rubicon.Transform;

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
    private Sprite sprite;

    private Transform lastTransform;
    private boolean   isDirty = false;

    /**
     * Default Constructor taking in color Vector
     *
     * @param color Color Vector
     */
    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
        this.isDirty = true;
    }

    /**
     * Default Constructor taking in a Texture resource
     *
     * @param sprite Sprite to use
     */
    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
        this.isDirty = true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Set the lastTransform to game object transform
     */
    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Check if the SpriteRenderer has become dirty since the last update.
     *
     * @param dt Delta time between calls.
     */
    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copyTo(lastTransform);
            this.isDirty = true;
        }
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
     * Set the color on the SpriteRenderer and flag dirty if different from existing.
     *
     * @param color updated color
     */
    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color.set(color);
            this.isDirty = true;
        }
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

    /**
     * Set the sprite on the spriteRenderer and flag dirty.
     *
     * @param sprite new Sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    /**
     * Return dirty state of SpriteRenderer
     *
     * @return isDirty
     */
    public boolean isDirty() {
        return isDirty;
    }

    /**
     * Set the dirty state to clean(true)
     */
    public void setClean() {
        this.isDirty = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void imgui() {
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker", imColor)) {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }

    }
}
