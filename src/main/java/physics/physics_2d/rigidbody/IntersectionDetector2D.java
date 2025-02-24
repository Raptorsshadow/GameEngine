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
        return pointInBounds(point, box.getLocalMin(), box.getLocalMax());
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
                     box.getRigidBody()
                        .getRotation(),
                     box.getRigidBody()
                        .getPosition());
        return pointInBounds(pointInSpace, box.getLocalMin(), box.getLocalMax());
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

        Vector2f min = box.getLocalMin();
        min.sub(line.getStart())
           .mul(unitVector);
        Vector2f max = box.getLocalMax();
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
        float theta = -box.getRigidBody()
                          .getRotation();
        Vector2f center = box.getRigidBody()
                             .getPosition();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());
        JMath.rotate(localStart, theta, center);
        JMath.rotate(localEnd, theta, center);
        return lineAndAABB(new Line2D(localStart, localEnd), new AABB(box.getLocalMin(), box.getLocalMax(), box.getRigidBody()));
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
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection())
                                                                  .mul(intersectWithCircle));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter())
                                                 .normalize();
            result.init(point, normal, intersectWithCircle, true);
        }

        return true;
    }

    public static boolean raycast(Ray2D ray, AABB box, RaycastResult result) {
        RaycastResult.reset(result);
        Vector2f unitVector = ray.getDirection();
        unitVector.normalize();

        unitVector.x = !Float.isNaN(unitVector.x) ? 1.0f / unitVector.x : 0f;
        unitVector.y = !Float.isNaN(unitVector.y) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getLocalMin();
        min.sub(ray.getOrigin())
                .mul(unitVector);
        Vector2f max = box.getLocalMax();
        max.sub(ray.getOrigin())
                .mul(unitVector);
        float tMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tMax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tMax < 0 || tMin > tMax) {
            return false;
        }
        float t = tMin < 0f ? tMax : tMin;

        boolean hit = t > 0f && t * t < ray.getMaximum();
        if(!hit) {
            return false;
        }

        if(result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point).normalize();
            result.init(point, normal, t, hit);
        }
        return true;
    }

    public static boolean raycast(Ray2D ray, Box2D box, RaycastResult result) {
        RaycastResult.reset(result);
        Vector2f halfSize = new Vector2f(box.getHalfSize());
        Vector2f xAxis = new Vector2f(1, 0);
        Vector2f yAxis = new Vector2f(0, 1);
        JMath.rotate(xAxis, -box.getRigidBody().getRotation(), new Vector2f(0,0));
        JMath.rotate(yAxis, -box.getRigidBody().getRotation(), new Vector2f(0,0));

        Vector2f p = new Vector2f(box.getRigidBody().getPosition()).sub(ray.getOrigin());
        //Project the direction of the ray onto each axis of box
        Vector2f f = new Vector2f(xAxis.dot(ray.getDirection()), yAxis.dot(ray.getDirection()));

        //Project p on every axis of box
        Vector2f e = new Vector2f(xAxis.dot(p), yAxis.dot(p));

        float [] tArr = {0,0,0,0};
        for( int i = 0; i < 2; i++) {
            if(JMath.compare(f.get(i), 0)) {
                //If array is parallel to current axis and origin is not inside, no hit.
                if(-e.get(i) - halfSize.get(i) > 0 || -e.get(i) + halfSize.get(i) < 0) {
                    return false;
                }
                f.setComponent(i, 0.00001f); //Set to a small value to avoid divide by zero.
            }
            tArr[i * 2] = (e.get(i) + halfSize.get(i)) / f.get(i); //TMax for the axis
            tArr[i * 2 + 1] = (e.get(i) - halfSize.get(i)) / f.get(i); //TMin for the axis
        }

        float tMin = Math.max(Math.min(tArr[0], tArr[1]), Math.min(tArr[2], tArr[3]));
        float tMax = Math.min(Math.max(tArr[0], tArr[1]), Math.max(tArr[2], tArr[3]));

        if (tMax < 0 || tMin > tMax) {
            return false;
        }
        float t = tMin < 0f ? tMax : tMin;

        boolean hit = t > 0f && t * t < ray.getMaximum();
        if(!hit) {
            return false;
        }

        if(result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point).normalize();
            result.init(point, normal, t, hit);
        }
        return true;
    }

    //
    //      Circle vs. Primitive Tests
    //

    public static boolean circleAndLine(Circle circle, Line2D line) {
        return lineAndCircle(line, circle);
    }

    public static boolean circleAndCircle(Circle c1, Circle c2) {
        Vector2f vecBetweenCenters = new Vector2f(c1.getCenter()).sub(c2.getCenter());
        float radiiSum = c1.getRadius() + c2.getRadius();
        return vecBetweenCenters.lengthSquared() <= radiiSum * radiiSum;
    }

    public static boolean circleAndAABB(Circle c1, AABB box) {
        Vector2f min = box.getLocalMin();
        Vector2f max = box.getLocalMax();
        Vector2f closestPointToCenter = new Vector2f(c1.getCenter());
        closestPointToCenter.x = Math.clamp(closestPointToCenter.x, min.x, max.x);
        closestPointToCenter.y = Math.clamp(closestPointToCenter.y, min.y, max.y);

        Vector2f circleToBox = new Vector2f(c1.getCenter()).sub(closestPointToCenter);
        return circleToBox.lengthSquared() <= c1.getRadius() * c1.getRadius();
    }

    public static boolean circleAndBox2D(Circle c1, Box2D box) {
        Vector2f min = new Vector2f();
        Vector2f max = new Vector2f(box.getSize());

        //Create a Circle in local space.
        Vector2f newCenter = new Vector2f(c1.getCenter()).sub(box.getRigidBody().getPosition());
        JMath.rotate(newCenter, -box.getRigidBody().getRotation(), min);
        Vector2f localCenterPos = new Vector2f(newCenter).add(box.getHalfSize());

        Vector2f closestPointToCenter = new Vector2f(localCenterPos);
        closestPointToCenter.x = Math.clamp(closestPointToCenter.x, min.x, max.x);
        closestPointToCenter.y = Math.clamp(closestPointToCenter.y, min.y, max.y);

        Vector2f circleToBox = new Vector2f(localCenterPos).sub(closestPointToCenter);
        return circleToBox.lengthSquared() <= c1.getRadius() * c1.getRadius();
    }

    //
    //      AABB vs. Primitive Tests
    //
    public static boolean aabbAndCircle(Circle c1, AABB box) {
        return circleAndAABB(c1, box);
    }

    public static boolean aabbAndAABB(AABB b1, AABB b2) {
        Vector2f [] axis = {new Vector2f(0, 1), new Vector2f(1, 0)};

        for(Vector2f a : axis) {
            if(!overlapOnAxis(b1, b2, a)) {
                return false;
            }
        }
        return true;
    }

    public static boolean aabbAndBox2D(AABB b1, Box2D b2) {
        Vector2f [] axis = {
                new Vector2f(0, 1), new Vector2f(1, 0),
                new Vector2f(0, 1), new Vector2f(1, 0)
        };
        //Adjust x/y axis vector Rotate for the Box2D element.
        JMath.rotate(axis[2], b2.getRigidBody().getRotation(), new Vector2f());
        JMath.rotate(axis[3], b2.getRigidBody().getRotation(), new Vector2f());

        for(Vector2f a : axis) {
            if(!overlapOnAxis(b1, b2, a)) {
                return false;
            }
        }
        return true;
    }

    private static Vector2f getInterval(AABB box, Vector2f axis) {
        Vector2f result = new Vector2f();
        Vector2f[] vertices = box.getVertices();

        float projection = axis.dot(vertices[0]);
        result.x = projection;
        result.y = projection;
        for(int i = 1; i < vertices.length; i++) {
            projection = axis.dot(vertices[i]);
            result.x = Math.min(result.x, projection);
            result.y = Math.max(result.y, projection);
        }
        return result;
    }
    private static boolean overlapOnAxis(AABB b1, AABB b2, Vector2f axis) {
        Vector2f iB1 = getInterval(b1, axis);
        Vector2f iB2 = getInterval(b2, axis);

        return iB2.x <= iB1.y && iB1.x <= iB2.y;
    }
}
