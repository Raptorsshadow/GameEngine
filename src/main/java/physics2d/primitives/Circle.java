package physics2d.primitives;

import lombok.Data;

@Data
public class Circle {
    private float radius = 1.0f;

    public Circle(float radius) {
        this.radius = radius;
    }
}
