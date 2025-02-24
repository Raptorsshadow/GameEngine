package physics.physics_2d.forces;

import kotlin.NotImplementedError;
import lombok.AllArgsConstructor;
import lombok.Data;
import physics.physics_2d.rigidbody.Rigidbody2D;

import java.util.Objects;

@AllArgsConstructor
@Data
public class ForceRegistration {
    private ForceGenerator fg;
    private Rigidbody2D rb;

    @Override
    public boolean equals(Object other) {
        if(other instanceof ForceRegistration fr) {
            return fr.getRb() == this.rb && fr.fg == this.fg;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fg, rb);
    }

    public void zeroForces() {
        throw new NotImplementedError();
    }
}
