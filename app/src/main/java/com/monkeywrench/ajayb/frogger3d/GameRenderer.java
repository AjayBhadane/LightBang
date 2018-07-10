package com.monkeywrench.ajayb.frogger3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES30.*;

import static android.opengl.Matrix.*;

import GLES.util.*;

public class GameRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "GameRenderer";

    private Context context;
    private FloatBuffer vertexBuffer, colorBuffer;
    private IntBuffer indicesBuffer;
    private Program program;
    private static final int VERTEX_POS_INDEX = 1;
    private static final int VERTEX_COLOR_INDEX = 0;

    private static final int NUM_INSTANCES = 4;
    private float orthoMat[] = new float[16];
    private float finalMat[] = new float[16];
    private int mvpMatrixHandle;

    float[] pos = {
        1, 0, 0, 0.2f,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    };

    float[] scale = {
        0.25f, 0, 0, 0.25f,
        0, 0.25f, 0, 0,
        0, 0, 0.25f, 0,
        0, 0, 0, 1
    };

    public GameRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.7f, 0.7f, 0.7f,1.0f);
        Shader vert_shader = new Shader(R.raw.v_shader, "vert", context);
        Shader frag_shader = new Shader(R.raw.f_shader, "frag", context);
        program = new Program(vert_shader, frag_shader);
        program.use_program();

        float[] vertices = {
                0.5f, 0.5f,
                -0.5f, 0.5f,
                -0.5f, -0.5f,
                0.5f, -0.5f,
        };

        int[] indices = {
                0,1,2,3
        };

        float[] color = {0.0f, 0.7f, 0.0f, 1.0f};
        //-----------------------------------------------------------------------------------------|
        // Store vertices
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        //-----------------------------------------------------------------------------------------|

        //-----------------------------------------------------------------------------------------|
        // Store vertices drawing order
        ByteBuffer idByteBuffer = ByteBuffer.allocateDirect(indices.length * 4);
        idByteBuffer.order(ByteOrder.nativeOrder());

        indicesBuffer = idByteBuffer.asIntBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        //-----------------------------------------------------------------------------------------|

        //-----------------------------------------------------------------------------------------|
        // Store color
        ByteBuffer cbByteBuffer = ByteBuffer.allocateDirect(color.length * 4);
        cbByteBuffer.order(ByteOrder.nativeOrder());

        colorBuffer = cbByteBuffer.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);
        //-----------------------------------------------------------------------------------------|

        //-----------------------------------------------------------------------------------------|
        // Get handle to Model View Projection (M.V.P.) Matrix
        mvpMatrixHandle = glGetUniformLocation(program.getProgram(), "mvpMatrix");
        //-----------------------------------------------------------------------------------------|
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        setOrthoMat(i,i1);
    }

    private void setOrthoMat(int width, int height){
        //-----------------------------------------------------------------------------------------|
        //Calculate orthogonal matrix
        final float aspectRatio = width > height ?
                (float) width / height :
                (float)height / width;

        if (width > height){
            orthoM(orthoMat, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        }else{
            orthoM(orthoMat, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }

        Log.i(TAG,Float.toString(aspectRatio));
        //-----------------------------------------------------------------------------------------|
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);
        glVertexAttrib4fv(VERTEX_COLOR_INDEX, colorBuffer);
        glUniformMatrix4fv(mvpMatrixHandle, 1, true, orthoMat, 0);
        glVertexAttribPointer(VERTEX_POS_INDEX,2,GL_FLOAT,false,0,vertexBuffer);
        glEnableVertexAttribArray(VERTEX_POS_INDEX);
        glDrawElements(GL_LINE_LOOP , 4,GL_UNSIGNED_INT, indicesBuffer);
        glDisableVertexAttribArray(VERTEX_POS_INDEX);
    }
}
