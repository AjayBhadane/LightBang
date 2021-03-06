#version 300 es

layout (location = 0) in vec4 a_color;
layout (location = 1) in vec4 a_position;

out vec4 v_color;

uniform mat4 orthoMat;

void main() {
    v_color = a_color;

	gl_Position = orthoMat * a_position;
}
