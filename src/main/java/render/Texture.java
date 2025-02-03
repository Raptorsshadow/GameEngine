package render;

import lombok.Data;
import org.lwjgl.BufferUtils;
import graphics.GLWrapper;
import graphics.LWJGLWrapper;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Class: Texture
 * Author: rapto
 * CreatedDate: 1/19/2025 : 1:29 AM
 * Project: GameEngine
 * Description: Texture definition file responsible for loading and parsing the Texture for use in Scene.
 */
@Data
public class Texture implements Serializable {

    // Filepath of the Texture
    private String filePath;

    // TextureId of registered resources
    private int textureId;

    //Width and Height of the Texture file
    private int width;
    private int height;

    private transient GLWrapper gl = new LWJGLWrapper();

    public void init(String filePath, GLWrapper gl) {
        this.gl = gl;
        this.filePath = filePath;
        // Generate and bind texture
        textureId = gl.glGenTextures();
        gl.glBindTexture(GL_TEXTURE_2D, textureId);

        //Implements Tile behavior across X and Y Axis
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //Define resize behavior of individual pixels
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Load and provision the texture.
        loadTexture();
    }
public void init(String filePath) {
    this.init(filePath, new LWJGLWrapper());
}
    /**
     * Responsible for loading and provisioning the texture.
     */
    protected void loadTexture() {

        //Provision Buffers
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        //When read into the system, it's loaded as a vertical mirror.  This flips it back.
        gl.stbi_set_flip_vertically_on_load(true);

        //Load the file and leverage the buffers to store relevant data.
        ByteBuffer image = gl.stbi_load(filePath, widthBuffer, heightBuffer, channels, 0);

        //If we loaded an image, ensure we select the appropriate color channel so we display it correctly.
        if (image != null) {
            int colorType = -1;
            if (channels.get(0) == 3) {
                colorType = GL_RGB;
            } else if (channels.get(0) == 4) {
                colorType = GL_RGBA;
            } else {
                assert false : "Error: Texture : Unknown channel count for texture " + channels.get(0);
            }
            //Populate the width and height from image channels.
            this.width = widthBuffer.get(0);
            this.height = heightBuffer.get(0);

            //Instruct the system to populate the provisioned space.
            gl.glTexImage2D(GL_TEXTURE_2D, 0, colorType, widthBuffer.get(0), heightBuffer.get(0), 0, colorType,
                    GL_UNSIGNED_BYTE,
                    image);
        } else {
            assert false : "Error : Texture : Could not load Image: " + this.filePath;
        }

        // Release the stbi buffers
        gl.stbi_image_free(image);
    }

    /**
     * Instruct the system to enable the texture.
     */
    public void bind() {
        gl.glBindTexture(GL_TEXTURE_2D, textureId);
    }

    /**
     * Instruct the system to disable the texture.
     */
    public void unbind() {
        gl.glBindTexture(GL_TEXTURE_2D, 0);
    }

}
