package gfx.Utilities.InputControllers.Keyboard;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import gfx.Display.DisplayInterface;
import gfx.Display.WindowBezierPatch;
import gfx.Display.WindowModelImport;
import gfx.Scene.Objects.BezierPatch;
import gfx.Scene.Objects.GfxObjectInterface;

/** Keyboard input controller class. */
public class KeyboardController implements KeyListener {

	private Set<KeyEvent> keySet = ConcurrentHashMap.newKeySet();
	private GfxObjectInterface gfxObjectInterface;
	private DisplayInterface display;

	public KeyboardController(GfxObjectInterface gfxObjectInterface, DisplayInterface display) {
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
				if (keyEvent.getKeyCode() == KeyEvent.VK_Q) {
					decAltitude();
				}
				if (keyEvent.getKeyCode() == KeyEvent.VK_E) {
					incAltitude();
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

	public void incAltitude() {
		gfxObjectInterface.incAltitude();
	}

	public void decAltitude() {
		gfxObjectInterface.decAltitude();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			display.shutDown();
		}
		// No fog
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			display.setMistType(3);
		}
		// Fog linear
		if (e.getKeyCode() == KeyEvent.VK_F2) {
			// display.setMistType(0);
			System.err.println("This Fog equation is turned of for this build.");
		}
		// Low intensity fog
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			// display.setMistType(1);
			System.err.println("This Fog equation is turned of for this build.");
		}
		// High intensity fog
		if (e.getKeyCode() == KeyEvent.VK_F4) {
			// display.setMistType(2);
			System.err.println("This Fog equation is turned of for this build.");
		}
		// Custom fog
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			display.setMistType(4);
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

		if (e.getKeyCode() == KeyEvent.VK_Q && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

			keySet.add(e);

		}

		if (e.getKeyCode() == KeyEvent.VK_E && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {

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

		if (e.getKeyCode() == KeyEvent.VK_Q && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_Q) {
					keySet.remove(keyEvent);
				}
			}

		}

		if (e.getKeyCode() == KeyEvent.VK_E && 0 == (InputEvent.AUTOREPEAT_MASK & e.getModifiers())) {
			for (KeyEvent keyEvent : keySet) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_E) {
					keySet.remove(keyEvent);
				}
			}

		}
	}
}
