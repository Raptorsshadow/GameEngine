package render;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Data
@AllArgsConstructor
public class Line2D {
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private int lifetime;

    public int beginFrame() {
        lifetime--;
        return this.lifetime;
    }
}
