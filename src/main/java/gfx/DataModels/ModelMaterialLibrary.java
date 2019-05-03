package gfx.DataModels;

import java.util.ArrayList;
import java.util.List;

import graphicslib3D.Material;

public class ModelMaterialLibrary {

	public List<TexturedMaterial> materials = new ArrayList<>();

	public Material findMaterial(String materialName) {
		for (TexturedMaterial tMaterial : materials) {
			if (tMaterial.materialName.equals(materialName)) {
				return new Material(tMaterial.getAmbient(), tMaterial.getDiffuse(), tMaterial.getSpecular(),
						tMaterial.getEmission(), tMaterial.getShininess());
			}
		}
		return new Material();
	}
}
