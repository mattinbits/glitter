package com.mjlivesey.gl.geometry;

import com.mjlivesey.gl.util.Log;
import com.mjlivesey.gl.render.Program;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Matrix4f;

import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Created by Matthew on 06/08/2014.
 */
public abstract class ColourGeometry extends Geometry {


    private static ColourGeometryProgram colourProgram=null;


    public ColourGeometry()
    {
        super();
        stride = 6;
    }

    public void setupProgram()
    {
        if(ColourGeometry.colourProgram==null)
        {
            ColourGeometry.colourProgram = new ColourGeometryProgram();
            ColourGeometry.colourProgram.compileAndLink();
        }
        program=ColourGeometry.colourProgram;
    }

    public void renderWithProgram(Matrix4f mvpMatrix, long elaspedTime){
        colourProgram.render(vboId,iboId,mvpMatrix,numVertices);
    }


    private void bindVertexBuffer()
    {

        vertexBuffer.rewind();
        vboId = GL15.glGenBuffers();
        Util.checkGLError();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        Util.checkGLError();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        Util.checkGLError();
    }

    public void bindIndexBuffer()
    {

        indexBuffer.rewind();
        iboId = GL15.glGenBuffers();
        Util.checkGLError();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,iboId);
        Util.checkGLError();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL15.GL_STATIC_DRAW);
        Util.checkGLError();
    }
    @Override
    public void bindBuffers() {
        bindVertexBuffer();
        bindIndexBuffer();
        GL20.glEnableVertexAttribArray(colourProgram.getPositionIndex());
        GL20.glVertexAttribPointer(colourProgram.getPositionIndex(),3, GL11.GL_FLOAT,false,FLOAT_SIZE*stride,0);
        Util.checkGLError();
        GL20.glEnableVertexAttribArray(colourProgram.getColourIndex());
        GL20.glVertexAttribPointer(colourProgram.getColourIndex(),3,GL11.GL_FLOAT,false,FLOAT_SIZE*stride,3*FLOAT_SIZE);
        Util.checkGLError();
    }

    static class ColourGeometryProgram extends Program {

        private FloatBuffer mvpMatrixBuffer =  BufferUtils.createFloatBuffer(16);


        public int getPositionIndex() {
            return positionIndex;
        }

        public int getColourIndex() {
            return colourIndex;
        }

        private int positionIndex = 1;
        private int colourIndex = 2;
        private int mvpMatrixIndex=3;

        @Override
        public void compileAndLink()  {
            String VertexShader="";
            try {
                VertexShader = readShaderSourceCode("/shaders/colour_geometry/vertex.vs");
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
                FragmentShader = readShaderSourceCode("/shaders/colour_geometry/frag.fs");
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
            GL20.glBindAttribLocation(programId, colourIndex, "color");

            GL20.glLinkProgram(programId);
            Util.checkGLError();

            // Print possible compile errors
            System.out.println("Program linking:");
            printProgramLog(programId);
            Util.checkGLError();

            mvpMatrixIndex = GL20.glGetUniformLocation(programId, "model_matrix");
            Util.checkGLError();
        }

        public void render(int vboId, int iboId, Matrix4f mvpMatrix, int length)
        {
            if(!(GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)==programId))
                GL20.glUseProgram(programId);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,iboId);
            if(Log.logger.getLevel()==Level.TRACE)
                Log.logger.trace("ColourGeometry.render - mvp:\r\n{}",Log.matrixToString(mvpMatrix));
            mvpMatrixBuffer.rewind();
            mvpMatrix.store(mvpMatrixBuffer);
            mvpMatrixBuffer.rewind();
            GL20.glUniformMatrix4(mvpMatrixIndex, false, mvpMatrixBuffer);
            GL11.glDrawElements(GL11.GL_TRIANGLES, length, GL11.GL_UNSIGNED_INT, 0);
        }
    }
}
