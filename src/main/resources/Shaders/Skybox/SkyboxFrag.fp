#version 450 core

in vec3 tex_coord;

out vec4 out_Color;

uniform  samplerCube texture_unit;


void main(void){

    out_Color = texture(texture_unit, tex_coord);

}

