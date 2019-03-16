package gfx.Display;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.FPSAnimator;

import gfx.Scene.Objects.TexturedPlane;
import gfx.Utilities.MatrixService;
import gfx.Utilities.Shaders.ShaderProgramCombined;

public class Window implements GLEventListener {
	private GLWindow window;
	private FPSAnimator animator;
	private TexturedPlane plane = new TexturedPlane();
	private ShaderProgramCombined program = new ShaderProgramCombined();
	private MatrixService matrixService = new MatrixService();
	private int programId;
	private float[] projectionMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	int width, height;

	public Window(GLWindow windowPassed, FPSAnimator animatorPassed, int width, int height, String name) {
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

	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		System.out.println("GL_RENDERER: " + gl4.glGetString(GL4.GL_RENDERER));
		System.out.println("GL_VERSION: " + gl4.glGetString(GL4.GL_VERSION));
		programId = program.initProgram(gl4);
		//TODO Check field init
		plane.setProgram(program);
		matrixService.setupUnitMatrix(projectionMatrix);
		matrixService.setupUnitMatrix(viewMatrix);
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.5f, 100.0f);
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		//TODO Init texture
		plane.init(drawable);
		gl4.glEnable(GL4.GL_DEPTH_TEST);
		gl4.glDepthFunc(GL4.GL_LESS);
		gl4.glClearColor(0.25f, 0.75f, 0.35f, 0.0f);
	}

	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		plane.dispose(drawable);
	}

	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		plane.display(drawable);
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		gl4.glUseProgram(0);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 100.0f);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		plane.reshape(drawable, 0, 0, width, height);
	}

	private void shutDown() {
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
