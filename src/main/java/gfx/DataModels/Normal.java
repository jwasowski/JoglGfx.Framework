package gfx.DataModels;
/** Object storing normal data as float array of x, y, z coordinates. */
public class Normal {

	public float[] normal = new float[3];

	public Normal(float x, float y, float z) {
		this.normal[0] = x;
		this.normal[1] = y;
		this.normal[2] = z;

	}
}
