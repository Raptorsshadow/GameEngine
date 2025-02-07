package component;

import rubicon.GameObject;
import rubicon.MouseListener;
import rubicon.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

/**
 * Class: MouseControls
 * Author: rapto
 * CreatedDate: 2/4/2025 : 2:33 AM
 * Project: GameEngine
 * Description: Component for managing Mouse Interactions with the Editor Scene.
 */
public class MouseControls extends Component {
    //State manager for selected GameObject.
    private transient GameObject holdingObject = null;

    /**
     * Used to attach to a new GameObject to the scene.
     *
     * @param go GameObject being dragged
     */
    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        Window.getScene()
              .addGameObjectToScene(go);
    }

    /**
     * Release the Game Object
     */
    public void place() {
        this.holdingObject = null;
    }

    /**
     * Update the GameObject coordinates.
     *
     * @param dt Delta Time
     */
    @Override
    public void update(float dt) {
        if (this.holdingObject != null) {
            this.holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            this.holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
