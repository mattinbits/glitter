package com.mjlivesey.gl.util;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.text.DecimalFormat;

/**
 * Created by Matthew on 16/08/2014.
 */
public class Log {
    public static org.apache.logging.log4j.Logger logger = LogManager.getLogger("No Name");

    public static void initialize(String name)
    {
        logger = LogManager.getLogger(name);
    }

    public static String matrixToString(Matrix4f matrix)
    {
        DecimalFormat df = new DecimalFormat("0.000");
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        sb.append(df.format(matrix.m00));
        sb.append("  ");
        sb.append(df.format(matrix.m10));
        sb.append("  ");
        sb.append(df.format(matrix.m20));
        sb.append("  ");
        sb.append(df.format(matrix.m30));
        sb.append("|\r\n");

        sb.append("|");
        sb.append(df.format(matrix.m01));
        sb.append("  ");
        sb.append(df.format(matrix.m11));
        sb.append("  ");
        sb.append(df.format(matrix.m21));
        sb.append("  ");
        sb.append(df.format(matrix.m31));
        sb.append("|\r\n");

        sb.append("|");
        sb.append(df.format(matrix.m02));
        sb.append("  ");
        sb.append(df.format(matrix.m12));
        sb.append("  ");
        sb.append(df.format(matrix.m22));
        sb.append("  ");
        sb.append(df.format(matrix.m32));
        sb.append("|\r\n");

        sb.append("|");
        sb.append(df.format(matrix.m03));
        sb.append("  ");
        sb.append(df.format(matrix.m13));
        sb.append("  ");
        sb.append(df.format(matrix.m23));
        sb.append("  ");
        sb.append(df.format(matrix.m33));
        sb.append("|");
/*

        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        matrix.store(fb);
        fb.rewind();
        float [] values = new float[16];
        for(int i=0; i<values.length; i++)
        {
            values[i] = fb.get();
        }
        sb.append("\r\n");
        sb.append("|");
        sb.append(df.format(values[0]));
        sb.append("  ");
        sb.append(df.format(values[4]));
        sb.append("  ");
        sb.append(df.format(values[8]));
        sb.append("  ");
        sb.append(df.format(values[12]));
        sb.append("|\r\n");

        sb.append("|");
        sb.append(df.format(values[1]));
        sb.append("  ");
        sb.append(df.format(values[5]));
        sb.append("  ");
        sb.append(df.format(values[9]));
        sb.append("  ");
        sb.append(df.format(values[13]));
        sb.append("|\r\n");

        sb.append("|");
        sb.append(df.format(values[2]));
        sb.append("  ");
        sb.append(df.format(values[6]));
        sb.append("  ");
        sb.append(df.format(values[10]));
        sb.append("  ");
        sb.append(df.format(values[14]));
        sb.append("|\r\n");

        sb.append("|");
        sb.append(df.format(values[3]));
        sb.append("  ");
        sb.append(df.format(values[7]));
        sb.append("  ");
        sb.append(df.format(values[11]));
        sb.append("  ");
        sb.append(df.format(values[15]));
        sb.append("|");

*/
        return sb.toString();
    }

}
