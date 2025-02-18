package rubicon;

import component.FontRenderer;
import component.SpriteRenderer;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class GameObjectTest {
    @Test
    void emptyConstructorTest() {
        GameObject test = new GameObject("Test");
        assertEquals("Test", test.getName());
        assertEquals(0, test.getZIndex());
        assertNotNull(test.transform);
        assertEquals(0, test.transform.position.x);
        assertEquals(0, test.transform.position.y);
        assertEquals(0, test.transform.scale.x);
        assertEquals(0, test.transform.scale.y);
        assertDoesNotThrow(() -> test.getComponent(SpriteRenderer.class));
    }

    @Test
    void constructorTest() {
        Transform t = new Transform(new Vector2f(1,2), new Vector2f(3,4));
        int zIndex = 12;
        String name = "TName";
        GameObject test = new GameObject(name, t, zIndex);
        assertEquals(name, test.getName());
        assertEquals(zIndex, test.getZIndex());
        assertNotNull(test.transform);
        assertEquals(t, test.transform);
        assertDoesNotThrow(() -> test.getComponent(SpriteRenderer.class));
    }

    @Test
    void addComponent() {
        GameObject t = new GameObject("test");
        SpriteRenderer spr  = new SpriteRenderer();
        t.addComponent(spr);
        assertEquals(t, spr.getGameObject());
        assertEquals(spr, t.getComponent(spr.getClass()));
    }

    @Test
    void lifecycle() {
        float delta = .25f;
        GameObject t = new GameObject("test");
        SpriteRenderer spr = new SpriteRenderer();
        spr.setColor(new Vector4f());
        SpriteRenderer s = Mockito.spy(spr);
        t.addComponent(s);
        assertEquals(t, s.getGameObject());
        assertEquals(s, t.getComponent(spr.getClass()));
        t.start();
        verify(s).start();
        t.update(delta);
        verify(s).update(delta);
        try (MockedStatic<ImGui> utilities = Mockito.mockStatic(ImGui.class)) {
            utilities.when(() -> ImGui.colorPicker4(any(), any())).thenReturn(false);
            t.imgui();
            verify(s).imgui();
        }
    }

    @Test
    void removeComponent() {
        GameObject t = new GameObject("test");
        SpriteRenderer spr = new SpriteRenderer();
        spr.setColor(new Vector4f());
        assertDoesNotThrow(() -> t.removeComponent(spr.getClass()));
        t.addComponent(spr);
        assertDoesNotThrow(() -> t.removeComponent(FontRenderer.class));
        assertEquals(spr, t.getComponent(spr.getClass()));
        t.removeComponent(spr.getClass());
        assertNull(t.getComponent(spr.getClass()));
    }
}
