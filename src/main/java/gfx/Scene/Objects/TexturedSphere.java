package gfx.Scene.Objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;

import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import graphicslib3D.Material;

public class TexturedSphere implements GfxObjectInterface {
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	public float[] modelMatrix = new float[16];
	public float[] viewMatrix;
	public float[] normalMatrix = new float[9];
	private ShaderProgramImportModel program;
	protected FloatBuffer fbVertices;
	protected IntBuffer ibIndices;
	private DeallocationHelper deallocator;
	protected final int[] indexBufferObject = new int[1];
	private MatrixService matrixService = new MatrixService();
	private TextureLoader textureLoader = new TextureLoader();
	public Material material;
	private Texture texture;
	private String textureName;
	private float[] textureNormalVertex, initialPosition, scale;
	private int[] indices;
	private int m, n, textureId;
	private float r, R;
	private boolean rotation;

	public TexturedSphere(int m, int n, float r, float R, String textureName, float[] initialPosition, float[] scale, boolean rotation) {
		this.m = m;
		this.n = n;
		this.r = r;
		this.R = R;
		this.textureName = textureName;
		this.initialPosition = initialPosition;
		this.scale = scale;
		this.rotation = rotation;
		System.out.println("Constructor values" + m + " , " + n + " , " + r + " , " + R + " , " + textureName);
	}

	public void setProgram(ShaderProgramImportModel program) {
		this.program = program;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		int m_ = m, n_ = n;
		System.out.println("Init values" + m + " , " + n + " , " + r + " , " + R + " , " + textureName);
		textureNormalVertex = new float[((m+1)*(n+1))*9]; //651*9
		indices= new int[2*n*(m + 1)];
		texture = textureLoader.loadTexture(textureName);
		textureId = texture.getTextureObject();
		System.out.println("GL_RENDERER: " + gl4.glGetString(GL4.GL_RENDERER));
		System.out.println("GL_VERSION: " + gl4.glGetString(GL4.GL_VERSION));
		gl4.glEnable(GL4.GL_DEPTH_TEST);
		matrixService.setupUnitMatrix(modelMatrix);
		matrixService.setupUnitMatrix3x3(normalMatrix);
		matrixService.translate(modelMatrix, initialPosition[0], initialPosition[1], initialPosition[2]);
		matrixService.scale(modelMatrix, scale[0], scale[1], scale[2]);
		int counter = 0;
		for (int i = 0; i <= n_; i++) {
			float phi = (float) ((-Math.PI / 2) + (Math.PI * i) / (float) n);
			for (int j = 0; j <= m_; j++) {
				float theta = (float) (2 * Math.PI / m_ * j);
				textureNormalVertex[counter++] = (float) ((R * Math.cos(phi)) * Math.sin(theta));
				textureNormalVertex[counter++] = (float) (R * Math.sin(phi));
				textureNormalVertex[counter++] = (float) ((R * Math.cos(phi)) * Math.cos(theta));
				textureNormalVertex[counter++] = 1.0f;
				textureNormalVertex[counter++] = (float) j / (float) m_;
				textureNormalVertex[counter++] = (float) i / (float) n_;
				textureNormalVertex[counter++] = (float) (Math.cos(phi) * Math.sin(theta));
				textureNormalVertex[counter++] = (float) Math.sin(phi);
				textureNormalVertex[counter++] = (float) (Math.cos(phi) * Math.cos(theta));

			}
		}

		//System.out.println("TextureNormalVertex-init: " + Arrays.toString(textureNormalVertex));
		int k = 0;
		for (int i = 0; i <= n_ - 1; i++) {
			for (int j = 0; j <= m_; j++) {
				indices[2 * (i * (m_ + 1) + j)] = k;
				indices[2 * (i * (m_ + 1) + j) + 1] = k + m_ + 1;
				k++;
			}
		}
		//System.out.println("Indices-init: " + Arrays.toString(indices));
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		if (gl4.glGetError() != 0 || gl4.glGetError() != GL4.GL_NO_ERROR) {
			System.err.println("Error code in MarsScene-init-1: " + gl4.glGetError());
		}
		gl4.glBindVertexArray(vertexArrayObject[0]);
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		fbVertices = Buffers.newDirectFloatBuffer(textureNormalVertex);
		final long verticesBufferSizeInBytes = textureNormalVertex.length * Buffers.SIZEOF_FLOAT;
		// Stride defines how many bytes there are in one set of vertex position
		// and texture position. That's four floats per vertex position and two floats
		// per
		// texture position.
		final int stride = 9 * Buffers.SIZEOF_FLOAT;
		// Offset defines how many bytes to skip, counted from beginning of
		// the Stride.
		final long textureOffset = 4 * Buffers.SIZEOF_FLOAT;
		final long normalOffset = 6 * Buffers.SIZEOF_FLOAT;
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, verticesBufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glEnableVertexAttribArray(0);
		gl4.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false, stride, textureOffset);
		gl4.glEnableVertexAttribArray(2);
		gl4.glVertexAttribPointer(3, 3, GL4.GL_FLOAT, false, stride, normalOffset);
		gl4.glEnableVertexAttribArray(3);
		// Indices buffer Setup
		gl4.glGenBuffers(indexBufferObject.length, indexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
		ibIndices = Buffers.newDirectIntBuffer(indices);
		final int indicesBufferSizeInBytes = indices.length * Buffers.SIZEOF_INT;
		gl4.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indicesBufferSizeInBytes, ibIndices, GL4.GL_STATIC_DRAW);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl4.glBindVertexArray(0);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			deallocator.deallocate(fbVertices);
			deallocator.deallocate(ibIndices);
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
		if(rotation == true) {
			matrixService.rotateAboutYAxis(modelMatrix, 0.5f);
			matrixService.rotateAboutXAxis(modelMatrix, 0.2f);
			matrixService.rotateAboutYAxis3x3(normalMatrix, -0.5f);
			matrixService.rotateAboutXAxis3x3(normalMatrix, -0.2f);
		}
		program.setModelMatrix(gl4, modelMatrix, program.getProgramId());
		program.setNormalMatrix(gl4, normalMatrix, program.getProgramId());
		program.setMaterial(gl4, material, program.getProgramId());
		gl4.glActiveTexture(GL4.GL_TEXTURE0);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, textureId);
		
		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);
	    gl4.glFrontFace(GL4.GL_CW);
	    gl4.glDrawElements(GL4.GL_TRIANGLE_STRIP, indices.length, GL4.GL_UNSIGNED_INT, 0);
	    
	    gl4.glDisable(GL4.GL_CULL_FACE);
	    
		gl4.glBindVertexArray(0);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateXAxisUp() {
		//matrixService.rotateAboutXAxis(modelMatrix, -0.5f);
		//matrixService.rotateAboutXAxis3x3(normalMatrix, 0.5f);
		matrixService.rotateAboutXAxis(viewMatrix, -0.5f);
	}

	@Override
	public void rotateXAxisDown() {
		//matrixService.rotateAboutXAxis(modelMatrix, 0.5f);
		//matrixService.rotateAboutXAxis3x3(normalMatrix, 0.5f);
		matrixService.rotateAboutXAxis(viewMatrix, 0.5f);
	}

	@Override
	public void rotateYAxisLeft() {
		//matrixService.rotateAboutYAxis(modelMatrix, 0.5f);
		//matrixService.rotateAboutYAxis3x3(normalMatrix, 0.5f);
		matrixService.rotateAboutYAxis(viewMatrix, 0.5f);
	}

	@Override
	public void rotateYAxisRight() {
		//matrixService.rotateAboutYAxis(modelMatrix, -0.5f);
		//matrixService.rotateAboutYAxis3x3(normalMatrix, 0.5f);
		matrixService.rotateAboutYAxis(viewMatrix, -0.5f);
	}

	@Override
	public void moveForward() {
		//matrixService.translate(modelMatrix, 0, 0, 0.5f);
		//matrixService.translate(normalMatrixOne, 0, 0, 0.5f);
		matrixService.translate(viewMatrix, 0, 0, 0.5f);
	}

	@Override
	public void moveBackwards() {
		//matrixService.translate(modelMatrix, 0, 0, -0.5f);
		//matrixService.translate(normalMatrixOne, 0, 0, -0.5f);
		matrixService.translate(viewMatrix, 0, 0, -0.5f);
	}

	@Override
	public void moveLeft() {
		//matrixService.translate(modelMatrix, 0.5f, 0, 0);
		//matrixService.translate(normalMatrixOne, 0.5f, 0, 0);
		matrixService.translate(viewMatrix, 0.5f, 0, 0);
	}

	@Override
	public void moveRight() {
		//matrixService.translate(modelMatrix, -0.5f, 0, 0);
		//matrixService.translate(normalMatrixOne, -0.5f, 0, 0);
		matrixService.translate(viewMatrix, -0.5f, 0, 0);
	}

	@Override
	public void incAltitude() {
		matrixService.translate(viewMatrix, 0, -0.5f, 0);

	}

	@Override
	public void decAltitude() {
		matrixService.translate(viewMatrix, 0, 0.5f, 0);

	}
}
