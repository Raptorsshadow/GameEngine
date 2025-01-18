package render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private final String filePath;
    private final int textureId;

    public Texture(String filePath) {
        this.filePath = filePath;
        // Generate textures
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        //Repeat across X/Y
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //Define resize behavior
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        loadTexture();
    }

    protected void loadTexture() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        if (image != null) {
            int COLOR_TYPE = -1;
            if (channels.get(0) == 3) {
                COLOR_TYPE = GL_RGB;
            } else if (channels.get(0) == 4) {
                COLOR_TYPE = GL_RGBA;
            } else {
                assert false : "Error: Texture : Unknown channel count for texture " + channels.get(0);
            }
            glTexImage2D(GL_TEXTURE_2D, 0, COLOR_TYPE, width.get(0), height.get(0), 0, COLOR_TYPE, GL_UNSIGNED_BYTE, image);
        } else {
            assert false : "Error : Texture : Could not load Image: " + this.filePath;
        }

        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
