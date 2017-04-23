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
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class PainterView extends View {
    public static final int LINE_DRAW = 0;
    public static final int RECT_DRAW = 1;
    public static final int CIRCLE_DRAW = 2;
    public static final int ERASER = 3;
    public static final int DRAWABLE = 4;

    private Drawable drawable;
    private Bitmap mBitmap;

    private Canvas mBitmapCanvas;
    private Bitmap sBitmap;

    private Canvas sBitmapCanvas;
    private Paint[] mPredefinedPaints;
    private int mNextPaint = 0;
    private PointF point;

    private Paint mEditModePaint = new Paint();
    private Paint paint = null;
    private Paint eraser = new Paint();

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
        eraser.setAntiAlias(true);
        eraser.setColor(Color.WHITE);
        eraser.setStrokeJoin(Paint.Join.ROUND);
        eraser.setStrokeCap(Paint.Cap.SQUARE);
        eraser.setStrokeWidth(getResources().getDimension(R.dimen.eraser_width));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.rect_drawable,null);
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

            Bitmap sbitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas scanvas = new Canvas(sbitmap);

            if (sBitmap != null) {
                scanvas.drawBitmap(sBitmap, 0, 0, null);
                sBitmap.recycle();
            }

            sBitmap = sbitmap;
            sBitmapCanvas = scanvas;
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
                paint = mPaints.get(event.getPointerId(event.getActionIndex()));

                if (point != null) {
                    float x = event.getX();
                    float y = event.getY();
                    drawCurrentType(point.x, point.y, x, y, paint);
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:

                if (point != null && currentCanvasType != LINE_DRAW && currentCanvasType != ERASER) {
                    float x = event.getX();
                    float y = event.getY();
                    sdrawCurrentType(point.x, point.y, x, y, paint);
                }
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }

    private void drawCurrentType(float startX, float startY, float endX, float endY, Paint paint) {
        if (currentCanvasType != LINE_DRAW && currentCanvasType != ERASER) {
            clear();
        }
        switch (currentCanvasType) {
            case DRAWABLE:
                drawableDirectionDraw(startX,startY,endX,endY,mBitmapCanvas);
                break;
            case RECT_DRAW:
                mBitmapCanvas.drawRect(startX, startY, endX, endY, paint);
                break;
            case CIRCLE_DRAW:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBitmapCanvas.drawOval(startX, startY, endX, endY, paint);
                    break;
                }
            case ERASER:
                mBitmapCanvas.drawLine(startX, startY, endX, endY, eraser);
                sBitmapCanvas.drawLine(startX, startY, endX, endY, eraser);
                point.x = endX;
                point.y = endY;
                break;
            default:
            case LINE_DRAW:
                mBitmapCanvas.drawLine(startX, startY, endX, endY, paint);
                sBitmapCanvas.drawLine(startX, startY, endX, endY, paint);
                point.x = endX;
                point.y = endY;
                break;
        }
    }

    private void drawableDirectionDraw(float startX, float startY, float endX, float endY, Canvas canvas) {
        int iStartX = (int) startX;
        int iStartY = (int) startY;
        int iEndX = (int) endX;
        int iEndY = (int) endY;
        int tmp;
        if (iStartX > iEndX ){
            tmp = iEndX;
            iEndX = iStartX;
            iStartX = tmp;
        }
        if (iStartY > iEndY){
            tmp = iEndY;
            iEndY = iStartY;
            iStartY = tmp;
        }
        drawable.setBounds(iStartX,iStartY,iEndX,iEndY);
        drawable.draw(canvas);
    }

    private void sdrawCurrentType(float startX, float startY, float endX, float endY, Paint paint) {
        switch (currentCanvasType) {
            case DRAWABLE:
                drawableDirectionDraw(startX,startY,endX,endY,sBitmapCanvas);
                break;
            case RECT_DRAW:
                sBitmapCanvas.drawRect(startX, startY, endX, endY, paint);
                break;
            case CIRCLE_DRAW:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sBitmapCanvas.drawOval(startX, startY, endX, endY, paint);
                    break;
                }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            canvas.drawRect(getWidth() / 10, getHeight() / 10, (getWidth() / 10) * 9,
                    (getHeight() / 10) * 9, mEditModePaint);
        }

        canvas.drawBitmap(sBitmap, 0, 0, null);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    public void clear() {
        mBitmapCanvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void sClear() {
        sBitmapCanvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setCurrentCanvasType(int currentCanvasType) {
        this.currentCanvasType = currentCanvasType;
    }
}


