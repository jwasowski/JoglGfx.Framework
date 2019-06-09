package gfx.Scene.Objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;

import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramTransparent;

public class TransparentCone implements GfxObjectInterface {
	public float[] modelMatrix = new float[16];
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	protected final int[] indexBufferObject = new int[1];
	private MatrixService matrixService = new MatrixService();
	private DeallocationHelper deallocator = new DeallocationHelper();
	protected ShaderProgramTransparent program;
	public float[] material;
	private FloatBuffer vertexBuffer;
	private IntBuffer indicesBuffer;
	/** Cone parameters. */
	private float height = 18.0f, radius = 1f;
	/** Cone circular base precision value. */
	private int n = 32;
	
	public void setProgram(ShaderProgramTransparent program) {
		this.program = program;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		matrixService.setupUnitMatrix(modelMatrix);
		matrixService.translate(modelMatrix, -5.35f, 4.8f, -3.8f);
		matrixService.rotateAboutXAxis(modelMatrix, 90f);
		
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);
		// Cone Data Setup
		List<Float> coneData = new ArrayList<>();
		coneData.add(0.0f);
		coneData.add(0f);
		coneData.add(0.0f);
		coneData.add(1.0f);
		for (int i = 0; i <= n; i++) {
			float phi = (float) ((2 * Math.PI) * i / n);
			coneData.add((float) (radius * Math.sin(phi)));
			coneData.add(height);
			coneData.add((float) (radius * Math.cos(phi)));
			coneData.add(1.0f);
		}
		
		float[] coneRawData = ArrayUtils.toPrimitive(coneData.toArray(new Float[0]), 0.0f);
		
		vertexBuffer = GLBuffers.newDirectFloatBuffer(coneRawData);
		coneData.clear();

		gl4.glEnableVertexAttribArray(0);
		final int stride = 4 * Buffers.SIZEOF_FLOAT;
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.limit() * Buffers.SIZEOF_FLOAT, vertexBuffer,
				GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glEnableVertexAttribArray(0);

		// Indices buffer Setup
		List<Integer> indicesData = new ArrayList<>();
		indicesData.add(0);
		for (int i = 1; i < n; i++) {
			indicesData.add(i);
		}
		indicesData.add(1);
		int[] indicesRawData = ArrayUtils.toPrimitive(indicesData.toArray(new Integer[0]), 0);
		indicesData.clear();
		gl4.glGenBuffers(indexBufferObject.length, indexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
		indicesBuffer = GLBuffers.newDirectIntBuffer(indicesRawData);

		gl4.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.limit() * Buffers.SIZEOF_INT, indicesBuffer,
				GL4.GL_STATIC_DRAW);
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
			deallocator.deallocate(vertexBuffer);
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
		matrixService.rotateAboutZAxis(modelMatrix, 1);
		program.setModelMatrix(gl4, modelMatrix, program.getProgramId());
		program.setColor(gl4, material, program.getProgramId());

		
		gl4.glCullFace(GL4.GL_BACK);
		gl4.glFrontFace(GL4.GL_CCW);

		gl4.glEnable(GL4.GL_BLEND);
		gl4.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);
		gl4.glDepthMask(false);

		gl4.glDrawElements(GL4.GL_TRIANGLE_FAN, n+2, GL4.GL_UNSIGNED_INT, 0);
		gl4.glDisable(GL4.GL_CULL_FACE);
		gl4.glDisable(GL4.GL_BLEND);
		gl4.glDepthMask(true);

		gl4.glBindVertexArray(0);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateXAxisUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateXAxisDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateYAxisLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateYAxisRight() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveForward() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveBackwards() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub

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
