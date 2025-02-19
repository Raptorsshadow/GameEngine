package physics2d.primitives;

import lombok.Data;
import org.joml.Vector2f;

@Data
public class RaycastResult {
    private Vector2f point;
    private Vector2f normal;
    private float t;
    private boolean hit;

    public RaycastResult() {
        this.point = new Vector2f();
        this.normal = new Vector2f();
        this.t = -1f;
        this.hit = false;
    }

    public void init(Vector2f point, Vector2f normal, float t, boolean hit) {
        this.point.set(point);
        this.normal.set(normal);
        this.t = t;
        this.hit = hit;
    }

    public static void reset(RaycastResult result) {
        if(result != null) {
            result.init(new Vector2f(0, 0), new Vector2f(0, 0), -1, false);
        }
    }
}
