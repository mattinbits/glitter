package com.mjlivesey.gl.geometry;

import com.mjlivesey.gl.render.Program;
import com.mjlivesey.gl.render.ProgramHolder;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Matthew on 06/08/2014.
 */
public abstract class Geometry implements ProgramHolder {

    protected static final int FLOAT_SIZE = 4;
    protected static int stride;
    protected int vboId=-1;
    protected int iboId=-1;
    protected FloatBuffer vertexBuffer;
    protected int numVertices;
    protected IntBuffer indexBuffer;
    protected Program program;

    abstract public void bindBuffers() throws Exception;
    abstract public void createBuffers() throws Exception;

     public int getVertexBufferId()
    {
       return vboId;
    }

     public int getIndexBufferId()
    {
        return iboId;
    }

    abstract public void setupProgram();

    abstract protected int getVertexCount();

    public Program getProgram()
    {
        return program;
    }


}
