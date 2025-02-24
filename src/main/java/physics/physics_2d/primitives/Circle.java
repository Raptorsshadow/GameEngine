package physics.physics_2d.primitives;

import lombok.Data;
import org.joml.Vector2f;
import physics.physics_2d.rigidbody.Rigidbody2D;

@Data
public class Circle {
    private float       radius = 1.0f;
    private Rigidbody2D rigidBody = null;

    public Circle(float radius) {
        this.radius = radius;
    }

    public Vector2f getCenter() {
        return rigidBody.getPosition();
    }
}
