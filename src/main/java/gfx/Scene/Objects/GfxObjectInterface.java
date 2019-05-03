package gfx.Scene.Objects;

import com.jogamp.opengl.GLAutoDrawable;

public interface GfxObjectInterface {

	void init(GLAutoDrawable drawable);

	void dispose(GLAutoDrawable drawable);

	void display(GLAutoDrawable drawable);

	void reshape(GLAutoDrawable drawable, int x, int y, int width, int height);

	void rotateXAxisUp();

	void rotateXAxisDown();

	void rotateYAxisLeft();

	void rotateYAxisRight();
	
	void moveForward();
	
	void moveBackwards();
	
	void moveLeft();
	
	void moveRight();
}