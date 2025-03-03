package imgui;

import imgui.extension.implot.ImPlot;
import imgui.extension.implot.ImPlotPoint;
import imgui.flag.ImGuiCond;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;

import java.awt.*;
import java.net.URI;

public class ExampleImPlot {
    private static final String    URL      = "https://github.com/epezent/implot/tree/555ff68";
    private static final ImBoolean showDemo = new ImBoolean(false);

    private static final int[] xs  = {0, 1, 2, 3, 4, 5};
    private static final int[] ys  = {0, 1, 2, 3, 4, 5};
    private static final int[] ys1 = {0, 0, 1, 2, 3, 4};
    private static final int[] ys2 = {1, 2, 3, 4, 5, 6};

    static {
        ImPlot.createContext();
    }

    public static void show(ImBoolean showImPlotWindow) {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport()
                                    .getPosX() + 100, ImGui.getMainViewport()
                                                           .getPosY() + 100, ImGuiCond.Once);
        if (ImGui.begin("ImPlot Demo", showImPlotWindow)) {
            ImGui.text("This a demo for ImPlot");

            ImGui.alignTextToFramePadding();
            ImGui.text("Repo:");
            ImGui.sameLine();
            if (ImGui.button(URL)) {
                try {
                    Desktop.getDesktop()
                           .browse(new URI(URL));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ImGui.checkbox("Show ImPlot Built-In Demo", showDemo);

            if (showDemo.get()) {
                ImPlot.showDemoWindow(showDemo);
            } else {
                if (ImPlot.beginPlot("Example Plot")) {
                    ImPlot.plotShaded("Shaded", xs, ys1, ys2);
                    ImPlot.plotLine("Line", xs, ys);
                    ImPlot.plotBars("Bars", xs, ys);
                    ImPlot.endPlot();
                }

                if (ImPlot.beginPlot("Example Scatterplot")) {
                    ImPlot.plotScatter("Scatter", xs, ys);
                    ImPlot.endPlot();
                }

                if (ImPlot.beginPlot("Example Piechart")) {
                    ImPlot.plotPieChart(new String[]{"1", "2", "3", "4", "5", "6"}, xs, .5, .5, .4);
                    ImPlot.endPlot();
                }

                if (ImPlot.beginPlot("Example Heatmap")) {
                    ImPlot.plotHeatmap("Heatmap", new int[]{1, 3, 6, 2, 8, 5, 4, 3}, 2, 4, 0, 0, "%d",
                                       new ImPlotPoint(0, 0), new ImPlotPoint(10, 10));
                    ImPlot.endPlot();
                }
            }
        }

        ImGui.end();
    }
}
