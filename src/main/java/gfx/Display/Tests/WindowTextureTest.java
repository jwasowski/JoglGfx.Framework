package gfx.Display.Tests;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;

import gfx.Utilities.MatrixService;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramCombined;

public class WindowTextureTest implements GLEventListener {
	private ShaderProgramCombined program = new ShaderProgramCombined();
	private MatrixService matrixService = new MatrixService();
	private GLWindow window;
	private FPSAnimator animator;
	private TextureLoader textureLoader = new TextureLoader();
	private int width, height, programId;
	private float[] projectionMatrix = new float[16];
	public float[] viewMatrix = new float[16];
	public float[] modelMatrix = new float[16];
	public float[] modelMatrixTwo = new float[16];
	public float[] modelMatrixThree = new float[16];
	public float[] modelMatrixFour = new float[16];
	public float[] modelMatrixFive = new float[16];
	public float[] vertexData = {-1f, -1f, 1f, 1f, -1f, 1f, 0f, 1f, 0f,
								   1f, -1f, 1f, 1f, -1f, -1f, 0f, 1f, 0f,
								   1f, -1f, -1f, -1f, -1f, -1f, 0f, 1f, 0f,
								   -1f, -1f, -1f, -1f, -1f, 1f, 0f, 1f, 0f,
								   -1f, -1f, -1f, 1f, -1f, 1f, -1f, -1f, 1f,
								   1f, -1f, 1f, -1f, -1f, -1f, 1f, -1f, -1f};
	public float[] textureData = {0f, 0f, 1f, 0f,  0.5f, 1f, 0f, 0f, 
								1f, 0f, 0.5f, 1f,  0f, 0f, 1f, 0f, 
								0.5f, 1f, 0f, 0f,  1f, 0f, 0.5f, 1f, 
								  0f, 0f, 1f, 1f,  0f, 1f, 1f, 1f, 
								 0f, 0f, 1f, 0f};
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[2];
	public int testTextureId;
	public int testTextureTwoId;
	public int testTextureThreeId;
	public int testTextureJPGId;
	public Texture testTexture;
	public Texture testTextureTwo;
	public Texture testTextureThree;
	public Texture testTextureJPG;
	public FloatBuffer fbVertices;
	public FloatBuffer textureCoords;
	
	public WindowTextureTest(GLWindow windowPassed, FPSAnimator animatorPassed, int width, int height, String name) {
		this.window = windowPassed;
		this.animator = animatorPassed;
		this.width = width;
		this.height = height;
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyNotify(WindowEvent arg0) {
				shutDown();
			};
		});
		window.addGLEventListener(this);
		window.setSize(width, height);
		window.setTitle(name);
		window.setVisible(true);
		animator.start();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();

		programId = program.initProgram(gl4);
		testTexture = textureLoader.loadTexture("ziemia.tga");
		testTextureId = testTexture.getTextureObject();
		testTextureTwo = textureLoader.loadTexture("merkury.tga");
		testTextureTwoId = testTextureTwo.getTextureObject();
		testTextureThree = textureLoader.loadTexture("ksiezyc.tga");
		testTextureThreeId = testTextureThree.getTextureObject();
		testTextureJPG = textureLoader.loadTexture("mars_1k_color.jpg");
		testTextureJPGId = testTextureJPG.getTextureObject();
		
		matrixService.setupUnitMatrix(modelMatrix);
		matrixService.setupUnitMatrix(modelMatrixTwo);
		matrixService.setupUnitMatrix(modelMatrixThree);
		matrixService.setupUnitMatrix(modelMatrixFour);
		matrixService.setupUnitMatrix(modelMatrixFive);
		matrixService.translate(modelMatrixTwo, 2, 2, 2);
		matrixService.translate(modelMatrixThree, -2, 2, 2);
		matrixService.translate(modelMatrixFour, 2, -2, 0);
		matrixService.translate(modelMatrixFive, -2, -2, -3);
		matrixService.setupUnitMatrix(projectionMatrix);
		matrixService.setupUnitMatrix(viewMatrix);
		matrixService.translate(viewMatrix, 0, 0, -10);
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 100.0f);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setModelMatrix(gl4, modelMatrix, programId);
		
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		
 		gl4.glBindVertexArray(vertexArrayObject[0]);
 		// Vbo vertices Setup
 		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
 		
 		fbVertices = Buffers.newDirectFloatBuffer(vertexData);
 		textureCoords = Buffers.newDirectFloatBuffer(textureData);
 		final long verticesBufferSizeInBytes = vertexData.length * Buffers.SIZEOF_FLOAT;
 		final long textureCoordsBufferSizeInBytes = textureData.length * Buffers.SIZEOF_FLOAT;
 		// Stride defines how many bytes there are in one set of vertex position
 		// and texture position. That's four floats per vertex position and two floats per
 		// texture position.
 		final int stride = 4 * Buffers.SIZEOF_FLOAT;
 		// Offset defines how many bytes to skip, counted from beginning of
 		// the Stride.
 		final long textureOffset = 4 * Buffers.SIZEOF_FLOAT;
 		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
 		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, verticesBufferSizeInBytes, fbVertices, GL4.GL_STATIC_DRAW);
 		gl4.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);
 		gl4.glEnableVertexAttribArray(0);
 		
 		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[1]);
 		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, textureCoordsBufferSizeInBytes, textureCoords, GL4.GL_STATIC_DRAW);
 		gl4.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false, 0, 0);
 		gl4.glEnableVertexAttribArray(2);
 		 
 		gl4.glBindVertexArray(0);
		
		
		gl4.glEnable(GL4.GL_DEPTH_TEST);
		gl4.glDepthFunc(GL4.GL_LEQUAL);
		gl4.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		if(gl4.glGetError() != 0){
		System.err.println("Error code: " + gl4.glGetError());
		}
		gl4.glBindVertexArray(vertexArrayObject[0]);
		gl4.glUseProgram(programId);
		
		matrixService.rotateAboutXAxis(modelMatrix, 2);
		matrixService.rotateAboutYAxis(modelMatrix, 2);
		program.setModelMatrix(gl4, modelMatrix, programId);
		program.setTextureUnit(gl4, 0);
		gl4.glActiveTexture(GL4.GL_TEXTURE0);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, testTextureId);
		
		
		if(gl4.glGetError() != 0){
			System.err.println("Error code: " + gl4.glGetError());
			}
		
		if(gl4.glGetError() != 0){
			System.err.println("Error code: " + gl4.glGetError());
			}
		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);
	    gl4.glFrontFace(GL4.GL_CW);
	    
	    gl4.glDrawArrays(GL4.GL_TRIANGLES, 0 , 18);
	    
	    matrixService.rotateAboutYAxis(modelMatrixTwo, 2);
	    program.setModelMatrix(gl4, modelMatrixTwo, programId);
	    program.setTextureUnit(gl4, 1);
	    gl4.glActiveTexture(GL4.GL_TEXTURE1);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, testTextureTwoId);
	    gl4.glDrawArrays(GL4.GL_TRIANGLES, 0 , 18);
	    
	    matrixService.rotateAboutYAxis(modelMatrixThree, 2);
	    program.setModelMatrix(gl4, modelMatrixThree, programId);
	    program.setTextureUnit(gl4, 2);
	    gl4.glActiveTexture(GL4.GL_TEXTURE2);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, testTextureThreeId);
	    gl4.glDrawArrays(GL4.GL_TRIANGLES, 0 , 18);
	    
	    matrixService.rotateAboutYAxis(modelMatrixFour, 2);
	    program.setModelMatrix(gl4, modelMatrixFour, programId);
	    program.setTextureUnit(gl4, 0);
	    gl4.glActiveTexture(GL4.GL_TEXTURE0);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, testTextureId);
	    gl4.glDrawArrays(GL4.GL_TRIANGLES, 0 , 18);
	    
	    matrixService.rotateAboutYAxis(modelMatrixFive, 2);
	    program.setModelMatrix(gl4, modelMatrixFive, programId);
	    program.setTextureUnit(gl4, 3);
	    gl4.glActiveTexture(GL4.GL_TEXTURE3);
		gl4.glBindTexture(GL4.GL_TEXTURE_2D, testTextureJPGId);
	    gl4.glDrawArrays(GL4.GL_TRIANGLES, 0 , 18);
	    
	    gl4.glDisable(GL4.GL_CULL_FACE);
		
		gl4.glBindVertexArray(0);
		gl4.glUseProgram(0);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glViewport(x, y, width, height);
	}

	
	public void shutDown() {
		// Use a dedicate thread to run the stop() to ensure that the
		// animator stops before program exits.
		new Thread() {
			@Override
			public void run() {
				System.out.println("Shut Down");
				animator.stop(); // stop the animator loop
				System.exit(0);
			}
		}.start();
	}
}
