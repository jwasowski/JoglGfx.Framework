package gfx.DataModels;

import java.util.ArrayList;
import java.util.List;

public class Model {
	public List<Float> verticesData = new ArrayList<>();
	public List<Float> normalsData = new ArrayList<>();
	public List<Float> texturesData = new ArrayList<>();
	public List<MaterialName> materialLineData = new ArrayList<>();
	public String materialLibName;
	public String modelName;
	public int facesCounter;
	
	public Model(String modelName) {
		this.modelName = modelName;
	}
}
