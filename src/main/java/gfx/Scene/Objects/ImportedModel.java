package gfx.Scene.Objects;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;

import gfx.DataModels.Model;
import gfx.DataModels.ModelPart;
import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.ModelImporter;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import graphicslib3D.Material;

public class ImportedModel {
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[3];
	protected final int[] indexBufferObject = new int[1];
	public float[] modelMatrixOne = new float[16];
	public float[] normalMatrixOne = new float[16];
	private ModelImporter modelImporter = new ModelImporter();
	private MatrixService matrixService = new MatrixService();
	private TextureLoader textureLoader = new TextureLoader();
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private FloatBuffer normalBuffer;
	private IntBuffer vertIndicesBuffer;
	
	//TODO create constructor with model name param pointing at file
	public Model model = new Model("krajobrazN.obj");
	public int modelNumber = 1;
	
	private ShaderProgramImportModel program;
	public Texture texture;
	public int textureId;
	
	public Material material;
	public int textureUnit;
	
	public void setProgram(ShaderProgramImportModel program) {
		this.program = program;
	}

	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		modelImporter.loadModel(model, gl4);
		matrixService.setupUnitMatrix(modelMatrixOne);
		matrixService.setupUnitMatrix(normalMatrixOne);
		String url = "SeaTexture.png";
		texture = textureLoader.LoadTexture(url);
		textureId = texture.getTextureObject();
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		//TODO Join data into single buffer
		
		for(ModelPart mp : model.modelParts) {
			
			model.generateVertexTextureNormal(mp);
		}
		//TODO create float array from one VTN object, wrap it in buffer and send to GPU
		
		gl4.glEnableVertexAttribArray(0);
		gl4.glEnableVertexAttribArray(2);
		gl4.glEnableVertexAttribArray(3);
		
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		
		/*float[] vertexData = ArrayUtils.toPrimitive(model.verticesData.toArray(new Float[0]), 0.0F);
		float[] textureData = ArrayUtils.toPrimitive(model.texturesData.toArray(new Float[0]), 0.0F);
		float[] normalData = ArrayUtils.toPrimitive(model.normalsData.toArray(new Float[0]), 0.0F);*/
		
		
		/*vertexBuffer = GLBuffers.newDirectFloatBuffer(vertexData);
		textureBuffer = GLBuffers.newDirectFloatBuffer(textureData);
		normalBuffer = GLBuffers.newDirectFloatBuffer(normalData);
		//TODO Setup strides and offsets for data

		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, vertexBuffer.limit()*Buffers.SIZEOF_FLOAT, vertexBuffer, GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, 0, 0);
		gl4.glEnableVertexAttribArray(0);
		
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[1]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, textureBuffer.limit()*Buffers.SIZEOF_FLOAT, textureBuffer, GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false, 0, 0);
		gl4.glEnableVertexAttribArray(0);
		
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[2]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, normalBuffer.limit()*Buffers.SIZEOF_FLOAT, normalBuffer, GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(3, 3, GL4.GL_FLOAT, false, 0, 0);
		gl4.glEnableVertexAttribArray(0);*/
		
		// Indices buffer Setup
		int[] vertIndicesData = ArrayUtils.toPrimitive(model.modelParts.get(modelNumber).vertIndicesData.toArray(new Integer[0]), 0);
		vertIndicesBuffer = GLBuffers.newDirectIntBuffer(vertIndicesData);
		gl4.glGenBuffers(indexBufferObject.length, indexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, vertIndicesBuffer.limit()*Buffers.SIZEOF_INT, vertIndicesBuffer, GL4.GL_STATIC_DRAW);
		
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
			deallocator.deallocate(vertexBuffer);
			deallocator.deallocate(textureBuffer);
			deallocator.deallocate(normalBuffer);
			deallocator.deallocate(vertIndicesBuffer);
			
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
		gl4.glBindVertexArray(vertexArrayObject[0]);
		gl4.glUseProgram(program.getProgramId());
		matrixService.rotateAboutYAxis(modelMatrixOne, 0.5f);
		matrixService.rotateAboutYAxis(normalMatrixOne, 0.5f);
		program.setModelMatrix(gl4, modelMatrixOne, program.getProgramId());
		program.setMaterial(gl4, material, program.getProgramId());
		program.setNormalMatrix(gl4, normalMatrixOne, program.getProgramId());
		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);
	
		//program.setTextureUnit(gl4, 0);
		gl4.glActiveTexture(GL4.GL_TEXTURE0);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, textureId);
		
		
		//gl4.glDrawElements(GL4.GL_TRIANGLES, model.modelParts.get(modelNumber).vertIndicesData.size(), GL4.GL_UNSIGNED_INT, 0);
		
		gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, model.modelParts.get(modelNumber).facesCounter);
		
		gl4.glDisable(GL4.GL_CULL_FACE);
		gl4.glBindVertexArray(0);
		gl4.glUseProgram(0);

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}
}
