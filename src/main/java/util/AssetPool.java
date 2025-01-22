package util;

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
    //Map of Shader assets
    private static final Map<String, Shader>  shaderMap  = new HashMap<>();
    //Map  of Texture assets
    private static final Map<String, Texture> textureMap = new HashMap<>();

    /**
     * Hide default constructor as this is a static util class.
     */
    private AssetPool() {
        //Hide Static class constructor
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
            assert false : "ERROR : AssetPool : Shader with given resourceName not found.";
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
                Texture texture = new Texture(resourceName);
                AssetPool.textureMap.put(file.getAbsolutePath(), texture);
                return texture;
            }
        } else {
            assert false : "ERROR : AssetPool : Texture with given resourceName not found.";
            return null;
        }
    }
}
