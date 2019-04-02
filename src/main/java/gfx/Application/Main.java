package gfx.Application;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import gfx.Display.Window;
import gfx.Display.WindowBezier;
import gfx.Display.WindowBezierPatch;
import gfx.Display.WindowModelImport;

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
		int height = 720, width = 1280;
		@SuppressWarnings("unused")
		//Window display = new Window(window, animator, width, height, "Test Framework");
		//WindowBezier bezier = new WindowBezier(window, animator, width, height, "Bezier test");
		WindowBezierPatch bezierPatch = new WindowBezierPatch(window, animator, width, height, "Bezier test");
		//WindowModelImport modelImport = new WindowModelImport(window, animator, width, height, "Model Import test");
		
	}

}
