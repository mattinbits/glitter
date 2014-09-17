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
}

mediump uniform mat4 u_mvp_matrix;
mediump uniform mat4 u_mv_matrix;
uniform point_light u_point_light;
uniform material u_material;
mediump uniform float u_ambient_component;

mediump attribute vec3 position;
mediump attribute vec2 a_TexCoordinate;
mediump attribute vec3 normal;
varying vec2 v_TexCoordinate;

void main () {
    gl_Position = mvp_matrix * vec4(position,1.0);
    v_TexCoordinate = a_TexCoordinate;
}