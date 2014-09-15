#version 120
#define lowp
#define mediump
#define highp

mediump attribute vec3 position;
mediump attribute vec3 color;

mediump uniform mat4 model_matrix;

mediump varying vec4 v_color;

void main () {
    gl_Position = model_matrix * vec4(position,1.0);
    v_color = vec4(color,1.0);
}