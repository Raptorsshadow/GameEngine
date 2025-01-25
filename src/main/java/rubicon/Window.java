package rubicon;

import imgui.ImFontConfig;
import imgui.ImFontGlyphRangesBuilder;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Color;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    //ImgUI params
    protected     ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    protected     ImGuiImplGl3  imGuiGl3  = new ImGuiImplGl3();
    //Actual resolution values for the window.
    float dt = -1.0f;
    // Provisioned identifier for the Window
    private long   glfwWindow;
    // The active scene
    private Scene  currentScene;
    private String glslVersion = null;

    /**
     * Default Constructor taking window initialization params
     *
     * @param config Window Configuration Data
     */
    private Window(Configuration config) {
        this.config = config;
    }

    /**
     * Instance accessor and retriever that passes a config to initialize the window.
     *
     * @param config Window Configuration Object
     * @return Instance of the window
     */
    public static Window get(Configuration config) {
        if (Window.window == null) {
            Window.window = new Window(config);
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
            Window.window = new Window(config);
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
                w.currentScene = new LevelEditorScene();
                break;
            case 1:
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

    /**
     * Load Resources from classpath
     *
     * @param name Resource Name/Path
     * @return resource data
     */
    private static byte[] loadFromResources(String name) {
        try {
            return Files.readAllBytes(Paths.get(Window.class.getResource(name)
                                                            .toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Accessor to retrieve Window Background Color
     *
     * @return windo background color
     */
    public static Color getBackgroundColor() {
        return get().colorBg;
    }

    /**
     * Initialize the Window and IMGui Framework.
     */
    public void init() {
        initWindow();
        initImGui();
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);
    }

    /**
     * Method to dispose all used application resources and destroy its window.
     */
    protected void dispose() {
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        disposeImGui();
        disposeWindow();
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

        decideGlGlslVersions();

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
     * Determine the GLSL Version from os.
     */
    private void decideGlGlslVersions() {
        final boolean isMac = System.getProperty("os.name")
                                    .toLowerCase()
                                    .contains("mac");
        if (isMac) {
            glslVersion = "#version 150";
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);  // 3.2+ only
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);          // Required on Mac
        } else {
            glslVersion = "#version 130";
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        }
    }

    /**
     * Method used to clear the OpenGL buffer.
     */
    private void clearBuffer() {
        GL11.glClearColor(colorBg.getRed(), colorBg.getGreen(), colorBg.getBlue(), colorBg.getAlpha());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Method called at the beginning of the main cycle.
     * It clears OpenGL buffer and starts an ImGui frame.
     */
    protected void startFrame() {
        clearBuffer();
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui and swaps GLFW buffers to show an updated frame.
     */
    protected void endFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        // Update and Render additional Platform Windows
        // (Platform functions may change the current OpenGL context, so we save/restore it to make it easier to paste this code elsewhere.
        //  For this specific demo app we could also call glfwMakeContextCurrent(window) directly)
        if (ImGui.getIO()
                 .hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupCurrentContext = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupCurrentContext);
        }

        renderBuffer();
    }

    /**
     * Method to render the OpenGL buffer and poll window events.
     */
    private void renderBuffer() {
        glfwSwapBuffers(glfwWindow);
        glfwPollEvents();
    }

    /**
     * Method to destroy Dear ImGui context.
     */
    protected void disposeImGui() {
        ImGui.destroyContext();
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
     * Method to initialize Dear ImGui context. Could be overridden to do custom Dear ImGui setup before application start.
     */
    protected void initImGui() {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);                                // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);

        initFonts(io);
    }

    /**
     * Example of fonts configuration
     * For more information read: https://github.com/ocornut/imgui/blob/33cdbe97b8fd233c6c12ca216e76398c2e89b0d8/docs/FONTS.md
     */
    private void initFonts(final ImGuiIO io) {
        // This enables FreeType font renderer, which is disabled by default.
        io.getFonts()
          .setFreeTypeRenderer(true);

        // Add default font for latin glyphs
        io.getFonts()
          .addFontDefault();

        // You can use the ImFontGlyphRangesBuilder helper to create glyph ranges based on text input.
        // For example: for a game where your script is known, if you can feed your entire script to it (using addText) and only build the characters the game needs.
        // Here we are using it just to combine all required glyphs in one place
        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
        rangesBuilder.addRanges(io.getFonts()
                                  .getGlyphRangesDefault());
        rangesBuilder.addRanges(io.getFonts()
                                  .getGlyphRangesCyrillic());
        rangesBuilder.addRanges(io.getFonts()
                                  .getGlyphRangesJapanese());
        rangesBuilder.addRanges(FontAwesomeIcons._IconRange);

        // Font config for additional fonts
        // This is a natively allocated struct so don't forget to call destroy after atlas is built
        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setMergeMode(true);  // Enable merge mode to merge cyrillic, japanese and icons with default font

        final short[] glyphRanges = rangesBuilder.buildRanges();
        io.getFonts()
          .addFontFromMemoryTTF(loadFromResources("/Tahoma.ttf"), 14, fontConfig, glyphRanges); // cyrillic glyphs
        io.getFonts()
          .addFontFromMemoryTTF(loadFromResources("/NotoSansCJKjp-Medium.otf"), 14, fontConfig,
                                glyphRanges); // japanese glyphs
        io.getFonts()
          .addFontFromMemoryTTF(loadFromResources("/fa-regular-400.ttf"), 14, fontConfig, glyphRanges); // font awesome
        io.getFonts()
          .addFontFromMemoryTTF(loadFromResources("/fa-solid-900.ttf"), 14, fontConfig, glyphRanges); // font awesome
        io.getFonts()
          .build();

        fontConfig.destroy();
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
        startFrame();
        preProcess(dt);
        process(dt);
        postProcess(dt);
        endFrame();
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
