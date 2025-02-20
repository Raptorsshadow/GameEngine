package render;

import graphics.GLWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.*;
import org.lwjgl.BufferUtils;
import scene.Settings;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Class: Shader
 * Author: rapto
 * CreatedDate: 1/19/2025 : 12:59 AM
 * Project: GameEngine
 * Description: Responsible for configuring and linking shader resources and managing lifecycle and destruction when finished.
 */
public class Shader {
    // Constant for parsing shader files used to key on type preprocessor
    public static final  String TYPE_CONSTANT = "#type ";
    private static final Logger log           = LogManager.getLogger(Shader.class);
    //Shader filepath
    private final        String filePath;
    GLWrapper gl;
    //Shader programId registered on GPU
    private int     shaderProgramId;
    //Lifecycle tracking variable.  True if shader has been registered and is running
    private boolean inUse;
    //Parsed out vertex source from shader file
    private String  vertexSource;
    //Parsed out fragment source from shader file
    private String  fragmentSource;

    /**
     * Constructor that attempts to load the given shader file and parse out the vertex and fragment sources.
     *
     * @param filePath Shader file combining vertex and fragment sources.
     */
    public Shader(String filePath) {
        gl = Settings.graphicsImpl;
        this.filePath = filePath;

        try {
            //Load shader file
            String source = new String(Files.readAllBytes(Paths.get(filePath)));

            //split the source on the type pre-processor
            String[] split = source.split("(#type)( )+([a-zA-Z]+)");

            int i = 1;
            int index;
            int eol = 0;
            String shaderType;
            /*
                Iterate through the source looking for type declarations, determine the appropriate type
                and store the resulting regex parsed index in the appropriate source variable.
             */
            do {
                index = source.indexOf(TYPE_CONSTANT, eol) + TYPE_CONSTANT.length();
                eol = source.indexOf("\n", index);
                if (index >= 6) {
                    shaderType = source.substring(index, eol)
                                       .trim();
                    switch (shaderType) {
                        case "vertex":
                            this.vertexSource = split[i++];
                            break;
                        case "fragment":
                            this.fragmentSource = split[i++];
                            break;
                        default:
                            assert false : "Unsupported shader type detected " + shaderType;
                    }
                }
            } while (index >= TYPE_CONSTANT.length());

        } catch (IOException e) {
            log.error("Error: Could not open file for shader {}", filePath, e);
            assert false;
        }
    }

    /**
     * Compile and Link the verted and fragment shader sources
     */
    public void compileAndLinkShader() {
        // =========================================
        // Generate and Compile Shader and Fragments
        // =========================================

        // First load and compile the vertex Shader
        int vertexID = gl.glCreateShader(GL_VERTEX_SHADER);
        gl.glShaderSource(vertexID, vertexSource);
        gl.glCompileShader(vertexID);

        //Check if there were errors in compilation
        int success = gl.glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = gl.glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            log.error("{} 'defaultShader.glsl'\n\tVertex Shader Compilation Failed.", filePath);
            log.error(gl.glGetShaderInfoLog(vertexID, len));
            assert false;
        }

        // First load and compile the vertex Shader
        int fragmentID = gl.glCreateShader(GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragmentID, fragmentSource);
        gl.glCompileShader(fragmentID);

        //Check if there were errors in compilation
        success = gl.glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = gl.glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            log.error("{} 'defaultShader.glsl'\n\tFragment Shader Compilation Failed.", filePath);
            log.error(gl.glGetShaderInfoLog(fragmentID, len));
            assert false;
        }

        // ========================
        // Link Shader and Fragment
        // ========================
        shaderProgramId = gl.glCreateProgram();
        gl.glAttachShader(shaderProgramId, vertexID);
        gl.glAttachShader(shaderProgramId, fragmentID);
        gl.glLinkProgram(shaderProgramId);

        success = gl.glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = gl.glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            log.error("{} 'defaultShader.glsl'\n\tLinking of Shaders Failed.", filePath);
            log.error(gl.glGetProgramInfoLog(shaderProgramId, len));
            assert false;
        }
    }

    /**
     * Responsible for telling the system to begin using the linked shader.
     */
    public void use() {
        if (!inUse) {
            gl.glUseProgram(shaderProgramId);
            inUse = true;
        }
    }

    /**
     * Responsible for telling the system to stop using the linked shader.
     */
    public void detach() {
        if (inUse) {
            gl.glUseProgram(0);
            inUse = false;
        }
    }

    /**
     * Registers a Matrix4f value with the given name in the graphics card for TextureShaders
     *
     * @param varName name of variable
     * @param mat     Matrix4f we wish to set
     */
    public void uploadMat4f(String varName, Matrix4f mat) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer); // This converts the matrix into a 1D array, a 4f Matrix is 4x4 -> 16 1D long Array
        gl.glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    /**
     * Registers a Matrix3f value with the given name in the graphics card for TextureShaders
     *
     * @param varName name of variable
     * @param mat     Matrix3f we wish to set
     */
    public void uploadMat3f(String varName, Matrix3f mat) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer); // This converts the matrix into a 1D array, a 4f Matrix is 4x4 -> 16 1D long Array
        gl.glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    /**
     * Registers a Vector4f value with the given name in the graphics card for TextureShaders
     *
     * @param varName name of variable
     * @param vec     Vector4f we wish to set
     */
    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        gl.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    /**
     * Registers a Vector3f value with the given name in the graphics card for TextureShaders
     *
     * @param varName name of variable
     * @param vec     Vector3f we wish to set
     */
    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        gl.glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    /**
     * Registers a Vector2f value with the given name in the graphics card for TextureShaders
     *
     * @param varName name of variable
     * @param vec     Vector2f we wish to set
     */
    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        gl.glUniform2f(varLocation, vec.x, vec.y);
    }

    /**
     * Registers a float value with the given name in the graphics card for TextureShaders
     *
     * @param varName name of variable
     * @param val     Vector4f we wish to set
     */
    public void uploadFloat(String varName, float val) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        gl.glUniform1f(varLocation, val);
    }

    /**
     * Registers an int value with the given name in the graphics card for TextureShaders
     *
     * @param varName name of variable
     * @param val     int we wish to set
     */
    public void uploadInt(String varName, int val) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        gl.glUniform1i(varLocation, val);
    }

    /**
     * Registers a Texture value with the given name in the graphics card for TextureShaders.  This has the same
     * behavior as uploadInt, however in the shader this is treated differently so we're making the distinction here.
     *
     * @param varName name of variable
     * @param slot    Texture slot we're forwarding.
     */
    public void uploadTexture(String varName, int slot) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        gl.glUniform1i(varLocation, slot);
    }

    /**
     * Registers an array of Texture values with the given name in the graphics card for TextureShaders.
     *
     * @param varName name of variable
     * @param slots   Texture slots we're forwarding.
     */
    public void uploadIntArray(String varName, int[] slots) {
        int varLocation = gl.glGetUniformLocation(shaderProgramId, varName);
        use();
        gl.glUniform1iv(varLocation, slots);
    }
}
