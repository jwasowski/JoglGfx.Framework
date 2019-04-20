package gfx.DataModels;

import java.util.ArrayList;
import java.util.List;

public class ModelPart {
	/** Vertices vectors. */
	public List<double[]> vertexData = new ArrayList<>();
	/** Normal vectors. */
	public List<double[]> normalsData = new ArrayList<>();
	/** Texture vectors. */
	public List<double[]> textureData = new ArrayList<>();
	/** Indices (faces) associated with Material and Smooth Flag. */
	public List<IndicesData> indicesData = new ArrayList<>();
	/** Material library name to read from in File system. */
	public String materialLibName;
	
	
}
