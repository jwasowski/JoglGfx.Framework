package gfx.DataModels;

import java.util.ArrayList;
import java.util.List;

import graphicslib3D.Vertex3D;

public class ModelPart {
	public String partName;
	
	public List<Integer> vertIndicesData = new ArrayList<>();
	public List<Integer> textIndicesData = new ArrayList<>();
	public List<Integer> normIndicesData = new ArrayList<>();
	public List<VTN> vtnList = new  ArrayList<>();
	public List<MaterialName> materialLineData = new ArrayList<>();
	public int facesCounter;

	public ModelPart(String partName) {
		this.partName = partName;
	}
}
