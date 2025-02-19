package rubicon;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CameraTest {
    @Test
    void constructorTest() {
        Vector2f pos = new Vector2f(10f, 10f);
        Camera c = new Camera(pos);
        assertEquals(pos, c.getPosition());
        assertNotNull(c.getProjectionMatrix());
        assertNotNull(c.getViewMatrix());
    }

    @Test
    void projectionMatrixTest() {
        Matrix4f proj = new Matrix4f();
        proj.identity();
        proj.ortho(0f, Camera.RIGHT_LENGTH, 0f, Camera.TOP_LENGTH, Camera.NEAR_DIST, Camera.FAR_DIST);
        Vector2f pos = new Vector2f(1f, 1f);
        Camera c = new Camera(pos);
        Matrix4f cProj = c.getProjectionMatrix();
        assertEquals(proj, cProj);
    }
}
