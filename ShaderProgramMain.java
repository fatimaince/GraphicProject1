
package myProject;


import org.lwjgl.opengl.GL33;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShaderProgramMain {

    private final int programId;
    private final Map<String, Integer> uniformVars = new HashMap<>();

    public ShaderProgramMain() {
        programId = GL33.glCreateProgram();
    }

    public ShaderProgramMain(String vertexShaderPath, String fragmentShaderPath) {
        programId = GL33.glCreateProgram();
        attachShader(vertexShaderPath, GL33.GL_VERTEX_SHADER);
        attachShader(fragmentShaderPath, GL33.GL_FRAGMENT_SHADER);
        link();
    }

    public void delete() {
        GL33.glDeleteProgram(programId);
    }

    public void link() {
        GL33.glLinkProgram(programId);

        int isLinked = GL33.glGetProgrami(programId, GL33.GL_LINK_STATUS);
        if (isLinked == GL33.GL_FALSE) {
            String log = GL33.glGetProgramInfoLog(programId);
            throw new RuntimeException("Program Linking Error:\n" + log);
        }
    }

    public void bind() {
        GL33.glUseProgram(programId);
    }

    public void addUniform(String varName) {
        int location = GL33.glGetUniformLocation(programId, varName);
        if (location == -1) {
            throw new RuntimeException("Uniform variable not found: " + varName);
        }
        uniformVars.put(varName, location);
    }

    public void setUniform(String varName, float x, float y, float z) {
        int location = uniformVars.getOrDefault(varName, -1);
        if (location != -1) {
            GL33.glUniform3f(location, x, y, z);
        } else {
            System.err.println("Uniform variable not found: " + varName);
        }
    }

    public void setUniform(String varName, float r, float g, float b, float a) {
        int location = uniformVars.getOrDefault(varName, -1);
        if (location != -1) {
            GL33.glUniform4f(location, r, g, b, a);
        } else {
            System.err.println("Uniform variable not found: " + varName);
        }
    }
    public void setUniform(String varName, float value) {
    int location = uniformVars.getOrDefault(varName, -1);
    if (location != -1) {
        GL33.glUniform1f(location, value);
    } else {
        System.err.println("Uniform variable not found: " + varName);
    }
}
public void unbind() {
    GL33.glUseProgram(0);
}


    public void attachShader(String fileName, int shaderType) {
        int shaderId = GL33.glCreateShader(shaderType);

        String sourceCode = getShaderFromFile(fileName);
        GL33.glShaderSource(shaderId, sourceCode);
        GL33.glCompileShader(shaderId);

        int isCompiled = GL33.glGetShaderi(shaderId, GL33.GL_COMPILE_STATUS);
        if (isCompiled == GL33.GL_FALSE) {
            String log = GL33.glGetShaderInfoLog(shaderId);
            String shaderTypeName = shaderType == GL33.GL_VERTEX_SHADER ? "Vertex Shader" : "Fragment Shader";
            throw new RuntimeException("Shader Compilation Error (" + shaderTypeName + "):\n" + log);
        }

        GL33.glAttachShader(programId, shaderId);
        GL33.glDeleteShader(shaderId);
    }

    private String getShaderFromFile(String fileName) {
        try {
            return Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader file: " + fileName, e);
        }
    }
}
