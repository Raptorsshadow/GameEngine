package imgui;

import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesEditorContext;
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation;
import imgui.extension.imnodes.flag.ImNodesPinShape;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiMouseButton;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

import java.awt.*;
import java.net.URI;

public class ExampleImNodes {
    private static final String URL = "https://github.com/Nelarius/imnodes/tree/857cc86";

    private static final ImInt LINK_A = new ImInt();
    private static final ImInt LINK_B = new ImInt();

    private static ImNodesEditorContext editorContext = null;

    static {
        ImNodes.createContext();
        editorContext = ImNodes.editorContextCreate();
    }

    public static void show(final ImBoolean showImNodesWindow, final Graph graph) {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport()
                                    .getPosX() + 100, ImGui.getMainViewport()
                                                           .getPosY() + 100, ImGuiCond.Once);
        if (ImGui.begin("ImNodes Demo", showImNodesWindow)) {
            ImGui.text("This a demo graph editor for ImNodes");

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

            ImNodes.editorContextSet(editorContext);
            ImNodes.beginNodeEditor();

            for (Graph.GraphNode node : graph.nodes.values()) {
                ImNodes.beginNode(node.nodeId);

                ImNodes.beginNodeTitleBar();
                ImGui.text(node.getName());
                ImNodes.endNodeTitleBar();

                ImNodes.beginInputAttribute(node.getInputPinId(), ImNodesPinShape.CircleFilled);
                ImGui.text("In");
                ImNodes.endInputAttribute();

                ImGui.sameLine();

                ImNodes.beginOutputAttribute(node.getOutputPinId());
                ImGui.text("Out");
                ImNodes.endOutputAttribute();

                ImNodes.endNode();
            }

            int uniqueLinkId = 1;
            for (Graph.GraphNode node : graph.nodes.values()) {
                if (graph.nodes.containsKey(node.outputNodeId)) {
                    ImNodes.link(uniqueLinkId++, node.getOutputPinId(), graph.nodes.get(node.outputNodeId)
                                                                                   .getInputPinId());
                }
            }

            final boolean isEditorHovered = ImNodes.isEditorHovered();

            ImNodes.miniMap(0.2f, ImNodesMiniMapLocation.BottomRight);
            ImNodes.endNodeEditor();

            if (ImNodes.isLinkCreated(LINK_A, LINK_B)) {
                final Graph.GraphNode source = graph.findByOutput(LINK_A.get());
                final Graph.GraphNode target = graph.findByInput(LINK_B.get());
                if (source != null && target != null && source.outputNodeId != target.nodeId) {
                    source.outputNodeId = target.nodeId;
                }
            }

            if (ImGui.isMouseClicked(ImGuiMouseButton.Right)) {
                final int hoveredNode = ImNodes.getHoveredNode();
                if (hoveredNode != -1) {
                    ImGui.openPopup("node_context");
                    ImGui.getStateStorage()
                         .setInt(ImGui.getID("delete_node_id"), hoveredNode);
                } else if (isEditorHovered) {
                    ImGui.openPopup("node_editor_context");
                }
            }

            if (ImGui.isPopupOpen("node_context")) {
                final int targetNode = ImGui.getStateStorage()
                                            .getInt(ImGui.getID("delete_node_id"));
                if (ImGui.beginPopup("node_context")) {
                    if (ImGui.button("Delete " + graph.nodes.get(targetNode)
                                                            .getName())) {
                        graph.nodes.remove(targetNode);
                        ImGui.closeCurrentPopup();
                    }
                    ImGui.endPopup();
                }
            }

            if (ImGui.beginPopup("node_editor_context")) {
                if (ImGui.button("Create New Node")) {
                    final Graph.GraphNode node = graph.createGraphNode();
                    ImNodes.setNodeScreenSpacePos(node.nodeId, ImGui.getMousePosX(), ImGui.getMousePosY());
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }
        }
        ImGui.end();
    }
}
