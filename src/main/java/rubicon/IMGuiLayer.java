package rubicon;

import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

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
    //Framework instances
    protected ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    protected ImGuiImplGl3 imGuiGl3  = new ImGuiImplGl3();

    //Open GL Version Information
    private String glslVersion = null;

    private boolean showText = false;

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
        io.setIniFilename(null);                                // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
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
     * Load Resources from classpath
     *
     * @param name Resource Name/Path
     * @return resource data
     */
    private static byte[] loadFromResources(String name) {
        try {
            return Files.readAllBytes(Paths.get(Objects.requireNonNull(Window.class.getResource(name))
                    .toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
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

        System.out.println("GLSL Version: " + this.glslVersion);
    }

    /**
     * Render the actual IMGui Interface.  currently just renders the example from the repo.
     */
    public void imgui() {
        ImGui.begin("Cool Window", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.text("OS: [" + System.getProperty("os.name") + "] Arch: [" + System.getProperty("os.arch") + "]");
        ImGui.text("Hello, World! " + FontAwesomeIcons.Smile);
        if (ImGui.button("I am a button")) {
            showText = true;
        }
        if (showText) {
            ImGui.text("You clicked a button");
            if (ImGui.button("Stop Showing Button")) {
                showText = false;
            }
        }
        ImGui.end();
        ImGui.newLine();
        Extra.show(Window.getBackgroundColor());

    }
}
