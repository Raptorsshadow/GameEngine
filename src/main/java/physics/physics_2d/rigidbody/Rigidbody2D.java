package physics.physics_2d.rigidbody;

import component.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joml.Vector2f;
import rubicon.Transform;
import util.JMath;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rigidbody2D extends Component {
    private Transform rawTransform;
    private final Vector2f position = new Vector2f();
    private float    rotation = 0.0f;
    private float mass = 0.0f;
    private float inverseMass = 0.0f;
    private Vector2f forceAccumulator = new Vector2f();

    private Vector2f linearVelocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float linearDamping = 0.0f;
    private float angularDamping = 0.0f;

    private boolean fixedRotation = false;

    public Rigidbody2D(Vector2f position, float rotation) {
        setPosition(position);
        this.rotation = rotation;
    }

    public Rigidbody2D(Vector2f position) {
        setPosition(position);
    }

    public void setPosition(Vector2f position) {
        this.position.set(position);
    }

    public void setTransform(Vector2f position, float rotation) {
        this.position.set(position);
        this.rotation = rotation;
    }

    public void setTransform(Vector2f position) {
        this.position.set(position);
    }

    public void setMass(float mass) {
        this.mass = mass;
        this.inverseMass = !JMath.compare(this.mass, 0.0f) ? 1.0f / this.mass : 0.0f;
    }
    
    public void physicsUpdate(float dt) {
        if(JMath.compare(this.mass, 0.0f)) return;
        
        // Calculate angular velocity
        Vector2f acceleration = new Vector2f(forceAccumulator).mul(this.inverseMass);
        linearVelocity.add(acceleration.mul(dt));
        
        // Update the linear position
        this.position.add(new Vector2f(linearVelocity).mul(dt));
        
        syncCollisionTransforms();
        clearAccumulator();
    }

    private void syncCollisionTransforms() {
        if(rawTransform != null) {
            rawTransform.position.set(this.position);
        }
    }

    public void clearAccumulator() {
        this.forceAccumulator.zero();
    }

    public void addForce(Vector2f force) {
        this.forceAccumulator.add(force);
    }

    public void setRawTransform(Transform rawTransform) {
        this.rawTransform = rawTransform;
        this.position.set(rawTransform.position);
    }
}
