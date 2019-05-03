package gfx.Utilities.InputControllers.Keyboard;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import gfx.Display.WindowBezierPatch;
import gfx.Display.WindowModelImport;
import gfx.Scene.Objects.BezierPatch;
import gfx.Scene.Objects.GfxObjectInterface;

public class KeyboardController implements KeyListener {

	// TODO Create interfaces for Objects and make controller universal across
	// all Object classes
	private Set<KeyEvent> keySet = ConcurrentHashMap.newKeySet();
	private GfxObjectInterface gfxObjectInterface;
	private WindowModelImport display;

	public KeyboardController(GfxObjectInterface gfxObjectInterface, WindowModelImport display) {
		this.gfxObjectInterface = gfxObjectInterface;
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
				if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
					moveForward();
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
					moveBackwards();
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
					moveLeft();
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
					moveRight();
				}
			});
		}
	}

	public void rotateYLeft() {
		gfxObjectInterface.rotateYAxisLeft();

	}

	public void rotateYRight() {
		gfxObjectInterface.rotateYAxisRight();

	}

	public void rotateXUp() {
		gfxObjectInterface.rotateXAxisUp();

	}

	public void rotateXDown() {
		gfxObjectInterface.rotateXAxisDown();

	}

	public void moveForward() {
		gfxObjectInterface.moveForward();
	}

	public void moveBackwards() {
		gfxObjectInterface.moveBackwards();
	}

	public void moveLeft() {
		gfxObjectInterface.moveLeft();
	}

	public void moveRight() {
		gfxObjectInterface.moveRight();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			display.shutDown();
		}
		// Rotate around Y axis
		if (e.getKeyCode() == KeyEvent.VK_LEFT && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			

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
		// Move forward
		if (e.getKeyCode() == KeyEvent.VK_W && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

			keySet.add(e);

		}
		// Move backwards
		if (e.getKeyCode() == KeyEvent.VK_S && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

			keySet.add(e);

		}
		// Move left
		if (e.getKeyCode() == KeyEvent.VK_A && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

			keySet.add(e);

		}
		// Move right
		if (e.getKeyCode() == KeyEvent.VK_D && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

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

			

		}

		// Rotate around Y axis
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
					keySet.remove(keyEvent);
				}
			}
			

		}
		// Rotate around X axis
		if (e.getKeyCode() == KeyEvent.VK_UP && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
					keySet.remove(keyEvent);
				}
			}
			
		}
		// Rotate around X axis
		if (e.getKeyCode() == KeyEvent.VK_DOWN && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
					keySet.remove(keyEvent);
				}
			}
			
		}
		// Move forward
		if (e.getKeyCode() == KeyEvent.VK_W && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
					keySet.remove(keyEvent);
				}
			}
			
		}
		// Move backwards
		if (e.getKeyCode() == KeyEvent.VK_S && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
					keySet.remove(keyEvent);
				}
			}
			
		}

		// Move left
		if (e.getKeyCode() == KeyEvent.VK_A && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
					keySet.remove(keyEvent);
				}
			}
			
		}
		// Move right
		if (e.getKeyCode() == KeyEvent.VK_D && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
					keySet.remove(keyEvent);
				}
			}
			
		}
	}
}
