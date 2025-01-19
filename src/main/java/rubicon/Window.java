package rubicon;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Class: Window
 * Author: rapto
 * CreatedDate: 1/19/2025 : 3:27 AM
 * Project: GameEngine
 * Description: This singleton is the main runtime of the Engine and is responsible for managing lifecycle of the engine and swapping
 * scenes as necessary.
 */
public class Window {

    // Default Resolution WIDTH and HEIGHT
    public static final int DEFAULT_WIDTH  = 1920;
    public static final int DEFAULT_HEIGHT = 1080;

    // Window singleton reference
    public static Window window;

    // Provisioned identifier for the Window
    public static long glfwWindow;

    // The active scene
    private static Scene currentScene;

    // Window title
    private final String title;

    //Default background colors for RGBA channels
    public float r = 1;
    public float g = 1;
    public float b = 1;
    public float a = 1;

    //Actual resolution values for the window.
    private int width;
    private int height;

    /**
     * Default Constructor taking window initialization params
     *
     * @param width  Width in pixels
     * @param height Height in pixels
     * @param title  Title bar for the Window
     */
    private Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
     * Instance initializer and retriever that passes a width, height and title to the window.
     *
     * @param width  Width in pixels
     * @param height Height in pixels
     * @param title  Title text for the window
     * @return Instance of the window
     */
    public static Window get(int width, int height, String title) {
        if (Window.window == null) {
            Window.window = new Window(width, height, title);
        }

        return Window.window;
    }

    /**
     * Instance initializer and retriever that passes a title to the window, leveragin default width and height values.
     *
     * @param title Title text for the window
     * @return Instance of the window
     */
    public static Window get(String title) {
        return get(Window.DEFAULT_WIDTH, Window.DEFAULT_HEIGHT, title);
    }

    /**
     * Default initializer that will create a window with default values if first call.
     *
     * @return Instance of the window
     */
    public static Window get() {
        return get(DEFAULT_WIDTH, DEFAULT_HEIGHT, "Tester");

    }

    /**
     * Helper method responsible for swapping a scene.
     *
     * @param sceneId scene identifier
     */
    public static void changeScene(int sceneId) {
        switch (sceneId) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : String.format("Unrecognized scene: %d", sceneId);
        }
        currentScene.init();
        currentScene.start();
    }

    /**
     * Listener responsible for updating the window width and height values on resize.
     *
     * @param window Window Identifier
     * @param width  new width
     * @param height new height
     */
    private static void sizeListener(long window, int width, int height) {
        Window.get().width = width;
        Window.get().height = height;
    }

    /**
     * Method responsible for allocating the window, looping the runtime and freeing resources on close.
     */
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        //Initialize the window
        init();

        //Loop until exit.
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null))
               .free();
    }

    /**
     * Initialize the window setting up error handling, binding event listeners, configuring window behavior, and
     * loading the first scene.
     */
    private void init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err)
                         .set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        //Register listeners to window
        registerListeners();

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);
    }

    /**
     * Register Listeners for Mouse, Keyboard, Window, etc...
     */
    private void registerListeners() {
        //Register Event Listeners
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, Window::sizeListener);
    }

    /**
     * Execution loop for the Engine.  Runs until the window is closed rendering the active scene.
     */
    private void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            // Set the clear color
            //Seizure Mode
            //glClearColor(r.nextFloat(0, 1), r.nextFloat(0, 1), r.nextFloat(0, 1), r.nextFloat(0, 1));
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer

            //If dt isn't 0, we call update on the scene.
            if (dt >= 0) {
                currentScene.update(dt);
            }

            // swap the color buffers
            glfwSwapBuffers(glfwWindow);

            //Calculate Delta Time
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
