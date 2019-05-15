#define MIST_LINEAR 0
#define MIST_EXP1 1
#define MIST_EXP2 2
#define MIST_NONE 3


layout (location = 0) out vec4 color;

in struct Vertex {
    vec2  texcoord;
    vec3  normal;
    vec3  light_dir;
    vec3  view_dir;
    float light_dist;
    float camera_dist;
} frag_vertex;

uniform struct PointLight{
    vec4 position;
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    vec3 attenuation;
}light;

uniform struct Material{
  vec4  ambient;
  vec4  diffuse;
  vec4  specular;
  vec4  emission;
  float shininess;
}material;

uniform sampler2D texture_unit;

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

   float attenuation = 1.0 /(light.attenuation[0] +
     light.attenuation[1] * frag_vertex.light_dist +
     light.attenuation[2] * frag_vertex.light_dist * frag_vertex.light_dist);

    vec3 normal   = normalize(frag_vertex.normal);
    vec3 light_dir = normalize(frag_vertex.light_dir);
    vec3 view_dir  = normalize(frag_vertex.view_dir);


    color = material.ambient * light.ambient;
    float n_dot_l = max(dot(normal, light_dir), 0.0);
    color += material.diffuse * light.diffuse * n_dot_l;
    float r_dot_v_pow = pow(max(dot(reflect(-light_dir, normal), view_dir), 0), material.shininess);
    color += material.specular * light.specular * r_dot_v_pow;
    color *= attenuation;
    color += material.emission;
    color *= texture(texture_unit, frag_vertex.texcoord);

    if(mist.type!=MIST_NONE) {
      float mist_factor = GetMistFactor(mist, frag_vertex.camera_dist);
      color = mix(color, mist.color, mist_factor) ;
    }


}

