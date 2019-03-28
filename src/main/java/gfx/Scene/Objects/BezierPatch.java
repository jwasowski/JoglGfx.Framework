package gfx.Scene.Objects;

import java.nio.Buffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;

import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.Shaders.ShaderProgramBezier;
import gfx.Utilities.Shaders.ShaderProgramBezierPatch;
import graphicslib3D.Material;

public class BezierPatch {
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	protected final int[] indexBufferObject = new int[1];
	public float[] modelMatrix = new float[16];
	
	private ShaderProgramBezierPatch program;
	private Buffer fbVertices;
	private Buffer ibIndices;
	public float[] points;
	
	public void setProgram(ShaderProgramBezierPatch program) {
		this.program = program;
	}
	
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);
		// Vbo vertices Setup
		fbVertices = GLBuffers.newDirectFloatBuffer(points);
		final long verticesBufferSizeInBytes = points.length * Buffers.SIZEOF_FLOAT;
		// Stride defines how many bytes there are in one set of vertex position
		// and color. That's four floats per vertex position and four floats per
		// color.
		final int stride = 4 * Buffers.SIZEOF_FLOAT;
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, verticesBufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glEnableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl4.glBindVertexArray(0);
		
		/*glGenVertexArrays(1,&vao_);
	    glBindVertexArray(vao_);

	    glGenBuffers(1, &vertex_buffer_);
	    glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer_);
	    glBufferData(GL_ARRAY_BUFFER, 64*sizeof(GLfloat), points, GL_STATIC_DRAW);
	    glVertexAttribPointer(0, 4, GL_FLOAT, GL_FALSE, 0, (GLvoid*)0);
	    glEnableVertexAttribArray(0);

	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);*/
	}

	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		DeallocationHelper deallocator = new DeallocationHelper();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			//deallocator.deallocate(fB);
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
		gl4.glDrawElements(GL4.GL_TRIANGLES, 10/*indices.length*/, GL4.GL_UNSIGNED_INT, 0);
		gl4.glBindVertexArray(0);
		gl4.glUseProgram(0);
		
		/*glBindVertexArray(vao_);

    glUseProgram(program);
    program.SetModelMatrix(model_matrix_);
    program.SetMaterial(material_);
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);


    program.SetLambda(1);
    glFrontFace(GL_CW);

    glPatchParameteri(GL_PATCH_VERTICES, 16);
    glDrawArrays(GL_PATCHES, 0, 16);

    program.SetLambda(-1);
    glFrontFace(GL_CCW);

    glPatchParameteri(GL_PATCH_VERTICES, 16);
    glDrawArrays(GL_PATCHES, 0, 16);


    glDisable(GL_CULL_FACE);
    glBindVertexArray(0);
    glUseProgram(0);*/
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}
}
