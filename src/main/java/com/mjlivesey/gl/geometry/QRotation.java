package com.mjlivesey.gl.geometry;


import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 24/07/2014.
 */
public class QRotation extends Rotation {

    public static final int IDENTITY = 0;
    public static final int X_90 = 1;
    public static final int Y_90 = 2;
    public static final int Z_90 = 3;
    public static final int X_180 = 4;
    public static final int Y_180 = 5;
    public static final int Z_180 = 6;
    public static final int POSITIVE = 0;
    public static final int NEGATIVE = 0;
    public float w;
    public float x;
    public float y;
    public float z;

    public QRotation() {
        //Identity Quaternion
        this.w = 1;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public QRotation(float w, float x, float y, float z) {
        //create directly from Quaternion components
        //Checking for normalisation (with some tolerance)
        float mag_squared = w * w + x * x + y * y + z * z;
        if (mag_squared - 1 > 0.00000001) {
            double mag = Math.sqrt(mag_squared);
            this.w = (float) (w / mag);
            this.x = (float) (x / mag);
            this.y = (float) (y / mag);
            this.z = (float) (z / mag);
        } else {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }


    public QRotation(float x_rot, float y_rot, float z_rot) {
        //Identity Quaternion
        this.w = 1;
        this.x = 0;
        this.y = 0;
        this.z = 0;

    }

    public static QRotation multiply(QRotation q1, QRotation q2) {
        /*
            http://www.cprogramming.com/tutorial/3d/quaternions.html
             Let Q1 and Q2 be two quaternions, which are defined, respectively, as (w1, x1, y1, z1) and (w2, x2, y2, z2).
            (Q1 * Q2).w = (w1w2 - x1x2 - y1y2 - z1z2)
            (Q1 * Q2).x = (w1x2 + x1w2 + y1z2 - z1y2)
            (Q1 * Q2).y = (w1y2 - x1z2 + y1w2 + z1x2)
            (Q1 * Q2).z = (w1z2 + x1y2 - y1x2 + z1w2
        */
        return new QRotation(
            /*w=*/    q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z,
            /*x=*/    q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y,
            /*y=*/    q1.w * q2.y - q1.x * q2.z + q1.y * q2.w + q1.z * q2.x,
            /*z=*/    q1.w + q2.z + q1.x * q2.y - q1.y * q2.x + q1.z * q2.w
        );
    }

    public static QRotation fromPreset(int type, int direction) {
        QRotation q = null;
        float root_two = (float) Math.sqrt(2);
        switch (type) {
            case IDENTITY:
                q = new QRotation();
                break;
            case X_180:
                q = new QRotation(0, 1, 0, 0);
                break;
            case Y_180:
                q = new QRotation(0, 0, 1, 0);
                break;
            case Z_180:
                q = new QRotation(0, 0, 0, 1);
                break;
            case X_90:
                q = new QRotation(root_two, root_two, 0, 0);
                break;
            case Y_90:
                q = new QRotation(root_two, 0, root_two, 0);
                break;
            case Z_90:
                q = new QRotation(root_two, 0, 0, root_two);
                break;
            default:
                throw new IllegalArgumentException("Invalid Quaternion type");
        }
        if (direction == NEGATIVE) {
            q.x *= -1;
            q.y *= -1;
            q.z *= -1;
        } else if (direction != POSITIVE) {
            throw new IllegalArgumentException("Invalid Quaternion Direction");
        }
        return q;
    }

    public static QRotation fromXRotation(float angle) {
        return fromAxisAndAngle(1, 0, 0, angle);
    }

    public static QRotation fromXRotationDeg(float angleDeg) {
        return fromXRotation((float) (angleDeg * Math.PI / 180.0f));
    }

    public static QRotation fromYRotation(float angle) {
        return fromAxisAndAngle(0, 1, 0, angle);
    }

    public static QRotation fromYRotationDeg(float angleDeg) {
        return fromYRotation((float) (angleDeg * Math.PI / 180.0f));
    }

    public static QRotation fromZRotation(float angle) {
        return fromAxisAndAngle(0, 0, 1, angle);
    }

    public static QRotation fromZRotationDeg(float angleDeg) {
        return fromZRotation((float) (angleDeg * Math.PI / 180.0f));
    }

    public static QRotation fromAxisAndAngle(float x, float y, float z, float angle) {
        double mag = Math.sqrt(x * x + y * y + z * z);
        double norm_x = x / mag;
        double norm_y = y / mag;
        double norm_z = z / mag;
        double sin_half_angle = Math.sin(angle / 2);
        double cos_half_angle = Math.cos(angle / 2);
        QRotation retQ = new QRotation((float) cos_half_angle, (float) (sin_half_angle * norm_x),
                (float) (sin_half_angle * norm_y), (float) (sin_half_angle * norm_z));
        if (Log.logger.getLevel() == Level.TRACE)
            Log.logger.trace("Magnitude of fromAxisAngle Quaternion is {}", retQ.getMagnitude());
        return retQ;
    }

    public static QRotation fromAxisAndAngleDeg(float x, float y, float z, float angleDeg) {
        return fromAxisAndAngle(x, y, z, (float) (angleDeg * Math.PI / 180.0f));
    }

    public float getMagnitude() {
        float mag = (float) Math.sqrt(w * w + x * x + y * y + z * z);
        return mag;
    }

    /*
     * Adjust this Quaternion with the rotation represented by q_in
     */
    public QRotation applyRotation(QRotation q_in) {
        //http://www.cprogramming.com/tutorial/3d/quaternions.html
        //total = local_rotation * total  //multiplication order matters on this line
        return QRotation.multiply(q_in, this);
    }

    private Matrix4f getAsRotationMatrix() {
       /*
            1-2y2-2z2	    2xy-2wz	    2xz+2wy	    0

            2xy+2wz	        1-2x2-2z2	2yz-2wx	    0

            2xz-2wy	        2yz+2wx	    1-2x2-2y2	0

            0	            0	        0	        1
        */
        Matrix4f result = new Matrix4f();
        //Row 1
        result.m00 = 1 - 2 * y * y - 2 * z * z;
        result.m10 = 2 * x * y - 2 * w * z;
        result.m20 = 2 * x * z + 2 * w * y;
        result.m30 = 0;
        //Row 2
        result.m01 = 2 * x * y + 2 * w * z;
        result.m11 = 1 - 2 * x * x - 2 * z * z;
        result.m21 = 2 * y * z - 2 * w * x;
        result.m31 = 0;
        //Row 3
        result.m02 = 2 * x * z - 2 * w * y;
        result.m12 = 2 * y * z + 2 * w * x;
        result.m22 = 1 - 2 * x * x - 2 * y * y;
        result.m32 = 0;
        //Row 4
        result.m03 = 0;
        result.m13 = 0;
        result.m23 = 0;
        result.m33 = 1;

        return result;
    }

    @Override
    public Matrix4f getMatrix() {
        return getAsRotationMatrix();
    }
}
