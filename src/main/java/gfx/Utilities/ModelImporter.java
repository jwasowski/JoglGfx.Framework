package gfx.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;

import gfx.DataModels.Model;
import gfx.DataModels.ModelMaterialLibrary;
import gfx.DataModels.ModelPart;
import gfx.DataModels.Normal;
import gfx.DataModels.TextureUV;
import gfx.DataModels.TexturedMaterial;
import gfx.DataModels.Vertex;
import graphicslib3D.Material;
import graphicslib3D.Vertex3D;

public class ModelImporter {
	/** Load model data from file including markers like: v, vt, vn, f, usemtl, mtlib. */
	public void loadModel(Model model, GL4 gl4) {
		try {
			File file = Paths.get(this.getClass().getResource("/Models/" + model.modelName).toURI()).toFile();
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			String line = "start";
			int verticesCounter = 0, normalsCounter = 0, texturesCounter = 0, facesCounter = 0, objectsCounter = 0,
					materialsCounter = 0, fileLineCounter = 0, currentPart = -1, indexOne = 1;
			String[] arrayHelper;
			float one = 1.0f;
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0L;

			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					if (line.startsWith("vn")) {
						arrayHelper = line.split("\\s");
						model.normalsData.add(new Normal(Float.parseFloat(arrayHelper[1]),
								Float.parseFloat(arrayHelper[2]), Float.parseFloat(arrayHelper[3])));

						normalsCounter++;
						fileLineCounter++;

					} else if (line.startsWith("vt")) {
						arrayHelper = line.split("\\s");
						model.texturesData
								.add(new TextureUV(Float.parseFloat(arrayHelper[1]), Float.parseFloat(arrayHelper[2])));

						texturesCounter++;
						fileLineCounter++;

					} else if (line.startsWith("f")) {
						arrayHelper = line.substring(2).split("\\s");
						for (String s : arrayHelper) {
							String v = s.split("/")[0];
							String vt = s.split("/")[1];
							String vn = s.split("/")[2];

							model.modelParts.get(currentPart).vertIndicesData.add(Integer.parseInt(v) - indexOne);
							model.modelParts.get(currentPart).textIndicesData.add(Integer.parseInt(vt) - indexOne);
							model.modelParts.get(currentPart).normIndicesData.add(Integer.parseInt(vn) - indexOne);

						}
						fileLineCounter++;
						facesCounter++;

					} else if (line.startsWith("v")) {
						arrayHelper = line.split("\\s");
						model.verticesData.add(new Vertex(Float.parseFloat(arrayHelper[1]),
								Float.parseFloat(arrayHelper[2]), Float.parseFloat(arrayHelper[3]), one));

						verticesCounter++;
						fileLineCounter++;

					} else if (line.startsWith("usemtl")) {
						if (currentPart >= 0) {
							model.modelParts.get(currentPart).facesCounter = facesCounter;
							facesCounter = 0;

						}

						currentPart++;
						arrayHelper = line.split("\\s");
						model.modelParts.add(new ModelPart(arrayHelper[1] + " Object"));

						model.modelParts.get(currentPart).materialName = arrayHelper[1];
						materialsCounter++;
						fileLineCounter++;

					} else if (line.startsWith("o")) {

						objectsCounter++;
						fileLineCounter++;
					} else if (line.startsWith("mtllib")) {

						arrayHelper = line.split("\\s");
						model.materialLibName = arrayHelper[1];
						fileLineCounter++;
					}
				}
			}
			model.modelParts.get(currentPart).facesCounter = facesCounter;
			elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println("Done!" + " MaterialLib: " + model.materialLibName + " Objects: " + objectsCounter
					+ " Materials: " + materialsCounter + " Vertices: " + verticesCounter + " Normals: "
					+ normalsCounter + " Textures: " + texturesCounter + " File lines: " + fileLineCounter);
			System.out.println("Vertices List size: " + model.verticesData.size() + " Normals List size: "
					+ model.normalsData.size() + " Textures List size: " + model.texturesData.size());
			System.out.println("Model Part 1 contains indice 1: "
					+ model.modelParts.get(0).vertIndicesData.contains(new Integer(1))
					+ " ,Model Part 2 contains indice 1: "
					+ model.modelParts.get(1).vertIndicesData.contains(new Integer(1))
					+ " ,Model Part 3 contains indice 1: "
					+ model.modelParts.get(2).vertIndicesData.contains(new Integer(1)));
			StringBuilder builder = new StringBuilder("");
			for (int i = 0; i < model.modelParts.size(); i++) {
				for (Integer n : model.modelParts.get(i).vertIndicesData) {
					if (n < 0) {

						builder.append(", " + n);
					}
				}
				System.out.println("Model part " + i + " values: " + builder.toString());
			}
			System.out.println("Model data import completed in: " + elapsedTime + " ms");

			buffer.close();

		} catch (URISyntaxException | IOException e) {
			System.out.println("File not found or IO error!");
			e.printStackTrace();
		}

	}
	/** Load materials from file including: shininess, ambient, diffuse, specular and texture name data.*/
	public void loadMaterials(ModelMaterialLibrary lib, String libName) {
		File file;
		try {
			file = Paths.get(this.getClass().getResource("/Materials/" + libName).toURI()).toFile();
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			String line = "start";
			String[] arrayHelper;
			int currentMaterial = -1;
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0L;

			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					if (line.startsWith("newmtl")) {
						arrayHelper = line.split("\\s");
						lib.materials.add(new TexturedMaterial(arrayHelper[1]));
						currentMaterial++;
					} else if (line.startsWith("Ns")) {
						arrayHelper = line.split("\\s");
						lib.materials.get(currentMaterial).setShininess(Float.parseFloat(arrayHelper[1]));

					} else if (line.startsWith("Ka")) {
						arrayHelper = line.split("\\s");
						lib.materials.get(currentMaterial).setAmbient(new float[] { Float.parseFloat(arrayHelper[1]),
								Float.parseFloat(arrayHelper[2]), Float.parseFloat(arrayHelper[3]), 1.0f });

					} else if (line.startsWith("Kd")) {
						arrayHelper = line.split("\\s");
						lib.materials.get(currentMaterial).setDiffuse(new float[] { Float.parseFloat(arrayHelper[1]),
								Float.parseFloat(arrayHelper[2]), Float.parseFloat(arrayHelper[3]), 1.0f });

					} else if (line.startsWith("Ks")) {
						arrayHelper = line.split("\\s");
						lib.materials.get(currentMaterial).setSpecular(new float[] { Float.parseFloat(arrayHelper[1]),
								Float.parseFloat(arrayHelper[2]), Float.parseFloat(arrayHelper[3]), 1.0f });

					} else if (line.startsWith("Ke")) {
						arrayHelper = line.split("\\s");
						lib.materials.get(currentMaterial).setEmission(new float[] { Float.parseFloat(arrayHelper[1]),
								Float.parseFloat(arrayHelper[2]), Float.parseFloat(arrayHelper[3]), 1.0f });
					} else if (line.startsWith("map_Kd")) {
						arrayHelper = line.split("\\s");
						arrayHelper = arrayHelper[arrayHelper.length - 1].split("\\" + "\\");
						lib.materials.get(currentMaterial).textureName = arrayHelper[arrayHelper.length - 1]; // textureName
						System.out.println("Texture name "+currentMaterial+" : "+lib.materials.get(currentMaterial).textureName);
						// TODO textureDiffuse
					}
				}
			}
			buffer.close();
			elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println("Number of materials: " + currentMaterial);
			System.out.println("MaterialLib import completed in: " + elapsedTime + " ms");
		} catch (URISyntaxException | IOException e) {
			System.out.println("File not found or IO error!");
			e.printStackTrace();
		}

	}
	/** For future use */
	public void loadTextures(TextureLoader textureLoader, ModelPart modelPart, ModelMaterialLibrary materialsLib) {
		String textureName = materialsLib.findTextureName(modelPart.materialName);
		if(!textureName.equals("") && !textureName.equals(".")) {
		Texture texture = textureLoader.LoadTexture(textureName);
		modelPart.textureId = texture.getTextureObject();
		}
	}

}
