package gfx.Application;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import gfx.Display.Window;
import gfx.Display.WindowBezier;

public class Main {

	public static void main(String[] args) {
		//TODO Dynamic resolution
		//TODO Create Object of Display Class
		//TODO Setup NEWT for Window
		final GLProfile glp = GLProfile.getDefault();
		// Specifies a set of OpenGL capabilities, based on your profile.
		GLCapabilities caps = new GLCapabilities(glp);
		// Create the OpenGL rendering canvas
		final GLWindow window = GLWindow.create(caps);
		final FPSAnimator animator = new FPSAnimator(window, 60, true);
		@SuppressWarnings("unused")
		//Window display = new Window(window, animator, 1280, 720, "Test Framework");
		WindowBezier bezier = new WindowBezier(window, animator, 1280, 720, "Bezier test");
		
	}

}
