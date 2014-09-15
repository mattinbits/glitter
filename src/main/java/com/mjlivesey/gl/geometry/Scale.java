package com.mjlivesey.gl.geometry;

import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 07/08/2014.
 */
public class Scale extends Transformation {

   Matrix4f scaleMatrix;

    public Scale(float scaleFactor)
    {
        scaleMatrix = new Matrix4f();
        Matrix4f.setIdentity(scaleMatrix);
        setScaleFactor(scaleFactor);
        if(Log.logger.getLevel()== Level.TRACE)
            Log.logger.trace("Scale created with initial matrix:\n{}",Log.matrixToString(scaleMatrix));
    }

    public void setScaleFactor(float scaleFactor){
        scaleMatrix.m00 = scaleFactor;
        scaleMatrix.m11 = scaleFactor;
        scaleMatrix.m22 = scaleFactor;
    }

    @Override
    public Matrix4f getMatrix() {
        return scaleMatrix;
    }
}
