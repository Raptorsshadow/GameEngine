package util;

import component.SpriteSheet;
import junitExtension.GlobalTestSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedConstruction;
import render.Shader;
import render.Texture;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class: AssetPoolTest
 * Author: rapto
 * CreatedDate: 2/19/2025 : 2:16 AM
 * Project: GameEngine
 * Description: AssetPool Unit Tests
 */
@ExtendWith(GlobalTestSetup.class)
class AssetPoolTest {

    @TempDir
    Path tempDir;

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<AssetPool> constructor = AssetPool.class.getDeclaredConstructor();
        constructor.setAccessible(true); // Allow access to private constructor
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void testGetSpriteSheet_Exists() {
        String resourceName = "testSprite.png";
        File file = new File(resourceName);
        String absolutePath = file.getAbsolutePath();

        // Mock sprite sheet object
        SpriteSheet mockSpriteSheet = mock(SpriteSheet.class);

        // Insert into spriteSheetMap
        AssetPool.spriteSheetMap.put(absolutePath, mockSpriteSheet);

        // Ensure the correct object is returned
        assertEquals(mockSpriteSheet, AssetPool.getSpriteSheet(resourceName));
    }

    @Test
    void testAddSpriteSheet_NotExists() {
        String resourceName = "testSprite.png";

        // Mock sprite sheet object
        SpriteSheet mockSpriteSheet = mock(SpriteSheet.class);

        assertThrows(AssertionError.class, () -> AssetPool.getSpriteSheet(resourceName));

        assertDoesNotThrow(() -> AssetPool.addSpriteSheet(resourceName, mockSpriteSheet));

        assertDoesNotThrow(() -> AssetPool.getSpriteSheet(resourceName));

        assertThrows(AssertionError.class, () -> AssetPool.addSpriteSheet(resourceName, mockSpriteSheet));

    }


    @Test
    void testGetSpriteSheet_NotExists() {
        String resourceName = "missingSprite.png";

        // Assertions are enabled in tests by default, so assertThrows should work
        AssertionError exception = assertThrows(AssertionError.class, () ->
                                                        AssetPool.getSpriteSheet(resourceName)
                                               );

        assertTrue(exception.getMessage()
                            .contains("ERROR : AssetPool : SpriteSheet with given resourceName not found."));
    }


    @Test
    void testGetTexture_ExistingTexture() {
        try (MockedConstruction<Texture> mockedConstruction = mockConstruction(Texture.class,
                                                                               (mockTexture, context) -> doNothing().when(
                                                                                                                            mockTexture)
                                                                                                                    .init(anyString()))) {
            File f = new File("assets/images/spritesheets/decorationsAndBlocks.png");

            //Asser the map is empty
            assertFalse(AssetPool.textureMap.containsKey(f.getAbsolutePath()));
            //Create the Texture
            Texture t = assertDoesNotThrow(
                    () -> AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"));
            //Assert the texture is in the map now
            assertTrue(AssetPool.textureMap.containsKey(f.getAbsolutePath()));
            //Retrieve and assert it's the same texture
            Texture tMap = AssetPool.textureMap.get(f.getAbsolutePath());
            assertEquals(t, tMap);
            //Re-Run getTexture and confirm it returns the previously created Texture.
            Texture tTwo = assertDoesNotThrow(
                    () -> AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"));
            assertEquals(t, tTwo);
        }
    }

    @Test
    void testGetTexture_FileNotFound() {
        String resourceName = "nonexistent.png";

        AssertionError exception = assertThrows(AssertionError.class, () ->
                                                        AssetPool.getTexture(resourceName)
                                               );

        assertNull(AssetPool.textureMap.get(new File(resourceName).getAbsolutePath()));
    }

    @Test
    void testGetShader_ExistingShader() {
        try (MockedConstruction<Shader> mockedConstruction = mockConstruction(Shader.class,
                                                                              (mockTexture, context) -> doNothing().when(
                                                                                                                           mockTexture)
                                                                                                                   .compileAndLinkShader())) {
            File f = new File("assets/shader/default.glsl");
            //Assert the map is empty
            assertFalse(AssetPool.shaderMap.containsKey(f.getAbsolutePath()));
            //Create the Shader
            Shader s = assertDoesNotThrow(() -> AssetPool.getShader("assets/shader/default.glsl"));
            //Assert the Shader is in the map now
            assertTrue(AssetPool.shaderMap.containsKey(f.getAbsolutePath()));
            //Retrieve and assert it's the same shader
            Shader sMap = AssetPool.shaderMap.get(f.getAbsolutePath());
            assertEquals(s, sMap);
            //Re-Run getShader and confirm it returns the previously created Shader
            Shader sTwo = assertDoesNotThrow(() -> AssetPool.getShader("assets/shader/default.glsl"));
            assertEquals(s, sTwo);
        }
    }

    @Test
    void testGetShader_FileNotFound() {
        String resourceName = "nonexistent.png";

        AssertionError exception = assertThrows(AssertionError.class, () ->
                                                        AssetPool.getShader(resourceName)
                                               );

        assertNull(AssetPool.shaderMap.get(new File(resourceName).getAbsolutePath()));
    }
}
