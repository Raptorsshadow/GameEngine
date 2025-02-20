package junitExtension;

import graphics.GLWrapper;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import scene.Settings;

/**
 * Class: GlobalTestSetup
 * Author: rapto
 * CreatedDate: 2/19/2025 : 2:19 AM
 * Project: GameEngine
 * Description: Configures application for unit test execution.
 */
public class GlobalTestSetup implements BeforeAllCallback {
    private static boolean initialized = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!initialized) {
            System.out.println("Global setup running...");
            //Ensure the GraphicsImpl has been mocked rather than going to native impl.
            Settings.graphicsImpl = Mockito.mock(GLWrapper.class);
            initialized = true;
        }
    }
}