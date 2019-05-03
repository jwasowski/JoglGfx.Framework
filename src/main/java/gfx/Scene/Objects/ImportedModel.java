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
import gfx.DataModels.ModelMaterialLibrary;
import gfx.DataModels.ModelPart;
import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.DeallocationHelper.Deallocator;
import gfx.Utilities.MatrixService;
import gfx.Utilities.ModelImporter;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import graphicslib3D.Material;

public class ImportedModel {
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	protected final int[] indexBufferObject = new int[1];
	public float[] modelMatrixOne = new float[16];
	public float[] normalMatrixOne = new float[16];
	private ModelImporter modelImporter = new ModelImporter();
	private MatrixService matrixService = new MatrixService();
	private TextureLoader textureLoader = new TextureLoader();
	private DeallocationHelper deallocator = new DeallocationHelper();
	//TODO create constructor with model name param pointing at file
	//public Model model = new Model("WomanAnimTest2.obj");
	public Model model = new Model("krajobrazN.obj");
	public ModelMaterialLibrary materialLib = new ModelMaterialLibrary();
	
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
		modelImporter.loadMaterials(materialLib,model.materialLibName);
		matrixService.setupUnitMatrix(modelMatrixOne);
		matrixService.setupUnitMatrix(normalMatrixOne);
		String url = "SeaTexture.png";
		texture = textureLoader.LoadTexture(url);
		textureId = texture.getTextureObject();
		// Voa and Vbo Setups are in Model class
		int vtnArraySize = 0;
		for(ModelPart mp : model.modelParts) {
			vtnArraySize = vtnArraySize + mp.vertIndicesData.size();
			model.generateVertexTextureNormal(mp,gl4);
			deallocator.deallocate(model.vtnBuffer);
		}
		program.setMaterial(gl4, material, program.getProgramId());			
		gl4.glBindVertexArray(0);
		
		
	}

	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			//deallocator.deallocate(vertIndicesBuffer);
			//TODO Deallocate buffers in each ModelPart
			
		} else {
			System.err.println(
					"Java version: " + System.getProperty("java.version") + " is not supported by buffer deallocator.");
		}
		gl4.glBindVertexArray(0);
		gl4.glDeleteVertexArrays(1, vertexArrayObject, 0);
		//TODO Delete VAOs from ModelParts
		if (program.getProgramId() != 0) {
			program.disposeProgram(gl4);
		}
	}

	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		
		gl4.glUseProgram(program.getProgramId());
		matrixService.rotateAboutYAxis(modelMatrixOne, 0.5f);
		matrixService.rotateAboutYAxis(normalMatrixOne, 0.5f);
		program.setModelMatrix(gl4, modelMatrixOne, program.getProgramId());
		
		program.setNormalMatrix(gl4, normalMatrixOne, program.getProgramId());
		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);
	
		//program.setTextureUnit(gl4, 0);
		gl4.glActiveTexture(GL4.GL_TEXTURE0);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, textureId);
		
		for(int i=0;i<model.modelParts.size();i++) {
			gl4.glBindVertexArray(model.modelParts.get(i).vertexArrayObject[0]);
			program.setMaterial(gl4, materialLib.findMaterial(model.modelParts.get(i).materialName), program.getProgramId());	
			gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, model.modelParts.get(i).vtnList.size());
			gl4.glBindVertexArray(0);
		}
		
		
		
		
		gl4.glDisable(GL4.GL_CULL_FACE);
		
		gl4.glUseProgram(0);

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}
}
