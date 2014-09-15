package com.mjlivesey.gl.util;

/**
 * Created by Matthew on 10/08/2014.
 */
public class FPSMeasure {

    int frames = 0;
    long lastTimer = 0;
    float mostRecentFPS = 0;
    int interval;

    public FPSMeasure(int interval)
    {
        this.interval=interval;
    }

    public FPSMeasure(){
        this(10000);
    }

    public void frame(long elapsedTime){
        frames++;
        if(elapsedTime -lastTimer > interval)
        {
            mostRecentFPS = ((float)frames) / (elapsedTime -lastTimer) * 1000.0f;
            Log.logger.info("FPS: {}", mostRecentFPS);
            frames = 0;
            lastTimer = elapsedTime;
        }
    }
}
