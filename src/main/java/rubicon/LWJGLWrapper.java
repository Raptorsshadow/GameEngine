package rubicon;


import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

/**
 * Class: LWJGLWrapper
 * Author: rapto
 * CreatedDate: 1/29/2025 : 3:56 AM
 * Project: GameEngine
 * Description: Implements the GLWrapper interface passing through to the relevant GLFW calls.
 */
public class LWJGLWrapper implements GLWrapper {
    @Override
    public void glBindTexture(int target, int texture) {
        GL11.glBindTexture(target, texture);
    }

    @Override
    public void glTexParameteri(int target, int pName, int pVal) {
        GL11.glTexParameteri(target, pName, pVal);
    }

    @Override
    public void stbi_set_flip_vertically_on_load(boolean shouldFlip) {
        org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load(shouldFlip);
    }

    @Override
    public ByteBuffer stbi_load(String fileName, IntBuffer x, IntBuffer y, IntBuffer channels, int channelCount) {
        return org.lwjgl.stb.STBImage.stbi_load(fileName, x, y, channels, channelCount);
    }

    @Override
    public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
        GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
    }

    @Override
    public void stbi_image_free(ByteBuffer image) {
        org.lwjgl.stb.STBImage.stbi_image_free(image);
    }

    @Override
    public int glGenTextures() {
        return GL11.glGenTextures();
    }

    @Override
    public void glShaderSource(int shader, String string) {
        GL20.glShaderSource(shader, string);
    }

    @Override
    public void glCompileShader(int shader) {
        GL20.glCompileShader(shader);
    }

    @Override
    public int glGetShaderi(int shader, int pname) {
        return GL20.glGetShaderi(shader, pname);
    }

    @Override
    public String glGetShaderInfoLog(int shader, int maxLength) {
        return GL20.glGetShaderInfoLog(shader, maxLength);
    }

    @Override
    public int glCreateShader(int type) {
        return GL20.glCreateShader(type);
    }

    @Override
    public int glCreateProgram() {
        return GL20.glCreateProgram();
    }

    @Override
    public void glAttachShader(int program, int shader) {
        GL20.glAttachShader(program, shader);
    }

    @Override
    public void glLinkProgram(int program) {
        GL20.glLinkProgram(program);
    }

    @Override
    public int glGetProgrami(int program, int pName) {
        return GL20.glGetProgrami(program, pName);
    }

    @Override
    public String glGetProgramInfoLog(int program, int maxLength) {
        return GL20.glGetProgramInfoLog(program, maxLength);
    }

    @Override
    public void glUseProgram(int program) {
        GL20.glUseProgram(program);
    }

    @Override
    public int glGetUniformLocation(int program, String name) {
        return GL20.glGetUniformLocation(program, name);
    }

    @Override
    public void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer buffer) {
        GL20.glUniformMatrix4fv(location, transpose, buffer);
    }

    @Override
    public void glUniformMatrix3fv(int location, boolean transpose, FloatBuffer value) {
        GL20.glUniformMatrix3fv(location, transpose, value);
    }

    @Override
    public void glUniform4f(int location, float v0, float v1, float v2, float v3) {
        GL20.glUniform4f(location, v0, v1, v2, v3);
    }

    @Override
    public void glUniform3f(int location, float v0, float v1, float v2) {
        GL20.glUniform3f(location, v0, v1, v2);
    }

    @Override
    public void glUniform2f(int location, float v0, float v1) {
        GL20.glUniform2f(location, v0, v1);
    }

    @Override
    public void glUniform1f(int location, float v0) {
        GL20.glUniform1f(location, v0);
    }

    @Override
    public void glUniform1i(int location, int v0) {
        GL20.glUniform1i(location, v0);
    }

    @Override
    public void glUniform1iv(int location, int[] value) {
        GL20.glUniform1iv(location, value);
    }

    @Override
    public int glGenVertexArrays() {
        return GL30C.glGenVertexArrays();
    }

    @Override
    public void glBindVertexArray(int i) {
        GL30C.glBindVertexArray(i);
    }

    @Override
    public int glGenBuffers() {
        return GL15C.glGenBuffers();
    }

    @Override
    public void glBindBuffer(int i, int i1) {
        GL15C.glBindBuffer(i, i1);
    }

    @Override
    public void glBufferData(int target, long size, int usage) {
        GL15C.glBufferData(target, size, usage);
    }

    @Override
    public void glBufferData(int target, int[] data, int usage) {
        GL15C.glBufferData(target, data, usage);
    }

    @Override
    public void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int pointer) {
        GL20C.glVertexAttribPointer(index, size, type, normalized, stride, pointer);
    }

    @Override
    public void glEnableVertexAttribArray(int i) {
        GL20C.glEnableVertexAttribArray(i);
    }

    @Override
    public void glBufferSubData(int target, int offset, float[] data) {
        GL15C.glBufferSubData(target, offset, data);
    }

    @Override
    public void glActiveTexture(int i) {
        GL13C.glActiveTexture(i);
    }

    @Override
    public void glDrawElements(int mode, int count, int type, long indices) {
        GL11C.glDrawElements(mode, count, type, indices);
    }

    @Override
    public void glDisableVertexAttribArray(int i) {
        GL20C.glDisableVertexAttribArray(i);
    }

    @Override
    public boolean glfwInit() {
        return GLFW.glfwInit();
    }

    @Override
    public void glfwWindowHint(int hint, int value) {
        GLFW.glfwWindowHint(hint, value);
    }

    @Override
    public long glfwCreateWindow(int width, int height, String title, long monitor, long share) {
        return GLFW.glfwCreateWindow(width, height, title, monitor, share);
    }

    @Override
    public void glfwDefaultWindowHints() {
        GLFW.glfwDefaultWindowHints();
    }

    @Override
    public void glfwSetWindowPos(long window, int xPos, int yPos) {
        GLFW.glfwSetWindowPos(window, xPos, yPos);
    }

    @Override
    public void glfwGetWindowSize(long window, IntBuffer width, IntBuffer height) {
        GLFW.glfwGetWindowSize(window, width, height);
    }

    @Override
    public void glfwMakeContextCurrent(long window) {
        GLFW.glfwMakeContextCurrent(window);
    }

    @Override
    public void createCapabilities() {
        GL.createCapabilities();
    }

    @Override
    public void glfwSwapInterval(int interval) {
        GLFW.glfwSwapInterval(interval);
    }

    @Override
    public void glfwShowWindow(long window) {
        GLFW.glfwShowWindow(window);
    }

    @Override
    public void glfwMaximizeWindow(long window) {
        GLFW.glfwMaximizeWindow(window);
    }

    @Override
    public void glfwSetWindowSizeCallback(long window, GLFWWindowSizeCallbackI cbFun) {
        GLFW.glfwSetWindowSizeCallback(window, cbFun);
    }

    @Override
    public void glEnable(int target) {
        GL11.glEnable(target);
    }

    @Override
    public void glBlendFunc(int sFactor, int dFactor) {
        GL11.glBlendFunc(sFactor, dFactor);
    }

    @Override
    public void enableErrors() {
        GLFWErrorCallback.createPrint(System.err)
                         .set();
    }

    @Override
    public void glClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void glClear(int mask) {
        GL11.glClear(mask);
    }

    @Override
    public void glfwSwapBuffers(long window) {
        GLFW.glfwSwapBuffers(window);
    }

    @Override
    public void glfwPollEvents() {
        GLFW.glfwPollEvents();
    }

    @Override
    public void freeCallbacks(long window) {
        Callbacks.glfwFreeCallbacks(window);
    }

    @Override
    public void disableErrors() {
        Objects.requireNonNull(glfwSetErrorCallback(null))
               .free();
    }

    @Override
    public void glfwDestroyWindow(long window) {
        GLFW.glfwDestroyWindow(window);
    }

    @Override
    public void glfwTerminate() {
        GLFW.glfwTerminate();
    }

    @Override
    public void glfwSetCursorPosCallback(long window, GLFWCursorPosCallbackI cbfun) {
        GLFW.glfwSetCursorPosCallback(window, cbfun);
    }

    @Override
    public void glfwSetMouseButtonCallback(long window, GLFWMouseButtonCallbackI cbfun) {
        GLFW.glfwSetMouseButtonCallback(window, cbfun);
    }

    @Override
    public void glfwSetScrollCallback(long window, GLFWScrollCallbackI cbfun) {
        GLFW.glfwSetScrollCallback(window, cbfun);
    }

    @Override
    public void glfwSetKeyCallback(long window, GLFWKeyCallbackI cbfun) {
        GLFW.glfwSetKeyCallback(window, cbfun);
    }

    @Override
    public boolean glfwWindowShouldClose(long window) {
        return GLFW.glfwWindowShouldClose(window);
    }

    @Override
    public double glfwGetTime() {
        return GLFW.glfwGetTime();
    }

    @Override
    public long glfwGetPrimaryMonitor() {
        return GLFW.glfwGetPrimaryMonitor();
    }

    @Override
    public GLFWVidMode glfwGetVideoMode(long monitor) {
        return GLFW.glfwGetVideoMode(monitor);
    }
}
