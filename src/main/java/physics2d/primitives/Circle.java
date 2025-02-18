package physics2d.primitives;

import lombok.Data;
import org.joml.Vector2f;
import physics2d.rigidbody.Rigidbody2D;

@Data
public class Circle {
    private float radius = 1.0f;
    private Rigidbody2D body  = null;
    public Circle(float radius) {
        this.radius = radius;
    }

    public Vector2f getCenter() {
        return body.getPosition();
    }
}
