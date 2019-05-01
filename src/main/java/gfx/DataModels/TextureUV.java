package gfx.DataModels;

public class TextureUV {

	public float[] textureCoordinates = new float[2];

	public TextureUV(float x, float y) {
		this.textureCoordinates[0] = x;
		this.textureCoordinates[1] = y;

	}
}
