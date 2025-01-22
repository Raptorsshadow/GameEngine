package render;

import components.SpriteRenderer;
import org.joml.Vector4f;
import rubicon.Window;
import util.AssetPool;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/**
 Class: RenderBatch
 Author: rapto
 CreatedDate: 1/22/2025 : 10:43 AM
 Project: GameEngine
 Description: Batches render calls to speed up performance.

 */
public class RenderBatch {

    // Vertex
    // ======
    // Pos              Color
    // float, float,    float, float, float, float
    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE = 6;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final SpriteRenderer [] sprites;
    private       int               numSprites;
    private       boolean  hasRoom = false;
    private final float [] vertices;

    private int vaoId;
    private       int    vboId;
    private final int    maxBatchSize;
    private final Shader shader;

    /**
     * Default Constructor initializes specific renderBatch
     * @param maxBatchSize maximum number of renders per batch.
     */
    public RenderBatch(int maxBatchSize) {
        this.shader = AssetPool.getShader("assets/shader/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;
        // maxBatchSize * 4 Vertices * VERTEX_SIZE
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
    }

    /**
     * Generates and allocates resources for vertex array, indices buffer and element buffers.
     */
    public void start() {
        //Generate and bind a Vertex Array Object
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Allocate space for vertices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create and upload indices buffer
        int eboId = glGenBuffers();
        int [] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    /**
     * Adds a sprite to be rendered and generates it's vertex properties.
     * @param spr SpriteRenderer to be added
     */
    public void addSprite(SpriteRenderer spr) {
        // Get index and add RenderObject

        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        //Add properties to local vertices array
        loadVertexProperties(index);
        this.hasRoom = numSprites < this.maxBatchSize;
    }

    /**
     * For each sprite, generate the indices for each element.
     * @return Array of indices for all sprites.
     */
    public int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int [] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements ,i);
        }
        return elements;
    }

    /**
     * Generate all the element indices for each render call.
     * @param elements Indices Array that has been pre-sized for all elements
     * @param index Index of the specific
     */
    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //3, 2, 0, 0, 2, 1      7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex++] = offset + 3;
        elements[offsetArrayIndex++] = offset + 2;
        elements[offsetArrayIndex++] = offset;

        // Triangle 2
        elements[offsetArrayIndex++] = offset;
        elements[offsetArrayIndex++] = offset + 2;
        elements[offsetArrayIndex] = offset + 1;
    }

    /**
     * Populates, draws and frees shader resource.
     */
    public void render() {
        //For now, rebuffer all data every frame.
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    /**
     * Loads vertex properties for the given sprite at index
     * @param index Sprite we want to render
     */
    public void loadVertexProperties(int index) {
        SpriteRenderer spr = this.sprites[index];
        //Find offset within array (4 vertices per sprite)

        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = spr.getColor();

        //Add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i = 0; i < 4; i++) {
            switch(i) {
                case 1:
                    yAdd = 0.0f;
                    break;
                case 2:
                    xAdd = 0.0f;
                    break;
                case 3:
                    yAdd = 1.0f;
                    break;
                default:
            }

            //Set Position Data
            vertices[offset] = spr.gameObject.transform.position.x + (xAdd * spr.gameObject.transform.scale.x);
            vertices[offset + 1] = spr.gameObject.transform.position.y + (yAdd * spr.gameObject.transform.scale.y);

            //Set Color Data
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //Increment the offset for next sprite
            offset += VERTEX_SIZE;
        }

    }

    /**
     * Check if we have room for more sprites.
     * @return hasRoom
     */
    public boolean hasRoom() {
        return this.hasRoom;
    }
}