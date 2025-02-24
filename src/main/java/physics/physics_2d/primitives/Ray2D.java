package physics.physics_2d.primitives;

import lombok.Data;
import org.joml.Vector2f;

@Data
public class Ray2D {
    private Vector2f origin;
    private Vector2f direction;
    private float maximum = Float.MAX_VALUE;

    public Ray2D(Vector2f origin, Vector2f direction) {
        this.origin = origin;
        this.direction = direction;
        this.direction.normalize();
    }
}
