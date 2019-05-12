package gfx.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class TextureLoader {

	/*
	 * public void Init(GLAutoDrawable drawable){ final GL4 gl4 =
	 * drawable.getGL().getGL4(); IntBuffer intBuffer =
	 * GLBuffers.newDirectIntBuffer(1); gl4.glGenTextures(1, intBuffer);
	 * gl4.glBindTexture(GL4.GL_TEXTURE_2D, intBuffer.get(0));
	 * 
	 * 
	 * // parametry interpolacji tekstury gl4.glTexParameteri(GL4.GL_TEXTURE_2D,
	 * GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
	 * gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER,
	 * GL4.GL_LINEAR);
	 * 
	 * // parametry ekstrapolacji tekstury gl4.glTexParameteri(GL4.GL_TEXTURE_2D,
	 * GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT); gl4.glTexParameteri(GL4.GL_TEXTURE_2D,
	 * GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
	 * 
	 * gl4.glBindTexture(GL4.GL_TEXTURE_2D, 0); }
	 */
	private static final String[] suffixes = { "posx", "negx", "posy", "negy", "posz", "negz" };
	private static final int[] targets = { GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_X, GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
			GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
			GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };

	public Texture loadCubeTexture(GL4 gl4, String textureFileName, boolean mipmapped) {
		Texture cubemap = TextureIO.newTexture(GL4.GL_TEXTURE_CUBE_MAP);
		
		try {
			for (int i = 0; i < suffixes.length; i++) {
				File file;
				file = Paths.get(this.getClass().getResource("/textures/" + textureFileName).toURI()).toFile();
				TextureData data = TextureIO.newTextureData(gl4.getGLProfile(), file, mipmapped,
						IOUtil.getFileSuffix(file));
				
				if (data == null) {
					throw new IOException("Unable to load texture " + textureFileName);
				}
				//data.setMustFlipVertically(true);
				/*cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
				cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
				cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
				cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);
				cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_R, GL4.GL_CLAMP_TO_EDGE);*/
				cubemap.updateImage(gl4, data, targets[i]);
				//cubemap.setMustFlipVertically(true);

			}
			cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
			cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
			cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
			cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);
			cubemap.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_R, GL4.GL_CLAMP_TO_EDGE);
			return cubemap;
		} catch (GLException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cubemap;

	}

	public Texture LoadTexture(String textureFileName) {
		Texture tex = null;
		try {
			File file = Paths.get(this.getClass().getResource("/textures/" + textureFileName).toURI()).toFile();
			tex = TextureIO.newTexture(file, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tex;
	}

}
