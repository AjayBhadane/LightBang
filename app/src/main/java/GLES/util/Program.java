package GLES.util;
import android.util.Log;

import static android.opengl.GLES20.GL_FALSE;
import static android.opengl.GLES20.GL_INFO_LOG_LENGTH;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_TRUE;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glUseProgram;


/**
 * Created by ajayb on 03-12-2017.
 */

public class Program {
    private int program;
    private static final String TAG = "PROGRAM";
    private boolean success = false;
    public Program(Shader... shaders){
        this.program = glCreateProgram();

        for(Shader shader : shaders){
            glAttachShader(this.program, shader.getShader());
        }
    }

    public boolean use_program(){
        glLinkProgram(this.program);
        int success[] = new int[1];
        glGetProgramiv(this.program, GL_LINK_STATUS, success, 0);
        switch(success[0]){
            case GL_TRUE:
                this.success = true;
                glUseProgram(this.program);
                break;
            case GL_FALSE:
                int length[] = new int[1];
                glGetProgramiv(this.program, GL_INFO_LOG_LENGTH, length, 0);
                if (length[0] > 1) {
                    Log.e(TAG, "use_program:\n" + glGetProgramInfoLog(this.program));
                }
                break;
        }

        return this.success;
    }

    public int getProgram(){
        if (! this.success){
            return -1;
        }
        return this.program;
    }
}
