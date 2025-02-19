package physics2d.rigidbody;

import org.joml.Vector2f;
import physics2d.primitives.AABB;
import physics2d.primitives.Box2D;
import physics2d.primitives.Circle;
import render.Line2D;
import util.JMath;

/**
 *
 */
public class IntersectionDetector2D {
    private IntersectionDetector2D() {
        //Hidden static util Constructor.
    }
    //
    //    Point vs Primitive Tests
    //

    /**
     * Test if a point is on a given line segment.
     *
     * @param point 2d Coordinate
     * @param line  Line Segment
     * @return Point is on the line
     */
    public static boolean pointOnLine(Vector2f point, Line2D line) {
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;
        float slope = dy / dx;
        float yIntercept = line.getEnd().y - (slope * line.getEnd().x);

        return JMath.compare(point.y, slope * point.x + yIntercept);
    }

    /**
     * Test if a point is inside a given circle.
     *
     * @param point  2d Coordinate
     * @param circle Circle
     * @return Point is contained within circle
     */
    public static boolean pointInCircle(Vector2f point, Circle circle) {
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);
        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    /**
     * Test if a point is inside a given Axis Aligned Bounding Box (AABB)
     *
     * @param point 2d Coordinate
     * @param box   Axis Aligned Bounding Box
     * @return Point is contained within the AABB
     */
    public static boolean pointInAABB(Vector2f point, AABB box) {
        return pointInBounds(point, box.getMin(), box.getMax());
    }

    /**
     * test if a point is inside a given 2D Box
     *
     * @param point 2d Coordinate
     * @param box   Bounding Box
     * @return Point is contained within the box.
     */
    public static boolean pointInBox2D(Vector2f point, Box2D box) {
        Vector2f pointInSpace = new Vector2f(point);
        JMath.rotate(pointInSpace,
                     box.getRigidbody()
                        .getRotation(),
                     box.getRigidbody()
                        .getPosition());
        return pointInBounds(pointInSpace, box.getMin(), box.getMax());
    }

    /**
     * Test if a given point is contained by given min and max bounding edges.
     *
     * @param point 2d Coordinate
     * @param min   2d min coordinate of bounding box
     * @param max   2d max coordinate of bounding box
     * @return Point is contained within min and max.
     */
    private static boolean pointInBounds(Vector2f point, Vector2f min, Vector2f max) {
        return point.x <= max.x && point.x >= min.x &&
               point.y <= max.y && point.y >= min.y;
    }

    //
    //    Line Vs Primitive Tests
    //

    public static boolean lineAndCircle(Line2D line, Circle circle) {
        //check if the segment starts or ends in the circle.  Can return immediately
        if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)) {
            return true;
        }
        //Get the length of the given line segment as a vector
        Vector2f ab = new Vector2f(line.getEnd()).sub(line.getStart());

        //Create a vector from the segment start to the center of the circle.
        Vector2f centerToLineStart = new Vector2f(circle.getCenter()).sub(line.getStart());

        //Calculate the dot product of of circSeg (DOT) ab / ab (DOT) ab
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        //If t is less than 0 or greater than 1 we're outside the bounds of the line segment.  Return false
        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        //Project the centerToLineStart on the original segment to find closest point
        Vector2f closestPoint = new Vector2f(line.getStart()).add(ab.mul(t));

        // Test if closes point is within the bounds of the circle
        return pointInCircle(closestPoint, circle);
    }


}
