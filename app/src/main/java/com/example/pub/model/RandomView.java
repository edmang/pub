package com.example.pub.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.pub.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RandomView extends View {

    private static final int MAX_COUNT_DOWN = 2500;
    private static final int INTERVAL = 30;
    private static final int LOGO_SIZE = 375;

    private CountDownTimer countDownTimer;
    private Bitmap bitmapDefault;
    private Bitmap bitmapQuestion;
    private Bitmap bitmapIntruder;
    private Map<Integer, Pointer> pointers = new ConcurrentHashMap<>();

    public RandomView(Context context) {
        super(context);
        init();
    }

    public RandomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RandomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RandomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.bitmapDefault = drawableToBitmap(getResources().getDrawable(R.drawable.corona));
        this.bitmapQuestion = drawableToBitmap(getResources().getDrawable(R.drawable.question));
        this.bitmapIntruder = drawableToBitmap(getResources().getDrawable(R.drawable.schwein));

        this.countDownTimer = new CountDownTimer(MAX_COUNT_DOWN, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                (new Runnable() {
                    @Override
                    public void run() {
                        resetAllBalls();
                        int luckyId = getRandomNumber();
                        if (pointers.get(luckyId) != null) {
                            pointers.get(luckyId).setQuestion();
                        }
                        invalidate();
                    }
                }).run();
            }

            @Override
            public void onFinish() {
                resetAllBalls();
                int luckyId = getRandomNumber();
                if (pointers.get(luckyId) != null) {
                    pointers.get(luckyId).setIntruder();
                }
                invalidate();
            }

            private int getRandomNumber() {
                Random random = new Random();
                int luckyNumber = random.nextInt(pointers.size());
                List<Integer> ids = new ArrayList<>(pointers.keySet());
                return ids.get(luckyNumber);
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Pointer pointer : pointers.values()) {
            canvas.drawBitmap(pointer.getBitmapToBeDrawn(), pointer.getX() - bitmapDefault.getWidth() / 2, pointer.getY() - bitmapDefault.getHeight() / 2, pointer.getPoint());
        }
    }

    private void resetAllBalls() {
        for (Pointer pointer : pointers.values()) {
            pointer.resetToInitial();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Pointer newAddedPointer = new Pointer(bitmapDefault, bitmapQuestion, bitmapIntruder);
                newAddedPointer.setPosition(event.getX(pointerIndex), event.getY(pointerIndex));
                this.pointers.put(pointerId, newAddedPointer);
                this.resetAllBalls();

                if (pointers.size() > 1) {
                    countDownTimer.start();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    Pointer currentPointer = pointers.get(event.getPointerId(i));
                    if (currentPointer != null) {
                        currentPointer.setPosition(event.getX(i), event.getY(i));
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                this.pointers.remove(pointerId);
                resetAllBalls();
                countDownTimer.cancel();
                break;
        }
        invalidate();
        return true;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(LOGO_SIZE, LOGO_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
