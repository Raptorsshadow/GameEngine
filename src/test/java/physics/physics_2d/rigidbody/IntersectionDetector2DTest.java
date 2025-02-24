package physics.physics_2d.rigidbody;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;
import physics.physics_2d.primitives.*;
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
        Rigidbody2D rb = new Rigidbody2D(new Vector2f(0, 0));
        c.setRigidBody(rb);
        assertTrue(IntersectionDetector2D.pointInCircle(point, c));
        rb.setPosition(new Vector2f(0, 1));
        assertTrue(IntersectionDetector2D.pointInCircle(point, c));
        rb.setPosition(new Vector2f(2, 1));
        assertFalse(IntersectionDetector2D.pointInCircle(point, c));
    }

    @Test
    void pointInAABBTest() {
        AABB box = new AABB(new Vector2f(0, 0), new Vector2f(1, 1));
        Rigidbody2D rb = new Rigidbody2D(new Vector2f(0.5f, 0.5f), 0);
        box.setRigidBody(rb);
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
        Rigidbody2D rb = new Rigidbody2D(new Vector2f(0.5f, 0.5f), 0);
        box.setRigidBody(rb);
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

    @Test
    void lineAndCircleTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(10, 10));
        Circle circle = new Circle(3);
        Rigidbody2D rb = new Rigidbody2D(new Vector2f(5, 5), 0);
        circle.setRigidBody(rb);
        assertTrue(IntersectionDetector2D.lineAndCircle(line, circle));

        //Handle To or From within the circle itself
        line.setFrom(new Vector2f(5, 5));
        assertTrue(IntersectionDetector2D.lineAndCircle(line, circle));
        line.setFrom(new Vector2f(0, 0));
        line.setTo(new Vector2f(6, 6));
        assertTrue(IntersectionDetector2D.lineAndCircle(line, circle));

        //Handle Miss
        line.setFrom(new Vector2f(10, 10));
        line.setTo(new Vector2f(12, 12));
        assertFalse(IntersectionDetector2D.lineAndCircle(line, circle));
        line.setFrom(new Vector2f(-12, -12));
        line.setTo(new Vector2f(-10, -10));
        assertFalse(IntersectionDetector2D.lineAndCircle(line, circle));
    }

    @Test
    void lineAndAABBTest() {
        AABB box = new AABB(new Vector2f(0, 0), new Vector2f(1, 1));
        Rigidbody2D rb = new Rigidbody2D(new Vector2f(0.5f, 0.5f), 0);
        box.setRigidBody(rb);
        Line2D line = new Line2D(new Vector2f(-3,-3), new Vector2f(5, 5));
        assertTrue(IntersectionDetector2D.lineAndAABB(line, box));

        //Handle To or From within the circle itself
        line.setFrom(new Vector2f(0, 0));
        assertTrue(IntersectionDetector2D.lineAndAABB(line, box));
        line.setFrom(new Vector2f(-5, -5));
        line.setTo(new Vector2f(0, 0));
        assertTrue(IntersectionDetector2D.lineAndAABB(line, box));

        //Handle a 0 length unit vector
        line.setFrom(new Vector2f(12, 12));
        line.setTo(new Vector2f(12, 12));
        assertFalse(IntersectionDetector2D.lineAndAABB(line, box));

        //Handle Miss
        line.setFrom(new Vector2f(10, 10));
        line.setTo(new Vector2f(12, 12));
        assertFalse(IntersectionDetector2D.lineAndAABB(line, box));
        line.setFrom(new Vector2f(12, 12));
        line.setTo(new Vector2f(10, 10));
        assertFalse(IntersectionDetector2D.lineAndAABB(line, box));
        line.setFrom(new Vector2f(-10, 10));
        line.setTo(new Vector2f(12, 12));
        assertFalse(IntersectionDetector2D.lineAndAABB(line, box));
        line.setFrom(new Vector2f(12, 12));
        line.setTo(new Vector2f(10, -10));
        assertFalse(IntersectionDetector2D.lineAndAABB(line, box));
    }

    @Test
    void lineAndBox2DTest() {
        Box2D box = new Box2D(new Vector2f(0, 0), new Vector2f(1, 1));
        Rigidbody2D rb = new Rigidbody2D(new Vector2f(0.5f, 0.5f), 0);
        box.setRigidBody(rb);
        Line2D line = new Line2D(new Vector2f(-3,-3), new Vector2f(5, 5));
        assertTrue(IntersectionDetector2D.lineAndBox2D(line, box));

        //Handle To or From within the circle itself
        line.setFrom(new Vector2f(0, 0));
        assertTrue(IntersectionDetector2D.lineAndBox2D(line, box));
        line.setFrom(new Vector2f(-5, -5));
        line.setTo(new Vector2f(0, 0));
        assertTrue(IntersectionDetector2D.lineAndBox2D(line, box));

        //Handle a 0 length unit vector
        line.setFrom(new Vector2f(12, 12));
        line.setTo(new Vector2f(12, 12));
        assertFalse(IntersectionDetector2D.lineAndBox2D(line, box));

        //Handle Miss
        line.setFrom(new Vector2f(10, 10));
        line.setTo(new Vector2f(12, 12));
        assertFalse(IntersectionDetector2D.lineAndBox2D(line, box));
        line.setFrom(new Vector2f(12, 12));
        line.setTo(new Vector2f(10, 10));
        assertFalse(IntersectionDetector2D.lineAndBox2D(line, box));
        line.setFrom(new Vector2f(-10, 10));
        line.setTo(new Vector2f(12, 12));
        assertFalse(IntersectionDetector2D.lineAndBox2D(line, box));
        line.setFrom(new Vector2f(12, 12));
        line.setTo(new Vector2f(10, -10));
        assertFalse(IntersectionDetector2D.lineAndBox2D(line, box));
    }

    @Test
    void raycastCircleTest() {
        Ray2D ray = new Ray2D(new Vector2f(0, 0), new Vector2f(1, 1));
        Circle circle = new Circle(3);
        Rigidbody2D rb = new Rigidbody2D(new Vector2f(5, 5), 0);
        circle.setRigidBody(rb);
        assertTrue(IntersectionDetector2D.raycast(ray, circle, null));
        ray = new Ray2D(new Vector2f(-10,0), new Vector2f(-1, -1));
        assertFalse(IntersectionDetector2D.raycast(ray, circle, null));

        //test starting within circle
        ray = new Ray2D(new Vector2f(5,5), new Vector2f(-1, -1));
        assertTrue(IntersectionDetector2D.raycast(ray, circle, null));

        RaycastResult rr = new RaycastResult();
        assertFalse(rr.isHit());
        assertEquals(new Vector2f(0,0), rr.getPoint());
        assertEquals(new Vector2f(0,0), rr.getNormal());
        assertEquals(-1f, rr.getT());
        assertTrue(IntersectionDetector2D.raycast(ray, circle, rr));
        assertTrue(rr.isHit());
        assertNotEquals(new Vector2f(0,0), rr.getPoint());
        assertNotEquals(new Vector2f(0,0), rr.getNormal());
        assertNotEquals(-1f, rr.getT());
    }
}
