package components;


import org.joml.Vector2f;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import render.Texture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Class: SpriteTest
 * Author: rapto
 * CreatedDate: 1/28/2025 : 2:20 AM
 * Project: GameEngine
 * Description: Sprite Unit Tests
 */
class SpriteTest {

    @Test
    void noArgs() {
        Sprite s = new Sprite();
        assertNull(s.getTexture());
        assertEquals(Sprite.DEF_VECTOR.length, s.getTexCoords().length);
        for (int i = 0; i < Sprite.DEF_VECTOR.length; i++) {
            assertEquals(Sprite.DEF_VECTOR[i], s.getTexCoords()[i]);
        }
    }

    @Test
    void texture() {
        Texture t = Mockito.mock(Texture.class);
        Sprite s = new Sprite(t);
        assertEquals(t, s.getTexture());
        assertEquals(Sprite.DEF_VECTOR.length, s.getTexCoords().length);
        for (int i = 0; i < Sprite.DEF_VECTOR.length; i++) {
            assertEquals(Sprite.DEF_VECTOR[i], s.getTexCoords()[i]);
        }
    }

    @Test
    void textureAndCoords() {
        Vector2f[] coords = {
                new Vector2f(0, 0),
                new Vector2f(1, 1),
                new Vector2f(2, 2),
                new Vector2f(3, 3)
        };
        Texture t = Mockito.mock(Texture.class);
        Sprite s = new Sprite(t, coords);
        assertEquals(t, s.getTexture());
        assertEquals(coords, s.getTexCoords());
    }
}
