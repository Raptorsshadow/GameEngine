package physics2d.primitives;

import lombok.Data;
import org.joml.Vector2f;

/**
 * Class: RaycastResult
 * Author: rapto
 * CreatedDate: 2/20/2025 : 3:07 AM
 * Project: GameEngine
 * Description: Store the results of a ray cast.
 */
@Data
public class RaycastResult {
    private Vector2f point;
    private Vector2f normal;
    private float    t;
    private boolean  hit;

    public RaycastResult() {
        this.point = new Vector2f();
        this.normal = new Vector2f();
        this.t = -1f;
        this.hit = false;
    }

    /**
     * Reset the state of a given result.
     *
     * @param result RaycastResult to be reset
     */
    public static void reset(RaycastResult result) {
        if (result != null) {
            result.init(new Vector2f(0, 0), new Vector2f(0, 0), -1, false);
        }
    }

    /**
     * Populate result with given values
     *
     * @param point  Point of intersection
     * @param normal Normal vector of the intersection
     * @param t      Vector length from ray to the circle
     * @param hit    Did the Raycast intersect
     */
    public void init(Vector2f point, Vector2f normal, float t, boolean hit) {
        this.point.set(point);
        this.normal.set(normal);
        this.t = t;
        this.hit = hit;
    }
}
