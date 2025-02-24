package physics.physics_2d.forces;

import physics.physics_2d.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

public class ForceRegistry {
    private final List<ForceRegistration> registry;

    public ForceRegistry() {
        this.registry = new ArrayList<>();
    }

    public void add(Rigidbody2D rb, ForceGenerator fg) {
        registry.add(new ForceRegistration(fg, rb));
    }

    public void remove(Rigidbody2D rb, ForceGenerator fg) {
        registry.remove(new ForceRegistration(fg, rb));
    }

    public void clear() {
        registry.clear();
    }

    public void update(final float dt) {
        registry.forEach(fr -> fr.getFg().updateForce(fr.getRb(), dt));
    }

    public void zeroForces() {
        registry.forEach(ForceRegistration::zeroForces);
    }
}
