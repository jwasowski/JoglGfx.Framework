package gfx.Scene.Objects;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;

import gfx.Utilities.DeallocationHelper;
import gfx.Utilities.MatrixService;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramFogImportModel;
import gfx.Utilities.Shaders.ShaderProgramSprite;

public class Sprite implements GfxObjectInterface{
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	private float[] modelMatrix = new float[16];
	private ShaderProgramSprite program;
	private FloatBuffer colorVertexBuffer;
	private DeallocationHelper deallocator = new DeallocationHelper();
	private TextureLoader textureLoader = new TextureLoader();
	private MatrixService matrixService = new MatrixService();
	private Texture texture;
	private int textureId;
	private float time = 0.0f;
	private int numberOfPoints = 1536;
	
	public void setProgram(ShaderProgramSprite program) {
		this.program = program;
	}

	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		texture = textureLoader.loadTexture("FogTexture.png");
		textureId = texture.getTextureObject();
		matrixService.setupUnitMatrix(modelMatrix);
		//matrixService.translate(modelMatrix, -5.35f, 4.8f, -3.8f);
		matrixService.rotateAboutXAxis(modelMatrix, 90);
		
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);
		List<Float> colorVertexData = new ArrayList<>();
		for ( int i = 0; i < numberOfPoints; i++){
			colorVertexData.add((randomFloat() * 2.0f - 1.0f) * 50.0f);
			colorVertexData.add((randomFloat() * 2.0f - 1.0f) * 50.0f);
			colorVertexData.add(randomFloat());
			colorVertexData.add(1.0f);
			colorVertexData.add( 0.5f + randomFloat() * 0.2f);
			colorVertexData.add( 0.5f + randomFloat() * 0.2f);
			colorVertexData.add( 0.5f + randomFloat() * 0.2f);
			colorVertexData.add( 1.0f);
		    }
		
		float[] colorVertexRawData = ArrayUtils.toPrimitive(colorVertexData.toArray(new Float[0]), 0.0f);
		colorVertexBuffer = GLBuffers.newDirectFloatBuffer(colorVertexRawData);

		gl4.glEnableVertexAttribArray(0);
		gl4.glEnableVertexAttribArray(1);
		// Stride defines how many bytes there are in one set of vertex position
		// and color. That's four floats per vertex position and four floats per
		// color.
		final int stride = 8 * Buffers.SIZEOF_FLOAT;
		final long colorOffset = 4 * Buffers.SIZEOF_FLOAT;
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, colorVertexBuffer.limit() * Buffers.SIZEOF_FLOAT, colorVertexBuffer,
				GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glVertexAttribPointer(1, 4, GL4.GL_FLOAT, false, stride, colorOffset);
		gl4.glBindVertexArray(0);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glDisableVertexAttribArray(0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		if ("8".equals(System.getProperty("java.version").substring(2, 3))
				|| "9".equals(System.getProperty("java.version"))) {
			deallocator.deallocate(colorVertexBuffer);
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

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		//matrixService.rotateAboutXAxis(modelMatrix, 1);
		program.setModelMatrix(gl4, modelMatrix, program.getProgramId());
		program.setTime(gl4, time);
		gl4.glBindVertexArray(vertexArrayObject[0]);

		gl4.glActiveTexture(GL4.GL_TEXTURE1);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, textureId);

		gl4.glEnable(GL4.GL_BLEND);
		gl4.glBlendFunc(GL4.GL_ONE, GL4.GL_ONE);

		gl4.glEnable(GL4.GL_PROGRAM_POINT_SIZE);

		gl4.glDrawArrays(GL4.GL_POINTS, 0, numberOfPoints);

		gl4.glDisable(GL4.GL_BLEND);
		gl4.glDisable(GL4.GL_PROGRAM_POINT_SIZE);
		gl4.glBindVertexArray(0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		
		
	}
	
	private float randomFloat() {
		return (float) (Math.random()/1.0);
	}
	
	public void move(float value) {
		this.time = value;
	}

	@Override
	public void rotateXAxisUp() {
		
		
	}

	@Override
	public void rotateXAxisDown() {
		
		
	}

	@Override
	public void rotateYAxisLeft() {
		
		
	}

	@Override
	public void rotateYAxisRight() {
		
		
	}

	@Override
	public void moveForward() {
		
		
	}

	@Override
	public void moveBackwards() {
		
		
	}

	@Override
	public void moveLeft() {
		
		
	}

	@Override
	public void moveRight() {
		
		
	}

	@Override
	public void incAltitude() {
		
		
	}

	@Override
	public void decAltitude() {
		
		
	}

}
