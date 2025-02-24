package physics.physics_2d.rigidbody;

import component.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joml.Vector2f;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Rigidbody2D extends Component {
    private final Vector2f position = new Vector2f();
    private float    rotation = 0.0f;

    private Vector2f linearVelocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float linearDamping = 0.0f;
    private float angularDamping = 0.0f;

    private boolean fixedRotation = false;

    public Rigidbody2D(Vector2f position, float rotation) {
        setPosition(position);
        this.rotation = rotation;
    }

    public Rigidbody2D(Vector2f position) {
        setPosition(position);
    }

    public void setPosition(Vector2f position) {
        this.position.set(position);
    }
    public void setTransform(Vector2f position, float rotation) {
        this.position.set(position);
        this.rotation = rotation;
    }

    public void setTransform(Vector2f position) {
        this.position.set(position);
    }
}
