package com.mjlivesey.gl.render;

import com.mjlivesey.gl.lighting.PointLight;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.Util;

import java.io.IOException;

/**
 * Created by Matthew on 16/09/2014.
 */
public class BasicVertexLightingProgram extends Program {

    private int positionIndex = 1;
    private int texCoordIndex = 2;
    private int mvpMatrixIndex=-1;
    private int texSamplerIndex=-1;

    private PointLight light;

    private float ambientIntensity;

    public BasicVertexLightingProgram(PointLight light, float ambientIntensity)
    {
        this.light = light;
        this.ambientIntensity = ambientIntensity;
    }


    @Override
    public void compileAndLink() {
        String VertexShader="";
        try {
            VertexShader = readShaderSourceCode("/shaders/vertex_shading/single_texture_single_directionallight.vs");
        }catch(IOException ex)
        {
            throw new RuntimeException(ex);
        }
        int vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        Util.checkGLError();

        GL20.glShaderSource(vertexShaderId, VertexShader);
        Util.checkGLError();

        GL20.glCompileShader(vertexShaderId);
        Util.checkGLError();

        // Print possible compile errors
        System.out.println("Vertex shader compilation:");
        printShaderLog(vertexShaderId);
        Util.checkGLError();

        //==========================================================
        // Load and compile fragment shader
        String FragmentShader ="";
        try{
            FragmentShader = readShaderSourceCode("/shaders/vertex_shading/single_texture_single_directionallight.fs");
        }catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }

        int fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        Util.checkGLError();

        GL20.glShaderSource(fragmentShaderId, FragmentShader);
        Util.checkGLError();

        GL20.glCompileShader(fragmentShaderId);
        Util.checkGLError();

        // Print possible compile errors
        System.out.println("Fragment shader compilation:");
        printShaderLog(fragmentShaderId);

        //==========================================================
        // Link shader program

        programId = GL20.glCreateProgram();
        Util.checkGLError();

        // Attach vertex shader
        GL20.glAttachShader(programId, vertexShaderId);
        Util.checkGLError();

        // Attach fragment shader
        GL20.glAttachShader(programId, fragmentShaderId);
        Util.checkGLError();

        // We tell the program how the vertex attribute indices will map
        // to named "in" variables in the vertex shader. This must be done
        // before compiling.
        GL20.glBindAttribLocation(programId, positionIndex, "position");
        Util.checkGLError();

        GL20.glBindAttribLocation(programId, texCoordIndex, "a_TexCoordinate");
        Util.checkGLError();

        GL20.glLinkProgram(programId);
        Util.checkGLError();

        // Print possible compile errors
        System.out.println("Program linking:");
        printProgramLog(programId);
        Util.checkGLError();

        mvpMatrixIndex = GL20.glGetUniformLocation(programId, "model_matrix");
        Util.checkGLError();
        texSamplerIndex = GL20.glGetUniformLocation(programId, "u_Texture");
        Util.checkGLError();
    }

    public void render()
    {

    }
}
