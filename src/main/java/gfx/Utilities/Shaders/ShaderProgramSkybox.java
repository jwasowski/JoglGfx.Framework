package gfx.Utilities.Shaders;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

public class ShaderProgramSkybox {
	private ShaderProgram program;
	private ShaderCode vertexShader;
	private ShaderCode fragmentShader;
	public int textureUnitLocation;
	public int projectionMatrixLocation;
	public int viewMatrixLocation;

	public int initProgram(GL4 gl4) {
		vertexShader = ShaderCode.create(gl4, GL4.GL_VERTEX_SHADER, this.getClass(), "Shaders/Skybox", null,
				"SkyboxVertex", null, null, true);
		fragmentShader = ShaderCode.create(gl4, GL4.GL_FRAGMENT_SHADER, this.getClass(), "Shaders/Skybox", null,
				"SkyboxFrag", null, null, true);
		program = new ShaderProgram();
		program.add(vertexShader);
		program.add(fragmentShader);
		
		program.link(gl4, System.out);
		program.validateProgram(gl4, System.err);
		System.out.println("SkyboxProgram: " + program.id());
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
		projectionMatrixLocation = getUniformLocation("projection_matrix", gl4);
		viewMatrixLocation = getUniformLocation("view_matrix", gl4);
		textureUnitLocation = getUniformLocation("texture_unit", gl4);
		System.out.println("ProjectionLoc: "+projectionMatrixLocation+" ViewLoc: "+viewMatrixLocation+" TextureLoc: "+textureUnitLocation);
	}
	
	public int getProgramId(){
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

	public void disposeProgram(GL4 gl4) {
		program.destroy(gl4);
		// Just to be sure
		program = null;
	}
}
