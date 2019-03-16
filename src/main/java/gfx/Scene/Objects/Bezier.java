package gfx.Scene.Objects;

import java.nio.Buffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;

import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.Shaders.ShaderProgramBezier;

public class Bezier {
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	protected final int[] indexBufferObject = new int[1];
	public float[] modelMatrix = new float[16];
	int maxPoints = 64;
	public int numPoints = 0;
	private ShaderProgramBezier program;
	private Buffer fB = GLBuffers.newDirectFloatBuffer(2);
	public short inputFlag;
	private float xCoord, yCoord;

	public ShaderProgramBezier getProgram() {
		return program;
	}

	public void setProgram(ShaderProgramBezier program) {
		this.program = program;
	}

	public void setXYCoords(float x, float y) {
		this.xCoord = x;
		this.yCoord = y;
	}

	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, maxPoints * 2 * Buffers.SIZEOF_FLOAT, null, GL4.GL_DYNAMIC_DRAW);
		gl4.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, 0, 0);
		gl4.glEnableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl4.glBindVertexArray(0);
	}

	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		DeallocationHelper deallocator = new DeallocationHelper();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			deallocator.deallocate(fB);
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
		gl4.glUseProgram(program.getProgramId());
		gl4.glBindVertexArray(vertexArrayObject[0]);
		gl4.glDrawArrays(GL4.GL_LINE_STRIP, 0, numPoints);
		gl4.glDrawArrays(GL4.GL_POINTS, 0, numPoints);

		if (numPoints >= 4) {
			gl4.glUseProgram(program.getProgramId());
			gl4.glDrawArrays(GL4.GL_LINE_STRIP_ADJACENCY, 0, numPoints);
		}
		if (inputFlag == 1) {
			if (addPoint(gl4, xCoord, yCoord)) {
				System.out.println("Bezier display second check - fired");
			}
		}
		gl4.glBindVertexArray(0);
		gl4.glUseProgram(0);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}

	// TODO If mouse event fired, call addPoint in Display method of Bezier
	public boolean addPoint(GL4 gl4, float x, float y) {
		System.out.println("Addpoint - numPoints: " + numPoints);
		if (numPoints == maxPoints) {
			return false;
		}
		fB.clear();
		float[] arr = new float[2];
		arr[0] = x;
		arr[1] = y;
		fB = GLBuffers.newDirectFloatBuffer(arr);
		gl4.glBindVertexArray(vertexArrayObject[0]);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferSubData(GL4.GL_ARRAY_BUFFER, (numPoints++) * 2 * Buffers.SIZEOF_FLOAT, 2 * Buffers.SIZEOF_FLOAT,
				fB);
		gl4.glBindVertexArray(0);
		return true;
	}
}
