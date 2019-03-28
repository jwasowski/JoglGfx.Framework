package gfx.Display;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.FPSAnimator;

import gfx.Scene.Objects.BezierPatch;
import gfx.Scene.Objects.TexturedPlane;
import gfx.Utilities.MatrixService;
import gfx.Utilities.Shaders.ShaderProgramBezierPatch;
import gfx.Utilities.Shaders.ShaderProgramCombined;
import graphicslib3D.Material;
import graphicslib3D.Point3D;
import graphicslib3D.light.PositionalLight;

public class WindowBezierPatch implements GLEventListener, MouseListener, KeyListener {
	private GLWindow window;
	private FPSAnimator animator;
	private BezierPatch bezierPatchOne = new BezierPatch();
	private BezierPatch bezierPatchTwo = new BezierPatch();
	private ShaderProgramBezierPatch program = new ShaderProgramBezierPatch();
	private MatrixService matrixService = new MatrixService();
	private int programId;
	Material material = new Material();
	PositionalLight light = new PositionalLight();
	Point3D plocation;
	private float[] projectionMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	int width, height;
	// TODO Check data input format
	private float[] bezierPatchOneData = { -2f, 1f, 0f, 1f, -2.0f / 3.0f, 1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f,
			2.0f / 3.0f, 1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2, 1, 0, 1, -1.5f, 0.5f, 0, 1, -0.5f, 1.0f / 6.0f, 1,
			1.0f / 3.0f, 0.5f, 1.0f / 6.0f, 1, 1.0f / 3.0f, 1.5f, 0.5f, 0, 1, -3, 0, 0, 1, -1, 0, 2, 1.0f / 3.0f, 1, 0,
			2, 1.0f / 3.0f, 3, 0, 0, 1, -2, -1, 0, 1, -2.0f / 3.0f, -1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2.0f / 3.0f,
			-1.0f / 3.0f, 4.0f / 3.0f, 1.0f / 3.0f, 2, -1, 0, 1 };
	private float[] bezierPatchTwoData = { 2, 1, 0, 1, 2.0f / 3.0f, 1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f,
			-2.0f / 3.0f, 1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f, -2, 1, 0, 1, 1.5f, 0.5f, 0, 1, 0.5f, 1.0f / 6.0f, -1,
			1.0f / 3.0f, -0.5f, 1.0f / 6.0f, -1, 1.0f / 3.0f, -1.5f, 0.5f, 0, 1, 3, 0, 0, 1, 1, 0, -2, 1.0f / 3.0f, -1,
			0, -2, 1.0f / 3.0f, -3, 0, 0, 1, 2, -1, 0, 1, 2.0f / 3.0f, -1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f,
			-2.0f / 3.0f, -1.0f / 3.0f, -4.0f / 3.0f, 1.0f / 3.0f, -2, -1, 0, 1 };

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
		window.addMouseListener(this);
		window.addKeyListener(this);
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
		//TODO Init both Patches and check init process
		programId = program.initProgram(gl4);

		// TODO Check field init
		bezierPatchOne.setProgram(program);
		matrixService.setupUnitMatrix(projectionMatrix);
		matrixService.setupUnitMatrix(viewMatrix);
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.5f, 100.0f);
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		
		bezierPatchOne.init(drawable);
		gl4.glEnable(GL4.GL_DEPTH_TEST);
		gl4.glDepthFunc(GL4.GL_LESS);
		gl4.glClearColor(0.25f, 0.75f, 0.35f, 0.0f);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		bezierPatchOne.dispose(drawable);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		bezierPatchOne.display(drawable);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		bezierPatchOne.reshape(drawable, 0, 0, width, height);

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

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			shutDown();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
