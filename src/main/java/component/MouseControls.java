package component;

import rubicon.GameObject;
import rubicon.MouseListener;
import rubicon.Window;
import scene.Settings;

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
            // Handles Snap to Grid functionality
            this.holdingObject.transform.position.x = (int) (MouseListener.getOrthoX() / Settings.GRID_WIDTH) * (float) Settings.GRID_WIDTH;
            this.holdingObject.transform.position.y = (int) (MouseListener.getOrthoY() / Settings.GRID_HEIGHT) * (float) Settings.GRID_HEIGHT;

            if (MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
