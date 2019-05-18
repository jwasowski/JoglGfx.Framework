package gfx.Utilities.InputControllers.Mouse;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import gfx.Display.DisplayInterface;
import gfx.Utilities.MatrixService;
/** Primitive mouse controller.*/
public class MouseController implements MouseListener{
	private int prevMouseX, prevMouseY;
	public int[] mouseCoords = new int[3];
	private float[] viewMatrix;
	float[] projectionMatrix;
	private float angleX,angleY;
	private DisplayInterface display;
	private MatrixService matrixService;


	public MouseController(DisplayInterface display, MatrixService matrixService){
		mouseCoords[0]=0;
		mouseCoords[1]=0;
		mouseCoords[2]=0;
		this.display = display;
		this.matrixService = matrixService;
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

		if(prevMouseX>= 0 && prevMouseY>=0) {
		angleX=(prevMouseX-x)*0.1f;
		angleY=(prevMouseY-y)*0.1f;
		
		projectionMatrix = display.getProjectionMatrix();
		
		prevMouseX = x;		//save current mouse loc for next drag
		prevMouseY = y;
		viewMatrix = display.getViewMatrix();
		
		
		
		
		//local
		matrixService.rotateAboutYAxis(viewMatrix, angleX);
		display.setViewMatrix(viewMatrix);
		//global
		matrixService.rotateAboutXAxis(projectionMatrix, angleY);
		
		display.setProjectionMatrix(projectionMatrix);
		}
	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		
	}

}
