package component;

import rubicon.GameObject;
import rubicon.MouseListener;
import rubicon.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;


public class MouseControls extends Component{
    GameObject holdingObject = null;
    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        Window.getScene().addGameObjectToScene(go);
    }

    public void place() {
        this.holdingObject = null;
    }

    public void update(float dt) {
        if(this.holdingObject != null) {
            this.holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            this.holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if(MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
