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
    private float orthoMat[] = new float[16];
    Shape shape;

    public GameRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.7f, 0.7f, 0.7f,1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        shape = Shape.getShapeFactory(i,i1,this.context);
        setOrthoMat(i,i1);
        shape.setOrthoMat(orthoMat);
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
        shape.drawSquare(200f,200f,50f,50f);
//        glVertexAttrib4fv(VERTEX_COLOR_INDEX, colorBuffer);
//        glUniformMatrix4fv(mvpMatrixHandle, 1, true, orthoMat, 0);
//        glVertexAttribPointer(VERTEX_POS_INDEX,2,GL_FLOAT,false,0,vertexBuffer);
//        glEnableVertexAttribArray(VERTEX_POS_INDEX);
//        glDrawElements(GL_LINE_LOOP , 4,GL_UNSIGNED_INT, indicesBuffer);
//        glDisableVertexAttribArray(VERTEX_POS_INDEX);
    }
}
