package gfx.Utilities.Shaders;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

import graphicslib3D.Material;
import graphicslib3D.light.PositionalLight;

public class ShaderProgramImportModel {
	private ShaderProgram program;
	private ShaderCode vertexShader;
	private ShaderCode fragmentShader;
	public int textureUnitLocation;
	public int modelMatrixLocation;
	public int projectionMatrixLocation;
	public int viewMatrixLocation;
	public int lambdaLocation;
	public int normalMatrixLocation;
	private int materialEmissionLocation;
	private int materialAmbientLocation;
	private int materialDiffuseLocation;
	private int materialSpecularLocation;
	private int materialShininessLocation;
	public int[] materialLocations;
	private int lightAmbientLocation;
	private int lightDiffuseLocation;
	private int lightSpercularLocation;
	private int lightPositionLocation;
	private int lightAttenuationLocation;
	public int[] lightLocations;

	public int initProgram(GL4 gl4) {
		vertexShader = ShaderCode.create(gl4, GL4.GL_VERTEX_SHADER, this.getClass(), "Shaders/ImportModel", null,
				"PointLightvertex", null, null, true);

		fragmentShader = ShaderCode.create(gl4, GL4.GL_FRAGMENT_SHADER, this.getClass(), "Shaders/ImportModel", null,
				"PointLightfragment", null, null, true);
		vertexShader.defaultShaderCustomization(gl4, true, true);
		fragmentShader.defaultShaderCustomization(gl4, true, true);
		program = new ShaderProgram();
		program.add(gl4, vertexShader, System.err);
		program.add(gl4, fragmentShader, System.err);
		
		program.link(gl4, System.out);
		program.validateProgram(gl4, System.err);
		
		System.out.println("ImportModelProgram: " + program.id());
		getAllUniformLocations(gl4);

		return program.program();
	}
	
	public void useProgram(GL4 gl4, ShaderState state) {
		state.setVerbose(false);
		state.attachShaderProgram(gl4, program, true);
	}

	public int getUniformLocation(String name, GL4 gl4) {
		int location = -1;
		gl4.glUseProgram(program.program());
		location = gl4.glGetUniformLocation(program.program(), name);
		if (location < 0) {
			System.err.println("ERROR: Cannot find uniform location: " + name);
		}
		gl4.glUseProgram(0);
		return location;
	}
	
	public void getAllUniformLocations(GL4 gl4) {
		modelMatrixLocation = getUniformLocation("model_matrix", gl4);
		viewMatrixLocation = getUniformLocation("view_matrix", gl4);
		projectionMatrixLocation = getUniformLocation("projection_matrix", gl4);
		textureUnitLocation = getUniformLocation("texture_unit", gl4);
		//lambdaLocation = getUniformLocation("lambda", gl4);
		normalMatrixLocation = getUniformLocation("normal_matrix", gl4);
		materialEmissionLocation = getUniformLocation("material.emission", gl4);
		materialAmbientLocation = getUniformLocation("material.ambient", gl4);
		materialDiffuseLocation = getUniformLocation("material.diffuse", gl4);
		materialSpecularLocation = getUniformLocation("material.specular", gl4);
		materialShininessLocation = getUniformLocation("material.shininess", gl4);
		materialLocations = new int[] { materialAmbientLocation, materialDiffuseLocation, materialSpecularLocation,
				materialEmissionLocation, materialShininessLocation };
		lightAmbientLocation = getUniformLocation("light.ambient", gl4);
		lightDiffuseLocation = getUniformLocation("light.diffuse", gl4);
		lightSpercularLocation = getUniformLocation("light.specular", gl4);
		lightPositionLocation = getUniformLocation("light.position", gl4);
		lightAttenuationLocation = getUniformLocation("light.attenuation", gl4);
		lightLocations = new int[] { lightPositionLocation, lightAmbientLocation, lightDiffuseLocation,
				lightSpercularLocation, lightAttenuationLocation };
		System.out.println("ProjectionLoc: "+projectionMatrixLocation+" ViewLoc: "+viewMatrixLocation+" TextureLoc: "+textureUnitLocation);
	}

	public int getProgramId() {
		return program.id();
	}

	public void setTextureUnit(GL4 gl4, int t) {
		gl4.glUseProgram(program.program());
		if (gl4.glGetError() != 0 || gl4.glGetError() != GL4.GL_NO_ERROR) {
			System.err.println("Error code in setTextUnit-1: " + gl4.glGetError());
		}
		gl4.glUniform1i(textureUnitLocation, t);
		if (gl4.glGetError() != 0 || gl4.glGetError() != GL4.GL_NO_ERROR) {
			System.err.println("Error code in setTextUnit-2: " + gl4.glGetError());
		}
	}

	public void setModelMatrix(GL4 gl4, float[] matrix, int program) {
		gl4.glUseProgram(program);
		gl4.glUniformMatrix4fv(modelMatrixLocation, 1, false, matrix, 0);
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetModelMatrix2: " + gl4.glGetError());
		}
	}

	public void setProjectionMatrix(GL4 gl4, float[] matrix, int program) {
		gl4.glUseProgram(program);
		gl4.glUniformMatrix4fv(projectionMatrixLocation, 1, false, matrix, 0);
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetProjectionMatrix: " + gl4.glGetError());
		}
	}

	public void setViewMatrix(GL4 gl4, float[] matrix, int program) {
		gl4.glUseProgram(program);
		gl4.glUniformMatrix4fv(viewMatrixLocation, 1, false, matrix, 0);
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetViewMatrix: " + gl4.glGetError());
		}
	}

	public void setLight(GL4 gl4, PositionalLight light, int program) {
		gl4.glUseProgram(program);
		gl4.glUniform4fv(lightLocations[0], 1, GLBuffers.newDirectFloatBuffer(
				new float[] { (float) light.getPosition().getX(), (float) light.getPosition().getY(),
						(float) light.getPosition().getZ(), (float) light.getPosition().getW() }));
		gl4.glUniform4fv(lightLocations[1], 1, GLBuffers.newDirectFloatBuffer(light.getAmbient()));
		gl4.glUniform4fv(lightLocations[2], 1, GLBuffers.newDirectFloatBuffer(light.getDiffuse()));
		// TODO Increase performance of handling points data
		gl4.glUniform4fv(lightLocations[3], 1,
				GLBuffers.newDirectFloatBuffer(light.getSpecular()));
		gl4.glUniform3fv(lightLocations[4], 1, GLBuffers.newDirectFloatBuffer(
				new float[] { light.getConstantAtt(), light.getLinearAtt(), light.getQuadraticAtt() }));
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetLight: " + gl4.glGetError());
		}
	}

	public void setMaterial(GL4 gl4, Material material, int program) {
		gl4.glUseProgram(program);
		gl4.glUniform4fv(materialLocations[0], 1, material.getAmbient(), 0);
		gl4.glUniform4fv(materialLocations[1], 1, material.getDiffuse(), 0);
		gl4.glUniform4fv(materialLocations[2], 1, material.getSpecular(), 0);
		gl4.glUniform4fv(materialLocations[3], 1, material.getEmission(), 0);
		gl4.glUniform1f(materialLocations[4], material.getShininess());
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetMaterial: " + gl4.glGetError());
		}
	}

	public void setLambda(GL4 gl4, float lambda, int program) {
		gl4.glUseProgram(program);
		gl4.glUniform1f(lambdaLocation, lambda);
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetLambda: " + gl4.glGetError());
		}
	}
	
	public void setNormalMatrix(GL4 gl4, float[]  matrix, int program) {
		gl4.glUseProgram(program);
		gl4.glUniformMatrix3fv(normalMatrixLocation, 1, true, matrix, 0);
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetNormalMatrix: " + gl4.glGetError());
		}
	}

	public void disposeProgram(GL4 gl4) {
		program.destroy(gl4);
		// Just to be sure
		program = null;
	}
}
