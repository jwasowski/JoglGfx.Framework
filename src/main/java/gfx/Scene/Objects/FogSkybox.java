package gfx.Scene.Objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;

import gfx.DataModels.Model;
import gfx.DataModels.ModelMaterialLibrary;
import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.ModelImporter;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramFogSkybox;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import gfx.Utilities.Shaders.ShaderProgramSkybox;
import graphicslib3D.Material;

public class FogSkybox implements GfxObjectInterface {
	//public float[] modelMatrixOne = new float[16];
	//public float[] normalMatrixOne = new float[16];
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	protected final int[] indexBufferObject = new int[1];
	public float[] viewMatrix;
	private MatrixService matrixService = new MatrixService();
	private TextureLoader textureLoader = new TextureLoader();
	private DeallocationHelper deallocator = new DeallocationHelper();
	protected ShaderProgramFogSkybox program;
	public List<Texture> textureData = new ArrayList<>();
	private FloatBuffer textureVertexBuffer;
	private IntBuffer indicesBuffer;

	public int skyTextureId;

	public void setProgram(ShaderProgramFogSkybox program) {
		this.program = program;
	}

	// TODO Write method for automatic TexCoord Y param value reversing
	float[] textureVertexData = { -50.0f, 50.0f, 50.0f, 1.0f, -1.0f, -1.0f, 1.0f, 50.0f, 50.0f, 50.0f, 1.0f, 1.0f,
			-1.0f, 1.0f, 50.0f, -50.0f, 50.0f, 1.0f, 1.0f, 1.0f, 1.0f, -50.0f, -50.0f, 50.0f, 1.0f, -1.0f, 1.0f, 1.0f,

			50.0f, 50.0f, -50.0f, 1.0f, 1.0f, -1.0f, -1.0f, -50.0f, 50.0f, -50.0f, 1.0f, -1.0f, -1.0f, -1.0f, -50.0f,
			-50.0f, -50.0f, 1.0f, -1.0f, 1.0f, -1.0f, 50.0f, -50.0f, -50.0f, 1.0f, 1.0f, 1.0f, -1.0f };
	/*
	 * { -50.0f, 50.0f, 50.0f, 1.0f, -1.0f, 1.0f, 1.0f, 50.0f, 50.0f, 50.0f, 1.0f,
	 * 1.0f, 1.0f, 1.0f, 50.0f, -50.0f, 50.0f, 1.0f, 1.0f, -1.0f, 1.0f, -50.0f,
	 * -50.0f, 50.0f, 1.0f, -1.0f, -1.0f, 1.0f,
	 * 
	 * 50.0f, 50.0f, -50.0f, 1.0f, 1.0f, 1.0f, -1.0f, -50.0f, 50.0f, -50.0f, 1.0f,
	 * -1.0f, 1.0f, -1.0f, -50.0f, -50.0f, -50.0f, 1.0f, -1.0f, -1.0f, -1.0f, 50.0f,
	 * -50.0f, -50.0f, 1.0f, 1.0f, -1.0f, -1.0f };
	 */
	int[] indices = { 0, 1, 3, 1, 2, 3, 4, 5, 7, 5, 6, 7, 5, 4, 0, 4, 1, 0, 7, 6, 2, 6, 3, 2, 5, 0, 6, 0, 3, 6, 1, 4, 2,
			4, 7, 2 };

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		System.out.println("Skybox Initialization");
		textureData.add(textureLoader.loadCubeTexture(gl4,
				Arrays.asList("Skybox/starrynightlf.png", "Skybox/starrynightrt.png", "Skybox/starrynightdn.png",
						"Skybox/starrynightup.png", "Skybox/starrynightft.png", "Skybox/starrynightbk.png"),
				false));
		skyTextureId = textureData.get(0).getTextureObject();
		System.out.println("Skybox TextureId: " + skyTextureId);
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);

		textureVertexBuffer = GLBuffers.newDirectFloatBuffer(textureVertexData);

		gl4.glEnableVertexAttribArray(0);
		gl4.glEnableVertexAttribArray(2);
		// Stride defines how many bytes there are in one set of vertex position
		// and color. That's four floats per vertex position and four floats per
		// color.
		final int stride = 7 * Buffers.SIZEOF_FLOAT;
		final long textureOffset = 4 * Buffers.SIZEOF_FLOAT;
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, textureVertexBuffer.limit() * Buffers.SIZEOF_FLOAT, textureVertexBuffer,
				GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glVertexAttribPointer(2, 3, GL4.GL_FLOAT, false, stride, textureOffset);
		gl4.glEnableVertexAttribArray(0);

		// Indices buffer Setup
		gl4.glGenBuffers(indexBufferObject.length, indexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
		indicesBuffer = GLBuffers.newDirectIntBuffer(indices);

		gl4.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.limit() * Buffers.SIZEOF_INT, indicesBuffer,
				GL4.GL_STATIC_DRAW);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl4.glBindVertexArray(0);
		// TODO Load cube texture with special parameters

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		DeallocationHelper deallocator = new DeallocationHelper();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			deallocator.deallocate(textureVertexBuffer);
			deallocator.deallocate(indicesBuffer);
		} else {
			System.err.println(
					"Java version: " + System.getProperty("java.version") + " is not supported by buffer deallocator.");
		}
		gl4.glBindVertexArray(0);
		gl4.glDeleteVertexArrays(1, vertexArrayObject, 0);

		if (program.getProgramId() != 0) {
			program.disposeProgram(gl4);

		}

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		
		gl4.glBindVertexArray(vertexArrayObject[0]);

		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);
		gl4.glFrontFace(GL4.GL_CCW);

		gl4.glActiveTexture(GL4.GL_TEXTURE1);
		gl4.glBindTexture(GL4.GL_TEXTURE_CUBE_MAP, skyTextureId);

		gl4.glDepthMask(false);
		gl4.glDrawElements(GL4.GL_TRIANGLES, indices.length, GL4.GL_UNSIGNED_INT, 0);
		gl4.glDepthMask(true);

		gl4.glDisable(GL4.GL_CULL_FACE);

		gl4.glBindTexture(GL4.GL_TEXTURE_CUBE_MAP, 0);

		gl4.glBindVertexArray(0);
		

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);

	}

	@Override
	public void rotateXAxisUp() {

	}

	@Override
	public void rotateXAxisDown() {

	}

	@Override
	public void rotateYAxisLeft() {

	}

	@Override
	public void rotateYAxisRight() {

	}

	@Override
	public void moveForward() {

	}

	@Override
	public void moveBackwards() {

	}

	@Override
	public void moveLeft() {

	}

	@Override
	public void moveRight() {

	}

	@Override
	public void incAltitude() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decAltitude() {
		// TODO Auto-generated method stub
		
	}
}
