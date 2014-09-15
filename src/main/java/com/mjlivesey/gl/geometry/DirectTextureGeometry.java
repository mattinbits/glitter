package com.mjlivesey.gl.geometry;

import com.mjlivesey.gl.util.Log;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by Matthew on 02/09/2014.
 */
public class DirectTextureGeometry extends TextureGeometry{


    private String pngLocation;


    float[] vertices;

    int[] vertex_indices;

    public DirectTextureGeometry(float[] vertices, int[] vertex_indices, String pngLocation) {
        super();
        this.vertex_indices = vertex_indices;
        this.vertices = vertices;
        this.pngLocation = pngLocation;
    }

    @Override
    public void createBuffers() throws Exception {
        vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.rewind();
        indexBuffer = BufferUtils.createIntBuffer(numVertices);
        indexBuffer.put(vertex_indices);
        indexBuffer.rewind();
        fetchPNGTexture();
    }

    @Override
    protected int getVertexCount() {
        return vertices.length;
    }

    @Override
    public boolean init() {
        try {
            numVertices = this.vertices.length;
            setupProgram();
            createBuffers();
            bindBuffers();
            return true;
        }catch(Exception ex)
        {
            Log.logger.error("Failed to init DirectTextureGeometry object",ex);
            return false;
        }
    }

    private void fetchPNGTexture() throws IOException {

        InputStream in = this.getClass().getResourceAsStream(pngLocation);
        // Link the PNG decoder to this stream
        PNGDecoder decoder = new PNGDecoder(in);

        // Get the width and height of the texture
        tWidth = decoder.getWidth();
        tHeight = decoder.getHeight();

        // Decode the PNG file in a ByteBuffer
        textureBuffer = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(textureBuffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        textureBuffer.flip();

        in.close();
        if(Log.logger.getLevel()== Level.DEBUG)
            Log.logger.debug("Texture {} decoded successfully. Width is {}. Height is {}.",pngLocation,tWidth,tHeight);
    }


}
