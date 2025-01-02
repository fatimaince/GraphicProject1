
package myProject;
import org.lwjgl.stb.STBImage;
import org.lwjgl.opengl.GL33;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TextureMain {
    private final int textureId;

    public TextureMain(String filePath) {
        textureId = GL33.glGenTextures();
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);

       
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, GL33.GL_REPEAT);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_LINEAR);
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_LINEAR);

       
        IntBuffer width = org.lwjgl.system.MemoryStack.stackMallocInt(1);
        IntBuffer height = org.lwjgl.system.MemoryStack.stackMallocInt(1);
        IntBuffer channels = org.lwjgl.system.MemoryStack.stackMallocInt(1);

        ByteBuffer image = STBImage.stbi_load(filePath, width, height, channels, 4);

        if (image != null) {
            GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, width.get(), height.get(), 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, image);
            GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);
        } else {
            throw new RuntimeException("Failed to load texture: " + filePath);
        }

        STBImage.stbi_image_free(image); 
    }

    public void bind() {
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
    }

    public void delete() {
        GL33.glDeleteTextures(textureId);
    }
}
