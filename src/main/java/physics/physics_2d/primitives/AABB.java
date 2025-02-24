package physics.physics_2d.primitives;

import lombok.Data;
import org.joml.Vector2f;
import physics.physics_2d.rigidbody.Rigidbody2D;

/**
 * Class: AABB
 * Author: rapto
 * CreatedDate: 2/12/2025 : 1:06 AM
 * Project: GameEngine
 * Description: Axis Aligned Bounding Box
 */
@Data
public class AABB {

    private Vector2f    size      = new Vector2f();
    private Vector2f    halfSize  = new Vector2f();
    private Rigidbody2D rigidBody = null;

    public AABB() {
        //Default no args
    }

    public AABB(Vector2f min, Vector2f max) {
        this();
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).mul(0.5f);
    }

    public AABB(Vector2f min, Vector2f max, Rigidbody2D rigidbody) {
        this(min, max);
        this.rigidBody = rigidbody;
    }

    public Vector2f getLocalMin() {
        return new Vector2f(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getLocalMax() {
        return new Vector2f(this.rigidBody.getPosition()).add(this.halfSize);
    }

    public Vector2f[] getVertices() {
        Vector2f min = this.getLocalMin();
        Vector2f max = this.getLocalMax();
        return new Vector2f[]{
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, min.y),
                new Vector2f(max.x, max.y),
        };
    }

    public void setSize(Vector2f size) {
        this.size.set(size);
        this.halfSize.set(size.x / 2.0f, size.y / 2.0f);
    }
}
