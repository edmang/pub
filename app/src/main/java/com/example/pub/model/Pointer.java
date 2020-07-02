package com.example.pub.model;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class Pointer {

    private Paint point;
    private Bitmap bitmapInitial;
    private Bitmap bitmapToBeDrawn;
    private Bitmap bitmapQuestion;
    private Bitmap bitmapIntruder;
    private float x;
    private float y;

    public Pointer(Bitmap bitmapToBeDrawn, Bitmap bitmapQuestion, Bitmap bitmapIntruder) {
        point = new Paint();

        this.bitmapInitial = bitmapToBeDrawn;
        this.bitmapQuestion = bitmapQuestion;
        this.bitmapToBeDrawn = bitmapToBeDrawn;
        this.bitmapIntruder = bitmapIntruder;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Paint getPoint() {
        return point;
    }

    public void resetToInitial() {
        this.bitmapToBeDrawn = bitmapInitial;
    }

    public Bitmap getBitmapToBeDrawn() {
        return bitmapToBeDrawn;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setQuestion() {
        this.bitmapToBeDrawn = bitmapQuestion;
    }

    public void setIntruder() {
        this.bitmapToBeDrawn = bitmapIntruder;
    }
}
