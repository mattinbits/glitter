#version 120
#define lowp
#define mediump
#define highp

struct directional_light{
    mediump vec3 direction;
    mediump float intensity;
}

struct material{
  mediump float shininess;
  mediump vec3 diffuseColor;
  mediump vec3 specularColor;
  mediump vec3 emissiveColor;
  
}

mediump uniform mat4 u_mvp_matrix;
mediump uniform mat4 u_mv_matrix; // aka normal matrix
uniform directional_light u_directional_light;
uniform material u_material;
mediump uniform float u_ambient_fraction; //ambient light as a fraction of diffuse.

mediump attribute vec3 a_position;
mediump attribute vec2 a_TexCoordinate;
mediump attribute vec3 a_normal;

varying vec3 v_diffuseColor;
varying vec3 v_specularColor;
varying vec2 v_TexCoordinate;

void main () {

    vec4 eye_space_normal = mv_matrix * vec4(a_normal,1.0);
    v_diffuseColor = diffuseReflection(u_directional_light, eye_space_normal.xyz, u_material.diffuseColor );
    v_specularColor = specularReflection(u_directional_light, eye_space_normal.xyz, u_material.specularColor );
    gl_Position = mvp_matrix * vec4(position,1.0);
    v_TexCoordinate = a_TexCoordinate;
}

vec3 specularReflection(directional_light lightVec, vec3 normal, vec3 specularColor)
{
    return vec3(0,0,0);
}

vec3 diffuseReflection(directional_light lightVec, vec3 normal, vec3 diffuseColor, float ambientFraction)
{

}