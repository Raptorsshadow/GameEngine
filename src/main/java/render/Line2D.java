package render;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Class: Line2D
 * Author: rapto
 * CreatedDate: 2/8/2025 : 7:39 PM
 * Project: GameEngine
 * Description: Describes a 2D line with a set lifetime.
 */
@Data
@AllArgsConstructor
public class Line2D {
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private int      lifetime;

    public Line2D(Vector2f from, Vector2f to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Decrement and return the lifetime counter
     *
     * @return lifetime
     */
    public int beginFrame() {
        lifetime--;
        return this.lifetime;
    }

    public Vector2f getStart() {
        return this.from;
    }

    public Vector2f getEnd() {
        return this.to;
    }

    /**
     * Return the length of the vector squared
     * @return (to - from) ^ 2
     */
    public float lengthSquared() {
        return new Vector2f(to).sub(from)
                               .lengthSquared();
    }
}
