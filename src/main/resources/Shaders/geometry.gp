#version 430

layout (lines_adjacency) in;
layout (line_strip, max_vertices=65) out;

out vec4 frag_color;

void Bezier(vec4 p0, vec4 p1, vec4 p2, vec4 p3){
    
    float t=0.0,h=1.0/64.0;
    vec4 q0, q1, q2, r0, r1, s;
    gl_Position = p0;
    EmitVertex();

    for(int i=1; i<64; i++){
        t +=h;
        q0=mix(p0, p1,t);
        q1=mix(p1, p2,t);
        q2=mix(p2, p3,t);
        r0=mix(q0, q1,t);
        r1=mix(q1, q2,t);
        gl_Position = mix(r0, r1, t);
        EmitVertex();
    }
    gl_Position = p3;
    EmitVertex();

    EndPrimitive();
}

void main(void){
       
    frag_color=vec4(1,0,1,1);
    
    vec4 v1 = (gl_in[2].gl_Position - gl_in[0].gl_Position)/2;
    vec4 p1plus = gl_in[1].gl_Position + v1/3.0;
    vec4 v2 = (gl_in[3].gl_Position - gl_in[1].gl_Position)/2;
    vec4 p2minus = gl_in[2].gl_Position - v2/3.0;
    

	Bezier(gl_in[1].gl_Position, p1plus, p2minus, gl_in[2].gl_Position);
}

