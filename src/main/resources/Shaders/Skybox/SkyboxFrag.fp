#version 430 core

in vec3 tex_coord;

out vec4 out_Color;

uniform  samplerCube textureUnit;


void main(void){

    out_Color = texture(textureUnit, tex_coord);

}

