#version 330 core

in vec2 TexCoord;

out vec4 FragColor;

uniform vec4 uColor;
uniform sampler2D uTexture; 
uniform float phase;

void main() {
   
    vec4 texColor = texture(uTexture, TexCoord) * uColor;

    // Ay evreleri için gölgeleme
    float centerX = 0.5; 
    vec2 center = vec2(0.5, 0.5); 
    float mainRadius = 0.5;

    if (phase == 0.0) { 
        // Kesici dairenin merkezi ve yarıçapı
        vec2 shadowCenter = vec2(0.65, 0.5); // Kesici dairenin merkezi
        float shadowRadius = 0.4; 

        // Pikselin ana daireye ve kesici daireye olan mesafesi
        float mainDist = distance(TexCoord, center);
        float shadowDist = distance(TexCoord, shadowCenter);

       
        if (mainDist < mainRadius && shadowDist > shadowRadius) {
            // Hilal bölgesi: Hiçbir işlem yapma, texture olduğu gibi kalsın
        } else {
            // Karanlık bölge: Pikselleri siyah yap
            texColor.rgb *= 0.0;
        }
    } else if (phase == 1.0) {
        if (TexCoord.x < centerX) {
            texColor.rgb *= 0.0; // Sol taraf karanlık
        }
    } else if (phase == 2.0) { 
        texColor.rgb *= 1.0; // Tam parlak
    } else if (phase == 3.0) {
        if (TexCoord.x > centerX) {
            texColor.rgb *= 0.0; // Sağ taraf karanlık
        }
    }

    FragColor = texColor;
}
