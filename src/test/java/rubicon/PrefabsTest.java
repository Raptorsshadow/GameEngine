package rubicon;

import component.Sprite;
import component.SpriteRenderer;
import org.joml.Vector2f;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class: PrefabsTest
 * Author: rapto
 * CreatedDate: 2/22/2025 : 4:12 AM
 * Project: GameEngine
 * Description: Unit tests for the Prefabs Class
 */
class PrefabsTest {
    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<Prefabs> constructor = Prefabs.class.getDeclaredConstructor();
        constructor.setAccessible(true); // Allow access to private constructor
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void generateSpriteObjectTest() {
        float scaleX = 1;
        float scaleY = 2;
        Sprite s = new Sprite();
        s.setTexCoords(new Vector2f[]{new Vector2f(1, 2), new Vector2f(3, 4)});
        GameObject go = Prefabs.generateSpriteObject(s, scaleX, scaleY);
        assertNotNull(go);
        assertEquals(s.getTexCoords(), go.getComponent(SpriteRenderer.class)
                                         .getTexCoords());
        assertEquals(scaleX, go.getTransform().scale.x);
        assertEquals(scaleY, go.getTransform().scale.y);

    }
}
