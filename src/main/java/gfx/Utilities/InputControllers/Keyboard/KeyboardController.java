package gfx.Utilities.InputControllers.Keyboard;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import gfx.Display.WindowBezierPatch;
import gfx.Scene.Objects.BezierPatch;

public class KeyboardController implements KeyListener {

	// TODO Create interfaces for Objects and make controller universal across
	// all Object classes
	private Set<KeyEvent> keySet = ConcurrentHashMap.newKeySet();
	private BezierPatch bezierPatchOne;
	private BezierPatch bezierPatchTwo;
	private BezierPatch bezierPatchFloor;
	WindowBezierPatch display;

	public KeyboardController(BezierPatch bezierPatchOne, BezierPatch bezierPatchTwo, BezierPatch bezierPatchFloor,
			WindowBezierPatch display) {
		this.bezierPatchOne = bezierPatchOne;
		this.bezierPatchTwo = bezierPatchTwo;
		this.bezierPatchFloor = bezierPatchFloor;
		this.display = display;
	}

	public void controlKeyboard() {
		if (!keySet.isEmpty()) {
			keySet.forEach(keyEvent -> {
				if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
					rotateYLeft();
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
					rotateYRight();
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
					rotateXUp();
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
					rotateXDown();
				}
			});
		}
	}

	public void rotateYLeft() {
		bezierPatchOne.rotateYAxisLeft();
		bezierPatchTwo.rotateYAxisLeft();
		bezierPatchFloor.rotateYAxisLeft();
	}

	public void rotateYRight() {
		bezierPatchOne.rotateYAxisRight();
		bezierPatchTwo.rotateYAxisRight();
		bezierPatchFloor.rotateYAxisRight();
	}

	public void rotateXUp() {
		bezierPatchOne.rotateXAxisUp();
		bezierPatchTwo.rotateXAxisUp();
		bezierPatchFloor.rotateXAxisUp();
	}

	public void rotateXDown() {
		bezierPatchOne.rotateXAxisDown();
		bezierPatchTwo.rotateXAxisDown();
		bezierPatchFloor.rotateXAxisDown();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			display.shutDown();
		}
		// Rotate around Y axis
		if (e.getKeyCode() == KeyEvent.VK_LEFT && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			System.out.println(e.isAutoRepeat());

			keySet.add(e);

		}
		// Rotate around Y axis
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

			keySet.add(e);

		}
		// Rotate around X axis
		if (e.getKeyCode() == KeyEvent.VK_UP && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

			keySet.add(e);

		}
		// Rotate around X axis
		if (e.getKeyCode() == KeyEvent.VK_DOWN && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

			keySet.add(e);

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Rotate around Y axis
		if (e.getKeyCode() == KeyEvent.VK_LEFT && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
					keySet.remove(keyEvent);
				}
			}

			System.out.println("Released");

		}

		// Rotate around Y axis
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
					keySet.remove(keyEvent);
				}
			}
			System.out.println("Released");

		}
		// Rotate around X axis
		if (e.getKeyCode() == KeyEvent.VK_UP && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
					keySet.remove(keyEvent);
				}
			}
			System.out.println("Released");
		}
		// Rotate around X axis
		if (e.getKeyCode() == KeyEvent.VK_DOWN && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
					keySet.remove(keyEvent);
				}
			}
			System.out.println("Released");
		}

	}
}
