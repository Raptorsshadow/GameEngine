package components;

import org.joml.Vector4f;
import rubicon.Component;

/**
 * Class: SpriteRenderer
 * Author: rapto
 * CreatedDate: 1/19/2025 : 12:52 AM
 * Project: GameEngine
 * Description: Component reponsible for rendering Text to a Scene
 */
public class SpriteRenderer extends Component {

    // 4 bit Color variable (RGBA)
    private final Vector4f color;

    /**
     * Default Constructor taking in color Vector
     * @param color Color Vector
     */
    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }
    /**
     * {@inheritDoc}
     *
     * @param dt Delta time between calls.
     */
    @Override
    public void update(float dt) {
        //Does Nothing
    }

    /**
     * Returns color vector.
     * @return color
     */
    public Vector4f getColor() {
        return color;
    }
}
