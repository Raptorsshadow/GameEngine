package physics.physics_2d.primitives;

import org.joml.Vector2f;
import util.JMath;

public class Box2D extends AABB {

    public Box2D(Vector2f min, Vector2f max) {
        super(min, max);
    }

    @Override
    public Vector2f[] getVertices() {
        Vector2f [] vertices = super.getVertices();
        if (getRigidBody().getRotation() != 0.0f) {
            for (Vector2f vertex : vertices) {
                JMath.rotate(
                        vertex,
                        getRigidBody().getRotation(),
                        getRigidBody().getPosition()
                );
            }
        }
        return vertices;
    }
}
