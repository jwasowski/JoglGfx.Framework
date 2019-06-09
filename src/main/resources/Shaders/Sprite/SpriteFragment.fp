
layout (location = 0) out vec4 color;

uniform sampler2D texture_unit;
flat in vec4 star_color;

void main(void){
    color = star_color * texture(texture_unit, gl_PointCoord);
}
