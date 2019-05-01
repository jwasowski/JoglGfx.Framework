package gfx.DataModels;

public class Vertex {

	public float[] position = new float[4];
	
	public Vertex(float x, float y, float z, float w) {
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = z;
		this.position[3] = w;
	}
}
