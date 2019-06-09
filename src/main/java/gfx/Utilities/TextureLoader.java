package gfx.Utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

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

	
	private static final String[] suffixes = { "posx", "negx", "posy", "negy", "posz", "negz" };
	private static final int[] targets = { GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_X, GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
			GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, GL4.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
			GL4.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };

	public Texture loadCubeTexture(GL4 gl4, List<String> textureFileNames, boolean mipmapped) {
		Texture cubemap = TextureIO.newTexture(GL4.GL_TEXTURE_CUBE_MAP);
		
		try {
			for (int i = 0; i < suffixes.length; i++) {
				InputStream iS = accessFile("/Textures/" + textureFileNames.get(i));
				TextureData data = TextureIO.newTextureData(gl4.getGLProfile(), iS, mipmapped,
						 null);
				
				if (data == null) {
					throw new IOException("Unable to load texture " + textureFileNames.get(i));
				}
				iS.close();
				cubemap.updateImage(gl4, data, targets[i]);
				

			}
			
			return cubemap;
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cubemap;

	}
	//TODO Fix tga format reading
	public Texture loadTexture(String textureFileName) {
		Texture tex = null;
		try {
			InputStream iS = accessFile("/Textures/" + textureFileName);
			tex = TextureIO.newTexture(iS, false, null);
			iS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tex;
	}
	/** https://stackoverflow.com/questions/20389255/reading-a-resource-file-from-within-jar*/
	public static InputStream accessFile(String path) {
        String resource = path;

        // this is the path within the jar file
        InputStream input = ModelImporter.class.getResourceAsStream(resource);
        if (input == null) {
            // this is how we load file within editor (eg eclipse)
            input = ModelImporter.class.getClassLoader().getResourceAsStream(resource);
        }

        return input;
    }

}
