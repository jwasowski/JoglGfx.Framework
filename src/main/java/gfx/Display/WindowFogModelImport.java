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
	private KeyboardController keyboardController = new KeyboardController(importedModel, this);
	private MouseController mouseController = new MouseController(this, matrixService);

	private float[] pLocationRaw = new float[16];
	private Point3D pLocation;
	private float[] projectionMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] mist = { 0.3f, 0.3f, 0.3f, 1.0f, 3.0f, 60.0f, 0.05f };
	private int programId, skyboxProgramId, mistType = 3;
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
		matrixService.setupUnitMatrix(pLocationRaw);
		matrixService.translate(pLocationRaw, 0.0f, 1.5f, 4.0f);
		pLocation = new Point3D(pLocationRaw[0], pLocationRaw[5], pLocationRaw[10], pLocationRaw[15]);
		light.setPosition(pLocation);
		light.setAmbient(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });
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

		programId = program.initProgram(gl4);
		program.useProgram(gl4, state);
		program.setLight(gl4, light, programId);
		program.setMist(gl4, mist, programId);
		program.setMistType(gl4, mistType, programId);

		matrixService.setupUnitMatrix(projectionMatrix);
		matrixService.setupUnitMatrix(viewMatrix);
		matrixService.translate(viewMatrix, 0, -4, -10);
		matrixService.rotateAboutXAxis(viewMatrix, 15);
		System.out.println("ViewMatrix: "+Arrays.toString(viewMatrix));
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 250.0f);
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
		
		/*
		 * matrixService.rotateAboutYAxis(pLocationRaw, 0.15f);
		 * pLocation.setX(pLocationRaw[0]); pLocation.setY(pLocationRaw[5]);
		 * pLocation.setZ(pLocationRaw[10]); light.setPosition(new
		 * Point3D(pLocationRaw)); program.setLight(gl4, light, programId);
		 */
		importedModel.viewMatrix = viewMatrix;
		importedModel.projectionMatrix = projectionMatrix;
		program.useProgram(gl4, state);
		projectionMatrix = importedModel.projectionMatrix;
		viewMatrix = importedModel.viewMatrix;
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

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL4 gl4 = drawable.getGL().getGL4();
		projectionMatrix = matrixService.createProjectionMatrix(60, (float) width / (float) height, 0.1f, 250.0f);
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
