#version 300 es

struct directional_light{
    vec3 direction;

    vec3 halfplane;
    vec4 ambiant_color;
    vec4 diffuse_color;
    vec4 specular_color;
};

struct material_properties{
    vec4 ambiant_color;
    vec4 diffuse_color;
    vec4 specular_color;
    float specular_exponent;
};

const float c_zero = 0.0;
const float c_one = 1.0;

uniform material_properties material;
uniform directional_light light;

uniform mat4 mvpMatrix;

vec4 directional_light_color( vec3 normal ){
    vec4 computed_color = vec4(c_zero, c_zero, c_zero, c_zero);

    float ndotl; //dot product of normal and light direction
    float ndoth; //doot product of normal and half-plane vector

    ndotl = max( c_zero, dot( normal, light.direction ) );
    ndoth = max( c_zero, dot( normal, light.halfplane ) );

    computed_color += ( light.ambiant_color * material.ambiant_color );
    computed_color += ( ndotl * light.diffuse_color * material.diffuse_color );

    if ( ndoth > c_zero ){
        computed_color += ( pow ( ndoth, material.specular_exponent )
                           * material.specular_color
                           * light.specular_color);
    }

    return computed_color;
}

layout (location = 0) in vec4 a_color;
layout (location = 1) in vec4 a_position;

out vec4 v_color;

void main() {
    v_color = a_color;
	gl_Position = mvpMatrix * a_position;
}
