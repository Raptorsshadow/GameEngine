package util;

import component.SpriteSheet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import render.Shader;
import render.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class: AssetPool
 * Author: rapto
 * CreatedDate: 1/22/2025 : 11:13 AM
 * Project: GameEngine
 * Description: Manages Asset resources from filesystem
 */
public class AssetPool {
    static final         Map<String, Shader>      shaderMap      = new HashMap<>();
    //Map  of Texture assets
    static final         Map<String, Texture>     textureMap     = new HashMap<>();
    //Map  of SpriteSheet assets
    static final         Map<String, SpriteSheet> spriteSheetMap = new HashMap<>();
    private static final Logger                   log            = LogManager.getLogger(AssetPool.class);

    /**
     * Hide default constructor as this is a static util class.
     */
    private AssetPool() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Attempts to retrieve a shader with given resourceName.  If not found will go to filesystem and attempt to load it.
     * If resources does not exist, will return null or terminate if assertions are enabled.
     *
     * @param resourceName Relative Shader path
     * @return Shader if file exists, null if not.
     */
    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (file.exists()) {
            if (shaderMap.containsKey(file.getAbsolutePath())) {
                return shaderMap.get(file.getAbsolutePath());
            } else {
                Shader shader = new Shader(resourceName);
                shader.compileAndLinkShader();
                AssetPool.shaderMap.put(file.getAbsolutePath(), shader);
                return shader;
            }
        } else {
            log.error("ERROR : AssetPool : Shader with given resourceName not found.");
            assert false;
            return null;
        }
    }

    /**
     * Attempts to retrieve a texture with given resourceName.  If not found will go to filesystem and attempt to load it.
     * If resources does not exist, will return null or terminate if assertions are enabled.
     *
     * @param resourceName Relative Texture path
     * @return Texture if file exists, null if not.
     */
    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (file.exists()) {
            if (textureMap.containsKey(file.getAbsolutePath())) {
                return textureMap.get(file.getAbsolutePath());
            } else {
                Texture texture = new Texture();
                texture.init(resourceName);
                AssetPool.textureMap.put(file.getAbsolutePath(), texture);
                return texture;
            }
        } else {
            log.error("ERROR : AssetPool : Texture with given resourceName not found.");
            assert false;
            return null;
        }
    }

    /**
     * Attempts to store a SpriteSheet with given resourceName.
     *
     * @param resourceName Relative Texture path
     * @param spriteSheet  SpriteSheet to store
     */
    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet) {
        File file = new File(resourceName);
        if (!spriteSheetMap.containsKey(file.getAbsolutePath())) {
            AssetPool.spriteSheetMap.put(file.getAbsolutePath(), spriteSheet);
        } else {
            log.error("ERROR : AssetPool : SpriteSheet with given resourceName not found.");
            assert false;
        }
    }

    /**
     * Attempts to retrieve a SpriteSheet with given resourceName.
     *
     * @param resourceName Relative Texture path
     * @return SpriteSheet found at resourceName, null otherwise.
     */
    public static SpriteSheet getSpriteSheet(String resourceName) {
        File file = new File(resourceName);
        assert spriteSheetMap.containsKey(
                file.getAbsolutePath()) : "ERROR : AssetPool : SpriteSheet with given resourceName not found.";
        return AssetPool.spriteSheetMap.getOrDefault(file.getAbsolutePath(), null);
    }
}
