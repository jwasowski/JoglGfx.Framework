package gfx.Utilities.InputControllers.Mouse;

import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import gfx.Display.WindowBezierPatch;
/** @deprecated */
public class MouseController implements MouseListener {
	private int width, height, prevMouseX, prevMouseY;
	public int[] mouseCoords = new int[3];
	private float cameraAngleAboutY, cameraDefaultZ = -10;
	private WindowBezierPatch display;

	public MouseController(WindowBezierPatch display){
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
		System.out.println("Mouse coords X: "+e.getX()+" Y: "+e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseCoords[0]=e.getX();
		mouseCoords[1]=e.getY();
		int x = e.getX();		//get current mouse loc
		int y = e.getY();
		Dimension size = new Dimension(width, height) ;  //get size of frame

		float mouseDeltaXInDegrees = 360.0f * ( (float)(x-prevMouseX) / (float)size.getWidth());
		float mouseDeltaYInScreenPercent = ( (float)(prevMouseY-y) / (float)size.getWidth());

		prevMouseX = x;		//save current mouse loc for next drag
		prevMouseY = y;

		cameraAngleAboutY += mouseDeltaXInDegrees;
		cameraDefaultZ = cameraDefaultZ - (mouseDeltaYInScreenPercent*cameraDefaultZ) ;

		display.viewMatrix[0] = (float) (cameraDefaultZ * Math.sin(Math.toRadians(cameraAngleAboutY)));
		display.viewMatrix[10] = (float) (cameraDefaultZ * Math.cos(Math.toRadians(cameraAngleAboutY)));

	}

	@Override
	public void mouseWheelMoved(MouseEvent e) {
		

	}
}
