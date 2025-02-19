package util;

import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class: JMathTest
 * Author: rapto
 * CreatedDate: 2/18/2025 : 11:58 PM
 * Project: GameEngine
 * Description: Unit Tests for JMath Utility
 */
class JMathTest {
    private static final float EPSILON = 1e-6f;

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<JMath> constructor = JMath.class.getDeclaredConstructor();
        constructor.setAccessible(true); // Allow access to private constructor
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void testRotate_90DegreesAroundOrigin() {
        Vector2f vec = new Vector2f(1, 0);
        Vector2f origin = new Vector2f(0, 0);
        JMath.rotate(vec, 90, origin);
        assertTrue(JMath.compare(vec, new Vector2f(0, 1), EPSILON));
    }

    @Test
    void testRotate_180DegreesAroundOrigin() {
        Vector2f vec = new Vector2f(1, 0);
        Vector2f origin = new Vector2f(0, 0);
        JMath.rotate(vec, 180, origin);
        assertTrue(JMath.compare(vec, new Vector2f(-1, 0), EPSILON));
    }

    @Test
    void testRotate_360Degrees() {
        Vector2f vec = new Vector2f(2, 3);
        Vector2f origin = new Vector2f(0, 0);
        Vector2f original = new Vector2f(vec);
        JMath.rotate(vec, 360, origin);
        assertTrue(JMath.compare(vec, original, EPSILON));
    }

    @Test
    void testRotate_AroundDifferentOrigin() {
        Vector2f vec = new Vector2f(2, 2);
        Vector2f origin = new Vector2f(1, 1);
        JMath.rotate(vec, 90, origin);
        assertTrue(JMath.compare(vec, new Vector2f(0, 2), EPSILON));
    }

    @Test
    void testCompare_FloatsEqual() {
        assertTrue(JMath.compare(1.0000001f, 1.0000002f, EPSILON));
    }

    @Test
    void testCompare_FloatsNotEqual() {
        assertFalse(JMath.compare(1.0f, 1.1f, EPSILON));
    }

    @Test
    void testCompare_FloatsWithDefaultEpsilon() {
        assertTrue(JMath.compare(1.0f, 1.0f + Float.MIN_VALUE));
    }

    @Test
    void testCompare_VectorsEqual() {
        Vector2f v1 = new Vector2f(1.0000001f, 2.0000001f);
        Vector2f v2 = new Vector2f(1.0000002f, 2.0000002f);
        assertTrue(JMath.compare(v1, v2, EPSILON));
    }

    @Test
    void testCompare_VectorsNotEqual_XDiffers() {
        Vector2f v1 = new Vector2f(1.0f, 2.0f);
        Vector2f v2 = new Vector2f(1.1f, 2.0f);
        assertFalse(JMath.compare(v1, v2));
        assertFalse(JMath.compare(v1, v2, EPSILON));
    }

    @Test
    void testCompare_VectorsNotEqual_YDiffers() {
        Vector2f v1 = new Vector2f(1.0f, 2.0f);
        Vector2f v2 = new Vector2f(1.0f, 2.1f);
        assertFalse(JMath.compare(v1, v2));
        assertFalse(JMath.compare(v1, v2, EPSILON));
    }

    @Test
    void testCompare_VectorsEqualWithDefaultEpsilon() {
        Vector2f v1 = new Vector2f(1.0f, 2.0f);
        Vector2f v2 = new Vector2f(1.0f + Float.MIN_VALUE, 2.0f + Float.MIN_VALUE);
        assertTrue(JMath.compare(v1, v2));
    }
}
