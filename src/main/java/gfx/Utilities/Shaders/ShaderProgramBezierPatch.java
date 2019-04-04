package gfx.Utilities.Shaders;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

import graphicslib3D.Material;
import graphicslib3D.light.PositionalLight;

public class ShaderProgramBezierPatch {
	private ShaderProgram program;
	private ShaderCode vertexShader;
	private ShaderCode tessControl;
	private ShaderCode tessEvaluation;
	private ShaderCode fragmentShader;
	public int textureUnitLocation;
	public int modelMatrixLocation;
	public int projectionMatrixLocation;
	public int viewMatrixLocation;
	public int lambdaLocation;
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
	public boolean disposedFlag;

	public int initProgram(GL4 gl4) {
		vertexShader = ShaderCode.create(gl4, GL4.GL_VERTEX_SHADER, this.getClass(), "Shaders/BezierPatch", null,
				"Vertex", null, null, true);

		fragmentShader = ShaderCode.create(gl4, GL4.GL_FRAGMENT_SHADER, this.getClass(), "Shaders/BezierPatch", null,
				"Fragment", null, null, true);
		tessControl = ShaderCode.create(gl4, GL4.GL_TESS_CONTROL_SHADER, this.getClass(), "Shaders/BezierPatch", null,
				"TesselationControl", null, null, true);
		tessEvaluation = ShaderCode.create(gl4, GL4.GL_TESS_EVALUATION_SHADER, this.getClass(), "Shaders/BezierPatch",
				null, "TesselationEvaluation", null, null, true);
		program = new ShaderProgram();
		program.add(vertexShader);
		program.add(fragmentShader);
		program.add(tessControl);
		program.add(tessEvaluation);
		program.link(gl4, System.out);
		program.validateProgram(gl4, System.err);
		System.out.println("Superprogram: " + program.id());
		modelMatrixLocation = getUniformLocation("model_matrix", gl4);
		viewMatrixLocation = getUniformLocation("view_matrix", gl4);
		projectionMatrixLocation = getUniformLocation("projection_matrix", gl4);
		lambdaLocation = getUniformLocation("lambda", gl4);
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

		return program.program();
	}

	public int getUniformLocation(String name, GL4 gl4) {
		int location = -1;
		location = gl4.glGetUniformLocation(program.id(), name);
		if (location < 0) {
			System.err.println("ERROR: Cannot find uniform location: " + name);
		}
		return location;
	}

	public int getProgramId() {
		return program.id();
	}

	public void setTextureUnit(GL4 gl4, int t) {
		gl4.glUseProgram(program.id());
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

	public void disposeProgram(GL4 gl4) {
		program.destroy(gl4);
		System.out.println("SHADER PROGRAM STATUS: Destroyed");
		// Just to be sure
		program = null;
		disposedFlag = true;
	}
}
