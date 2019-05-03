package gfx.DataModels;

import graphicslib3D.Material;

public class TexturedMaterial extends Material {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6925994347595713979L;

	public String textureName = "";
	public String materialName;
	
	public TexturedMaterial(String materialName) {
		this.materialName = materialName;
	}
	
	
}
