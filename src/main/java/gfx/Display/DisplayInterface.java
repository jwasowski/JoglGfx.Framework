package gfx.Display;

public interface DisplayInterface {

	void shutDown();
	void setMistType(int type);
	public float[] getLocalModelMatrix();
	public void setLocalModelMatrix(float[] modelMatrix);
	public float[] getViewMatrix();
	public void setViewMatrix(float[] viewMatrix);
	public float[] getProjectionMatrix();
	public void setProjectionMatrix(float[] projectionMatrix);
}