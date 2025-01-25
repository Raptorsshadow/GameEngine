package rubicon;

import imgui.Extra;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

/**
 * Class: IMGuiLayer
 * Author: rapto
 * CreatedDate: 1/25/2025 : 3:34 AM
 * Project: GameEngine
 * Description: IMGui sample layer that runs the imgui-java Example UI's as a demo.
 */
public class IMGuiLayer {
    private boolean showText = false;

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
