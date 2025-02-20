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
    private Vector2f position = new Vector2f();
    private float    rotation = 0.0f;
}
