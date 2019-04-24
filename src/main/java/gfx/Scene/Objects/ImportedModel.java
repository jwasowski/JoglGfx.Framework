package gfx.Scene.Objects;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;

import gfx.DataModels.ModelPart;
import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.ModelImporter;
import gfx.Utilities.Shaders.ShaderProgramBezierPatch;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import graphicslib3D.Material;

public class ImportedModel {
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	public float[] modelMatrixOne = new float[16];
	private ModelImporter modelImporter = new ModelImporter();
	private MatrixService matrixService = new MatrixService();
	private DoubleBuffer dbBuffer;
	
	private ShaderProgramImportModel program;
	public Texture texture;
	
	public Material material;
	public int textureUnit;
	
	public void setProgram(ShaderProgramImportModel program) {
		this.program = program;
	}

	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		matrixService.setupUnitMatrix(modelMatrixOne);
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		/*System.out.println("Points value: " + Arrays.toString(vertexData));
		System.out.println("Material values: " + Arrays.toString(material.getAmbient()) + ", "
				+ Arrays.toString(material.getDiffuse()) + "," + Arrays.toString(material.getSpecular()) + ", "
				+ Arrays.toString(material.getEmission()) + "," + material.getShininess());
		dbBuffer = GLBuffers.newDirectDoubleBuffer(vertexData);
		final long pointsOneBufferSizeInBytes = vertexData.length * Buffers.SIZEOF_FLOAT;
		// Stride defines how many bytes there are in one set of vertex position
		// and color. That's four floats per vertex position and four floats per
		// color.
		final int stride = 4 * Buffers.SIZEOF_FLOAT;
		
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, pointsOneBufferSizeInBytes, dbBuffer, GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glEnableVertexAttribArray(0);
		
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl4.glBindVertexArray(0);*/

		modelImporter.loadModel("krajobraz.obj", gl4);
		//modelParts.get(0).vertexData.forEach(doubleI -> System.out.println(Arrays.toString(doubleI)));
	}

	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		DeallocationHelper deallocator = new DeallocationHelper();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			//deallocator.deallocate(fbPointsOne);
		} else {
			System.err.println(
					"Java version: " + System.getProperty("java.version") + " is not supported by buffer deallocator.");
		}
		gl4.glBindVertexArray(0);
		gl4.glDeleteVertexArrays(1, vertexArrayObject, 0);
		//TODO Fix NPE when dispose is chaining from different objects
		if (program.getProgramId() != 0) {
			program.disposeProgram(gl4);
		}
	}

	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		
/*
		gl4.glBindVertexArray(vertexArrayObject[0]);
		gl4.glUseProgram(program.getProgramId());
		program.setModelMatrix(gl4, modelMatrixOne, program.getProgramId());
		program.setMaterial(gl4, material, program.getProgramId());
		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);
		
		program.setLambda(gl4, 1, program.getProgramId());
		gl4.glFrontFace(GL4.GL_CW);
		gl4.glPatchParameteri(GL4.GL_PATCH_VERTICES, 16);
		gl4.glDrawArrays(GL4.GL_PATCHES, 0, 16);

		program.setLambda(gl4, -1, program.getProgramId());
		gl4.glFrontFace(GL4.GL_CCW);
		gl4.glPatchParameteri(GL4.GL_PATCH_VERTICES, 16);
		gl4.glDrawArrays(GL4.GL_PATCHES, 0, 16);
		
		gl4.glDisable(GL4.GL_CULL_FACE);
		gl4.glBindVertexArray(0);
		gl4.glUseProgram(0);*/
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}
}
