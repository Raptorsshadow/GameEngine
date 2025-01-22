package rubicon;

import org.joml.Vector2f;

/**
 * Class: Transform
 * Author: rapto
 * CreatedDate: 1/22/2025 : 11:10 AM
 * Project: GameEngine
 * Description: Stores transformation data to be used when rendering resources.
 */
public class Transform {

    // X/Y Position data
    public final Vector2f position;
    // X/Y Scaling data
    public final Vector2f scale;

    /**
     * Default Constructor
     */
    public Transform() {
        this(new Vector2f(), new Vector2f());
    }

    /**
     * Override constructor accepting positional data
     *
     * @param position X/Y Position coordinates
     */
    public Transform(Vector2f position) {
        this(position, new Vector2f());
    }

    /**
     * Override constructor accepting positional and scaling data
     *
     * @param position X/Y Position coordinates
     * @param scale    X/Y Scaling data
     */
    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }
}
