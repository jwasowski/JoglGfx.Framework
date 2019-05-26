package gfx.Display;

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

import gfx.Scene.Objects.FogImportedModel;
import gfx.Scene.Objects.FogSkybox;
import gfx.Utilities.MatrixService;
import gfx.Utilities.InputControllers.Keyboard.KeyboardController;
import gfx.Utilities.InputControllers.Mouse.MouseController;
import gfx.Utilities.Shaders.ShaderProgramFogImportModel;
import gfx.Utilities.Shaders.ShaderProgramFogSkybox;
import graphicslib3D.Material;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.light.DistantLight;
import graphicslib3D.light.PositionalLight;

public class WindowFogModelImport implements GLEventListener, DisplayInterface {
	private GLWindow window;
	private FPSAnimator animator;
	int width, height;
	public ShaderState state = new ShaderState();
	private ShaderProgramFogImportModel program = new ShaderProgramFogImportModel();
	private ShaderProgramFogSkybox skyboxProgram = new ShaderProgramFogSkybox();
	private FogImportedModel importedModel = new FogImportedModel();
	private FogSkybox skybox = new FogSkybox();
	private MatrixService matrixService = new MatrixService();
	private PositionalLight light = new PositionalLight();
	private DistantLight dirLight = new DistantLight();
	private KeyboardController keyboardController = new KeyboardController(importedModel, this);
	private MouseController mouseController = new MouseController(this, matrixService);
	private float timer;
	private int timerFactor = 30;
	private Point3D pLocation;
	private float[] projectionMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] mist = { 0.2f, 0.2f, 0.2f, 1.0f, 1.0f, 60.0f, 0.10f };
	private float[] highAmbientLightning = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lowAmbientLightning = { 0.1f, 0.1f, 0.1f, 1.0f };
	private int programId, skyboxProgramId, mistType = 4;
	private Material material;

	public WindowFogModelImport(GLWindow windowPassed, FPSAnimator animatorPassed, int width, int height, String name) {
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
		window.addMouseListener(mouseController);
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
		pLocation = new Point3D(-4.8f, 5.0f, -4.0f, 1.0f);
		light.setPosition(pLocation);
		light.setAmbient(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });
		light.setDiffuse(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		light.setSpecular(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		light.setConstantAtt(0.5f);
		light.setLinearAtt(0.05f);
		light.setQuadraticAtt(0.0125f);

		pLocation = new Point3D(1f, 1f, -1.0f, 1.0f);
		dirLight.setDirection(new Vector3D(pLocation));
		dirLight.setAmbient(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });
		dirLight.setDiffuse(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		dirLight.setSpecular(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });

		material = new Material();
		material.setAmbient(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		material.setDiffuse(new float[] { 0.8f, 0.007409f, 0.014076f, 1.0f });
		material.setSpecular(new float[] { 0.323789f, 0.323789f, 0.323789f, 1.0f });
		material.setEmission(new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
		material.setShininess(96.078431f);

		programId = program.initProgram(gl4);
		program.useProgram(gl4, state);
		program.setLight(gl4, light, programId);
		program.setDirLight(gl4, dirLight, programId);
		program.setMist(gl4, mist, programId);
		program.setMistType(gl4, mistType, programId);

		matrixService.setupUnitMatrix(projectionMatrix);
		matrixService.setupUnitMatrix(viewMatrix);
		matrixService.translate(viewMatrix, 0, -2, -10);
		// matrixService.rotateAboutXAxis(viewMatrix, 15);
		System.out.println("ViewMatrix: " + Arrays.toString(viewMatrix));
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 100.0f);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setTextureUnit(gl4, 0);

		importedModel.viewMatrix = viewMatrix;
		importedModel.setProgram(program);
		importedModel.material = material;
		importedModel.init(drawable);

		skyboxProgramId = skyboxProgram.initProgram(gl4);
		skyboxProgram.useProgram(gl4, state);
		skyboxProgram.setProjectionMatrix(gl4, projectionMatrix, skyboxProgramId);
		skyboxProgram.setViewMatrix(gl4, viewMatrix, skyboxProgramId);
		skyboxProgram.setTextureUnit(gl4, 1);
		skyboxProgram.setMist(gl4, mist, skyboxProgramId);
		skyboxProgram.setMistType(gl4, mistType, skyboxProgramId);

		skybox.setProgram(skyboxProgram);
		skybox.init(drawable);
		timer = (float) (Math.random() * timerFactor);
		gl4.glEnable(GL4.GL_DEPTH_TEST);
		gl4.glDepthFunc(GL4.GL_LESS);
		gl4.glClearColor(0.8f, 0.9f, 1.0f, 1.0f);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		importedModel.dispose(drawable);
		skybox.dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		keyboardController.controlKeyboard();

		importedModel.viewMatrix = viewMatrix;
		importedModel.projectionMatrix = projectionMatrix;
		program.useProgram(gl4, state);
		projectionMatrix = importedModel.projectionMatrix;
		viewMatrix = importedModel.viewMatrix;
		timerController(gl4, timer);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		program.setViewMatrix(gl4, viewMatrix, programId);
		program.setMistType(gl4, mistType, programId);
		importedModel.display(drawable);

		skyboxProgram.useProgram(gl4, state);
		skyboxProgram.setProjectionMatrix(gl4, projectionMatrix, skyboxProgramId);
		skyboxProgram.setViewMatrix(gl4, viewMatrix, skyboxProgramId);
		skyboxProgram.setMistType(gl4, mistType, skyboxProgramId);
		skybox.display(drawable);
	}
	
	private void timerController(GL4 gl4,float timer) {
		if (timer <= 1.0f) {
			program.setDirLightAmbient(gl4, highAmbientLightning, programId);
			this.timer = (float) (Math.random() * timerFactor);
		} else {
			program.setDirLightAmbient(gl4, lowAmbientLightning, programId);
		}
		this.timer = this.timer - 0.2f;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 100.0f);
		program.setProjectionMatrix(gl4, projectionMatrix, programId);
		gl4.glViewport(0, 0, width, height);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gfx.Display.DisplayShutDownInterface#shutDown()
	 */
	@Override
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
		mistType = type;
	}

	@Override
	public float[] getLocalModelMatrix() {

		return importedModel.modelMatrix;
	}

	@Override
	public void setLocalModelMatrix(float[] modelMatrix) {

		this.importedModel.modelMatrix = modelMatrix;
	}

	@Override
	public float[] getViewMatrix() {

		return viewMatrix;
	}

	@Override
	public void setViewMatrix(float[] viewMatrix) {
		this.viewMatrix = viewMatrix;

	}

	@Override
	public float[] getProjectionMatrix() {
		// TODO Auto-generated method stub
		return projectionMatrix;
	}

	@Override
	public void setProjectionMatrix(float[] projectionMatrix) {
		this.projectionMatrix = projectionMatrix;

	}

}
