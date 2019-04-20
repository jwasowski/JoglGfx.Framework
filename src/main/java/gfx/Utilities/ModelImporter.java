package gfx.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import gfx.DataModels.IndicesData;
import gfx.DataModels.ModelPart;

public class ModelImporter {
//TODO Change implementation to read dynamically and use data without storing 
	public void loadModel(String modelName, List<ModelPart> modelParts) {
		// TODO Implement and test
		try {
			File file = Paths.get(this.getClass().getResource("/Models/" + modelName).toURI()).toFile();
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			String line = "start";
			int currentModelPartIndex = -1;
			int currentMaterialIndex = -1;
			//int currentFacesLineIndex = 0;
			StringBuilder stringParser = new StringBuilder();
			String[] arrayHelper;
			//TODO Tweak size
			double[] doubleArrayHelper = new double[128];
			String lastLineHelper = "";
			

			while (line != null) {
				if (lastLineHelper == "") {
					line = buffer.readLine();
				}
				
			}
			System.out.println("Done");
			buffer.close();

		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double[] parseString(double[] doubleArray, String[] array) {
		for (int i = 1; i < array.length; i++)
			doubleArray[i-1] = Double.parseDouble(array[i]);
		return doubleArray;
	}
	//TODO Implement and test
	private double[] parseFacesData(String[] stringArr, double[] data) {
		String[] arrayHelper;
		int j = 0;
		for(int i = 1; i < stringArr.length;i++) {
			
			arrayHelper = stringArr[1].split("\\/\\/");
			data[j] = Double.parseDouble(arrayHelper[0]);
			data[j+1] = Double.parseDouble(arrayHelper[1]);
			arrayHelper = stringArr[2].split("\\/\\/");
			data[j+2] = Double.parseDouble(arrayHelper[0]);
			data[j+3] = Double.parseDouble(arrayHelper[1]);
			arrayHelper = stringArr[3].split("\\/\\/");
			data[j+4] = Double.parseDouble(arrayHelper[0]);
			data[j+5] = Double.parseDouble(arrayHelper[1]);
			j=j+6;	
		}
		return data;
	}
	
	/*
	lastLineHelper = "";
	if (line.startsWith("v")) {
		if (line.startsWith(" ", 1)) {
			arrayHelper = line.split("\\s");
			modelParts.get(currentModelPartIndex).vertexData
					.add(parseString(doubleArrayHelper, arrayHelper));
		} else if (line.startsWith("t", 1)) {
			arrayHelper = line.split("\\s");
			modelParts.get(currentModelPartIndex).textureData
					.add(parseString(doubleArrayHelper, arrayHelper));
		} else if (line.startsWith("n", 1)) {
			arrayHelper = line.split("\\s");
			modelParts.get(currentModelPartIndex).normalsData
					.add(parseString(doubleArrayHelper, arrayHelper));
		} else {
			System.out.println("Wrong line content");
		}

	} else if (line.startsWith("usemtl")) {
		arrayHelper = line.split("\\s");
		modelParts.get(currentModelPartIndex).indicesData.add(new IndicesData());
		currentMaterialIndex++;
		
		modelParts.get(currentModelPartIndex).indicesData
				.get(currentMaterialIndex).materialName = arrayHelper[1];
		line = buffer.readLine();
		while (line.startsWith("f")) {
			//currentFacesLineIndex++;
			
				arrayHelper = line.split("\\s");
				doubleArrayHelper = parseFacesData(arrayHelper, doubleArrayHelper);
				for(int i=0; i<doubleArrayHelper.length;i=i+2) {
					modelParts.get(currentModelPartIndex).indicesData
					.get(currentMaterialIndex).vertexIndicesData.add(doubleArrayHelper[i]);
					modelParts.get(currentModelPartIndex).indicesData
					.get(currentMaterialIndex).normalsIndicesData.add(doubleArrayHelper[i+1]);
				}
			 

		}
		lastLineHelper = line;

	}  else if (line.startsWith("o") || line.startsWith("mtllib")) {
		modelParts.add(new ModelPart());
		currentModelPartIndex++;
		arrayHelper = line.split("\\s");
		modelParts.get(currentModelPartIndex).materialLibName = arrayHelper[1];
		currentMaterialIndex = -1;
	} */
}
