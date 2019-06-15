# JoglGfx.Framework
This project is about learning how to use OpenGL. It is written in Java using "Java OpenGL" from JoGAmp project https://jogamp.org/ <br />

## Status

jogl-JW-project Computer graphics class Project continuation using JOGL. <br />
Goals for future versions:<br />
Wavefront OBJ model importer and Skybox implementation -> Framework version 0.5<br /> 
Fog implementation -> Framework version 0.62<br />
Transparency implementation -> Framework version 0.64<br />
GLM Math library implementation -> Framework version 0.65<br />
Mouse Controller implementation -> Framework version 0.66<br />
FPS camera controller implementation -> Framework version 0.67<br />
Sprites implementation -> Framework version 0.75<br /> 
Bump mapping implementation -> Framework version 0.87<br />
Depth of field implementation -> Framework version 1.0<br /> 
Implement Shadow mapping -> Framework version 1.1<br />
Extraction of Interfaces and  modeling of Superclasses -> Framework version 1.2<br />

### Requirements

Project requirements are Java 8+, IDE like Eclipse or InteliJ, configured Maven for dependency management and OpenGL 4.x capable GPU.<br />
It may not run on Java 11 due to changes in how dependencies are managed.

### Running Application

To run the app, you simply have to import the project into your IDE and then launch it ("Run" button in Eclipse IDE).
Typical Controls are following (version dependent):
```
Arrow keys manipulate  different axsises
Home - switch to Perspective Projection
End - switch to Orthogonal Projection
PageUp - zoom in
PageDown - zoom out
Escape - close app
F1 to F4 - are used for changing fog type in scenes supporting fog
```
To create a jar with the app, simply use maven command:
```
mvn package
```
in project root folder.

## Authors

* **Jakub Wąsowski** - [JWasowski](https://github.com/jwasowski)

## License

This project is licensed under the GNU General Public License v2.0 - see the [LICENSE.md](LICENSE) file for details <br />
JogAmp license - see https://jogamp.org/git/?p=jogl.git;a=blob;f=LICENSE.txt for details <br />
Starry night Skybox textures - author: Komaokc, https://gamebanana.com/textures/1881, license https://creativecommons.org/licenses/by-nc-nd/4.0/ <br />