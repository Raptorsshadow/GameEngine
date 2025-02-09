package rubicon;

import org.joml.Vector2f;

import java.util.Objects;

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

    /**
     * Return a copy of this Transform
     *
     * @return new Transform
     */
    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    /**
     * Update data from this transform into the given transform.
     *
     * @param to Transform to update
     */
    public void copyTo(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Transform t) {
            return t.position.equals(this.position) && t.scale.equals(this.scale);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, scale);
    }
}
