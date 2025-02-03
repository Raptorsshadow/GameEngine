package component;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import render.Texture;
import rubicon.GameObject;
import rubicon.Transform;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class SpriteRendererTest {

    @Test
    void colorConstructor() {
        Vector4f vec = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
        SpriteRenderer sr = new SpriteRenderer();
        sr.setColor(vec);
        assertNotNull(sr.getColor());
        assertEquals(vec, sr.getColor());
        assertTrue(sr.isDirty());
        assertNull(sr.getTexture());
    }

    @Test
    void spriteConstructor() {
        Texture t = Mockito.mock(Texture.class);
        Sprite sprite = new Sprite();
        sprite.setTexture(t);
        SpriteRenderer sr = new SpriteRenderer();
        sr.setSprite(sprite);
        assertNotNull(sr.getColor());
        assertEquals(SpriteRenderer.DEF_COLOR, sr.getColor());
        assertTrue(sr.isDirty());
        assertNotNull(sr.getTexture());
        assertEquals(t, sr.getTexture());
        assertNotNull(sr.getTexCoords());
        assertEquals(sprite.getTexCoords(), sr.getTexCoords());
    }

    @Test
    void startTest() {
        SpriteRenderer spr = new SpriteRenderer();
        spr.setGameObject(new GameObject("Test", new Transform(new Vector2f(123, 456)), 1));
        assertNull(spr.lastTransform);
        spr.start();
        assertEquals(spr.getGameObject().transform.position.x, spr.lastTransform.position.x);
        assertEquals(spr.getGameObject().transform.position.y, spr.lastTransform.position.y);
    }

    @Test
    void updateTest() {
        SpriteRenderer spr = new SpriteRenderer();
        spr.setGameObject(new GameObject("Test", new Transform(new Vector2f(123, 456)), 1));
        spr.start();
        assertTrue(spr.isDirty());
        spr.setClean();
        assertFalse((spr.isDirty()));
        spr.update(1);
        assertFalse(spr.isDirty());
        spr.getGameObject().transform.position.set(2);
        assertNotEquals(spr.getGameObject().transform.position.x, spr.lastTransform.position.x);
        assertNotEquals(spr.getGameObject().transform.position.y, spr.lastTransform.position.y);
        spr.update(2);
        assertTrue(spr.isDirty());
        assertEquals(spr.getGameObject().transform.position.x, spr.lastTransform.position.x);
        assertEquals(spr.getGameObject().transform.position.y, spr.lastTransform.position.y);
    }

    @Test
    void setColor() {
        SpriteRenderer spr = new SpriteRenderer();
        spr.setClean();
        spr.setColor(SpriteRenderer.DEF_COLOR);
        assertFalse(spr.isDirty());
        Vector4f col = new Vector4f(1, 2, 3, 4);
        spr.setColor(col);
        assertTrue(spr.isDirty());
        assertEquals(col, spr.getColor());
    }

    @Test
    void getTexture() {
        SpriteRenderer spr = new SpriteRenderer();
        try {
            spr.setSprite(null);
            spr.getTexture();
            fail();
        } catch (AssertionError err) {
            assertTrue(true);
        }
    }

    @Test
    void getTextureCoords() {
        SpriteRenderer spr = new SpriteRenderer();
        try {
            spr.setSprite(null);
            spr.getTexCoords();
            fail();
        } catch (AssertionError err) {
            assertTrue(true);
        }
    }

    @Test
    void imguiTrue() {
        try (MockedStatic<ImGui> utilities = Mockito.mockStatic(ImGui.class)) {
            utilities.when(() -> ImGui.colorPicker4(any(), any())).thenReturn(true);
            SpriteRenderer spr = new SpriteRenderer();
            spr.setClean();
            spr.imgui();
            assertTrue(spr.isDirty());
        }

    }

    @Test
    void imguiFalse() {
        try (MockedStatic<ImGui> utilities = Mockito.mockStatic(ImGui.class)) {
            utilities.when(() -> ImGui.colorPicker4(any(), any())).thenReturn(false);
            SpriteRenderer spr = new SpriteRenderer();
            spr.setClean();
            spr.imgui();
            assertEquals(SpriteRenderer.DEF_COLOR.x, spr.getColor().x);
            assertFalse(spr.isDirty());
        }

    }
}
