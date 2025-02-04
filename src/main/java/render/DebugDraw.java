package render;

import graphics.GLWrapper;
import graphics.LWJGLWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import rubicon.Window;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

public class DebugDraw {
    private static final Logger log = LogManager.getLogger(DebugDraw.class);

    public static final int DEF_LIFETIME = 300;
    public static final Vector3f DEF_COLOR = new Vector3f(1f, 0f, 1f);
    private static final int MAX_LINES = 500;
    private static final List<Line2D> lines = new ArrayList<>();
    //6 floats per vertex, 2 vertices per line.
    private static final float [] vertexArray = new float[MAX_LINES * 6 * 2];
    private static final Shader shader = AssetPool.getShader("assets/shader/debugLine2D.glsl");
    private static int vaoId;
    private static int vboId;
    private static boolean isStarted = false;
    private static GLWrapper gl;

    private DebugDraw() {
        DebugDraw.setGLWrapper(new LWJGLWrapper());
    }

    public static void setGLWrapper(GLWrapper gl) {
        DebugDraw.gl = gl;
    }
    /**
     * Initialize the vao and vbo resources.
     */
    public static void start() {
        vaoId = gl.glGenVertexArrays();
        gl.glBindVertexArray(vaoId);

        vboId = gl.glGenBuffers();
        gl.glBindBuffer(GL_ARRAY_BUFFER, vboId);
        gl.glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //X, Y, Z Positions
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        //R, G, B Color Bits
        gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        gl.glEnableVertexAttribArray(1);

        gl.glLineWidth(2f);
    }

    /**
     * Ensures the DebugDraw has been started and clean out expired lines.
     */
    public static void beginFrame() {
        if(!isStarted) {
            start();
            isStarted = true;
        }

        //Remove Deadlines
        lines.removeIf(line -> line.beginFrame() < 0);
    }

    /**
     * Draw all lines to the screen
     */
    public static void draw() {
        if(lines.isEmpty()) {
            return;
        }

        //Generate vertexArray of all lines.
        int index = 0;
        for(Line2D line : lines) {
            for(int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                //Set Position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = 2;

                //Set color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }
        //Populate the vbo
        gl.glBindBuffer(GL_ARRAY_BUFFER, vboId);
        gl.glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

        if(shader == null) {
            log.warn("Debug Shader is null, unable to draw.");
            return;
        }
        // Enable the Shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        //Bind the vao
        gl.glBindVertexArray(vaoId);
        gl.glEnableVertexAttribArray(0);
        gl.glEnableVertexAttribArray(1);

        //Draw the batch
        gl.glDrawArrays(GL_LINES, 0, lines.size()*6*2);

        //Release Resources
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glBindVertexArray(0);

        //Unbind Shader
        shader.detach();
    }

    public static void addLine2D(Vector2f from, Vector2f to) {
        addLine2D(from, to, DEF_COLOR, DEF_LIFETIME);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
        addLine2D(from, to, color, DEF_LIFETIME);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if(lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }
}
