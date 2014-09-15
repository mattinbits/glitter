package com.mjlivesey.gl.util;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Created by Matthew on 03/08/2014.
 */
public abstract class Application {

    private long startTime = 0;

    public Application(String name)
    {
        Log.initialize(name);
    }


    public void resetTime()
    {
        startTime=System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return System.currentTimeMillis()-startTime;
    }

    public void init(int width, int height) throws RuntimeException
    {
        init(width,height,true,false);
    }

    public void init(int width, int height, boolean fullScreen) throws RuntimeException
    {
        init(width, height, fullScreen, true);
    }

    public void init(boolean fullScreen) throws RuntimeException
    {
        init(fullScreen,true);

    }

    public void init(int width, int height, boolean fullScreen, boolean vSync)  throws RuntimeException
    {
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setFullscreen(fullScreen);
            Display.setVSyncEnabled(vSync);
            Display.create();
        }catch(LWJGLException ex)
        {
            throw new RuntimeException("Error creating Display", ex);
        }
    }

    public void init(boolean fullScreen, boolean vSync) throws RuntimeException
    {
        try{
            Display.setDisplayMode(Display.getDesktopDisplayMode());
            Display.setVSyncEnabled(vSync);
            Display.setFullscreen(fullScreen);
            Display.create();
        }catch (LWJGLException ex)
        {
            throw new RuntimeException("Error creating Display", ex);
        }
    }

    public void init()
    {
        init(true,true);
    }

    protected void update()
    {
        Display.update();
    }

    abstract public void exit();

}
