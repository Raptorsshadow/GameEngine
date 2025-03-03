package physics.physics_2d.rigidbody;

import org.joml.Vector2f;
import physics.physics_2d.primitives.Circle;

public class Collisions {
    public static CollisionManifold findCollisionFeatures(Circle c1, Circle c2) {
        CollisionManifold result = new CollisionManifold();
        float sumRadii = c1.getRadius() + c2.getRadius();
        Vector2f distance = new Vector2f(c2.getCenter()).sub(c1.getCenter());
        if (distance.lengthSquared() - (sumRadii * sumRadii) > 0) {
            return result;
        }

        //Multiply by 0.5 to separate each circle by the same amount.
        //This may change as we incorporate velocity and mass
        float depth = Math.abs(distance.length() - sumRadii) * .5f;
        Vector2f normal = new Vector2f(distance);
        normal.normalize();

        float distanceToPoint = c1.getRadius() - depth;
        Vector2f contactPoint = new Vector2f(c1.getCenter()).add(new Vector2f(normal).mul(distanceToPoint));
        CollisionManifold cm = new CollisionManifold(normal, depth);
        cm.addContact(contactPoint);
        return cm;
    }
}
