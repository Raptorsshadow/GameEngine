package rubicon;

import org.lwjgl.glfw.GLFW;

public class KeyListener {

    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];

    private KeyListener() {

    }
    public static KeyListener get() {
        if(KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long ignoredWindow, int key, int ignoredScancode, int action, int ignoredMods) {
        if(action == GLFW.GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if(action == GLFW.GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
