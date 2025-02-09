package util;

import org.joml.Vector2f;

/**
 * Class: JMath
 * Author: rapto
 * CreatedDate: 2/8/2025 : 10:04 PM
 * Project: GameEngine
 * Description: Helper class for mutating Vectors.
 */
public class JMath {

    private JMath() {
        //Hide default constructor for static classes.
    }

    /**
     * Rotate a vector around an origin by the given angle
     * @param vec Vertex to rotate
     * @param angleDeg Degree of rotation
     * @param origin Origin of rotation
     */
    public static void rotate(Vector2f vec, float angleDeg, Vector2f origin) {
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        float cos = (float) Math.cos(Math.toRadians(angleDeg));
        float sin = (float) Math.sin(Math.toRadians(angleDeg));

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        xPrime += origin.x;
        yPrime += origin.y;

        vec.x = xPrime;
        vec.y = yPrime;
    }

    /**
     * Compare 2 float values with a given margin of error
     * @param f1 float 1
     * @param f2 float 2
     * @param epsilon margin of error
     * @return true if f1 and f2 are within epsilon
     */
    public static boolean compare(float f1, float f2, float epsilon) {
        return Math.abs(f1 - f2) <= epsilon * Math.max(1.0f, Math.max(Math.abs(f1), Math.abs(f2)));
    }

    /**
     * Compare 2 Vector2f values within a given margin of error
     * @param vec1 first Vector2f
     * @param vec2 second Vector2f
     * @param epsilon margin of error
     * @return true if the x and y coordinates of vec1 and vec2 are within epsilon
     */
    public static boolean compare(Vector2f vec1, Vector2f vec2, float epsilon) {
        return compare(vec1.x, vec2.x, epsilon) && compare(vec1.y, vec2.y, epsilon);
    }

    /**
     * Compare 2 floats values, defaults to Float.MIN_VALUE for margin of error
     * @param f1 float 1
     * @param f2 float 2
     * @return true if f1 and f2 are within Float.MIN_VALUE of each other.
     */
    public static boolean compare(float f1, float f2) {
        return compare(f1, f2, Float.MIN_VALUE);
    }

    /**
     * Compare 2 Vector2f values, default to Float.MIN_VALUE for the margin of error
     * @param vec1 first Vector2f
     * @param vec2 second Vector2f
     * @return true if the x and y coordinates of vec1 and vec2 are withing Float.MIN_VALUE of each other.
     */
    public static boolean compare(Vector2f vec1, Vector2f vec2) {
        return compare(vec1.x, vec2.x) && compare(vec1.y, vec2.y);
    }
}