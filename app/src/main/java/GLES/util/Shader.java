package GLES.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FALSE;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_INFO_LOG_LENGTH;
import static android.opengl.GLES20.GL_TRUE;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glShaderSource;

/**
 * Created by ajayb on 03-12-2017.
 */

public class Shader {
    private int shader = 0;
    private static final String TAG  = "SHADER";
    private boolean success = false;
    public Shader(int id, String type, Context context){
        StringBuilder source = new StringBuilder();

        try{
            Resources res = context.getResources();
            InputStream ins = res.openRawResource(id);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = reader.readLine();
            while(line != null){
                source.append(line);
                source.append("\n");
                line = reader.readLine();
            }
         }catch (Exception e){
            e.printStackTrace();
        }
        if (type.equals("vert")) {
            this.shader = glCreateShader(GL_VERTEX_SHADER);
        }else if(type.equals("frag")){
            this.shader = glCreateShader(GL_FRAGMENT_SHADER);
        }

        glShaderSource(shader, source.toString());
        glCompileShader(shader);

        int success[] = new int[1];

        glGetShaderiv(shader, GL_COMPILE_STATUS, success, 0);
        switch(success[0]){
            case GL_TRUE:
                this.success = true;
                break;
            case GL_FALSE:
                int length[] = new int[1];
                glGetShaderiv(shader, GL_INFO_LOG_LENGTH, length, 0);

                if (length[0] > 1){
                    String info_log =  glGetShaderInfoLog(shader);
                    Log.e(TAG, "Shader source\n: "+source.toString());
                    Log.e(TAG, "Shader info log: "+info_log);
                }
                break;
        }
    }

    public int getShader(){
        if (this.success){
            return this.shader;
        }else{
            return -1;
        }
    }
}
