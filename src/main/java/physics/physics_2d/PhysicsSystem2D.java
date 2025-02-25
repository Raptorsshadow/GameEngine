package physics.physics_2d;

import org.joml.Vector2f;
import physics.physics_2d.forces.ForceRegistry;
import physics.physics_2d.forces.Gravity2D;
import physics.physics_2d.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem2D {
    private final ForceRegistry forceRegistry;
    private final List<Rigidbody2D> rigidBodies;
    private final Gravity2D gravity;
    private final float fixedUpdate;

    public PhysicsSystem2D(float fixedUpdateDt, Vector2f gravity) {
        this.forceRegistry = new ForceRegistry();
        this.rigidBodies = new ArrayList<>();
        this.fixedUpdate = fixedUpdateDt;
        this.gravity = new Gravity2D(gravity);
    }

    public void update(float dt) {
        fixedUpdate();
    }

    public void fixedUpdate() {
        forceRegistry.update(fixedUpdate);
        //Update the velocities of all rigidbodies
        rigidBodies.forEach(rb -> rb.physicsUpdate(fixedUpdate));
    }

    public void addRigidBody(Rigidbody2D rb) {
        this.rigidBodies.add(rb);
        //Register Gravity
        this.forceRegistry.add(rb, gravity);
    }
}
