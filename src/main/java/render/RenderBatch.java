package render;

import components.SpriteRenderer;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector4f;
import rubicon.Window;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/**
 * Class: RenderBatch
 * Author: rapto
 * CreatedDate: 1/22/2025 : 10:43 AM
 * Project: GameEngine
 * Description: Batches render calls to speed up performance.
 */
public class RenderBatch implements Comparable<RenderBatch> {

    // Vertex
    // ======
    // Pos              Color                           TexCoords       TexId
    // float, float,    float, float, float, float,     float, float,   float
    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int TEX_COORDS_SIZE = 2;
    private static final int TEX_ID_SIZE = 1;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private static final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private static final int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private final SpriteRenderer[] sprites;
    private final float[] vertices;
    private final List<Texture> textures;
    private final int maxBatchSize;
    private final Shader shader;
    private final int zIndex;
    private int numSprites;
    private boolean hasRoom;
    private int vaoId;
    private int vboId;

    /**
     * Default Constructor initializes specific renderBatch
     *
     * @param maxBatchSize maximum number of renders per batch.
     */
    public RenderBatch(int maxBatchSize, int zIndex) {
        this.shader = AssetPool.getShader("assets/shader/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.textures = new ArrayList<>();
        this.maxBatchSize = maxBatchSize;
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
        this.zIndex = zIndex;
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
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    /**
     * Adds a sprite to be rendered and generates it's vertex properties.
     *
     * @param spr SpriteRenderer to be added
     */
    public void addSprite(SpriteRenderer spr) {
        // Get index and add RenderObject

        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        if (spr.getTexture() != null && !textures.contains(spr.getTexture())) {
            textures.add(spr.getTexture());
        }
        //Add properties to local vertices array
        loadVertexProperties(index);
        this.hasRoom = numSprites < this.maxBatchSize;
    }

    /**
     * For each sprite, generate the indices for each element.
     *
     * @return Array of indices for all sprites.
     */
    public int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    /**
     * Generate all the element indices for each render call.
     *
     * @param elements Indices Array that has been pre-sized for all elements
     * @param index    Index of the specific
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
        boolean rebufferData = false;
        /*
            Loop over sprites and check if any are dirty and need
            re-rendered.
         */
        for (int i = 0; i < this.numSprites; i++) {
            SpriteRenderer spr = sprites[i];
            if (spr.isDirty()) {
                loadVertexProperties(i);
                spr.setClean();
                rebufferData = true;
            }
        }
        //If rebuffered, send the data to the GPU
        if (rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene()
                .getCamera()
                .getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene()
                .getCamera()
                .getViewMatrix());

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i)
                    .bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);

        for (Texture texture : textures) {
            texture.unbind();
        }
        shader.detach();
    }

    /**
     * Loads vertex properties for the given sprite at index
     *
     * @param index Sprite we want to render
     */
    public void loadVertexProperties(int index) {
        SpriteRenderer spr = this.sprites[index];
        //Find offset within array (4 vertices per sprite)

        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = spr.getColor();
        int texId = 0;
        Vector2f[] texCoords = spr.getTexCoords();
        if (spr.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i)
                        .equals(spr.getTexture())) {
                    texId = i + 1;
                    break;
                }
            }
        }

        //Add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
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

            //Set Texture Coordinates
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;
            //Set Texture Id
            vertices[offset + 8] = texId;
            //Increment the offset for next sprite
            offset += VERTEX_SIZE;
        }

    }

    /**
     * Check if we have room for more sprites.
     *
     * @return hasRoom
     */
    public boolean hasRoom() {
        return this.hasRoom;
    }

    public boolean hasSpriteRoom() {
        return this.textures.size() < texSlots.length;
    }

    public boolean hasSprite(Texture t) {
        return this.textures.contains(t);
    }

    public int getzIndex() {
        return this.zIndex;
    }

    @Override
    public int compareTo(@NotNull RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }
}
