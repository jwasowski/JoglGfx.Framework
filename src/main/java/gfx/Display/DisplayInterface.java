package gfx.Display;

public interface DisplayInterface {

	void shutDown();
	void setMistType(int type);
	public float[] getViewMatrix();
	public void setViewMatrix(float[] viewMatrix);
}