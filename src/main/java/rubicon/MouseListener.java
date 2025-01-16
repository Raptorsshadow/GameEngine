package rubicon;

import org.lwjgl.glfw.GLFW;

import java.util.stream.IntStream;

public class MouseListener {
    private double lastX, lastY, x, y;
    private double scrollX, scrollY;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private static MouseListener instance;

    private MouseListener() {
        this.lastX = 0.0;
        this.lastY = 0.0;
        this.x = 0.0;
        this.y = 0.0;
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.isDragging = false;
    }

    public static MouseListener get() {
        if(instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePosCallback(long ignoredWindow, double xPos, double yPos) {
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = xPos;
        get().y = yPos;
        get().isDragging = IntStream.range(0, get().mouseButtonPressed.length).anyMatch(pos -> get().mouseButtonPressed[pos]);
    }

    public static void mouseButtonCallback(long ignoredWindow, int button, int action, int ignoredMods) {
        if(action == GLFW.GLFW_PRESS) {
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW.GLFW_RELEASE){
            get().isDragging = false;
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
            }
        }
    }

    public static void mouseScrollCallback(long ignoredWindow, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().x;
        get().lastY = get().y;
    }

    public static float getX() {
        return (float) get().x;
    }

    public static float getY() {
        return (float) get().y;
    }

    public static float getDx() {
        return (float) (get().lastX - get().x);
    }

    public static float getDy() {
        return (float) (get().lastY - get().y);
    }
    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    static boolean isMouseButtonDown(int button) {
        if(button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }
        return false;
    }


}
