package com.mjlivesey.gl.geometry;

import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 18/08/2014.
 */
public class Translation extends Transformation {

    float x;
    float y;
    float z;
    Matrix4f translationMatrix;

    public Translation()
    {
        this(0,0,0);
    }

    public Translation(float x, float y, float z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
        translationMatrix = new Matrix4f();
        translationMatrix.setIdentity();
        updateMatrix();
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
        updateMatrix();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateMatrix();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateMatrix();
    }

    private void updateMatrix()
    {
        translationMatrix.m30=x;
        translationMatrix.m31=y;
        translationMatrix.m32=z;
        if(Log.logger.getLevel()== Level.TRACE)
            Log.logger.trace("Translation Matrix updated to {}",Log.matrixToString(translationMatrix));
    }

    public void increment(float x, float y, float z)
    {
        this.x+=x;
        this.y+=y;
        this.z+=z;
        updateMatrix();
    }

    @Override
    public Matrix4f getMatrix() {
        return translationMatrix;
    }
}
