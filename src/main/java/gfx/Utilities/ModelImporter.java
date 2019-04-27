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

public class ModelImporter {
//TODO Change implementation to read dynamically and use data without storing 
	public void loadModel(Model model, GL4 gl4) {
		// TODO Implement and test
		try {
			File file = Paths.get(this.getClass().getResource("/Models/" + model.modelName).toURI()).toFile();
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			String line = "start";
			int verticesCounter = 0, normalsCounter = 0, texturesCounter = 0, facesCounter = 0, objectsCounter = 0,
					materialsCounter = 0, fileLineCounter = 0;
			String[] arrayHelper;
			float one = 1.0f;
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0L;

			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					if (line.startsWith("vn")) {
						arrayHelper = line.split("\\s");
						model.normalsData.add(Float.parseFloat(arrayHelper[1]));
						model.normalsData.add(Float.parseFloat(arrayHelper[2]));
						model.normalsData.add(Float.parseFloat(arrayHelper[3]));
						normalsCounter++;
						fileLineCounter++;

					} else if (line.startsWith("vt")) {
						arrayHelper = line.split("\\s");
						model.texturesData.add(Float.parseFloat(arrayHelper[1]));
						model.texturesData.add(Float.parseFloat(arrayHelper[2]));
						texturesCounter++;
						fileLineCounter++;

					} else if (line.startsWith("f")) {

						fileLineCounter++;
						facesCounter++;

					} else if (line.startsWith("v")) {
						arrayHelper = line.split("\\s");
						model.verticesData.add(Float.parseFloat(arrayHelper[1]));
						model.verticesData.add(Float.parseFloat(arrayHelper[2]));
						model.verticesData.add(Float.parseFloat(arrayHelper[3]));
						model.verticesData.add(one);
						verticesCounter++;
						fileLineCounter++;

					} else if (line.startsWith("usemtl")) {
						arrayHelper = line.split("\\s");

						model.materialLineData.add(new MaterialName(fileLineCounter, arrayHelper[1]));
						materialsCounter++;
						fileLineCounter++;

					} else if (line.startsWith("o")) {

						objectsCounter++;
						fileLineCounter++;
					} else if (line.startsWith("mtllib")) {
						System.out.println(" |MTLIB| ");
						arrayHelper = line.split("\\s");
						model.materialLibName = arrayHelper[1];
						fileLineCounter++;
					}
				}
			}
			model.facesCounter = facesCounter;
			elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println("Done!" + " MaterialLib: " + model.materialLibName + " Objects: " + objectsCounter
					+ " Materials: " + materialsCounter + " Vertices: " + verticesCounter + " Normals: "
					+ normalsCounter + " Textures: " + texturesCounter + " Faces: " + facesCounter + " File lines: "
					+ fileLineCounter);
			System.out.println("Vertices List size: " + model.verticesData.size() + " Normals List size: "
					+ model.normalsData.size() + " Textures List size: " + model.texturesData.size());
			System.out.print("Material names used in file: ");
			model.materialLineData.forEach(
					materialName -> System.out.print(materialName.name + " @line: " + materialName.lineNumber + ", "));
			System.out.println();
			System.out.println("Import completed in: " + elapsedTime + " ms");
			
			buffer.close();

		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double[] parseString(double[] doubleArray, String[] array) {
		for (int i = 1; i < array.length; i++)
			doubleArray[i - 1] = Double.parseDouble(array[i]);
		return doubleArray;
	}

	// TODO Implement and test
	private double[] parseFacesData(String[] stringArr, double[] data) {
		String[] arrayHelper;
		int j = 0;
		for (int i = 1; i < stringArr.length; i++) {

			arrayHelper = stringArr[1].split("\\/\\/");
			data[j] = Double.parseDouble(arrayHelper[0]);
			data[j + 1] = Double.parseDouble(arrayHelper[1]);
			arrayHelper = stringArr[2].split("\\/\\/");
			data[j + 2] = Double.parseDouble(arrayHelper[0]);
			data[j + 3] = Double.parseDouble(arrayHelper[1]);
			arrayHelper = stringArr[3].split("\\/\\/");
			data[j + 4] = Double.parseDouble(arrayHelper[0]);
			data[j + 5] = Double.parseDouble(arrayHelper[1]);
			j = j + 6;
		}
		return data;
	}
}
