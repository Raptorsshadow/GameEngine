package physics2d.primitives;

import org.joml.Vector2f;
import physics2d.rigidbody.Rigidbody2D;

/**
 *
 Class: AABB
 Author: rapto
 CreatedDate: 2/12/2025 : 1:06 AM
 Project: GameEngine
 Description: Axis Aligned Bounding Box

 */
public class AABB {

    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private Rigidbody2D rigidbody = null;
    public AABB() {
        //Default no args
        this.halfSize = new Vector2f(size).mul(0.5f);
    }
    public AABB(Vector2f min, Vector2f max) {
        this();
        this.size = new Vector2f(max).sub(min);
    }

    public Vector2f getMin() {
        return new Vector2f(this.rigidbody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax() {
        return new Vector2f(this.rigidbody.getPosition()).add(this.halfSize);
    }
}
