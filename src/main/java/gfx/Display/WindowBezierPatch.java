package gfx.Display;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import gfx.Scene.Objects.BezierPatch;
import gfx.Utilities.MatrixService;
import gfx.Utilities.InputControllers.Keyboard.KeyboardController;
import gfx.Utilities.InputControllers.Mouse.MouseController;
import gfx.Utilities.Shaders.ShaderProgramBezierPatch;
import graphicslib3D.Material;
import graphicslib3D.Point3D;
import graphicslib3D.light.PositionalLight;

@SuppressWarnings("deprecation")
public class WindowBezierPatch implements GLEventListener {
	private GLWindow window;
	private FPSAnimator animator;
	private BezierPatch bezierPatchOne = new BezierPatch();
	private BezierPatch bezierPatchTwo = new BezierPatch();
	private BezierPatch bezierPatchFloor = new BezierPatch();
	private ShaderProgramBezierPatch program = new ShaderProgramBezierPatch();
	private MatrixService matrixService = new MatrixService();
	/*KeyboardController keyboardController = new KeyboardController(bezierPatchOne, bezierPatchTwo, bezierPatchFloor,
			this);*/
	MouseController mouseController = new MouseController(this);
	private int programId;
	Material material = new Material();
	PositionalLight light = new PositionalLight();
	Point3D plocation;
	GLU glu = new GLU();
	private float[] projectionMatrix = new float[16];
	public float[] viewMatrix = new float[16];
	private int width, height;
	// TODO Check data input format
	private float[] bezierPatchOneData = { -3f, 1f, 0f, 1f, -2.0f / 3.0f, 1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f,
			2.0f / 3.0f, 1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2, 1, 0, 1, -1.5f, 0.5f, 0, 1, -0.5f, 1.0f / 6.0f, 1,
			1.0f / 3.0f, 0.5f, 1.0f / 6.0f, 1, 1.0f / 3.0f, 1.5f, 0.5f, 0, 1, -3, 0, 0, 1, -1, 0, 2, 1.0f / 3.0f, 1, 0,
			2, 1.0f / 3.0f, 3, 0, 0, 1, -2, -1, 0, 1, -2.0f / 3.0f, -1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2.0f / 3.0f,
			-1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2, -1, 0, 1 };
	private float[] bezierPatchTwoData = { 2, 1, 0, 1, 2.0f / 3.0f, 1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f,
			-2.0f / 3.0f, 1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f, -3, 1, 0, 1, 1.5f, 0.5f, 0, 1, 0.5f, 1.0f / 6.0f, -1,
			1.0f / 3.0f, -0.5f, 1.0f / 6.0f, -1, 1.0f / 3.0f, -1.5f, 0.5f, 0, 1, 3, 0, 0, 1, 1, 0, -2, 1.0f / 3.0f, -1,
			0, -2, 1.0f / 3.0f, -3, 0, 0, 1, 2, -1, 0, 1, 2.0f / 3.0f, -1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f,
			-2.0f / 3.0f, -1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f, -2, -1, 0, 1 };
	private float[] bezierPatchFloorData = { -2, -1, 0, 1, -2.0f / 3.0f, -1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f,
			2.0f / 3.0f, -1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2, -1, 0, 1, -2, -1, 0, 1, 2.0f / 3.0f, -1.0f / 3.0f,
			-4.0f / 3.0f, 1.0f / 3.0f, -2.0f / 3.0f, -1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f, 2, -1, 0, 1, 2, -1, 0, 1,
			-2.0f / 3.0f, -1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2.0f / 3.0f, -1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f,
			-2, -1, 0, 1, 2, -1, 0, 1, 2.0f / 3.0f, -1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f, -2.0f / 3.0f, -1.0f / 3.0f,
			-4.0f / 3.0f, 1.0f / 3.0f, -2, -1, 0, 1 };

	public WindowBezierPatch(GLWindow windowPassed, FPSAnimator animatorPassed, int width, int height, String name) {
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
		//window.addMouseListener(mouseController);
		//window.addKeyListener(keyboardController);
		window.setSize(width, height);
		window.setTitle(name);
		window.setVisible(true);
		animator.start();

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		System.out.println("GL_RENDERER: " + gl4.glGetString(GL4.GL_RENDERER));
		System.out.println("GL_VERSION: " + gl4.glGetString(GL4.GL_VERSION));
		// Material SETUP
		material.setAmbient(new float[] { 0.6f, 0.6f, 0.6f, 1.0f });
		material.setDiffuse(new float[] { 1.0f, 1.0f, 0.0f, 1.0f });
		material.setSpecular(new float[] { 0.6f, 0.6f, 0.6f, 1.0f });
		material.setEmission(new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
		material.setShininess(60.0f);
		// Light SETUP
		plocation = new Point3D(0.0f, 9.5f, 3.0f, 1.0f);
		light.setPosition(plocation);
		light.setAmbient(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });
		light.setDiffuse(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		light.setSpecular(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		light.setConstantAtt(0.5f);
		light.setLinearAtt(0.005f);
		light.setQuadraticAtt(0.0125f);

		programId = program.initProgram(gl4);
		program.setLight(gl4, light, programId);

		bezierPatchOne.material = (Material) material.clone();
		bezierPatchTwo.material = (Material) material.clone();
		bezierPatchFloor.material = (Material) material.clone();

		matrixService.setupUnitMatrix(projectionMatrix);
		matrixService.setupUnitMatrix(viewMatrix);
		matrixService.translate(viewMatrix, 0, 0, -10);
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 100.0f);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		program.setViewMatrix(gl4, viewMatrix, programId);

		bezierPatchOne.pointsOne = bezierPatchOneData;

		bezierPatchTwo.pointsOne = bezierPatchTwoData;
		bezierPatchFloor.pointsOne = bezierPatchFloorData;

		bezierPatchOne.setProgram(program);
		bezierPatchTwo.setProgram(program);
		bezierPatchFloor.setProgram(program);

		bezierPatchOne.init(drawable);
		bezierPatchTwo.init(drawable);
		bezierPatchFloor.init(drawable);

		gl4.glEnable(GL4.GL_DEPTH_TEST);
		gl4.glDepthFunc(GL4.GL_LESS);
		gl4.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		bezierPatchOne.dispose(drawable);
		bezierPatchTwo.dispose(drawable);
		bezierPatchFloor.dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		//keyboardController.controlKeyboard();
		lookAt();
		bezierPatchOne.display(drawable);
		bezierPatchTwo.display(drawable);
		bezierPatchFloor.display(drawable);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		program.setViewMatrix(gl4, viewMatrix, programId);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 100.0f);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		gl4.glViewport(0, 0, width, height);
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

	private void lookAt() {
		glu.gluLookAt(viewMatrix[0], viewMatrix[5], viewMatrix[10],
				mouseController.mouseCoords[0], mouseController.mouseCoords[1], 0/* z */, 0, 1, 0);
	}
}
