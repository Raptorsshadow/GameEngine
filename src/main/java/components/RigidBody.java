package components;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joml.Vector3f;
import org.joml.Vector4f;
import rubicon.Component;

/**
 *
 Class: RigidBody
 Author: rapto
 CreatedDate: 2/3/2025 : 3:34 AM
 Project: GameEngine
 Description: Manages a RigidBody Component

 */
@EqualsAndHashCode(callSuper = true)
@Data()
@NoArgsConstructor()
public class RigidBody extends Component {
    private int colliderType = 0;
    private float friction = 0.8f;
    public Vector3f velocity = new Vector3f(0, 0.5f, 0);
    public transient Vector4f tmp = new Vector4f(0,0,0,0);
}
