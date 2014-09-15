package com.mjlivesey.gl.render;

import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Matthew on 06/08/2014.
 */
public abstract class Program{

    public int getProgramId() {
        return programId;
    }


    protected int  programId;

    abstract public void compileAndLink();

    public static void printProgramLog(int programId) {
        int logLength = GL20.glGetProgram(programId, GL20.GL_INFO_LOG_LENGTH);
        Util.checkGLError();
        if(Log.logger.getLevel()== Level.DEBUG)
            Log.logger.debug("Program.printProgramLog() Log (length " + logLength + " chars)");
        String log = GL20.glGetProgramInfoLog(programId, logLength);
        Util.checkGLError();
        if(Log.logger.getLevel()== Level.DEBUG)
            Log.logger.debug(log);
    }

    public static void printShaderLog(int id) {
        int logLength = GL20.glGetShader(id, GL20.GL_INFO_LOG_LENGTH);
        Util.checkGLError();
        if(Log.logger.getLevel()== Level.DEBUG)
            Log.logger.debug("  Log (length " + logLength + " chars)");
        String log = GL20.glGetShaderInfoLog(id, logLength);
        Util.checkGLError();
        if(Log.logger.getLevel()== Level.DEBUG)
            Log.logger.debug(log);

    }

    public static String readShaderSourceCode(String file) throws IOException {
        String code = "";
        String line;
        InputStream fileStream = code.getClass().getResourceAsStream(file);
        BufferedReader reader = new BufferedReader( new InputStreamReader(fileStream));
        while ((line = reader.readLine()) != null) {
            code += line + "\n";
        }
        return code;
    }

}
