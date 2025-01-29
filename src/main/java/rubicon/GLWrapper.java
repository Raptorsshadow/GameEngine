package rubicon;

import org.lwjgl.glfw.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Class: GLWrapper
 * Author: rapto
 * CreatedDate: 1/29/2025 : 3:53 AM
 * Project: GameEngine
 * Description: Wrapper that allows for Mocking and substitution of Native calls for testing purposes.
 */
public interface GLWrapper {

    void glBindTexture(int target, int texture);

    void glTexParameteri(int target, int pName, int pVal);

    void stbi_set_flip_vertically_on_load(boolean shouldFlip);

    ByteBuffer stbi_load(String fileName, IntBuffer x, IntBuffer y, IntBuffer channels, int channelCount);

    void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer pixels);

    void stbi_image_free(ByteBuffer image);

    int glGenTextures();

    void glShaderSource(int shader, String string);

    void glCompileShader(int shader);

    int glGetShaderi(int shader, int pname);

    String glGetShaderInfoLog(int shader, int maxLength);

    int glCreateShader(int type);

    int glCreateProgram();

    void glAttachShader(int program, int shader);

    void glLinkProgram(int program);

    int glGetProgrami(int program, int pName);

    String glGetProgramInfoLog(int program, int maxLength);

    void glUseProgram(int program);

    int glGetUniformLocation(int program, String name);

    void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer buffer);

    void glUniformMatrix3fv(int location, boolean transpose, FloatBuffer value);

    void glUniform4f(int location, float v0, float v1, float v2, float v3);

    void glUniform3f(int location, float v0, float v1, float v2);

    void glUniform2f(int location, float v0, float v1);

    void glUniform1f(int location, float v0);

    void glUniform1i(int location, int v0);

    void glUniform1iv(int location, int[] value);

    int glGenVertexArrays();

    void glBindVertexArray(int i);

    int glGenBuffers();

    void glBindBuffer(int i, int i1);

    void glBufferData(int target, long size, int usage);

    void glBufferData(int target, int[] data, int usage);

    void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int pointer);

    void glEnableVertexAttribArray(int i);

    void glBufferSubData(int target, int offset, float[] data);

    void glActiveTexture(int i);

    void glDrawElements(int mode, int count, int type, long indices);

    void glDisableVertexAttribArray(int i);

    boolean glfwInit();

    void glfwWindowHint(int hint, int value);

    long glfwCreateWindow(int width, int height, String title, long monitor, long share);

    void glfwDefaultWindowHints();

    void glfwSetWindowPos(long window, int xPos, int yPos);

    void glfwGetWindowSize(long window, IntBuffer width, IntBuffer height);

    void glfwMakeContextCurrent(long window);

    void createCapabilities();

    void glfwSwapInterval(int interval);

    void glfwShowWindow(long window);

    void glfwMaximizeWindow(long window);

    void glfwSetWindowSizeCallback(long window, GLFWWindowSizeCallbackI cbFun);

    void glEnable(int target);

    void glBlendFunc(int sFactor, int dFactor);

    void enableErrors();

    void glClearColor(float red, float green, float blue, float alpha);

    void glClear(int mask);

    void glfwSwapBuffers(long window);

    void glfwPollEvents();

    void freeCallbacks(long window);

    void disableErrors();

    void glfwDestroyWindow(long window);

    void glfwTerminate();

    void glfwSetCursorPosCallback(long window, GLFWCursorPosCallbackI cbfun);

    void glfwSetMouseButtonCallback(long window, GLFWMouseButtonCallbackI cbfun);

    void glfwSetScrollCallback(long window, GLFWScrollCallbackI cbfun);

    void glfwSetKeyCallback(long window, GLFWKeyCallbackI cbfun);

    boolean glfwWindowShouldClose(long window);

    double glfwGetTime();

    long glfwGetPrimaryMonitor();

    GLFWVidMode glfwGetVideoMode(long monitor);
}
