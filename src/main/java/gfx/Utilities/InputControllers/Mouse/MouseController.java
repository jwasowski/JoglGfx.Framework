package gfx.Utilities.InputControllers.Mouse;

import java.util.Arrays;

import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.glu.GLU;

import gfx.Display.DisplayInterface;
import gfx.Display.WindowBezierPatch;
import gfx.Utilities.MatrixService;
/** */
public class MouseController implements MouseListener {
	private int prevMouseX, prevMouseY;
	public int[] mouseCoords = new int[3];
	private float[] viewMatrix;
	private float angleX,angleY;
	private DisplayInterface display;
	private MatrixService matrixService = new MatrixService();


	public MouseController(DisplayInterface display){
		mouseCoords[0]=0;
		mouseCoords[1]=0;
		mouseCoords[2]=0;
		this.display = display;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		

	}

	@Override
	public void mouseExited(MouseEvent e) {
		

	}

	@Override
	public void mousePressed(MouseEvent e) {
		prevMouseX = e.getX();
		prevMouseY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("Mouse coords X: "+e.getX()+" Y: "+e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseCoords[0]=e.getX();
		mouseCoords[1]=e.getY();
		int x = e.getX();		//get current mouse loc
		int y = e.getY();
		//Dimension size = new Dimension(width, height) ;  //get size of frame
		
		Dimension size = new Dimension(1280, 720) ;
		if(prevMouseX>= 0 && prevMouseY>=0) {
		angleX=(prevMouseY-y)/2;
		angleY=(prevMouseX-x)/2;
		
		
		prevMouseX = x;		//save current mouse loc for next drag
		prevMouseY = y;

		
		
		viewMatrix = display.getViewMatrix();
		//System.out.println("ViewMatrix coords X: "+viewMatrix[0]+" Y: "+viewMatrix[5]+" Z: "+viewMatrix[10]);
		matrixService.rotateAboutXAxis(viewMatrix, angleX);
		matrixService.rotateAboutYAxis(viewMatrix, angleY);
		//System.out.println("ViewMatrix coords: "+Arrays.toString(viewMatrix));
		display.setViewMatrix(viewMatrix);}
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		

	}

}
