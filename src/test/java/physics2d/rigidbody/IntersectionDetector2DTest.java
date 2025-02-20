package physics2d.rigidbody;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;
import physics2d.primitives.AABB;
import physics2d.primitives.Box2D;
import physics2d.primitives.Circle;
import render.Line2D;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionDetector2DTest {

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<IntersectionDetector2D> constructor = IntersectionDetector2D.class.getDeclaredConstructor();
        constructor.setAccessible(true); // Allow access to private constructor
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void pointOnLineTest() {
        Vector2f point = new Vector2f(1, 1);
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(5, 5));
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
        point = new Vector2f(0, 0);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
        point = new Vector2f(10, 10);
        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
        point = new Vector2f(1, 2);
        assertFalse(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    void pointInCircleTest() {
        Vector2f point = new Vector2f(0, 0);
        Circle c = new Circle(1);
        Rigidbody2D rb = new Rigidbody2D();
        rb.setPosition(new Vector2f(0, 0));
        c.setBody(rb);
        assertTrue(IntersectionDetector2D.pointInCircle(point, c));
        rb.setPosition(new Vector2f(0, 1));
        assertTrue(IntersectionDetector2D.pointInCircle(point, c));
        rb.setPosition(new Vector2f(2, 1));
        assertFalse(IntersectionDetector2D.pointInCircle(point, c));
    }

    @Test
    void pointInAABBTest() {
        AABB box = new AABB(new Vector2f(0, 0), new Vector2f(1, 1));
        Rigidbody2D rb = new Rigidbody2D();
        rb.setPosition(new Vector2f(0.5f, 0.5f));
        rb.setRotation(0f);
        box.setRigidbody(rb);
        Vector2f point = new Vector2f(0.5f, 0.5f);
        assertTrue(IntersectionDetector2D.pointInAABB(point, box));
        point = new Vector2f(0f, 0f);
        assertTrue(IntersectionDetector2D.pointInAABB(point, box));
        point = new Vector2f(2f, 0f);
        assertFalse(IntersectionDetector2D.pointInAABB(point, box));
        point = new Vector2f(-2f, 0f);
        assertFalse(IntersectionDetector2D.pointInAABB(point, box));
        point = new Vector2f(0f, 2f);
        assertFalse(IntersectionDetector2D.pointInAABB(point, box));
        point = new Vector2f(0f, -2f);
        assertFalse(IntersectionDetector2D.pointInAABB(point, box));
    }

    @Test
    void pointInBoxTest() {
        Box2D box = new Box2D(new Vector2f(0, 0), new Vector2f(1, 1));
        Rigidbody2D rb = new Rigidbody2D();
        rb.setPosition(new Vector2f(0.5f, 0.5f));
        rb.setRotation(0f);
        box.setRigidbody(rb);
        Vector2f point = new Vector2f(0.5f, 0.5f);
        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
        point = new Vector2f(0f, 0f);
        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
        point = new Vector2f(2f, 0f);
        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
        point = new Vector2f(-2f, 0f);
        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
        point = new Vector2f(0f, 2f);
        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
        point = new Vector2f(0f, -2f);
        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));
        point = new Vector2f(0f, 0f);

        rb.setRotation(45f);
        assertFalse(IntersectionDetector2D.pointInBox2D(point, box));

        rb.setRotation(180);
        assertTrue(IntersectionDetector2D.pointInBox2D(point, box));
    }
}
