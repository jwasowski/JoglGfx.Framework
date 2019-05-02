package gfx.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL4;

import gfx.DataModels.MaterialName;
import gfx.DataModels.Model;
import gfx.DataModels.ModelPart;
import gfx.DataModels.Normal;
import gfx.DataModels.TextureUV;
import gfx.DataModels.Vertex;
import graphicslib3D.Vertex3D;

public class ModelImporter {
 
	public void loadModel(Model model, GL4 gl4) {
		// TODO Implement and test
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
			System.out.println("Model Part 1 faces: " + model.modelParts.get(0).facesCounter + " ,Model Part 2 faces: "
					+ model.modelParts.get(1).facesCounter + " ,Model Part 3 faces: "
					+ model.modelParts.get(2).facesCounter /*+ " ,Model Part 4 faces: "
					+ model.modelParts.get(3).facesCounter + " ,Model Part 5 faces: "
					+ model.modelParts.get(4).facesCounter + " ,Model Part 6 faces: "
					+ model.modelParts.get(5).facesCounter)*/);
			System.out.println("Model Part 1 indices size: " + model.modelParts.get(0).vertIndicesData.size()
					+ " ,Model Part 2 indices size: " + model.modelParts.get(1).vertIndicesData.size()
					+ " ,Model Part 3 indices size: " + model.modelParts.get(2).vertIndicesData.size()
					/*+ " ,Model Part 4 indices size: " + model.modelParts.get(3).vertIndicesData.size()
					+ " ,Model Part 5 indices size: " + model.modelParts.get(4).vertIndicesData.size()
					+ " ,Model Part 6 indices size: " + model.modelParts.get(5).vertIndicesData.size())*/);
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
			System.out.println("Import completed in: " + elapsedTime + " ms");

			buffer.close();

		} catch (URISyntaxException | IOException e) {
			
			e.printStackTrace();
		}

	}

	
}
