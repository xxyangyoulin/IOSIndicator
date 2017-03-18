package com.mnnyang.iosindicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mnnyang on 2017/3/13/013.
 */

public class IOSIndicator extends View {

    /**
     * 字体大小 sp
     */
    private int textSize = 16;

    /**
     * 边框宽度
     */
    private float strokeWidth = 1;

    /**
     * 背景颜色
     */
    private int backgroundColor = 0xFFFFFFFF;

    /**
     * 圆角半径
     */
    private int radius = 5;


    /**
     * 初始化背景边框
     */
    private boolean isInitBorder;

    /**
     * item选中颜色
     */

    private int itemSelectedColor = 0xFF3787B4;
    /**
     * 框线颜色
     */
    private int strokeColor = 0xFF3787B4;

    /**
     * 文字颜色
     */
    private int textNormalColor = 0xFF3787B4;
    private int textSelectedColor = 0xFFEEEEEE;

    /**
     * item标题
     */
    private List<String> itemTitles;

    /**
     * item的数量
     */
    private int itemNumber;

    /**
     * item的宽度
     */
    private float itemWidth;

    /**
     * 宽高
     */
    private int height;
    private int width;

    /**
     * 当前页
     */
    private int currentPage = 0;

    private Paint paint;

    List<Path> pathList = new ArrayList<Path>();


    public IOSIndicator(Context context) {
        super(context);
        init();
    }

    public IOSIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    /**
     * 初始化
     */
    private void init() {

        initPaint();
    }

    /**
     * 初始化 画笔
     */
    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(dp2px(strokeWidth));
        paint.setTextSize(dp2px(textSize));
        paint.setLinearText(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 初始化path
     */
    private void initPath() {

        int radius = dp2px(this.radius);

        for (int i = 0; i < itemNumber; i++) {
            Path path = new Path();

            //第一个
            if (i == 0) {
                path.moveTo(0, radius);
                path.quadTo(0, 0, radius, 0);
                path.lineTo(itemWidth, 0);
                path.lineTo(itemWidth, height);
                path.lineTo(radius, height);
                path.quadTo(0, height, 0, height - radius);
                path.close();
                //最后一个
            } else if (i == itemNumber - 1) {
                path.moveTo(itemWidth * i, 0);
                path.lineTo(width - radius, 0);
                path.quadTo(width, 0, width, radius);
                path.lineTo(width, height - radius);
                path.quadTo(width, height, width - radius, height);
                path.lineTo(itemWidth * i, height);
                path.close();

                //中间的
            } else {
                path.moveTo(itemWidth * i, 0);
                path.lineTo(itemWidth * (i + 1), 0);
                path.lineTo(itemWidth * (i + 1), height);
                path.lineTo(itemWidth * i, height);
                path.close();
            }

            pathList.add(path);

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);

        if (itemNumber < 2) {
            throwException();
        }

        itemWidth = (width / itemNumber);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);

        /**绘制背景外框线*/
        if (!isInitBorder) {
            isInitBorder = true;
            initPath();//初始化path

            GradientDrawable gd = new GradientDrawable();
            gd.setColor(backgroundColor);
            gd.setCornerRadius(dp2px(5));
            gd.setStroke(dp2px(strokeWidth), strokeColor);
            this.setBackgroundDrawable(gd);
        }

        /**绘制竖线*/
        for (int i = 1; i < itemNumber; i++) {
            canvas.drawLine(itemWidth * i, 0, itemWidth * i, height, paint);
        }

        /**
         * 绘制选择背景
         */
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(itemSelectedColor);


        for (int i = 0; i < itemNumber; i++) {
            if (currentPage == i) {
                canvas.drawPath(pathList.get(i), paint);
            }
        }


        /**绘制文字*/
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        //基线中间点的y轴计算公式
        int baseLineY = (int) (height / 2 - top / 2 - bottom / 2);

        for (int i = 0; i < itemNumber; i++) {

            if (currentPage != i) {
                paint.setColor(textNormalColor);
            } else {
                paint.setColor(textSelectedColor);
            }

            canvas.drawText(itemTitles.get(i), itemWidth / 2f + itemWidth * i, baseLineY, paint);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                handleActionUp(event.getX(), event.getY());
                break;
            default:
                break;
        }

        return true;
    }


    private void handleActionUp(float x, float y) {
        if (y <= height && y >= 0) {//手指移除范围
            int newCurrentPage = (int) (x / itemWidth);

            if (currentPage == newCurrentPage) {
                //已经是当前页
                if (mListener != null) {
                    mListener.onClick(newCurrentPage, true);
                }
            } else {
                if (mListener != null) {
                    mListener.onClick(newCurrentPage, false);
                }
                currentPage = newCurrentPage;
            }

            invalidate();
        }
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        invalidate();
    }

    private ClickListener mListener;

    /**
     * 设置item切换监听
     * @param listener
     */
    public void setSwitchListener(ClickListener listener) {
        mListener = listener;
    }

    public interface ClickListener {
        /**
         *
         * @param currentIndex 当前点击页数
         * @param isRepeat 是否重复点击
         */
        void onClick(int currentIndex, boolean isRepeat);
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*
     * set / get
     *
     */
    public int getTextSize() {
        return textSize;
    }

    public IOSIndicator setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public List<String> getItemTitles() {
        return itemTitles;
    }

    public IOSIndicator setItemTitles(List<String> itemTitles) {

        this.itemTitles = itemTitles;
        itemNumber = itemTitles.size();

        if (itemNumber < 2) {
            throwException();
        }

        return this;
    }

    private void throwException() {
        throw new IllegalArgumentException("The number of the item may not be less than 2");
    }

    public int getNormalBackgroundColor() {
        return backgroundColor;
    }

    public IOSIndicator setNormalBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public IOSIndicator setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public int getItemSelectedColor() {
        return itemSelectedColor;
    }

    public IOSIndicator setItemSelectedColor(int itemSelectedColor) {
        this.itemSelectedColor = itemSelectedColor;
        return this;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public IOSIndicator setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public int getTextNormalColor() {
        return textNormalColor;
    }

    public IOSIndicator setTextNormalColor(int textNormalColor) {
        this.textNormalColor = textNormalColor;
        return this;
    }

    public int getTextSelectedColor() {
        return textSelectedColor;
    }

    public IOSIndicator setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
        return this;
    }
}



