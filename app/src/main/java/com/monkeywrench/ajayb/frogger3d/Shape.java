package com.monkeywrench.ajayb.frogger3d;

public class Shape {

    private static Shape shape = null;

    private Shape(){

    }

//    Returns the shape factory object

    public static Shape getShapeFactory(){
        if ( shape == null ){
            shape = new Shape();
        }

        return shape;
    }

    public void drawCircle(){

    }

    public void drawSquare(){

    }
}
