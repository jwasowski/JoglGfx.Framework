package gfx.Scene.Objects;

import java.nio.Buffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;

import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.Shaders.ShaderProgramCombined;

public class TexturedPlane {
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	protected final int[] indexBufferObject = new int[1];
	public float[] modelMatrix = new float[16];
	private float[] textureVertex = { -30.0f, 0.0f, -40.0f, 1.0f, -30.0f, -40.0f, 30.0f, 0.0f, -40.0f, 1.0f, 30.0f,
			-40.0f, 30.0f, 0.0f, 40.0f, 1.0f, 30.0f, 40.0f, -30.0f, 0.0f, 40.0f, 1.0f, -30.0f, 40.0f };
	private int[] indices = { 0, 1, 3, 2 };
	private ShaderProgramCombined program;
	private MatrixService matrixService = new MatrixService();
	private Buffer fbVertices;
	private Buffer ibIndices;

	public ShaderProgramCombined getProgram() {
		return program;
	}

	public void setProgram(ShaderProgramCombined program) {
		this.program = program;
	}

	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		matrixService.setupUnitMatrix(modelMatrix);
		program.setModelMatrix(gl4, modelMatrix, program.getProgramId());
		gl4.glEnable(GL4.GL_DEPTH_TEST);
		// TODO Init Data on GPU using GLBuffers
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		System.err.println("Error code: " + gl4.glGetError());
		gl4.glBindVertexArray(vertexArrayObject[0]);
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		fbVertices = GLBuffers.newDirectFloatBuffer(textureVertex);
		final long verticesBufferSizeInBytes = textureVertex.length * Buffers.SIZEOF_FLOAT;
		// Stride defines how many bytes there are in one set of vertex position
		// and color. That's four floats per vertex position and four floats per
		// color.
		final int stride = 6 * Buffers.SIZEOF_FLOAT;
		// Offset defines how many bytes to skip, counted from beginning of
		// the Stride.
		final long textureOffset = 4 * Buffers.SIZEOF_FLOAT;
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, verticesBufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glEnableVertexAttribArray(0);
		gl4.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false, stride, textureOffset);
		gl4.glEnableVertexAttribArray(1);
		// Indices buffer Setup
		gl4.glGenBuffers(indexBufferObject.length, indexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
		ibIndices = Buffers.newDirectIntBuffer(indices);
		final int indicesBufferSizeInBytes = indices.length * Buffers.SIZEOF_INT;
		gl4.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indicesBufferSizeInBytes, ibIndices, GL4.GL_STATIC_DRAW);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl4.glBindVertexArray(0);
	}

	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		DeallocationHelper deallocator = new DeallocationHelper();
		gl4.glDisableVertexAttribArray(2);
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			deallocator.deallocate(ibIndices);
			deallocator.deallocate(fbVertices);
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

	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		gl4.glUseProgram(program.getProgramId());
		gl4.glBindVertexArray(vertexArrayObject[0]);
		program.setModelMatrix(gl4, modelMatrix, program.getProgramId());
		gl4.glDrawElements(GL4.GL_TRIANGLES, indices.length, GL4.GL_UNSIGNED_INT, 0);
		gl4.glBindVertexArray(0);
		gl4.glUseProgram(0);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}
}
