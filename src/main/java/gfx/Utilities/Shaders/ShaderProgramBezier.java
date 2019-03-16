package gfx.Utilities.Shaders;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

public class ShaderProgramBezier {
	private ShaderProgram program;
	private ShaderCode vertexShader;
	private ShaderCode geometryShader;
	private ShaderCode fragmentShader;
	public int textureUnitLocation;
	public int modelMatrixLocation;
	public int projectionMatrixLocation;
	public int viewMatrixLocation;

	public int initProgram(GL4 gl4) {
		vertexShader = ShaderCode.create(gl4, GL4.GL_VERTEX_SHADER, this.getClass(), "Shaders", null,
				"geometryVertex", null, null, true);
		geometryShader = ShaderCode.create(gl4, GL4.GL_GEOMETRY_SHADER, this.getClass(), "Shaders", null,
				"geometry", null, null, true);
		fragmentShader = ShaderCode.create(gl4, GL4.GL_FRAGMENT_SHADER, this.getClass(), "Shaders", null,
				"geometryFragment", null, null, true);
		program = new ShaderProgram();
		program.add(vertexShader);
		program.add(geometryShader);
		program.add(fragmentShader);
		program.link(gl4, System.out);
		program.validateProgram(gl4, System.err);
		System.out.println("Superprogram: " + program.id());
		return program.program();
	}
	
	public int getProgramId(){
		return program.id();
	}
	
	public void disposeProgram(GL4 gl4) {
		program.destroy(gl4);
		// Just to be sure
		program = null;
	}
}
