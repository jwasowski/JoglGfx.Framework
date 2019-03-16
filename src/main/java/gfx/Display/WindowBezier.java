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

import gfx.Scene.Objects.Bezier;
import gfx.Utilities.Shaders.ShaderProgramBezier;

public class WindowBezier implements GLEventListener, MouseListener, KeyListener {
	private GLWindow window;
	private FPSAnimator animator;

	private ShaderProgramBezier program = new ShaderProgramBezier();
	private int programId;
	Bezier bezier = new Bezier();
	int width, height;
	short inputFlag;
	float xCoord, yCoord;

	public WindowBezier(GLWindow windowPassed, FPSAnimator animatorPassed, int width, int height, String name) {
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
		programId = program.initProgram(gl4);
		bezier.setProgram(program);
		bezier.init(drawable);
		gl4.glEnable(GL4.GL_PROGRAM_POINT_SIZE);
		gl4.glPointSize(6);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		bezier.dispose(drawable);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL4 gl4 = drawable.getGL().getGL4();
		gl4.glClear(GL4.GL_COLOR_BUFFER_BIT);
		bezier.inputFlag = inputFlag;
		bezier.setXYCoords(xCoord, yCoord);
		bezier.display(drawable);
		inputFlag = 0;
		bezier.inputFlag = inputFlag;
		bezier.setXYCoords(0, 0);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		bezier.reshape(drawable, 0, 0, width, height);

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
	public void mouseClicked(MouseEvent e) {

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
		if (e.getButton() == MouseEvent.BUTTON1) {
			// when the button is released
			xCoord = e.getX();
			xCoord = xCoord * 2.0f / width - 1.0f;
			yCoord = e.getY();
			yCoord = -yCoord * 2.0f / height + 1.0f;
			inputFlag = 1;
			System.out.println("Mouse event- fired! X: " + xCoord + "Y: " + yCoord);
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			bezier.numPoints = 0;
		}
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
}
