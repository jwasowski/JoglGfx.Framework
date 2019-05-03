package gfx.Utilities;

import java.io.File;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class TextureLoader {

	/*public void Init(GLAutoDrawable drawable){
		final GL4 gl4 = drawable.getGL().getGL4();
		IntBuffer intBuffer = GLBuffers.newDirectIntBuffer(1);
		gl4.glGenTextures(1, intBuffer);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, intBuffer.get(0));
		
		
		// parametry interpolacji tekstury
	    gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
	    gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);

	    // parametry ekstrapolacji tekstury
	    gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
	    gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);

	    gl4.glBindTexture(GL4.GL_TEXTURE_2D, 0);
	}*/
	
	public Texture LoadTexture(String textureFileName){
		Texture tex = null;
		try {
			File file = Paths.get(this.getClass().getResource("/textures/"+textureFileName).toURI()).toFile();
			tex = TextureIO.newTexture(file, false);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return tex;
	}
	
}
