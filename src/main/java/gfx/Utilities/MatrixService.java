package gfx.Utilities;

import java.util.Arrays;

import com.jogamp.opengl.math.FloatUtil;

public class MatrixService {
	FloatUtil mathService = new FloatUtil();
	private float[] scaleMatrix = new float[16];
	private float[] scaleMatrix3x3 = new float[16];
	private float[] rotationMatrix = new float[16];
	private float[] rotationMatrix3x3 = new float[9];
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

	// TODO Implement methods for Matrix 3x3

	public void scale(float[] matrix, float x, float y, float z) {
		setupUnitMatrix(scaleMatrix);
		scaleMatrix[0] = x;
		scaleMatrix[5] = y;
		scaleMatrix[10] = z;
		multiplyBy(matrix, scaleMatrix);
	}

	public void scale3x3(float[] matrix, float x, float y, float z) {
		setupUnitMatrix3x3(scaleMatrix3x3);
		scaleMatrix[0] = x;
		scaleMatrix[4] = y;
		scaleMatrix[8] = z;
		multiplyBy3x3(matrix, scaleMatrix3x3);
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
	
	public void setupUnitMatrix3x3(float[] matrix) {
		for (int i = 0; i < 9; i++)
			matrix[i] = 0;
		matrix[0] = matrix[4] = matrix[8] = 1;
	}

	public void rotateAboutXAxis3x3(float[] matrix, float degrees) {
		setupUnitMatrix3x3(rotationMatrix3x3);
		float radians = (float) (degrees * Math.PI / 180.0f);
		float sine = (float) Math.sin(radians);
		float cosine = (float) Math.cos(radians);
		rotationMatrix3x3[4] = cosine;
		rotationMatrix3x3[5] = sine;
		rotationMatrix3x3[7] = -sine;
		rotationMatrix3x3[8] = cosine;
		multiplyBy3x3(matrix, rotationMatrix3x3);
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

	public void rotateAboutYAxis3x3(float[] matrix, float degrees) {
		setupUnitMatrix3x3(rotationMatrix3x3);
		float radians = (float) (degrees * Math.PI / 180.0f);
		float sine = (float) Math.sin(radians);
		float cosine = (float) Math.cos(radians);
		rotationMatrix3x3[0] = cosine;
		rotationMatrix3x3[2] = -sine;
		rotationMatrix3x3[6] = sine;
		rotationMatrix3x3[8] = cosine;
		multiplyBy3x3(matrix, rotationMatrix3x3);
	}

	public float[] multiplyBy3x3(float[] matrixA, float[] matrixB) {
		float[] new_matrix = new float[9];
		int row, column, row_offset;
		for (row = 0, row_offset = row * 3; row < 3; ++row, row_offset = row * 3) {
			for (column = 0; column < 3; ++column) {
				new_matrix[row_offset + column] = 
						(matrixB[row_offset + 0] * matrixA[column + 0])
						+ (matrixB[row_offset + 1] * matrixA[column + 3])
						+ (matrixB[row_offset + 2] * matrixA[column + 6]);
			}
		}
		for (int i = 0; i < 9; i++)
			matrixA[i] = new_matrix[i];
		return matrixA;
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

	public void rotateAboutZAxis3x3(float[] matrix, float degrees) {
		setupUnitMatrix3x3(rotationMatrix3x3);
		float radians = (float) (degrees * Math.PI / 180.0f);
		float sine = (float) Math.sin(radians);
		float cosine = (float) Math.cos(radians);
		rotationMatrix3x3[0] = cosine;
		rotationMatrix3x3[1] = sine;
		rotationMatrix3x3[3] = -sine;
		rotationMatrix3x3[4] = cosine;
		multiplyBy3x3(matrix, rotationMatrix3x3);
	}

	public float[] createOrthoMatrix(float l, float r, float b, float t, float n, float f) {
		float[] matrix = new float[16];
		matrix[0] = (2 / (r - l));
		matrix[5] = (2 / (t - b));
		matrix[10] = -(2 / (f - n));
		matrix[12] = -((r + l) / (r - l));
		matrix[13] = -((t + b) / (t - b));
		matrix[14] = -((f + n) / (f - n));
		matrix[15] = 1;
		return matrix;
	}
}
