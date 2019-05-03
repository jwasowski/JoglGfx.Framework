package gfx.DataModels;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

import graphicslib3D.Vertex3D;

public class Model {

	public List<Vertex> verticesData = new ArrayList<>();
	public List<Normal> normalsData = new ArrayList<>();
	public List<TextureUV> texturesData = new ArrayList<>();
	public List<ModelPart> modelParts = new ArrayList<>();
	public String materialLibName;
	public String modelName;
	public FloatBuffer vtnBuffer;

	public Model(String modelName) {
		this.modelName = modelName;
	}

	// TODO Verify and test method
	public void generateVertexTextureNormal(ModelPart modelParts, GL4 gl4) {
		if (modelParts.vertIndicesData.size() == modelParts.textIndicesData.size()
				&& modelParts.vertIndicesData.size() == modelParts.normIndicesData.size()) {
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0L;
			for (int i = 0; i < modelParts.vertIndicesData.size(); i++) {
				modelParts.vtnList.add(new VTN(verticesData.get(modelParts.vertIndicesData.get(i)),
						texturesData.get(modelParts.textIndicesData.get(i)),
						normalsData.get(modelParts.normIndicesData.get(i))));	
			}
			gl4.glGenVertexArrays(1, modelParts.vertexArrayObject, 0);
			gl4.glBindVertexArray(modelParts.vertexArrayObject[0]);
			
			gl4.glEnableVertexAttribArray(0);
			gl4.glEnableVertexAttribArray(2);
			gl4.glEnableVertexAttribArray(3);
			
			gl4.glGenBuffers(modelParts.vertexBufferObject.length, modelParts.vertexBufferObject, 0);
			gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, modelParts.vertexBufferObject[0]);
			
			List<Float> vtnDataCombined = new ArrayList<>();
			
			for(VTN vtn : modelParts.vtnList) {
				vtnDataCombined.add(vtn.vertex.position[0]);
				vtnDataCombined.add(vtn.vertex.position[1]);
				vtnDataCombined.add(vtn.vertex.position[2]);
				vtnDataCombined.add(vtn.vertex.position[3]);
				vtnDataCombined.add(vtn.textureUV.textureCoordinates[0]);
				vtnDataCombined.add(vtn.textureUV.textureCoordinates[1]);
				vtnDataCombined.add(vtn.normal.normal[0]);
				vtnDataCombined.add(vtn.normal.normal[1]);
				vtnDataCombined.add(vtn.normal.normal[2]);
				
			}
			float[] vtnDataRaw = ArrayUtils.toPrimitive(vtnDataCombined.toArray(new Float[0]), 0.0F);
			vtnBuffer = GLBuffers.newDirectFloatBuffer(vtnDataRaw);
			final int stride = 9 * Buffers.SIZEOF_FLOAT;
			final long textureOffset = 4 * Buffers.SIZEOF_FLOAT;
			final long normalOffset = 6 * Buffers.SIZEOF_FLOAT;
			gl4.glBufferData(GL4.GL_ARRAY_BUFFER, vtnBuffer.limit()*Buffers.SIZEOF_FLOAT, vtnBuffer, GL4.GL_STATIC_DRAW);
						
			gl4.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, stride, 0);				
			gl4.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false, stride, textureOffset);	
			gl4.glVertexAttribPointer(3, 3, GL4.GL_FLOAT, false, stride, normalOffset);
						
			gl4.glBindVertexArray(0);
			elapsedTime = System.currentTimeMillis() - startTime;
			System.out.println("Generation completed in: " + elapsedTime + " ms");
			System.out.println("Generated list size: "+modelParts.vtnList.size()+" Generated list faces: "+modelParts.vtnList.size()/3);
			
		}
	}
}
