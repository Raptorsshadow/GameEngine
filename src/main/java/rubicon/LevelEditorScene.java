package rubicon;


import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import render.Shader;
import render.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Class: LevelEditorScene
 * Author: rapto
 * CreatedDate: 1/19/2025 : 3:49 AM
 * Project: GameEngine
 * Description: Editor Scene for testing capabilities.
 */
public class LevelEditorScene extends Scene {

    //Scene Shader
    private final Shader     shader;
    //Scene Texture
    private final Texture    texture;
    //Scene GameObject
    private final GameObject testObject;
    //Scene VertexArray
    private final float[]    vertexArray  = {
            //position            //color                     UV Coords
            100f, 0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1, 0,       //Bottom Right
            0f, 100f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0, 1,       //Top Left
            100f, 100f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1, 1,       //Top Right
            0f, 0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0, 0        //Bottom Left
    };
    //Scene Element Array
    //Important Counter Clockwise Order
    private final int[]      elementArray = {
            2, 1, 0,  //Top Right Triangle
            0, 1, 3   //Bottom Left Triangle
    };

    //Registered View Array Object Identifier
    private int vaoID;

    /**
     * Default constructor used to initialize the scene pieces
     */
    public LevelEditorScene() {
        this.texture = new Texture("assets/images/testImage.png");
        this.shader = new Shader("assets/shader/default.glsl");
        this.camera = new Camera(new Vector2f());
        this.testObject = new GameObject("test");
    }

    /**
     * Init method called when the scene is loaded, responsible for initializing all components.
     */
    @Override
    public void init() {

        this.shader.compileAndLinkShader();

        this.testObject.addComponent(new SpriteRenderer());
        this.testObject.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObject);
        // =========================================================
        // Generate VAO, VBO, and EBO buffer objects and send to GPU
        // =========================================================

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray)
                    .flip();

        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray)
                     .flip();

        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add The Vertex Attribute Pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeInBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        //Enable the Position Attribute.  Note the index 0 maps to VertexShader location = 0
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        //Enable the Color Attribute.  Note the index 1 maps to VertexShader location = 1
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeInBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        //Enable the UV Attribute
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeInBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    /**
     * Responsible for rendering each frame of the scene at delta time.
     *
     * @param dt Delta Time since last scene
     */
    @Override
    public void update(float dt) {
        //Bind the shader program
        this.camera.getPosition().x -= dt * 50.0f;
        this.camera.getPosition().y -= dt * 50.0f;
        this.shader.use();
        // Upload Texture
        this.shader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        this.shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        this.shader.uploadMat4f("uView", camera.getViewMatrix());
        this.shader.uploadFloat("uTime", Time.getTime());

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        this.shader.detach();

        this.gameObjects.forEach(go -> go.update(dt));

        if (this.gameObjects.size() == 1) {
            System.out.println("Creating Game Objects");
            GameObject go = new GameObject("Game Test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
        }
    }
}
