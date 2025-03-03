package physics.physics_2d.rigidbody;

import lombok.Getter;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

@Data
public class CollisionManifold {
    private Vector2f normal;
    private final List<Vector2f> contactPoints = new ArrayList<>();
    private float depth;
    private boolean isColliding;

    public CollisionManifold() {

    }

    public CollisionManifold(Vector2f normal, float depth) {
        this.normal = normal;
        this.depth = depth;
        this.isColliding = true;
    }

    public void addContact(Vector2f contact) {
        this.contactPoints.add(contact);
    }
}
