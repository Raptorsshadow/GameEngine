package render;

import graphics.GLWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import rubicon.Window;
import scene.Settings;
import util.AssetPool;
import util.JMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

/**
 * Class: DebugDraw
 * Author: rapto
 * CreatedDate: 2/8/2025 : 7:41 PM
 * Project: GameEngine
 * Description: Render Line2D components to the screen.
 */
public class DebugDraw {
    // Default Lifetime duration
    public static final  int          DEF_LIFETIME = 300;
    // Default Color to draw with
    public static final  Vector3f     DEF_COLOR    = new Vector3f(1f, 0f, 1f);
    private static final Logger       log          = LogManager.getLogger(DebugDraw.class);
    // Max number of debug lines to render.
    private static final int          MAX_LINES    = 500;
    // Collection to hold all the lines we're rendering
    private static final List<Line2D> lines        = new ArrayList<>();
    // 6 floats per vertex, 2 vertices per line.
    private static final float[]      vertexArray  = new float[MAX_LINES * 6 * 2];
    // Shader for rendering a 2D line.
    private static final Shader       shader       = AssetPool.getShader("assets/shader/debugLine2D.glsl");
    //Vertex Array Object Id
    private static       int          vaoId;
    //Vertex Buffer Object Id
    private static       int          vboId;
    //State variable for managing lifecycle
    private static       boolean      isStarted    = false;

    private DebugDraw() {
        //Hidden private constructor
    }

    /**
     * Initialize the vao and vbo resources.
     */
    public static void start() {
        GLWrapper gl = Settings.graphicsImpl;
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
        if (!isStarted) {
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
        GLWrapper gl = Settings.graphicsImpl;
        if (lines.isEmpty()) {
            return;
        }

        //Generate vertexArray of all lines.
        int index = 0;
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) {
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

        if (shader == null) {
            log.warn("Debug Shader is null, unable to draw.");
            return;
        }
        // Enable the Shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene()
                                                .getCamera()
                                                .getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene()
                                          .getCamera()
                                          .getViewMatrix());

        //Bind the vao
        gl.glBindVertexArray(vaoId);
        gl.glEnableVertexAttribArray(0);
        gl.glEnableVertexAttribArray(1);

        //Draw the batch
        gl.glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        //Release Resources
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glBindVertexArray(0);

        //Unbind Shader
        shader.detach();
    }

    /**
     * Add a 2D line using default color and lifetime
     *
     * @param from Vector2f source vertex
     * @param to   Vector2f dest vertex
     */
    public static void addLine2D(Vector2f from, Vector2f to) {
        addLine2D(from, to, DEF_COLOR, DEF_LIFETIME);
    }

    /**
     * Add a 2D line using default lifetime
     *
     * @param from  Vector2f source vertex
     * @param to    Vector2f dest vertex
     * @param color Vector3f color definition
     */
    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
        addLine2D(from, to, color, DEF_LIFETIME);
    }

    /**
     * Add a 2D line
     *
     * @param from     Vector2f source vertex
     * @param to       Vector2f dest vertex
     * @param color    Vector3f color definition
     * @param lifetime duration line should be rendered
     */
    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (lines.size() >= MAX_LINES) {
            return;
        }
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    /**
     * Render a box that with no rotation, default color and 1 frame lifetime.
     *
     * @param center Vector2f center point
     * @param dim    Vector2f dimensions
     */
    public static void addBox2D(Vector2f center, Vector2f dim) {
        addBox2D(center, dim, 0f, DEF_COLOR, 1);
    }

    /**
     * Render a box that can rotate with default color and 1 frame lifetime
     *
     * @param center   Vector2f center point
     * @param dim      Vector2f dimensions
     * @param rotation rotation value to transform the vertices
     */
    public static void addBox2D(Vector2f center, Vector2f dim, float rotation) {
        addBox2D(center, dim, rotation, DEF_COLOR, 1);
    }

    /**
     * Render a box that can rotate with 1 frame lifetime
     *
     * @param center   Vector2f center point
     * @param dim      Vector2f dimensions
     * @param rotation rotation value to transform the vertices
     * @param color    Vector3f color definition
     */
    public static void addBox2D(Vector2f center, Vector2f dim, float rotation, Vector3f color) {
        addBox2D(center, dim, rotation, color, 1);
    }

    /**
     * Render a box that can rotate
     *
     * @param center   Vector2f center point
     * @param dim      Vector2f dimensions
     * @param rotation rotation value to transform the vertices
     * @param color    Vector3f color definition
     * @param lifetime duration box should be rendered
     */
    public static void addBox2D(Vector2f center, Vector2f dim, float rotation, Vector3f color, int lifetime) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dim).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dim).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y), new Vector2f(max.x, max.y), new Vector2f(max.x,
                                                                                                                 min.y)
        };

        if (rotation != 0f) {
            for (Vector2f v : vertices) {
                JMath.rotate(v, rotation, center);
            }
        }

        addLine2D(vertices[0], vertices[1], color, lifetime);
        addLine2D(vertices[0], vertices[3], color, lifetime);
        addLine2D(vertices[1], vertices[2], color, lifetime);
        addLine2D(vertices[2], vertices[3], color, lifetime);
    }

    /**
     * Render a circle with default color.
     *
     * @param center Vector2f center point
     * @param radius radius of circle
     */
    public static void addCircle(Vector2f center, float radius) {
        addCircle(center, radius, DEF_COLOR, 1);
    }

    /**
     * Render a circle
     *
     * @param center Vector2f center point
     * @param radius radius of circle
     * @param color  Vector3f color definition
     */
    public static void addCircle(Vector2f center, float radius, Vector3f color) {
        addCircle(center, radius, color, 1);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color, int lifetime) {
        Vector2f[] points = new Vector2f[32];
        int increment = 360 / points.length;
        int currentAngle = 0;
        for (int i = 0; i < points.length; i++) {
            Vector2f tmp = new Vector2f(radius, 0);
            JMath.rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            if (i > 0) {
                addLine2D(points[i - 1], points[i], color, lifetime);
            }
            currentAngle += increment;
        }
        addLine2D(points[0], points[points.length - 1], color, lifetime);
    }
}
