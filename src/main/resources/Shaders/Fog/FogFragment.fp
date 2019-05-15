#version 430 core
#define MIST_LINEAR 0
#define MIST_EXP1 1
#define MIST_EXP2 2
#define MIST_NONE 3

in vec3 tex_coord;

out vec4 color;

uniform  samplerCube texture_unit;

uniform struct Mist
{
   vec4 color; // Mist color
   float start; // This is only for linear fog
   float end; // This is only for linear fog
   float density; // For exp and exp2 equation

   int type; // 0 = linear, 1 = exp, 2 = exp2 3 = no mist at all
} mist;

float GetMistFactor(Mist params, float coord){
   float res = 0.0;
   switch(params.type){
   case MIST_LINEAR:
      res = (params.end-coord)/(params.end-params.start);
      break;
   case MIST_EXP1:
       res = exp(-params.density*coord);
      break;
   case MIST_EXP2:
      res = exp(-pow(params.density*coord, 2.0));
      break;
   }
   return 1.0 - clamp(res, 0.0, 1.0);
}


void main(void){
    color = texture(texture_unit, tex_coord);
    if(mist.type!= MIST_NONE) {
      float mist_coord = 40;
      float mist_factor =GetMistFactor(mist, mist_coord);
      color = mix(color, mist.color, mist_factor) ;
    }
}
