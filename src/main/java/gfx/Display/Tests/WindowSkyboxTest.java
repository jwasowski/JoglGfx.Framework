package gfx.Display.Tests;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.DebugGL4;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.util.texture.Texture;

import gfx.Utilities.MatrixService;
import gfx.Utilities.TextureLoader;
import gfx.Utilities.Shaders.ShaderProgramCombined;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import gfx.Utilities.Shaders.ShaderProgramSkybox;

public class WindowSkyboxTest implements GLEventListener {

	private GLWindow window;
	private FPSAnimator animator;
	private TextureLoader textureLoader = new TextureLoader();
	private MatrixService matrixService = new MatrixService();
	private int width, height, skyboxProgramId, importProgramId;
	private float[] projectionMatrix = new float[16];
	public float[] viewMatrix = new float[16];
	public float[] modelMatrix = new float[16];
	
	float[] textureVertexData = { -50.0f, 50.0f, 50.0f, 1.0f, -1.0f, 1.0f, 1.0f, 50.0f, 50.0f, 50.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 50.0f, -50.0f, 50.0f, 1.0f, 1.0f, -1.0f, 1.0f, -50.0f, -50.0f, 50.0f, 1.0f, -1.0f, -1.0f, 1.0f,

			50.0f, 50.0f, -50.0f, 1.0f, 1.0f, 1.0f, -1.0f, -50.0f, 50.0f, -50.0f, 1.0f, -1.0f, 1.0f, -1.0f, -50.0f,
			-50.0f, -50.0f, 1.0f, -1.0f, -1.0f, -1.0f, 50.0f, -50.0f, -50.0f, 1.0f, 1.0f, -1.0f, -1.0f };

	int[] indices = { 0, 1, 3, 1, 2, 3, 4, 5, 7, 5, 6, 7, 5, 4, 0, 4, 1, 0, 7, 6, 2, 6, 3, 2, 5, 0, 6, 0, 3, 6, 1, 4, 2,
			4, 7, 2 };
	protected final int[] vertexArrayObject = new int[1];
	protected final int[] vertexBufferObject = new int[1];
	protected final int[] indexBufferObject = new int[1];
	public int testTextureId;
	private ShaderState state = new ShaderState();
	protected ShaderProgramSkybox skyboxProgram = new ShaderProgramSkybox();
	protected ShaderProgramImportModel importProgram = new ShaderProgramImportModel();
	public List<Texture> textureData = new ArrayList<>();
	private FloatBuffer textureVertexBuffer;
	private IntBuffer indicesBuffer;

	public WindowSkyboxTest(GLWindow windowPassed, FPSAnimator animatorPassed, int width, int height, String name) {
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
		drawable.setGL(new DebugGL4(gl4));
		System.out.println("GL_RENDERER: " + gl4.glGetString(GL4.GL_RENDERER));
		System.out.println("GL_VERSION: " + gl4.glGetString(GL4.GL_VERSION));
		skyboxProgramId = skyboxProgram.initProgram(gl4);
		skyboxProgram.useProgram(gl4, state);
		textureData.add(textureLoader.loadCubeTexture(gl4, "ziemia.tga", false));
		testTextureId = textureData.get(0).getTextureObject();
		System.out.println("TextureId: "+testTextureId);
		matrixService.setupUnitMatrix(viewMatrix);
		matrixService.setupUnitMatrix(modelMatrix);
		matrixService.translate(viewMatrix, 0, 0, -30);
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 100.0f);
		skyboxProgram.getAllUniformLocations(gl4);
		
		
		
		importProgramId = importProgram.initProgram(gl4);
		
		
		// Voa Setup
		gl4.glGenVertexArrays(1, vertexArrayObject, 0);
		gl4.glBindVertexArray(vertexArrayObject[0]);

		textureVertexBuffer = GLBuffers.newDirectFloatBuffer(textureVertexData);
		
		gl4.glEnableVertexAttribArray(0);
		gl4.glEnableVertexAttribArray(2);
		// Stride defines how many bytes there are in one set of vertex position
		// and color. That's four floats per vertex position and four floats per
		// color.
		final int stride = 7 * Buffers.SIZEOF_FLOAT;
		final long textureOffset = 4 * Buffers.SIZEOF_FLOAT;
		// Vbo vertices Setup
		gl4.glGenBuffers(vertexBufferObject.length, vertexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferObject[0]);
		gl4.glBufferData(GL4.GL_ARRAY_BUFFER, textureVertexBuffer.limit() * Buffers.SIZEOF_FLOAT, textureVertexBuffer,
				GL4.GL_STATIC_DRAW);
		gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);
		gl4.glVertexAttribPointer(2, 3, GL4.GL_FLOAT, false, stride, textureOffset);
		gl4.glEnableVertexAttribArray(0);

		// Indices buffer Setup
		gl4.glGenBuffers(indexBufferObject.length, indexBufferObject, 0);
		gl4.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
		indicesBuffer = GLBuffers.newDirectIntBuffer(indices);
		gl4.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.limit() * Buffers.SIZEOF_INT, indicesBuffer,
				GL4.GL_STATIC_DRAW);
		gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl4.glBindVertexArray(0);

		gl4.glEnable(GL4.GL_DEPTH_TEST);
		gl4.glDepthFunc(GL4.GL_LESS);
		gl4.glClearColor(0.8f, 0.9f, 1.0f, 1.0f);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		
		skyboxProgram.useProgram(gl4, state);
		
		matrixService.rotateAboutYAxis(viewMatrix, 0.1f);
		
		skyboxProgram.setViewMatrix(gl4, viewMatrix, skyboxProgramId);
		skyboxProgram.setProjectionMatrix(gl4, projectionMatrix, skyboxProgramId);
		skyboxProgram.setTextureUnit(gl4, 0);
		
		gl4.glBindVertexArray(vertexArrayObject[0]);
		
		gl4.glEnable(GL4.GL_CULL_FACE);
		gl4.glCullFace(GL4.GL_BACK);
		gl4.glFrontFace(GL4.GL_CCW);
		
		gl4.glActiveTexture(GL4.GL_TEXTURE0);
		gl4.glBindTexture(GL4.GL_TEXTURE_CUBE_MAP, testTextureId);
		//GL_TEXTURE_CUBE_MAP
		gl4.glDepthMask(false);
		gl4.glDrawElements(GL4.GL_TRIANGLES, indices.length, GL4.GL_UNSIGNED_INT, 0);
		gl4.glDepthMask(true);
		
		gl4.glDisable(GL4.GL_CULL_FACE);
		
		gl4.glBindTexture(GL4.GL_TEXTURE_CUBE_MAP,0);
		
		gl4.glBindVertexArray(0);
		
		importProgram.useProgram(gl4, state);
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