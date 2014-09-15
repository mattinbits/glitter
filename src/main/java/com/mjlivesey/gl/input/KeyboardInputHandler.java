package com.mjlivesey.gl.input;

import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Created by Matthew on 03/08/2014.
 */
public class KeyboardInputHandler {

    List<InputAction> actions;

    public void  initialize(List<InputAction> actions) throws RuntimeException {
        this.actions = actions;
        try {
            Keyboard.create();
            Keyboard.enableRepeatEvents(true);
        }catch (LWJGLException ex)
        {
            throw new RuntimeException("Failed to initialize LWJGL keyboard");
        }
    }

    public void checkForInput()
    {
        while (Keyboard.next()) {
            if(Log.logger.getLevel()== Level.TRACE)
                Log.logger.trace("KeyboardInputHandler.checkForInput() Input Received: {}",
                        Keyboard.getKeyName(Keyboard.getEventKey()));
            for (InputAction action: actions)
            {
                InputAction.Direction d =
                        (Keyboard.getEventKeyState()) ? InputAction.Direction.down : InputAction.Direction.up;
                action.action(InputAction.Source.keyboard, d, Keyboard.getEventKey());
            }
        }
    }
}
