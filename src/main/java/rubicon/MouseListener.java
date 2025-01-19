package rubicon;

import org.lwjgl.glfw.GLFW;

import java.util.stream.IntStream;

/**
 * Class: MouseListener
 * Author: rapto
 * CreatedDate: 1/19/2025 : 3:00 AM
 * Project: GameEngine
 * Description: Singleton that manages user mouse input.
 */
public class MouseListener {

    //Singleton listener
    private static MouseListener instance;

    //Status array for tracking the pressed buttons
    private final boolean[] mouseButtonPressed = new boolean[3];

    //Positional tracking variables for x/y
    private double lastX;
    private double lastY;
    //Positional tracking variables for last x/y
    private double x;
    private double y;

    //Positional tracking variables for X/Y during a drag event
    private double scrollX;
    private double scrollY;

    //Track if the user is dragging the mouse (A button is pressed while moving the mouse)
    private boolean isDragging;

    /**
     * Constructor for initializing all variables.
     */
    private MouseListener() {
        this.lastX = 0.0;
        this.lastY = 0.0;
        this.x = 0.0;
        this.y = 0.0;
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.isDragging = false;
    }

    /**
     * Static initializer and retriever.
     *
     * @return MouseListener Instance
     */
    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }
        return instance;
    }

    /**
     * @param window the window that received the event
     * @param xPos   the new cursor x-coordinate, relative to the left edge of the content area
     * @param yPos   the new cursor y-coordinate, relative to the top edge of the content area
     * @see org.lwjgl.glfw.GLFWCursorPosCallbackI#invoke
     * Callback method that follows the {@link org.lwjgl.glfw.GLFWCursorPosCallbackI#invoke} interface
     */
    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = xPos;
        get().y = yPos;
        get().isDragging = IntStream.range(0, get().mouseButtonPressed.length)
                                    .anyMatch(pos -> get().mouseButtonPressed[pos]);
    }

    /**
     * @param window the window that received the event
     * @param button the mouse button that was pressed or released
     * @param action the button action. One of:<br><table><tr><td>{@link GLFW#GLFW_PRESS PRESS}</td><td>{@link GLFW#GLFW_RELEASE RELEASE}</td></tr></table>
     * @param mods   bitfield describing which modifiers keys were held down
     * @see org.lwjgl.glfw.GLFWMouseButtonCallbackI#invoke
     * Callback method that follows the {@link org.lwjgl.glfw.GLFWMouseButtonCallbackI#invoke} interface
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW.GLFW_RELEASE) {
            get().isDragging = false;
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
            }
        }
    }

    /**
     * @param window  the window that received the event
     * @param xOffset the scroll offset along the x-axis
     * @param yOffset the scroll offset along the y-axis
     * @see org.lwjgl.glfw.GLFWScrollCallbackI#invoke
     * Callback method that follows the {@link org.lwjgl.glfw.GLFWScrollCallbackI#invoke} interface
     */
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    /**
     * Drag has ended, clear out the scroll x/y vars and update last x/y
     */
    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().x;
        get().lastY = get().y;
    }

    /**
     * Get the mouse x position
     *
     * @return x
     */
    public static float getX() {
        return (float) get().x;
    }

    /**
     * Get the mouse y position
     *
     * @return y
     */
    public static float getY() {
        return (float) get().y;
    }

    /**
     * Get the Delta X between current and last x
     *
     * @return delta x
     */
    public static float getDx() {
        return (float) (get().lastX - get().x);
    }

    /**
     * Get the Delta Y between current and last y
     *
     * @return delta y
     */
    public static float getDy() {
        return (float) (get().lastY - get().y);
    }

    /**
     * Get the Scroll X
     *
     * @return scrollX
     */
    public static float getScrollX() {
        return (float) get().scrollX;
    }

    /**
     * Get the Scroll Y
     *
     * @return scrollY
     */
    public static float getScrollY() {
        return (float) get().scrollY;
    }

    /**
     * Return if the mouse is being dragged.
     *
     * @return isDragging
     */
    public static boolean isDragging() {
        return get().isDragging;
    }

    /**
     * Return if the given mouse button is pressed
     *
     * @param button the mouse button we wish to check.
     * @return true if button is pressed, false if not.
     */
    static boolean isMouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }
        return false;
    }


}
