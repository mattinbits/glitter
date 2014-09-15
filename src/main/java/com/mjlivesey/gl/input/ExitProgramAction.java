package com.mjlivesey.gl.input;

import com.mjlivesey.gl.util.Application;
import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;

/**
 * Created by Matthew on 03/08/2014.
 */
public class ExitProgramAction implements InputAction {

    private Application app;
    private int key;

    public ExitProgramAction(Application app)
    {
        this(app,InputAction.KEY_ESCAPE);
    }

    public ExitProgramAction(Application app, int key)
    {
        this.app = app;
        this.key=key;
    }


    @Override
    public void action(Source s, Direction d, int button) {

        if (s==Source.keyboard && d == Direction.up && button == key)
        {
            if(Log.logger.getLevel()== Level.DEBUG)
                Log.logger.debug("ExitProgramAction - Input Received: {} {} {}",
                    s.toString(),d.toString(),button);
            app.exit();
        }

    }
}
