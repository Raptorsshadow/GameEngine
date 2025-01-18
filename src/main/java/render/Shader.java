package render;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    public static final String TYPE_CONSTANT = "#type ";
    private int shaderProgramId;
    private boolean inUse;
    private String vertexSource;
    private String fragmentSource;
    private final String filePath;
    public Shader(String filePath) {
        this.filePath = filePath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] split = source.split("(#type)( )+([a-zA-Z]+)");

            int i = 1;
            int index;
            int eol = 0;
            String shaderType;
            do {
                index = source.indexOf(TYPE_CONSTANT, eol) + TYPE_CONSTANT.length();
                eol = source.indexOf("\n", index);
                if(index >= 6) {
                    shaderType = source.substring(index, eol).trim();
                    switch(shaderType) {
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
            } while(index >= TYPE_CONSTANT.length());
            //compile();
        } catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader " + filePath;
        }
    }
    public void compileAndLinkShader() {
        // =========================================
        // Generate and Compile Shader and Fragments
        // =========================================

        // First load and compile the vertex Shader
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        //Check if there were errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filePath + " 'defaultShader.glsl'\n\tVertex Shader Compilation Failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // First load and compile the vertex Shader
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        //Check if there were errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filePath + "'defaultShader.glsl'\n\tFragment Shader Compilation Failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // ========================
        // Link Shader and Fragment
        // ========================
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexID);
        glAttachShader(shaderProgramId, fragmentID);
        glLinkProgram(shaderProgramId);

        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filePath + " 'defaultShader.glsl'\n\tLinking of Shaders Failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramId, len));
            assert false : "";
        }
    }

    public void use() {
        if(!inUse) {
            glUseProgram(shaderProgramId);
            inUse = true;
        }
    }

    public void detach() {
        if(inUse) {
            glUseProgram(0);
            inUse = false;
        }
    }

    /**
     * Registers a Matrix4f value with the given name in the graphics card for TextureShaders
     * @param varName name of variable
     * @param mat Matrix4f we wish to set
     */
    public void uploadMat4f(String varName, Matrix4f mat) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer); // This converts the matrix into a 1D array, a 4f Matrix is 4x4 -> 16 1D long Array
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    /**
     * Registers a Matrix3f value with the given name in the graphics card for TextureShaders
     * @param varName name of variable
     * @param mat Matrix3f we wish to set
     */
    public void uploadMat3f(String varName, Matrix3f mat) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer); // This converts the matrix into a 1D array, a 4f Matrix is 4x4 -> 16 1D long Array
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    /**
     * Registers a Vector4f value with the given name in the graphics card for TextureShaders
     * @param varName name of variable
     * @param vec Vector4f we wish to set
     */
    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }
    /**
     * Registers a Vector3f value with the given name in the graphics card for TextureShaders
     * @param varName name of variable
     * @param vec Vector3f we wish to set
     */
    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }
    /**
     * Registers a Vector2f value with the given name in the graphics card for TextureShaders
     * @param varName name of variable
     * @param vec Vector2f we wish to set
     */
    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }
    /**
     * Registers a float value with the given name in the graphics card for TextureShaders
     * @param varName name of variable
     * @param val Vector4f we wish to set
     */
    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1f(varLocation, val);
    }

    /**
     * Registers an int value with the given name in the graphics card for TextureShaders
     * @param varName name of variable
     * @param val int we wish to set
     */
    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1i(varLocation, val);
    }

    /**
     * Registers a Texture value with the given name in the graphics card for TextureShaders.  This has the same
     * behavior as uploadInt, however in the shader this is treated differently so we're making the distinction here.
     * @param varName name of variable
     * @param slot Texture slot we're forwarding.
     */
    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1i(varLocation, slot);
    }
}
