#version 330 core

layout (location = 0) in vec3 inPosition;  

uniform vec3 uMove;       
layout(location = 1) in vec2 aTexCoord; 

out vec2 TexCoord;
void main()
{

gl_Position = vec4(inPosition+uMove, 1.0);
 TexCoord = aTexCoord;
}
