package rubicon;

import imgui.app.Color;
import imgui.app.Configuration;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Class: Window
 * Author: rapto
 * CreatedDate: 1/19/2025 : 3:27 AM
 * Project: GameEngine
 * Description: This singleton is the main runtime of the Engine and is responsible for managing lifecycle of the engine and swapping
 * scenes as necessary.
 */
public class Window {
    // Window singleton reference
    public static   Window window;
    //Default background colors for RGBA channels
    protected final Color  colorBg = new Color(1, 1, 1, 1);

    // Window config
    private final Configuration config;
    //ImGui Layer used to render overlays.
    private final IMGuiLayer    guiLayer;
    //Actual resolution values for the window.
    float dt = -1.0f;
    // Provisioned identifier for the Window
    private long  glfwWindow;
    // The active scene
    private Scene currentScene;

    /**
     * Default Constructor taking window initialization params
     *
     * @param config   Window Configuration Data
     * @param guiLayer ImGuiLayer Object that manages ImGui resources
     */
    private Window(Configuration config, IMGuiLayer guiLayer) {
        this.config = config;
        this.guiLayer = guiLayer;
    }

    /**
     * Instance accessor and retriever that passes a config and IMGuiLayer to initialize the window.
     *
     * @param config   Window Configuration Object
     * @param guiLayer ImGuiLayer Object that manages ImGui resources
     * @return Instance of the window
     */
    public static Window get(Configuration config, IMGuiLayer guiLayer) {
        if (Window.window == null) {
            Window.window = new Window(config, guiLayer);
            Window.window.init();
        }

        return Window.window;
    }

    /**
     * Default accessor that will create a window with default values if first call.
     *
     * @return Instance of the window
     */
    public static Window get() {
        if (Window.window == null) {
            final Configuration config = new Configuration();
            config.setHeight(1080);
            config.setWidth(1920);
            config.setTitle("Test with IMGui");
            Window.window = new Window(config, new IMGuiLayer());
            Window.window.init();
        }
        return Window.window;
    }

    /**
     * Helper method responsible for swapping a scene.
     *
     * @param sceneId scene identifier
     */
    public static void changeScene(int sceneId) {
        Window w = Window.get();

        switch (sceneId) {
            case 0:
                if (w.currentScene != null) {
                    w.currentScene.dispose();
                }
                w.currentScene = new LevelEditorScene();
                break;
            case 1:
                if (w.currentScene != null) {
                    w.currentScene.dispose();
                }
                w.currentScene = new LevelScene();
                break;
            default:
                assert false : String.format("Unrecognized scene: %d", sceneId);
        }
        w.currentScene.init();
        w.currentScene.start();
    }

    /**
     * Get the Active Scene
     *
     * @return Currently Active Scene in the Window
     */
    public static Scene getScene() {
        return get().currentScene;
    }

    /**
     * Listener responsible for updating the window width and height values on resize.
     *
     * @param window Window Identifier
     * @param width  new width
     * @param height new height
     */
    private static void sizeListener(long window, int width, int height) {
        Window.get().config.setWidth(width);
        Window.get().config.setHeight(height);
    }

    /**
     * Retrieve Window Width
     *
     * @return window width
     */
    public static int getWidth() {
        return get().config.getWidth();
    }

    /**
     * Retrieve Window Height
     *
     * @return window height
     */
    public static int getHeight() {
        return get().config.getHeight();
    }

    public static long getWindowId() {
        return get().glfwWindow;
    }

    /**
     * Accessor to retrieve Window Background Color
     *
     * @return window background color
     */
    public static Color getBackgroundColor() {
        return get().colorBg;
    }

    /**
     * Initialize the Window and IMGui Framework.
     */
    public void init() {
        initWindow();
        guiLayer.init();
    }

    /**
     * Method to dispose all used application resources and destroy its window.
     */
    protected void dispose() {
        disposeWindow();
        guiLayer.dispose();
    }

    /**
     * Method responsible for allocating the window, looping the runtime and freeing resources on close.
     */
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        //Loop until exit.
        this.loop();

        //Dispose of the window and release resources
        this.dispose();
    }

    /**
     * Initialize and configure the Window
     */
    protected void initWindow() {
        GLFWErrorCallback.createPrint(System.err)
                         .set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }


        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindow = glfwCreateWindow(config.getWidth(), config.getHeight(), config.getTitle(), MemoryUtil.NULL,
                                      MemoryUtil.NULL);

        if (glfwWindow == MemoryUtil.NULL) {
            throw new IllegalArgumentException("Failed to create the GLFW window");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default

        //Register listeners to window
        registerListeners();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer pWidth = stack.mallocInt(1); // int*
            final IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(glfwWindow, pWidth, pHeight);
            final GLFWVidMode vidmode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
            glfwSetWindowPos(glfwWindow, (vidmode.width() - pWidth.get(0)) / 2,
                             (vidmode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(glfwWindow);

        GL.createCapabilities();

        glfwSwapInterval(GLFW_TRUE);

        if (config.isFullScreen()) {
            glfwMaximizeWindow(glfwWindow);
        } else {
            glfwShowWindow(glfwWindow);
        }

        //Enable Alpha blending.
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);

        clearBuffer();
        renderBuffer();

        glfwSetWindowSizeCallback(glfwWindow, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(final long window, final int width, final int height) {
                runFrame(dt);
            }
        });
    }


    /**
     * Method used to clear the OpenGL buffer.
     */
    private void clearBuffer() {
        GL11.glClearColor(colorBg.getRed(), colorBg.getGreen(), colorBg.getBlue(), colorBg.getAlpha());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }


    /**
     * Method to render the OpenGL buffer and poll window events.
     */
    private void renderBuffer() {
        glfwSwapBuffers(glfwWindow);
        glfwPollEvents();
    }

    /**
     * Method to destroy GLFW window.
     */
    protected void disposeWindow() {
        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null))
               .free();
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
     * Main application loop.
     */
    protected void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        while (!glfwWindowShouldClose(glfwWindow)) {
            runFrame(dt);

            //Calculate Delta Time
            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    /**
     * Method used to run the next frame.
     */
    protected void runFrame(float dt) {
        clearBuffer();
        preProcess(dt);
        process(dt);
        postProcess(dt);
        renderBuffer();
    }

    /**
     * Perform pre-render setup if necessary
     *
     * @param dt delta time of frame
     */
    private void preProcess(float dt) {
        //Not Implemented
    }

    /**
     * Call out to the scene to render
     *
     * @param dt delta time of frame
     */
    private void process(float dt) {
        //If dt isn't 0, we call update on the scene.
        if (dt >= 0) {
            currentScene.update(dt);
        }
        this.guiLayer.update(dt, currentScene);
        if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            System.exit(0);
        }
    }

    /**
     * Hook to perform scene cleanup if necessary
     *
     * @param dt delta time of frame
     */
    private void postProcess(float dt) {
        //Not Implemented
    }
}
