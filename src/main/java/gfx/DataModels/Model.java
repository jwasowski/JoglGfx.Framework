package gfx.DataModels;

import java.util.ArrayList;
import java.util.List;

import graphicslib3D.Vertex3D;

public class Model {

	public List<Vertex> verticesData = new ArrayList<>();
	public List<Normal> normalsData = new ArrayList<>();
	public List<TextureUV> texturesData = new ArrayList<>();
	public List<ModelPart> modelParts = new ArrayList<>();
	public String materialLibName;
	public String modelName;

	public Model(String modelName) {
		this.modelName = modelName;
	}

	// TODO Generate vertices for rendering, point at Data via modelParts indices
	// data
	public void generateVertexTextureNormal(ModelPart modelParts) {
		if (modelParts.vertIndicesData.size() == modelParts.textIndicesData.size()
				&& modelParts.vertIndicesData.size() == modelParts.normIndicesData.size()) {
			for (int i = 0; i < modelParts.vertIndicesData.size(); i++) {
				modelParts.vtnList.add(new VTN(verticesData.get(modelParts.vertIndicesData.get(i)),
						texturesData.get(modelParts.textIndicesData.get(i)),
						normalsData.get(modelParts.normIndicesData.get(i))));
				/*modelParts.vtnList.add(new VTN(verticesData.get(modelParts.vertIndicesData.get(i+1)),
						texturesData.get(modelParts.textIndicesData.get(i+1)),
						normalsData.get(modelParts.normIndicesData.get(i+1))));
				modelParts.vtnList.add(new VTN(verticesData.get(modelParts.vertIndicesData.get(i+2)),
						texturesData.get(modelParts.textIndicesData.get(i+2)),
						normalsData.get(modelParts.normIndicesData.get(i+2))));*/
			}
		}
	}
}
