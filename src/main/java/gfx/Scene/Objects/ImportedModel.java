package gfx.Scene.Objects;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;

import gfx.DataModels.Model;
import gfx.DataModels.ModelMaterialLibrary;
import gfx.DataModels.ModelPart;
import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.ModelImporter;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import graphicslib3D.Material;

public class ImportedModel implements GfxObjectInterface {

	public float[] modelMatrixOne = new float[16];
	public float[] normalMatrixOne = new float[16];
	public float[] viewMatrix;
	private MatrixService matrixService = new MatrixService();
	private TextureLoader textureLoader = new TextureLoader();
	private DeallocationHelper deallocator = new DeallocationHelper();
	protected ShaderProgramImportModel program;
	public List<Texture> textureData = new ArrayList<>();
	public Material material;
	ModelImporter modelImporter = new ModelImporter();
	// TODO create constructor with model name param pointing at file
	public Model model = new Model("krajobraz.obj");
	public ModelMaterialLibrary materialLib = new ModelMaterialLibrary();

	public int seaTextureId, lighthouseTextureId, terrainTextureId;

	public void setProgram(ShaderProgramImportModel program) {
		this.program = program;
	}

	public ImportedModel() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gfx.Scene.Objects.GfxObjectInterface#init(com.jogamp.opengl.GLAutoDrawable)
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		modelImporter.loadModel(model, gl4);
		modelImporter.loadMaterials(materialLib, model.materialLibName);
		
		// TODO Texture assignment and loading should be model dependent, mtlLib needs
		// to be edited manually each time
		/*
		 * for(ModelPart modelPart : model.modelParts) {
		 * modelImporter.loadTextures(textureLoader, modelPart, materialLib); }
		 */
		textureData.add(textureLoader.LoadTexture("SeaTexture.png"));
		seaTextureId = textureData.get(0).getTextureObject();
		textureData.add(textureLoader.LoadTexture("TerrainTexture.png"));
		terrainTextureId = textureData.get(1).getTextureObject();
		textureData.add(textureLoader.LoadTexture("LighthouseTexture.png"));
		lighthouseTextureId = textureData.get(2).getTextureObject();
		model.modelParts.get(0).textureId = seaTextureId;
		model.modelParts.get(1).textureId = terrainTextureId;
		model.modelParts.get(2).textureId = lighthouseTextureId;
		model.modelParts.get(3).textureId = lighthouseTextureId;
		model.modelParts.get(4).textureId = lighthouseTextureId;
		model.modelParts.get(5).textureId = lighthouseTextureId;
		System.out.println("SeaText ID: " + seaTextureId + " TerrainText ID: " + terrainTextureId
				+ " LighthouseText ID: " + lighthouseTextureId);
		// TODO Check and Load textures from MaterialsLib
		matrixService.setupUnitMatrix(modelMatrixOne);
		matrixService.setupUnitMatrix(normalMatrixOne);

		// Voa and Vbo Setups are in Model class
		int vtnArraySize = 0;
		for (ModelPart mp : model.modelParts) {
			vtnArraySize = vtnArraySize + mp.vertIndicesData.size();
			model.generateVertexTextureNormal(mp, gl4);
			if ("8".equals(System.getProperty("java.version").substring(2, 3))
					|| "9".equals(System.getProperty("java.version"))) {
				deallocator.deallocate(model.vtnBuffer);
			}
		}
		program.setMaterial(gl4, material, program.getProgramId());
		gl4.glBindVertexArray(0);
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gfx.Scene.Objects.GfxObjectInterface#dispose(com.jogamp.opengl.
	 * GLAutoDrawable)
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			// deallocator.deallocate(vertIndicesBuffer);

		} else {
			System.err.println(
					"Java version: " + System.getProperty("java.version") + " is not supported by buffer deallocator.");
		}

		for (ModelPart modelpart : model.modelParts) {
			gl4.glBindVertexArray(0);
			gl4.glDeleteVertexArrays(1, modelpart.vertexArrayObject, 0);
		}
		if (program.getProgramId() != 0) {
			program.disposeProgram(gl4);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gfx.Scene.Objects.GfxObjectInterface#display(com.jogamp.opengl.
	 * GLAutoDrawable)
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();

		gl4.glUseProgram(program.getProgramId());
		program.setModelMatrix(gl4, modelMatrixOne, program.getProgramId());
		program.setNormalMatrix(gl4, normalMatrixOne, program.getProgramId());
		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);

		for (int i = 0; i < model.modelParts.size(); i++) {
			gl4.glBindVertexArray(model.modelParts.get(i).vertexArrayObject[0]);
			program.setMaterial(gl4, materialLib.findMaterial(model.modelParts.get(i).materialName),
					program.getProgramId());
			gl4.glActiveTexture(GL4.GL_TEXTURE0);
			gl4.glBindTexture(GL4.GL_TEXTURE_2D, model.modelParts.get(i).textureId);

			gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, model.modelParts.get(i).vtnList.size());
			gl4.glBindVertexArray(0);
		}

		gl4.glDisable(GL4.GL_CULL_FACE);

		gl4.glUseProgram(0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gfx.Scene.Objects.GfxObjectInterface#reshape(com.jogamp.opengl.
	 * GLAutoDrawable, int, int, int, int)
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}

	@Override
	public void rotateXAxisUp() {
		matrixService.rotateAboutXAxis(modelMatrixOne, -0.5f);
		matrixService.rotateAboutXAxis(normalMatrixOne, -0.5f);
		// matrixService.rotateAboutXAxis(viewMatrix, -0.5f);
	}

	@Override
	public void rotateXAxisDown() {
		matrixService.rotateAboutXAxis(modelMatrixOne, 0.5f);
		matrixService.rotateAboutXAxis(normalMatrixOne, 0.5f);
		// matrixService.rotateAboutXAxis(viewMatrix, 0.5f);
	}

	@Override
	public void rotateYAxisLeft() {
		matrixService.rotateAboutYAxis(modelMatrixOne, 0.5f);
		matrixService.rotateAboutYAxis(normalMatrixOne, 0.5f);
		// matrixService.rotateAboutYAxis(viewMatrix, 0.5f);
	}

	@Override
	public void rotateYAxisRight() {
		matrixService.rotateAboutYAxis(modelMatrixOne, -0.5f);
		matrixService.rotateAboutYAxis(normalMatrixOne, -0.5f);
		// matrixService.rotateAboutYAxis(viewMatrix, -0.5f);
	}

	@Override
	public void moveForward() {
		matrixService.translate(modelMatrixOne, 0, 0, 0.5f);
		matrixService.translate(normalMatrixOne, 0, 0, 0.5f);
		// matrixService.translate(viewMatrix, 0, 0, 0.5f);
	}

	@Override
	public void moveBackwards() {
		matrixService.translate(modelMatrixOne, 0, 0, -0.5f);
		matrixService.translate(normalMatrixOne, 0, 0, -0.5f);
		// matrixService.translate(viewMatrix, 0, 0, -0.5f);
	}

	@Override
	public void moveLeft() {
		matrixService.translate(modelMatrixOne, 0.5f, 0, 0);
		matrixService.translate(normalMatrixOne, 0.5f, 0, 0);
		// matrixService.translate(viewMatrix, 0.5f, 0, 0);
	}

	@Override
	public void moveRight() {
		matrixService.translate(modelMatrixOne, -0.5f, 0, 0);
		matrixService.translate(normalMatrixOne, -0.5f, 0, 0);
		// matrixService.translate(viewMatrix, -0.5f, 0, 0);
	}

}