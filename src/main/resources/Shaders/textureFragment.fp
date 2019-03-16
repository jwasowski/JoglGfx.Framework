#version 450

in vec2 texCoord;

out vec4 outColor;
uniform sampler2D textureUnit;

void main(void){
		
	outColor = texture(textureUnit, texCoord);
	
	
   
   
}
