package gfx.Display.Tests;

import java.util.Arrays;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.DebugGL4;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.glsl.ShaderState;

import gfx.Display.DisplayInterface;
import gfx.Scene.Objects.ImportedModel;
import gfx.Scene.Objects.Skybox;
import gfx.Scene.Objects.TexturedSphere;
import gfx.Utilities.MatrixService;
import gfx.Utilities.InputControllers.Keyboard.KeyboardController;
import gfx.Utilities.Shaders.ShaderProgramImportModel;
import gfx.Utilities.Shaders.ShaderProgramSkybox;
import graphicslib3D.Material;
import graphicslib3D.Point3D;
import graphicslib3D.light.PositionalLight;

public class WindowSolarSystemTest implements GLEventListener, DisplayInterface {
	private GLWindow window;
	private FPSAnimator animator;
	int width, height;
	public ShaderState state = new ShaderState();
	private ShaderProgramImportModel program = new ShaderProgramImportModel();
	private ShaderProgramSkybox skyboxProgram = new ShaderProgramSkybox();
	// private ImportedModel importedModel = new ImportedModel();
	private TexturedSphere mars = new TexturedSphere(20, 30, 2f, 0.75f, "mars_1k_color.jpg", new float[] { 0f, 0f, 9f },
			new float[] { 1f, 1f, 1f }, true);
	private TexturedSphere moon = new TexturedSphere(20, 30, 2f, 0.75f, "2k_moon.jpg", new float[] { 0f, 0f, 11f },
			new float[] { 0.3f, 0.3f, 0.3f }, true);
	private TexturedSphere sun = new TexturedSphere(20, 30, 2f, 0.75f, "2k_sun.jpg", new float[] { 0f, 0f, 0f },
			new float[] { 3f, 3f, 3f }, false);
	private Skybox skybox = new Skybox(Arrays.asList("SpaceSkybox/spaceBox_left.png", "SpaceSkybox/spaceBox_right.png",
			"SpaceSkybox/spaceBox_down.png", "SpaceSkybox/spaceBox_up.png", "SpaceSkybox/spaceBox_front.png",
			"SpaceSkybox/spaceBox_back.png"));
	private MatrixService matrixService = new MatrixService();
	private PositionalLight light = new PositionalLight();
	private KeyboardController keyboardController = new KeyboardController(mars, this);

	private float[] pLocationRaw = new float[16];
	private Point3D pLocation;
	private float[] projectionMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private int programId, skyboxProgramId;
	private Material material, moonMaterial, sunMaterial;

	public WindowSolarSystemTest(GLWindow windowPassed, FPSAnimator animatorPassed, int width, int height,
			String name) {
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
		// window.addMouseListener(this);
		window.addKeyListener(keyboardController);
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
		drawable.setGL(new DebugGL4(gl4));
		// Light SETUP
		matrixService.setupUnitMatrix(pLocationRaw);
		matrixService.translate(pLocationRaw, 0.0f, 0f, 0f);
		pLocation = new Point3D(pLocationRaw[0], pLocationRaw[5], pLocationRaw[10], pLocationRaw[15]);
		light.setPosition(pLocation);
		light.setAmbient(new float[] { 0.3f, 0.3f, 0.3f, 1.0f });
		light.setDiffuse(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		light.setSpecular(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		light.setConstantAtt(0.5f);
		light.setLinearAtt(0.005f);
		light.setQuadraticAtt(0.0125f);

		material = new Material();
		material.setAmbient(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		material.setDiffuse(new float[] { 0.8f, 0.007409f, 0.014076f, 1.0f });
		material.setSpecular(new float[] { 0.323789f, 0.323789f, 0.323789f, 1.0f });
		material.setEmission(new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
		material.setShininess(96.078431f);

		sunMaterial = new Material();
		sunMaterial.setAmbient(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		sunMaterial.setDiffuse(new float[] { 0.5f, 0.007409f, 0.014076f, 1.0f });
		sunMaterial.setSpecular(new float[] { 0.5f, 0.5f, 0.5f, 1.0f });
		sunMaterial.setEmission(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		sunMaterial.setShininess(96.078431f);

		programId = program.initProgram(gl4);
		program.useProgram(gl4, state);
		program.setLight(gl4, light, programId);

		matrixService.setupUnitMatrix(projectionMatrix);
		matrixService.setupUnitMatrix(viewMatrix);
		matrixService.translate(viewMatrix, 0, 0, -20);
		matrixService.rotateAboutXAxis(viewMatrix, 15);
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 200.0f);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setTextureUnit(gl4, 0);

		mars.setProgram(program);
		mars.viewMatrix = viewMatrix;
		mars.material = material;
		mars.init(drawable);
		moon.setProgram(program);
		moon.viewMatrix = viewMatrix;
		moon.material = material;
		moon.init(drawable);
		sun.setProgram(program);
		sun.viewMatrix = viewMatrix;
		sun.material = sunMaterial;
		sun.init(drawable);

		skyboxProgramId = skyboxProgram.initProgram(gl4);
		skyboxProgram.useProgram(gl4, state);
		skyboxProgram.setProjectionMatrix(gl4, projectionMatrix, skyboxProgramId);
		skyboxProgram.setViewMatrix(gl4, viewMatrix, skyboxProgramId);
		skyboxProgram.setTextureUnit(gl4, 1);

		skybox.setProgram(skyboxProgram);
		skybox.init(drawable);

		gl4.glEnable(GL4.GL_DEPTH_TEST);
		gl4.glDepthFunc(GL4.GL_LESS);
		gl4.glClearColor(0.8f, 0.9f, 1.0f, 1.0f);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// importedModel.dispose(drawable);
		skybox.dispose(drawable);
		mars.dispose(drawable);
		moon.dispose(drawable);
		sun.dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		keyboardController.controlKeyboard();

		/*
		 * importedModel.viewMatrix = viewMatrix; program.useProgram(gl4, state);
		 * program.setProjectionMatrix(gl4, projectionMatrix, programId); viewMatrix =
		 * importedModel.viewMatrix; program.setViewMatrix(gl4, viewMatrix, programId);
		 * importedModel.display(drawable);
		 */
		program.useProgram(gl4, state);
		mars.viewMatrix = viewMatrix;
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		viewMatrix = mars.viewMatrix;
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setLight(gl4, light, programId);
		mars.display(drawable);
		moon.display(drawable);
		sun.display(drawable);

		skyboxProgram.useProgram(gl4, state);
		skyboxProgram.setProjectionMatrix(gl4, projectionMatrix, skyboxProgramId);
		skyboxProgram.setViewMatrix(gl4, viewMatrix, skyboxProgramId);
		skybox.display(drawable);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 200.0f);
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

	@Override
	public void setMistType(int type) {
		// TODO Auto-generated method stub

	}

	@Override
	public float[] getLocalModelMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocalModelMatrix(float[] modelMatrix) {
		// TODO Auto-generated method stub

	}

	@Override
	public float[] getViewMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setViewMatrix(float[] viewMatrix) {
		// TODO Auto-generated method stub

	}

	@Override
	public float[] getProjectionMatrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProjectionMatrix(float[] projectionMatrix) {
		// TODO Auto-generated method stub

	}
}
