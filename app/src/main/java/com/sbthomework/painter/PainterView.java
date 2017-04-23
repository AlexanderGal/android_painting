package com.sbthomework.painter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 22.04.2017.
 */

public class PainterView extends View {
    public static final int LINE_DRAW = 0;
    public static final int RECT_DRAW = 1;
    public static final int CIRCLE_DRAW = 2;
    public static final int MULTY_MODE = 0;
    public static final int ERASE_MODE = 1;
    private int currentMode = 0;

    private Bitmap mBitmap;

    private Canvas mBitmapCanvas;
    private Paint[] mPredefinedPaints;
    private int mNextPaint = 0;
    private PointF point;

    private Paint mEditModePaint = new Paint();

    private int currentCanvasType;
    private SparseArray<Paint> mPaints = new SparseArray<>(10);

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
            TypedArray ta = getResources().obtainTypedArray(R.array.paint_colors);
            mPredefinedPaints = new Paint[ta.length()];

            for (int i = 0; i < ta.length(); i++) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(ta.getColor(i, 0));
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeWidth(getResources().getDimension(R.dimen.default_paint_width));
                mPredefinedPaints[i] = paint;
            }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerId = event.getPointerId(event.getActionIndex());
                point = new PointF(event.getX(event.getActionIndex()), event.getY(event.getActionIndex()));
                mPaints.put(pointerId, mPredefinedPaints[mNextPaint % mPredefinedPaints.length]);
                mNextPaint++;
                return true;
            case MotionEvent.ACTION_MOVE:
                Paint paint = mPaints.get(event.getPointerId(event.getActionIndex()));

                if (point != null) {
                    float x = event.getX();
                    float y = event.getY();
                    drawCurrentType(point.x, point.y, x, y, paint);
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                return true;
        }

        return super.onTouchEvent(event);
    }

    private void drawCurrentType(float startX, float startY, float endX, float endY, Paint paint) {
        if (currentCanvasType != 0 && currentMode == 1) {
            clear();
        }
        switch (currentCanvasType) {
            case RECT_DRAW:
                mBitmapCanvas.drawRect(startX, startY, endX, endY, paint);
                break;
            case CIRCLE_DRAW:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBitmapCanvas.drawOval(startX, startY, endX, endY, paint);
                    break;
                }
            default:
            case LINE_DRAW:
                mBitmapCanvas.drawLine(startX, startY, endX, endY, paint);
                point.x = endX;
                point.y = endY;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawRect(getWidth() / 10, getHeight() / 10, (getWidth() / 10) * 9,
                    (getHeight() / 10) * 9, mEditModePaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    public void clear() {
        mBitmapCanvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setCurrentCanvasType(int currentCanvasType) {

        this.currentCanvasType = currentCanvasType;
    }

    public void setMode(int mode) {
      currentMode = mode;
    }
}


