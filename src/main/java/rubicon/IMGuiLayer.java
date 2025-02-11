package rubicon;

import editor.GameViewWindow;
import imgui.*;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scene.Scene;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class: IMGuiLayer
 * Author: rapto
 * CreatedDate: 1/25/2025 : 3:34 AM
 * Project: GameEngine
 * Description: IMGui Scene Layer that configures and manages an IMGui Overlay for editing.  Currently launches the
 * repo example app.
 */
public class IMGuiLayer {
    private static final Logger log = LogManager.getLogger(IMGuiLayer.class);

    //Framework instances
    protected ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    protected ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    //Open GL Version Information
    private String glslVersion = null;

    /**
     * Initialize the IMGui Framework.
     */
    public void init() {
        decideGlGlslVersions();

        initImGui();
        imGuiGlfw.init(Window.getWindowId(), true);
        imGuiGl3.init(glslVersion);
    }

    /**
     * Dispose of the imgui resources.
     */
    protected void dispose() {
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        disposeImGui();
    }

    /**
     * Method to destroy Dear ImGui context.
     */
    protected void disposeImGui() {
        ImGui.destroyContext();
    }

    /**
     * Method called at the beginning of the main cycle.
     * It clears OpenGL buffer and starts an ImGui frame.
     */
    protected void startFrame() {
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui and ensures it's sent to the draw buffer.
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
    }


    /**
     * Method to initialize Dear ImGui context. Could be overridden to do custom Dear ImGui setup before application start.
     */
    protected void initImGui() {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini");                                // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);

        initFonts(io);
    }

    /**
     * Example of fonts configuration
     * For more information read: <a href="https://github.com/ocornut/imgui/blob/33cdbe97b8fd233c6c12ca216e76398c2e89b0d8/docs/FONTS.md">docs</a>
     */
    private void initFonts(final ImGuiIO io) {
        // This enables FreeType font renderer, which is disabled by default.
        io.getFonts()
                .setFreeTypeRenderer(true);

        // You can use the ImFontGlyphRangesBuilder helper to create glyph ranges based on text input.
        // For example: for a game where your script is known, if you can feed your entire script to it (using addText) and only build the characters the game needs.
        // Here we are using it just to combine all required glyphs in one place
        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
        rangesBuilder.addRanges(io.getFonts()
                .getGlyphRangesDefault());
        rangesBuilder.addRanges(FontAwesomeIcons.ICON_RANGE);

        // Font config for additional fonts
        // This is a natively allocated struct so don't forget to call destroy after atlas is built
        final ImFontConfig fontConfig = new ImFontConfig();

        io.getFonts()
                .addFontFromFileTTF("assets/fonts/NimbusMonoPS-Regular.otf", 18, fontConfig);

        fontConfig.destroy();
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
            glslVersion = "#version 460";
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        }
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
        log.info("GLSL Version: {}", this.glslVersion);
    }

    /**
     * Render the actual IMGui Interface.  currently just renders the example from the repo.
     * @param dt delta time
     * @param scene scene to attach
     */
    public void update(float dt, Scene scene) {
        startFrame();
        setupDockspace();
        scene.sceneImgui();
        GameViewWindow.imgui();
        ImGui.end();
        endFrame();
    }

    /**
     * Create the DockSpace for our Window and provide a framework for adding Viewports.
     */
    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("DockspaceDemo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        //DockSpace
        ImGui.dockSpace(ImGui.getID("Dockspace"));

    }
}
