#version 120
#define lowp
#define mediump
#define highp

mediump varying vec4 v_color;

void main () {
    gl_FragColor = v_color;
}