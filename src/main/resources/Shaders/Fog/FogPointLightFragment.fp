#define MIST_LINEAR 0
#define MIST_EXP1 1
#define MIST_EXP2 2
#define MIST_NONE 3
#define MIST_CUSTOM 4

layout (location = 0) out vec4 color;

in struct Vertex {
	vec2 texcoord;
	vec3 normal;
	vec3 light_dir;
	vec3 view_dir; // camera to point vector
	vec4 camera_pos; // camera position
	float light_dist;
	float camera_dist; // camera to point distance
} frag_vertex;

uniform struct PointLight {
	vec4 position;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec3 attenuation;
} light;

uniform struct DirLight {
	vec4 direction;
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
} dirLight;

uniform struct Material {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec4 emission;
	float shininess;
} material;

uniform sampler2D texture_unit;
//uniform sampler2D cloud_texture_unit;

uniform struct Mist {
	vec4 color; // Mist color
	float start; // This is only for linear fog
	float end; // This is only for linear fog
	float density; // For exp and exp2 equation

	int type; // 0 = linear, 1 = exp, 2 = exp2 3 = no mist at all
} mist;

vec4 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir) {
	vec3 lightDir = normalize(-light.direction.xyz);

	float diff = max(dot(normal, lightDir), 0.0);

	vec3 reflectDir = reflect(-lightDir, normal);
	float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

	vec4 ambient = light.ambient * material.diffuse;
	vec4 diffuse = light.diffuse * diff * material.diffuse;
	vec4 specular = light.specular * spec * material.specular;
	return (ambient + diffuse + specular);
}

vec4 CalcPointLight(PointLight light, vec3 normal, vec3 viewDir) {
	vec3 light_dir = normalize(frag_vertex.light_dir);

	float diff = max(dot(normal, light_dir), 0.0);

	vec3 reflectDir = reflect(-light_dir, normal);
	float spec = pow(max(dot(reflectDir, viewDir), 0), material.shininess);

	float attenuation =
			1.0
					/ (light.attenuation.x
							+ (light.attenuation.y * frag_vertex.light_dist)
							+ (light.attenuation.z
									* (frag_vertex.light_dist
											* frag_vertex.light_dist)));

	vec4 ambient = light.ambient * material.ambient;
	vec4 diffuse = light.diffuse * diff * material.diffuse;
	vec4 specular = light.specular * spec * material.specular;
	ambient *= attenuation;
	diffuse *= attenuation;
	specular *= attenuation;
	return (ambient + diffuse + specular);
}

float GetMistFactor(Mist params, float coord) {
	float res = 0.0;
	float fall_off = 0.08;
	switch (params.type) {
	case MIST_LINEAR:
		res = (params.end - coord) / (params.end - params.start);
		break;
	case MIST_EXP1:
		res = exp(-params.density * coord);
		break;
	case MIST_EXP2:
		res = exp(-pow(params.density * coord, 2.0));
		break;
	case MIST_CUSTOM:
		res = 1.0 - exp(-frag_vertex.camera_dist * fall_off);
		break;
	}
	return clamp(res, 0.0, 1.0);
}

void main(void) {

	vec3 normal = normalize(frag_vertex.normal);
	vec3 view_dir = normalize(frag_vertex.view_dir);
	vec4 result = CalcDirLight(dirLight, normal, view_dir);
	result += CalcPointLight(light, normal, view_dir);
	result += material.emission;
	result *= texture(texture_unit, frag_vertex.texcoord);
	color = result;

	if (mist.type != MIST_NONE) {
		float mist_factor = GetMistFactor(mist, frag_vertex.camera_dist);
		color = mix(color, mist.color, mist_factor);
	}

}

