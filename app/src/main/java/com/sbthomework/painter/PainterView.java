package com.sbthomework.painter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 22.04.2017.
 */

public class PainterView extends View {

    private Bitmap mBitmap;
    private Canvas mBitmapCanvas;
    Path path;


    private float startX = 0.0f;
    private float startY = 0.0f;

    private float endX = 0.0f;
    private float endY = 0.0f;

    private Paint mEditModePaint = new Paint();


    public PainterView(Context context) {
        super(context);
        init();
    }

    public PainterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PainterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PainterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (getRootView().isInEditMode()) {
            mEditModePaint.setColor(Color.MAGENTA);
        } else {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(getResources().getColor(R.color.colorPrimary));
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(getResources().getDimension(R.dimen.default_paint_width));

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, 0, 0, null);
                mBitmap.recycle();
            }

            mBitmap = bitmap;
            mBitmapCanvas = canvas;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                startX = event.getX();
                startY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:

                mBitmapCanvas.drawOval(startX, startY, event.getX(), event.getY(), mEditModePaint);
                mBitmapCanvas.save();
                invalidate();
                return true;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
//                mBitmapCanvas.drawOval(startX,startY,event.getX(),event.getY(),mEditModePaint);
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawRect(getWidth() / 10, getHeight() / 10, (getWidth() / 10) * 9,
                    (getHeight() / 10) * 9, mEditModePaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * Очищает нарисованное
     */
    public void clear() {
        mBitmapCanvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}


