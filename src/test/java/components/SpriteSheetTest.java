package components;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import render.Texture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SpriteSheetTest {
    @Test
    void constructorTest() {
        float delta = 0.0001f; // Tolerance for floating-point precision

        Texture t = Mockito.mock(Texture.class);
        when(t.getWidth()).thenReturn(100);
        when(t.getHeight()).thenReturn(20);
        SpriteSheet sheet = new SpriteSheet(t, 10, 10, 20, 0);
        for (int i = 0; i < 20; i++) {
            Sprite s = sheet.getSprite(i);
            assertNotNull(s);
            assertEquals(10f / 20f, (s.getTexCoords()[0].y - s.getTexCoords()[1].y), delta);
            assertEquals(10f / 100f, (s.getTexCoords()[1].x - s.getTexCoords()[2].x), delta);

        }
        assertThrows(IndexOutOfBoundsException.class, () -> sheet.getSprite(20));
    }
}
