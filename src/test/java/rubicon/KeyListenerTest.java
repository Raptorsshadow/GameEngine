package rubicon;

import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

class KeyListenerTest {
    @Test
    void getTest() {
        KeyListener kl = KeyListener.get();
        assertNotNull(kl);
        assertEquals(kl, KeyListener.get());
    }

    @Test
    void keyCallbackTest() {
        assertFalse(KeyListener.isKeyPressed(GLFW.GLFW_KEY_E));
        KeyListener.keyCallback(0, GLFW.GLFW_KEY_E, 0, GLFW.GLFW_PRESS, 0);
        assertTrue(KeyListener.isKeyPressed(GLFW.GLFW_KEY_E));
        KeyListener.keyCallback(0, GLFW.GLFW_KEY_E, 0, GLFW.GLFW_REPEAT, 0);
        assertTrue(KeyListener.isKeyPressed(GLFW.GLFW_KEY_E));
        KeyListener.keyCallback(0, GLFW.GLFW_KEY_E, 0, GLFW.GLFW_RELEASE, 0);
        assertFalse(KeyListener.isKeyPressed(GLFW.GLFW_KEY_E));
    }
}
