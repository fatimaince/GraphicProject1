
package myProject;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LastTryingMain {

    private long window;
  
     private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private ShaderProgramMain shaderProgram;

    private List<Float> vertices = new ArrayList<>();

    public static void main(String[] args) {
        new LastTryingMain().run();
    }

    public void run() {
        init();
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(1200, 1200, "Pheses Of The Moon", NULL, NULL);
       

        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(window);
        glfwSetKeyCallback(window, this::keyCallback);

        GL.createCapabilities();
   
GLFW.glfwSetFramebufferSizeCallback(window, (win, width, height) -> {
    GL33.glViewport(0, 0, width, height); 
});


int[] framebufferWidth = new int[1];
int[] framebufferHeight = new int[1];
GLFW.glfwGetFramebufferSize(window, framebufferWidth, framebufferHeight);
GL33.glViewport(0, 0, framebufferWidth[0], framebufferHeight[0]);


         
        for (int i = 0; i < 4; i++) {
            buildCircle(0.2f, 36);
            createCircleVAO(); 
        }


        shaderProgram = new ShaderProgramMain("C:\\Users\\enise\\Documents\\NetBeansProjects\\LatestVersion\\src\\myProject\\vertex.glsl", "C:\\Users\\enise\\Documents\\NetBeansProjects\\LatestVersion\\src\\myProject\\fragment.glsl");
        shaderProgram.addUniform("uMove");
        shaderProgram.addUniform("uColor");
        shaderProgram.addUniform("phase");

    }
    private void buildCircle(float radius, int vertexCount) {
        float angleStep = 360.0f / vertexCount;
        List<Float> tempVertices = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            float angle = (float) Math.toRadians(angleStep * i);
            float x = radius * (float) Math.cos(angle);
            float y = radius * (float) Math.sin(angle);
            tempVertices.add(x);
            tempVertices.add(y);
            tempVertices.add(0.0f);
            
           
        float s = (x / radius + 1.0f) / 2.0f;
        float t = (y / radius + 1.0f) / 2.0f;
        tempVertices.add(s);
        tempVertices.add(t);
        }
 
        for (int i = 1; i < vertexCount - 1; i++) {
            
            vertices.add(tempVertices.get(0));
            vertices.add(tempVertices.get(1));
            vertices.add(tempVertices.get(2));
            vertices.add(tempVertices.get(3)); //s
            vertices.add(tempVertices.get(4)); // t
        
        vertices.add(tempVertices.get(5 * i)); 
        vertices.add(tempVertices.get(5 * i + 1)); 
        vertices.add(tempVertices.get(5 * i + 2)); 
        vertices.add(tempVertices.get(5 * i + 3)); 
        vertices.add(tempVertices.get(5 * i + 4)); 

        vertices.add(tempVertices.get(5 * (i + 1))); 
        vertices.add(tempVertices.get(5 * (i + 1) + 1)); 
        vertices.add(tempVertices.get(5 * (i + 1) + 2)); 
        vertices.add(tempVertices.get(5 * (i + 1) + 3));
        vertices.add(tempVertices.get(5 * (i + 1) + 4)); 
        }
    }
   private void createCircleVAO() {
        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.size());
        for (Float vertex : vertices) {
            vertexBuffer.put(vertex);
        }
        vertexBuffer.flip();

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
       
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0); // Pozisyon
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES); // Texture koordinatları
        glEnableVertexAttribArray(1);


        MemoryUtil.memFree(vertexBuffer);

        vaos.add(vao);
        vbos.add(vbo);
    }




    private void loop() {
       
    TextureMain texture = new TextureMain("C:\\Users\\enise\\Documents\\NetBeansProjects\\LatestVersion\\src\\myProject\\ay.jpg");
        float[][] positions = {
        { -0.8f, 0.0f, 0.0f },  // Daire 1 pozisyonu
        {  -0.25f, 0.0f, 0.0f },  // Daire 2 pozisyonu
        { 0.25f, 0.0f, 0.0f }, // Daire 3 pozisyonu
        { 0.8f, 0.0f, 0.0f }  // Daire 4 pozisyonu
    };
        
       
        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            shaderProgram.bind();
      
        texture.bind();
     
       float[] phases = {0.0f, 1.0f, 2.0f, 3.0f}; // Azalan Hilal, İlkdördün, Dolunay, Artan Hilal


            for (int i = 0; i < 4; i++) {
                shaderProgram.setUniform("uMove", positions[i][0], positions[i][1], positions[i][2]);
                shaderProgram.setUniform("uColor", 0.5f, 0.5f, 0.5f, 1.0f); 
                shaderProgram.setUniform("phase", phases[i]);
                glBindVertexArray(vaos.get(i));
                glDrawArrays(GL_TRIANGLES, 0, vertices.size() / 5);
            }
            shaderProgram.unbind(); 
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
    }
}
