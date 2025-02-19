package physics2d.rigidbody;

import component.Component;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joml.Vector2f;

@EqualsAndHashCode(callSuper = true)
@Data
public class Rigidbody2D extends Component {
    private Vector2f position = new Vector2f();
    private float    rotation = 0.0f;
}
