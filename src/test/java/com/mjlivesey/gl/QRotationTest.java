package com.mjlivesey.gl;

import com.mjlivesey.gl.geometry.QRotation;
import org.junit.Assert;
import org.junit.Test;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 28/08/2014.
 */
public class QRotationTest {

    @Test
    public void testGetMatrix() {
    /*Test from https://github.com/google/closure-library/blob/master/closure/goog/vec/quaternion_test.html*/
        QRotation q = new QRotation(0.497051689967954f, 0.22094256606638f, 0.53340203646030f,
                0.64777022739548f);

        float [] expected = {
                -0.408248f, 0.8796528f, -0.244016935f, 0,
                -0.4082482f, 0.06315623f, 0.9106836f, 0,
                0.8164965f, 0.47140452f, 0.3333333f, 0,
                0, 0, 0, 1
        };
        Matrix4f mat = q.getMatrix();
        Assert.assertArrayEquals("Testing Qrotation to matrix",expected,TestUtil.matrixToArray(mat),TestUtil.DELTA);
    }

    @Test public void fromAxisAngleIdentity()
    {
            /*Test from https://github.com/google/closure-library/blob/master/closure/goog/vec/quaternion_test.html*/
        float x = -0.408248f;
        float y = 0.8796528f;
        float z = -0.244016f;
        QRotation q = QRotation.fromAxisAndAngle(x,y,z,0);
        Assert.assertEquals("Testing from 0 angle w",1.0f,q.w,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle x",0.0f,q.x,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle y",0.0f,q.y,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle z",0.0f,q.z,TestUtil.DELTA);
    }

    @Test public void fromAxisAngle()
    {
            /*Test from https://github.com/google/closure-library/blob/master/closure/goog/vec/quaternion_test.html*/
        float x = -0.408248f;
        float y = 0.8796528f;
        float z = -0.244016f;
        QRotation q = QRotation.fromAxisAndAngle(x,y,z,(float)Math.PI / 2f);

        Assert.assertEquals("Testing from 0 angle w",0.70710678f,q.w,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle x",-0.288675032f,q.x,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle y",0.622008682f,q.y,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle z",-0.17254543f,q.z,TestUtil.DELTA);
    }

    @Test public void fromAxisAngle2()
    {
        /*Test from https://github.com/google/closure-library/blob/master/closure/goog/vec/quaternion_test.html*/
        float x = 5f;
        float y = -2f;
        float z = -10f;
        QRotation q = QRotation.fromAxisAndAngle(x,y,z,(float)Math.PI / 5f);
        Assert.assertEquals("Testing from 0 angle w",0.951056516f,q.w,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle x",0.136037146f,q.x,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle y",-0.0544148586f,q.y,TestUtil.DELTA);
        Assert.assertEquals("Testing from 0 angle z",-0.27207429f,q.z,TestUtil.DELTA);
    }
}
