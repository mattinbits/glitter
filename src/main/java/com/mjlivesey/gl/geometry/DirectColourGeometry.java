package com.mjlivesey.gl.geometry;

import org.lwjgl.BufferUtils;

/**
 * Created by Matthew on 07/08/2014.
 */
public class DirectColourGeometry extends ColourGeometry {

    float[] vertices;

    int[] vertex_indices;

    public DirectColourGeometry(float[] vertices, int[] vertex_indices) {
        super();
        this.vertex_indices = vertex_indices;
        this.vertices = vertices;
    }

    public void createBuffers() {
        vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.rewind();
        indexBuffer = BufferUtils.createIntBuffer(numVertices);
        indexBuffer.put(vertex_indices);
        indexBuffer.rewind();
    }

    @Override
    protected int getVertexCount() {
        return vertices.length;
    }

    @Override
    public boolean init() {
        numVertices = this.vertices.length;
        setupProgram();
        createBuffers();
        bindBuffers();
        return true;
    }
}
