package physics2d.rigidbody;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;
import render.Line2D;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntersectionDetector2DTest {
    @Test
    void pointOnLineTest() {
        Vector2f point = new Vector2f(1,1);
        Line2D line = new Line2D(new Vector2f(0,0), new Vector2f(5,5));
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
        point = new Vector2f(0,0);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
        point = new Vector2f(10,10);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
        point = new Vector2f(1,2);
        assertFalse(IntersectionDetector2D.pointOnLine(point, line));
    }
}
