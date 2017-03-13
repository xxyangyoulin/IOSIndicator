package com.mnnyang.iosindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mnnyang on 2017/3/13/013.
 */

public class SDF extends View {

    private Paint paint;
    private Path path;

    public SDF(Context context) {
        super(context);
    }

    public SDF(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);

        path = new Path();
        path.moveTo(10,10);
        path.lineTo(100,10);
        path.lineTo(100,100);
        path.lineTo(10,100);
        path.close();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);






        canvas.drawPath(path,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        paint.setStyle(Paint.Style.STROKE);
        invalidate();

        return true;
    }
}
