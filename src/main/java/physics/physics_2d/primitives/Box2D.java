package physics.physics_2d.primitives;

import org.joml.Vector2f;
import util.JMath;

public class Box2D extends AABB {

    public Box2D(Vector2f min, Vector2f max) {
        super(min, max);
    }

    public Vector2f[] getVertices() {
        Vector2f min = getMin();
        Vector2f max = getMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, min.y),
                new Vector2f(max.x, max.y),
                };
        if (this.getRigidbody()
                .getRotation() != 0.0f) {
            for (Vector2f vertex : vertices) {
                JMath.rotate(vertex, this.getRigidbody()
                                         .getRotation(), this.getRigidbody()
                                                             .getPosition());
            }
        }
        return vertices;
    }
}
