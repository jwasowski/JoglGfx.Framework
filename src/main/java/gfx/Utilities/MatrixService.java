package gfx.Utilities;

import com.jogamp.opengl.math.FloatUtil;

public class MatrixService {
	FloatUtil mathService = new FloatUtil();
	private float[] scaleMatrix = new float[16];
	private float[] rotationMatrix = new float[16];
	private float[] translationMatrix = new float[16];

	public void setupUnitMatrix(float[] matrix) {
		FloatUtil.makeIdentity(matrix);
	}

	public float[] createProjectionMatrix(float fovY, float aspectRatio, float nearPlane, float farPlane) {
		float[] matrix = new float[16];
		float yScale = (float) (1.0 / Math.tan(fovY * Math.PI / 360.0));
		float xScale = yScale / aspectRatio;
		float frustumLength = farPlane - nearPlane;

		matrix[0] = xScale;
		matrix[5] = yScale;
		matrix[10] = -((farPlane + nearPlane) / frustumLength);
		matrix[11] = -1;
		matrix[14] = -((2 * nearPlane * farPlane) / frustumLength);
		return matrix;
	}

	public float[] multiplyBy(float[] matrixA, float[] matrixB) {
		return FloatUtil.multMatrix(matrixA, matrixB);
	}

	public void scale(float[] matrix, float x, float y, float z) {
		setupUnitMatrix(scaleMatrix);
		scaleMatrix[0] = x;
		scaleMatrix[5] = y;
		scaleMatrix[10] = z;
		multiplyBy(matrix, scaleMatrix);
	}

	public void translate(float[] matrix, float x, float y, float z) {
		setupUnitMatrix(translationMatrix);
		translationMatrix[12] = x;
		translationMatrix[13] = y;
		translationMatrix[14] = z;
		multiplyBy(matrix, translationMatrix);
	}

	public void rotateAboutXAxis(float[] matrix, float degrees) {
		setupUnitMatrix(rotationMatrix);
		float radians = (float) (degrees * Math.PI / 180.0f);
		float sine = (float) Math.sin(radians);
		float cosine = (float) Math.cos(radians);
		rotationMatrix[5] = cosine;
		rotationMatrix[6] = sine;
		rotationMatrix[9] = -sine;
		rotationMatrix[10] = cosine;

		multiplyBy(matrix, rotationMatrix);
	}

	public void rotateAboutYAxis(float[] matrix, float degrees) {
		setupUnitMatrix(rotationMatrix);
		float radians = (float) (degrees * Math.PI / 180.0f);
		float sine = (float) Math.sin(radians);
		float cosine = (float) Math.cos(radians);
		rotationMatrix[0] = cosine;
		rotationMatrix[2] = -sine;
		rotationMatrix[8] = sine;
		rotationMatrix[10] = cosine;

		multiplyBy(matrix, rotationMatrix);
	}

	public void rotateAboutZAxis(float[] matrix, float degrees) {
		setupUnitMatrix(rotationMatrix);
		float radians = (float) (degrees * Math.PI / 180.0f);
		float sine = (float) Math.sin(radians);
		float cosine = (float) Math.cos(radians);
		rotationMatrix[0] = cosine;
		rotationMatrix[1] = sine;
		rotationMatrix[4] = -sine;
		rotationMatrix[5] = cosine;

		multiplyBy(matrix, rotationMatrix);
	}

	public float[] createOrthoMatrix(float l, float r, float b, float t, float n, float f) {
		float[] matrix = new float[16];
		matrix[0] = (2/(r-l));
		matrix[5] = (2/(t-b));
		matrix[10] = -(2/(f-n));
		matrix[12] = -((r+l)/(r-l));
		matrix[13] = -((t+b)/(t-b));
		matrix[14] = -((f+n)/(f-n)); 
		matrix[15] = 1;
		return matrix;
	}
}
