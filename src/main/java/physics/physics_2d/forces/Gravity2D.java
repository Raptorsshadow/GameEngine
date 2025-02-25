package physics.physics_2d.forces;

import lombok.Data;
import org.joml.Vector2f;
import physics.physics_2d.rigidbody.Rigidbody2D;

@Data
public class Gravity2D implements ForceGenerator {

    private Vector2f gravity = new Vector2f();

    public Gravity2D(Vector2f force) {
        this.gravity.set(force);
    }
    @Override
    public void updateForce(Rigidbody2D body, float dt) {
        body.addForce(new Vector2f(gravity).mul(body.getMass()));
    }
}
