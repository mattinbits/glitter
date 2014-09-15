package com.mjlivesey.gl.geometry;

import com.mjlivesey.gl.render.Program;
import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by Matthew on 02/09/2014.
 */
public abstract class TextureGeometry extends Geometry {

    private static TextureGeometryProgram textureProgram=null;

    protected ByteBuffer textureBuffer = null;
    int tWidth = 0;
    int tHeight = 0;
    int texId = -1;

    protected static final int TEX_UNIT = GL13.GL_TEXTURE0;


    public TextureGeometry()
    {
        super();
        stride=5;
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
        putTextureBuffer();
        GL20.glEnableVertexAttribArray(textureProgram.getPositionIndex());
        GL20.glVertexAttribPointer(textureProgram.getPositionIndex(),3, GL11.GL_FLOAT,false,FLOAT_SIZE*stride,0);
        Util.checkGLError();
        GL20.glEnableVertexAttribArray(textureProgram.getTexCoordIndex());
        GL20.glVertexAttribPointer(textureProgram.getTexCoordIndex(),
                2,GL11.GL_FLOAT,false,FLOAT_SIZE*stride,3*FLOAT_SIZE);
        Util.checkGLError();
    }



    public void setupProgram()
    {
        if(TextureGeometry.textureProgram==null)
        {
            TextureGeometry.textureProgram = new TextureGeometryProgram();
            TextureGeometry.textureProgram.compileAndLink();
        }
        program=TextureGeometry.textureProgram;
    }



    public void renderWithProgram(Matrix4f mvpMatrix, long elaspedTime){
        textureProgram.render(vboId,iboId,mvpMatrix,texId,numVertices);
    }

    private void putTextureBuffer()
    {
        // Create a new texture object in memory and bind it
        texId = GL11.glGenTextures();
        Util.checkGLError();

        GL13.glActiveTexture(TEX_UNIT);
        Util.checkGLError();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        Util.checkGLError();
        // All RGB bytes are aligned to each other and each component is 1 byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        Util.checkGLError();
        // Upload the texture data and generate mip maps (for scaling)

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, tWidth, tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureBuffer);
        Util.checkGLError();

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        Util.checkGLError();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        Util.checkGLError();
        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);
        Util.checkGLError();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_LINEAR);
        Util.checkGLError();
    }


    static class TextureGeometryProgram extends Program {

        private FloatBuffer mvpMatrixBuffer =  BufferUtils.createFloatBuffer(16);


        public int getPositionIndex() {
            return positionIndex;
        }

        public int getTexCoordIndex() {
            return texCoordIndex;
        }

        private int positionIndex = 1;
        private int texCoordIndex = 2;
        private int mvpMatrixIndex=-1;
        private int texSamplerIndex=-1;

        @Override
        public void compileAndLink()  {
            String VertexShader="";
            try {
                VertexShader = readShaderSourceCode("/shaders/texture_geometry/vertex.vs");
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
                FragmentShader = readShaderSourceCode("/shaders/texture_geometry/frag.fs");
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

        public void render(int vboId, int iboId, Matrix4f mvpMatrix, int texId, int length)
        {
            if(!(GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)==programId))
                GL20.glUseProgram(programId);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,iboId);
            if(Log.logger.getLevel()== Level.TRACE)
                Log.logger.trace("TextureGeometry.render - mvp:\r\n{}",Log.matrixToString(mvpMatrix));
            mvpMatrixBuffer.rewind();
            mvpMatrix.store(mvpMatrixBuffer);
            mvpMatrixBuffer.rewind();
            GL20.glUniformMatrix4(mvpMatrixIndex, false, mvpMatrixBuffer);
            GL20.glUniform1i(texSamplerIndex,0);
            GL11.glDrawElements(GL11.GL_TRIANGLES, length, GL11.GL_UNSIGNED_INT, 0);
        }
    }

}
