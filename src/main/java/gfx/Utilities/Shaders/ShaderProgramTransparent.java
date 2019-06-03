package gfx.Utilities.Shaders;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

public class ShaderProgramTransparent {
	private ShaderProgram program;
	private ShaderCode vertexShader;
	private ShaderCode fragmentShader;
	
	public int modelMatrixLocation;
	public int projectionMatrixLocation;
	public int viewMatrixLocation;
	private int materialLocation;
	
	public int initProgram(GL4 gl4) {

		vertexShader = ShaderCode.create(gl4, GL4.GL_VERTEX_SHADER, this.getClass(), "Shaders/Transparency", null,
				"TransparentVertex", null, null, true);

		fragmentShader = ShaderCode.create(gl4, GL4.GL_FRAGMENT_SHADER, this.getClass(), "Shaders/Transparency", null,
				"TransparentFragment", null, null, true);
		vertexShader.defaultShaderCustomization(gl4, true, true);
		fragmentShader.defaultShaderCustomization(gl4, true, true);
		program = new ShaderProgram();
		program.add(gl4, vertexShader, System.err);
		program.add(gl4, fragmentShader, System.err);

		program.link(gl4, System.out);
		program.validateProgram(gl4, System.err);

		System.out.println("TransparencyProgram: " + program.id());
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
		materialLocation = getUniformLocation("material", gl4);
		System.out.println("ProjectionLoc: " + projectionMatrixLocation + " ViewLoc: " + viewMatrixLocation
				+ " ModelLoc: " + modelMatrixLocation);
	}
	
	public int getProgramId() {
		return program.program();
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
	
	public void setColor(GL4 gl4, float[] color, int program) {
		gl4.glUseProgram(program);
		gl4.glUniform4fv(materialLocation, 1, color, 0);
		if (gl4.glGetError() != 0) {
			System.err.println("Error code in SetColor: " + gl4.glGetError());
		}
	}
}
