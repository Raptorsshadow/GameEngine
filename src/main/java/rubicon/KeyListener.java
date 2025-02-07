package rubicon;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Class: KeyListener
 * Author: rapto
 * CreatedDate: 1/19/2025 : 2:20 AM
 * Project: GameEngine
 * Description: Singleton that manages user keyboard input.
 */
public class KeyListener implements GLFWKeyCallbackI {

    // Private singleton instance
    private static KeyListener instance;

    // keypress tracking array (350 ensures that all constants can be safely stored without modification)
    private final boolean[] keyPressed = new boolean[350];

    // Private default constructor to prevent multiple instances.
    private KeyListener() {

    }

    /**
     * Static initializer and retriever.
     *
     * @return KeyListener instance
     */
    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    /**
     * Returns if a given keyCode is pressed
     *
     * @param keyCode Key we're interested
     * @return true if key pressed, false otherwise
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

    /**
     * @param window   the window that received the event
     * @param key      the keyboard key that was pressed or released
     * @param scancode the platform-specific scancode of the key
     * @param action   the key action. One of:<br><table><tr><td>{@link GLFW#GLFW_PRESS PRESS}</td><td>{@link GLFW#GLFW_RELEASE RELEASE}</td><td>{@link GLFW#GLFW_REPEAT REPEAT}</td></tr></table>
     * @param mods     bitfield describing which modifiers keys were held down
     * @see org.lwjgl.glfw.GLFWKeyCallbackI#invoke
     * Callback method that follows the {@link org.lwjgl.glfw.GLFWKeyCallbackI#invoke} interface
     */
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }
}
