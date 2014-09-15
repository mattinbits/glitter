package com.mjlivesey.gl.render;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 06/08/2014.
 */
public interface ProgramHolder {

    public Program getProgram();
    public void renderWithProgram(Matrix4f VPmatrix, long elapsedTime);
    public boolean init();
}
