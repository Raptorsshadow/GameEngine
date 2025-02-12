package physics2d.rigidbody;

import component.Component;
import lombok.Data;
import org.joml.Vector2f;

@Data
public class Rigidbody2D extends Component {
    private Vector2f position = new Vector2f();
    private float rotation = 0.0f;
}
