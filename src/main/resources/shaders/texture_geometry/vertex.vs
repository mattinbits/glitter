#version 120
#define lowp
#define mediump
#define highp

mediump attribute vec3 position;

mediump uniform mat4 model_matrix;

attribute vec2 a_TexCoordinate;
varying vec2 v_TexCoordinate;

void main () {
    gl_Position = model_matrix * vec4(position,1.0);
    v_TexCoordinate = a_TexCoordinate;
}