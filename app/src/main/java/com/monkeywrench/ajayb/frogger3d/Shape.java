package com.monkeywrench.ajayb.frogger3d;

import android.content.Context;
import static android.opengl.GLES30.*;

import android.graphics.RectF;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import GLES.util.*;

public class Shape {

    private static final String TAG = "Shape";

    private int screenWidth, screenHeight;
    private Context context;
    private static Shape shape = null;
    private Program program;
    private FloatBuffer SQUARE_VERTEX_BUFFER,CIRCLE_VERTEX_BUFFER, COLOR_BUFFER;
    private IntBuffer SQUARE_INDICES_BUFFER;
    private float[] orthomat = new float[16];
    private boolean isOrthoMatSet = false;
    private float[] color;
    private int[] square_indices_order;

    private int orthhoMatHandle;

    private static final int VERTEX_COLOR_INDEX = 0;
    private static final int VERTEX_POS_INDEX = 1;

    private Shape(int width, int height, Context context){
        this.screenWidth = width; this.screenHeight = height;
        this.context = context;
        this.color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
        this.square_indices_order = new int[]{0,1,2,3};
        initBuffers();
        initBaseShaders();
        initHandlesToProgram();

    }

//    Returns the shape factory object

    public static Shape getShapeFactory(int width, int height, Context context){
        if ( shape == null ){
            shape = new Shape(width, height, context);
            }

        return shape;
    }

    private void initHandlesToProgram(){
        orthhoMatHandle = glGetUniformLocation(program.getProgram(), "orthoMat");
    }

    private void initBuffers(){
        ByteBuffer verticesByteBuffer = ByteBuffer.allocateDirect( 8 * 4); //Twice Vertices of square times bytes per float
        verticesByteBuffer.order(ByteOrder.nativeOrder());

        SQUARE_VERTEX_BUFFER = verticesByteBuffer.asFloatBuffer();

        ByteBuffer colorByteBuffer = ByteBuffer.allocateDirect(4 * 4); //Number of color components (4) times bytes per float
        colorByteBuffer.order(ByteOrder.nativeOrder());

        COLOR_BUFFER = colorByteBuffer.asFloatBuffer();

        ByteBuffer indicesByteBuffer = ByteBuffer.allocateDirect(4 * 4); //Number of vertices timees bytes per float
        indicesByteBuffer.order(ByteOrder.nativeOrder());

        SQUARE_INDICES_BUFFER = indicesByteBuffer.asIntBuffer();
    }

    private void initBaseShaders(){

        Shader vert_shader = new Shader(R.raw.v_base_shader, "vert", this.context);
        Shader frag_shader = new Shader(R.raw.f_base_shader, "frag", this.context);
        program = new Program(vert_shader, frag_shader);
        program.use_program();
    }

    public void drawCircle(){
        if (isOrthoMatSet){

            // Do drawing

        }

        else{
            Log.e(this.TAG, "Orthogonal projection matrix not set!");
        }
    }

    public void drawSquare(float top, float left, float width, float height){
        if (isOrthoMatSet){
            top = map(top, 0, this.screenWidth, -1, 1);
            left = map(left, 0, this.screenHeight, -1, 1);
            width = map(width, 0, this.screenWidth, 0, 2);
            height = map(height, 0, this.screenHeight, 0,2);

            // Anticlock wise direction
            float[] vertices = {
                left + width, top,      //1'st vertex (x1 = left + width, y1 = top)
                left, top,              //2'nd vertex (x2 = left, y2 = top)
                left, -top,             //3'rd vertex (x3 = left, y3 = -top)
                left + width, -top      //4'th vertex (x4 = left + width, y4 - top)
            };

            Log.i(TAG, Arrays.toString(vertices));

            this.SQUARE_VERTEX_BUFFER.put(vertices);
            this.SQUARE_VERTEX_BUFFER.position(0);

            this.COLOR_BUFFER.put(this.color);
            this.COLOR_BUFFER.position(0);

            this.SQUARE_INDICES_BUFFER.put(this.square_indices_order);
            this.SQUARE_INDICES_BUFFER.position(0);

            glVertexAttrib4fv(VERTEX_COLOR_INDEX, this.COLOR_BUFFER);
            glUniformMatrix4fv(this.orthhoMatHandle, 1, true, this.orthomat, 0);
            glVertexAttribPointer(VERTEX_POS_INDEX,2,GL_FLOAT,false,0,this.SQUARE_VERTEX_BUFFER);
            glEnableVertexAttribArray(VERTEX_POS_INDEX);
            glDrawElements(GL_LINE_LOOP , 4,GL_UNSIGNED_INT, SQUARE_INDICES_BUFFER);
            glDisableVertexAttribArray(VERTEX_POS_INDEX);
        }

        else{
            Log.e(this.TAG, "Orthogonal projection matrix not set!");
        }
    }

    public void setOrthoMat(float[] orthoMat){
        this.orthomat = orthoMat;
        this.isOrthoMatSet = true;
    }

    public void serColor(){

    }

    private float map(float x, float in_min, float in_max, float out_min, float out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
