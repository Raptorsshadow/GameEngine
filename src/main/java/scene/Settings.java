package scene;

import graphics.GLWrapper;
import graphics.LWJGLWrapper;

/**
 * Class: Settings
 * Author: rapto
 * CreatedDate: 2/8/2025 : 7:53 PM
 * Project: GameEngine
 * Description: Constants class for the Engine
 */
public class Settings {
    public static final int GRID_WIDTH  = 32;
    public static final int GRID_HEIGHT = 32;

    public static GLWrapper graphicsImpl = new LWJGLWrapper();

    private Settings() {

    }
}
