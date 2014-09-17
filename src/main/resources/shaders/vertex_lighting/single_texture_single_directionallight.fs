#version 120
#define lowp
#define mediump
#define highp

mediump varying vec2 v_TexCoordinate;
uniform sampler2D u_Texture;

void main () {
    gl_FragColor =  texture2D(u_Texture, v_TexCoordinate);
}