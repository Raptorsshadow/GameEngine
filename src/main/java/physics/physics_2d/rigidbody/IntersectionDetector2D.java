package physics.physics_2d.rigidbody;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import physics.physics_2d.primitives.*;
import render.Line2D;
import util.JMath;

/**
 * Class: IntersectionDetector2D
 * Author: rapto
 * CreatedDate: 2/19/2025 : 10:20 PM
 * Project: GameEngine
 * Description: Collection of Collision detection utilities for various supported shapes and primitives.
 */
public class IntersectionDetector2D {
    public static final Logger log = LogManager.getLogger(IntersectionDetector2D.class);
    private IntersectionDetector2D() {
        throw new UnsupportedOperationException("Utility class");
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

    /**
     * Check if a line segment intersects a circle
     *
     * @param line   Line Segment
     * @param circle Circle
     * @return line intersects circle
     */
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

    /**
     * Check if a line segment intersects an AABB Box
     *
     * @param line Line Segment
     * @param box  AABB
     * @return line intersects box
     */
    public static boolean lineAndAABB(Line2D line, AABB box) {
        if (pointInAABB(line.getStart(), box) || pointInAABB(line.getEnd(), box)) {
            return true;
        }
        Vector2f unitVector = new Vector2f(line.getEnd()).sub(line.getStart());
        unitVector.normalize();

        unitVector.x = !Float.isNaN(unitVector.x) ? 1.0f / unitVector.x : 0f;
        unitVector.y = !Float.isNaN(unitVector.y) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(line.getStart())
           .mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(line.getStart())
           .mul(unitVector);
        float tMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tMax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tMax < 0 || tMin > tMax) {
            return false;
        }
        float t = tMin < 0f ? tMax : tMin;

        return t > 0f && t * t < line.lengthSquared();
    }

    /**
     * Check if a line segment intersects a Box2D box
     *
     * @param line Line Segment
     * @param box  Box2D
     * @return line intersects box
     */
    public static boolean lineAndBox2D(Line2D line, Box2D box) {
        float theta = -box.getRigidbody()
                          .getRotation();
        Vector2f center = box.getRigidbody()
                             .getPosition();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());
        JMath.rotate(localStart, theta, center);
        JMath.rotate(localEnd, theta, center);
        return lineAndAABB(new Line2D(localStart, localEnd), new AABB(box.getMin(), box.getMax(), box.getRigidbody()));
    }

    //
    //      Raycasts
    //

    /**
     * Check if a particular Ray will intersect a Circle.
     * Can optionally pass a RaycaseResult object to store details of the intersection.
     *
     * @param ray Ray2D
     * @param circle Circle
     * @param result Optional result object to store details of intersection if it occurs
     * @return ray intersects circle.  Populates result if not null.
     */
    public static boolean raycast(Ray2D ray, Circle circle, RaycastResult result) {
        float intersectWithCircle;

        RaycastResult.reset(result);

        Vector2f originToCircle = new Vector2f(circle.getCenter()).sub((ray.getOrigin()));
        float radiusSquared = circle.getRadius() * circle.getRadius();
        float originToCircleLengthSquared = originToCircle.lengthSquared();

        //Project the vector from the ray origin onto the direction of the ray
        float projectionLength = originToCircle.dot(ray.getDirection());
        float bSquared = originToCircleLengthSquared - projectionLength * projectionLength;

        //Check if there was a hit.  Anything less than 0 can be considered a miss/No Intersection
        if (radiusSquared - bSquared < 0.0f) {
            return false;
        }

        //Calculate the length of the projection that occurs within the bounds of the circle.
        float projectionWithinCircleLength = (float) Math.sqrt(radiusSquared - bSquared);

        /*
            Calculate intersection of ray with circle.
            If the ray starts within the circle then we add the to the projectionlength
            Otherwise we subtract.
         */
        if (originToCircleLengthSquared < radiusSquared) {
            intersectWithCircle = projectionLength + projectionWithinCircleLength;
        } else {
            intersectWithCircle = projectionLength - projectionWithinCircleLength;
        }

        //Populate Results object if passed.
        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(ray.getDirection()
                                                                  .mul(intersectWithCircle));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter())
                                                 .normalize();
            result.init(point, normal, intersectWithCircle, true);
        }

        return true;
    }
}
