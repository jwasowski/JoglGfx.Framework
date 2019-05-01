package gfx.DataModels;

public class VTN {

	public Vertex vertex;
	public TextureUV textureUV;
	public Normal normal;
	
	public VTN (Vertex vertex, TextureUV textureUV, Normal normal) {
		this.vertex = vertex;
		this.textureUV = textureUV;
		this.normal = normal;
	}
}
