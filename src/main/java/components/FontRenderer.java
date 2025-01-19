package components;

import rubicon.Component;

/**
 * Component responsible for rendering Text in the Scene Window.
 */
public class FontRenderer extends Component {

    @Override
    public void update(float dt) {

    }

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Font Renderer");
        }
    }
}
