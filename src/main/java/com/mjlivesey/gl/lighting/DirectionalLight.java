package com.mjlivesey.gl.lighting;

/**
 * Created by Matthew on 16/09/2014.
 */
public class DirectionalLight extends Light {

    private float intensity;
    private float[] direction;

    public DirectionalLight() {
        this( new float[]{0, 1.0f, 0}, 1.0f);
    }

    public DirectionalLight(float[] direction, float intensity) {
        this.intensity = intensity;
        this.direction = direction;
    }


    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float[] getDirection() {
        return direction;
    }

    public void setDirection(float[] direction) {
        this.direction = direction;
    }

    public void setDirection(float x, float y, float z) {
        direction[0] = x;
        direction[1] = y;
        direction[2] = z;
    }

    public void setDirection(float value, int index) {
        direction[index] = value;
    }

    public float getDirection(int index) {
        return direction[index];
    }

}
