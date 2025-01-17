package render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    public static final String TYPE_CONSTANT = "#type ";
    private int shaderProgramId;
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
        glUseProgram(shaderProgramId);

    }

    public void detach() {
        glUseProgram(0);
    }
}
