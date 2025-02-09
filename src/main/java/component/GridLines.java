package component;

import org.joml.Vector2f;
import org.joml.Vector3f;
import render.DebugDraw;
import rubicon.Window;
import scene.Settings;

/**
 * Class: GridLines
 * Author: rapto
 * CreatedDate: 2/8/2025 : 10:00 PM
 * Project: GameEngine
 * Description: Render gridlines to the Scene to aide in alignment.
 */
public class GridLines extends Component {
    public static final Vector3f GRID_COLOR = new Vector3f(.2f, .2f, .2f);

    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getScene()
                                   .getCamera()
                                   .getPosition();
        Vector2f projectionSize = Window.getScene()
                                        .getCamera()
                                        .getProjectionSize();

        //Set up the first X/Y coordinate  Shift left 1 to account for left edge of first box
        int firstX = ((int) (cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int) (cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        //Count how many lines we'll have in the horizontal and vertical directions.  Add 2 to account for shift and last line
        int numVLines = (int) (projectionSize.x / Settings.GRID_WIDTH) + 2;
        int numHLines = (int) (projectionSize.y / Settings.GRID_HEIGHT) + 2;

        //Calculate the width and height of the grids.  Mul by 2 to account for shift and last lines.
        int height = (int) projectionSize.y + Settings.GRID_HEIGHT * 2;
        int width = (int) projectionSize.x + Settings.GRID_WIDTH * 2;

        //Determine the max number of lines to render
        int maxLines = Math.max(numHLines, numVLines);

        //Create Horizontal and Vertical lines forming a grid.
        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, (float) firstY + height), GRID_COLOR, 1);
            }

            if (i < numVLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f((float) firstX + width, y), GRID_COLOR, 1);
            }
        }
    }
}
