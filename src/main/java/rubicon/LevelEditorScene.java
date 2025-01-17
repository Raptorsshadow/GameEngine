package rubicon;


import org.lwjgl.BufferUtils;
import render.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private final Shader shader;
    private int vaoID;
    private final float [] vertexArray = {
      //position         //color
      0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,    //Bottom Right
      -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,    //Top Left
      0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,     //Top Right
      -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f    //Bottom Left
    };
    //Important Counter Clockwise Order
    private final int[] elementArray = {
      2, 1, 0,  //Top Right Triangle
      0, 1, 3   //Bottom Left Triangle
    };
    public LevelEditorScene() {
        this.shader = new Shader("assets/shader/default.glsl");
    }

    @Override
    public void init() {

        this.shader.compileAndLinkShader();

        // =========================================================
        // Generate VAO, VBO, and EBO buffer objects and send to GPU
        // =========================================================

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add The Vertex Attribute Pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeInBytes = 4;
        int vertexSizeInBytes = (positionsSize + colorSize) * floatSizeInBytes;

        //Enable the Position Attribute.  Note the index 0 maps to VertexShader location = 0
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        //Enable the Color Attribute.  Note the index 1 maps to VertexShader location = 1
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionsSize * floatSizeInBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //Bind the shader program
        this.shader.use();
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        this.shader.detach();
    }
}
