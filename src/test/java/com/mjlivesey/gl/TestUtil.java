package com.mjlivesey.gl;

import com.mjlivesey.gl.util.Log;
import org.lwjgl.util.vector.Matrix4f;

import java.util.logging.Logger;

/**
 * Created by Matthew on 28/08/2014.
 */
public class TestUtil {

    public final static float DELTA = 0.000001f;
    public static float[] matrixToArray(Matrix4f m){
        float[]  ret = {m.m00, m.m01, m.m02, m.m03, m.m10, m.m11, m.m12, m.m13, m.m20, m.m21, m.m22, m.m23, m.m30,
                        m.m31, m.m32, m.m33};
        System.out.println(Log.matrixToString(m));
        return ret;
    }
}
