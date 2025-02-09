package render;

import graphics.GLWrapper;
import graphics.LWJGLWrapper;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL30C.*;

/**
 * Class: FrameBuffer
 * Author: rapto
 * CreatedDate: 2/8/2025 : 10:48 PM
 * Project: GameEngine
 * Description: Frame buffer's are used to scale a rendered scene into the actual window and can scale all
 * items at once.
 */
@Data
public class FrameBuffer {
    private final Logger  log   = LogManager.getLogger(FrameBuffer.class);
    private final Texture texture;
    private       int     fboID = 0;

    private GLWrapper gl;

    public FrameBuffer(int width, int height) {
        this(width, height, new LWJGLWrapper());
    }

    /**
     * Instantiates the Frame Buffer with the given width, height and GLWrapper
     * and generates an empty texture for storing all our buffered render data.
     *
     * @param width  buffer width
     * @param height buffer height
     * @param gl     LWJGL Implementation
     */
    public FrameBuffer(int width, int height, GLWrapper gl) {
        this.gl = gl;

        //Generate the frame buffer
        this.fboID = gl.glGenFramebuffers();
        bind();

        //Create the texture to render the data to, and attach it to our frame buffer
        this.texture = new Texture(width, height);
        gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTextureId(), 0);

        // Create RenderBuffer store the depth info
        int rboId = glGenRenderbuffers();
        gl.glBindRenderbuffer(GL_RENDERBUFFER, rboId);
        gl.glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboId);

        //Check it completed successfully.
        if (gl.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            log.error("Frame buffer is not complete");
            assert false;
        }
        //Release the FrameBuffer
        unbind();
    }

    /**
     * Bind the framebuffer resources
     */
    public void bind() {
        gl.glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    /**
     * Release the frame buffer resources
     */
    public void unbind() {
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Get the frame buffer texture Id.
     *
     * @return textureId
     */
    public int getTextureId() {
        return this.texture.getTextureId();
    }
}
