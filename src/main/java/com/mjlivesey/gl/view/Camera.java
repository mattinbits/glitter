package com.mjlivesey.gl.view;

import com.mjlivesey.gl.util.Log;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 07/08/2014.
 */
public abstract class Camera {

    public abstract Matrix4f getViewMatrix();

    public abstract Matrix4f getProjectionMatrix();

    public static Camera getLookAtCameraWithFrustum()
    {
        return getLookAtCameraWithFrustum(1000.0f);
    }

    public static Camera getLookAtCameraWithFrustum(float far)
    {
        return getLookAtCameraWithFrustum(90, (float)(Display.getWidth())/Display.getHeight(), far);
    }

    public static Camera getLookAtCameraWithFrustum(float hfov, float aspectRatio, float far)
    {
        Log.logger.info("getLookAtCameraWithFrustum - {}",aspectRatio);
        return getLookAtCameraWithFrustum(hfov, aspectRatio,1,far);
    }

    public static Camera getLookAtCameraWithFrustum(float hfov, float aspectRatio, float near, float far)
    {
        return new SimpleCamera(hfov, aspectRatio,near,far);
    }

    protected Matrix4f getProjection(float hfov, float aspectRatio,float near, float far)
    {
            /*
         T xmin,xmax,ymin,ymax;
        ymax= zNear* tan(fovy*Math<T>::PI/360.0);
        ymin= -ymax;
        xmin= ymin*aspectRatio;
        xmax= ymax*aspectRatio;
        Frustum(xmin,xmax,ymin,ymax,zNear,zFar);
             */
         /*Prefer  to use horizontal field of view*/
        float right = (float) (near * Math.tan(hfov/2 *Math.PI/360));
        float left = -right;
        float top = right/aspectRatio;
        float bottom = -top;
        Log.logger.info("getProjection() - right {}",right);
        Log.logger.info("getProjection() - left {}",left);
        Log.logger.info("getProjection() - top {}",top);
        Log.logger.info("getProjection() - bottom {}",bottom);
        Log.logger.info("getProjection() - aspect ratio {}",aspectRatio);
        return getFrustum(top, bottom, right, left, near, far);
    }

    protected Matrix4f getFrustum(float top, float bottom, float right, float left, float near, float far)
    {
           /* Page 114 of Maths book */
        Matrix4f frustum = new Matrix4f();
        frustum.setZero();
        frustum.m00 = (2*near)/(right-left);
        frustum.m20 = (right+left) / (right-left);
        frustum.m11 = (2*near)/(top-bottom);
        frustum.m21 = (top+bottom)/(top-bottom);
        frustum.m22 = - (far+near)/(far-near);
        frustum.m32 = - (2*near*far)/(far-near);
        frustum.m23 = -1;
        return frustum;
    }

    public static class SimpleCamera extends Camera {

        public Matrix4f getViewMatrix() {
            return viewMatrix;
        }

        public Matrix4f getProjectionMatrix() {
            return projectionMatrix;
        }

        private Matrix4f viewMatrix;
        private Matrix4f projectionMatrix;

        public SimpleCamera(float hfov, float aspectRatio,float near, float far)
        {
            Log.logger.debug("Simple Camera created as: hfov={} aspectRatio={} near={} far={}",
                    hfov,aspectRatio,near,far);
            projectionMatrix = getProjection(hfov, aspectRatio, near, far);
            viewMatrix = new Matrix4f();
            viewMatrix.setIdentity();
            Log.logger.info("Simple Camera created Projection Matrix as: \r\n{}",
                    Log.matrixToString(projectionMatrix));
            Log.logger.info("Simple Camera created View Matrix as: \r\n{}",
                    Log.matrixToString(viewMatrix));
        }
    }
}
