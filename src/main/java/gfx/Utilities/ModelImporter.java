package gfx.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.GL4;

import gfx.DataModels.IndicesData;
import gfx.DataModels.ModelPart;

public class ModelImporter {
//TODO Change implementation to read dynamically and use data without storing 
	public void loadModel(String modelName, GL4 gl4) {
		// TODO Implement and test
		try {
			File file = Paths.get(this.getClass().getResource("/Models/" + modelName).toURI()).toFile();
			FileReader fr = new FileReader(file);
			BufferedReader buffer = new BufferedReader(fr);
			String line = "start", materialLibName = "";
			int currentModelPartIndex = -1;
			int currentMaterialIndex = -1;
			//int currentFacesLineIndex = 0;
			int verticesCounter = 0, normalsCounter = 0, texturesCounter = 0, facesCounter = 0, objectsCounter =0, materialsCounter =0, fileLineCounter=0;
			StringBuilder stringParser = new StringBuilder();
			String[] arrayHelper;
			//TODO Tweak size
			double[] doubleArrayHelper = new double[128];
			List<Double> verticesData = new ArrayList<>();
			List<Double> normalsData = new ArrayList<>();
			List<Double> texturesData = new ArrayList<>();
			List<Integer> materialLineData = new ArrayList<>();
			String lastLineHelper = "";
			

			while (line != null) {
					line = buffer.readLine();
				if(line != null) {	
				if (line.startsWith("vn")) {
					//System.out.print(" |VN| ");
					arrayHelper = line.split("\\s");
					normalsData.add(Double.parseDouble(arrayHelper[1]));
					normalsData.add(Double.parseDouble(arrayHelper[2]));
					normalsData.add(Double.parseDouble(arrayHelper[3]));
					normalsCounter++;
					fileLineCounter++;
					
				} else if (line.startsWith("vt")) {
					arrayHelper = line.split("\\s");
					texturesData.add(Double.parseDouble(arrayHelper[1]));
					texturesData.add(Double.parseDouble(arrayHelper[2]));
					texturesCounter++;
					fileLineCounter++;
					//System.out.print(" |VT| ");

				} else if (line.startsWith("f")) {
					arrayHelper = line.split("\\s");
					
					fileLineCounter++;
					facesCounter++;
					//System.out.print(" |F| ");
						
				}
				 else if (line.startsWith("v")) {
					arrayHelper = line.split("\\s");
					verticesData.add(Double.parseDouble(arrayHelper[1]));
					verticesData.add(Double.parseDouble(arrayHelper[2]));
					verticesData.add(Double.parseDouble(arrayHelper[3]));
					//System.out.print(" |V| ");
					verticesCounter++;
					fileLineCounter++;

				} else if (line.startsWith("usemtl")) {
					arrayHelper = line.split("\\s");
					//System.out.println(" |UMTL| ");
					materialLineData.add(fileLineCounter);
					materialsCounter++;
					fileLineCounter++;

				}  else if (line.startsWith("o")){
					//System.out.println(" |O| ");
					objectsCounter++;
					fileLineCounter++;
				}
				else if(line.startsWith("mtllib")) {
					System.out.println(" |MTLIB| ");
					arrayHelper = line.split("\\s");
					materialLibName = arrayHelper[1];
					fileLineCounter++;
				} 
				}
			}
			System.out.println("Done!"+" MaterialLib: "+materialLibName+" Objects:"+objectsCounter+" Materials: "+materialsCounter+" Vertices: "+verticesCounter+
					" Normals: "+normalsCounter+" Textures: "+texturesCounter+" Faces: "+facesCounter+" File lines: "+fileLineCounter);
			System.out.println("Vertices List size: "+verticesData.size()+" Normals List size: "+normalsData.size()+
					" Textures List size: "+texturesData.size());
			System.out.println("Material changed lines: "+Arrays.toString(materialLineData.toArray()));
			/*verticesData.clear();
			normalsData.clear();*/
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
