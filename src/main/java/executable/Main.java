package executable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rubicon.Window;

/**
 * Class: Main
 * Author: rapto
 * CreatedDate: 1/19/2025 : 1:45 AM
 * Project: GameEngine
 * Description: Main entry point of the Game engine.
 */
public class Main {

    public static void main(String... args) {
        Logger log = LogManager.getLogger(Main.class);
        //Create a window and run it.
        try {
            Window window = Window.get();
            window.run();
        } catch(Exception e) {
            log.error("A Fatal Exception was encountered", e);
        }
    }
}
