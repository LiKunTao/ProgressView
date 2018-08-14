package com.lkt.pview.progerssveiw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lkt.pview.progerssveiw.R;
import com.lkt.pview.progerssveiw.bean.PointBean;
import com.lkt.pview.progerssveiw.utils.DensityUtils;
import com.lkt.pview.progerssveiw.viewinterface.IOnPointClick;

import java.util.ArrayList;
import java.util.List;

public class ProgressView extends View {

    private List<PointBean> datas;//要显示的数据源
    private int width;
    private TextPaint textPaint = new TextPaint();//文字画笔
    private Paint linePaint = new Paint();//画笔
    private Paint boldPaint = new Paint();//竖线
    private Paint imagePaint = new Paint();//画图片
    private Bitmap bitmapBlue, bitmapRed, bitmapEmpty;
    private int lastX, lastY;
    private int bitmapTag = 0;//根据该值判断bitmap
    private int hightOfCell = 150;//单行的高度
    private int paddingOfLine = 10;//横竖线距离bitmap的间距
    private int hight;


    public ProgressView(Context context) {
        super(context);
        initView(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        hightOfCell = DensityUtils.dp2px(context, 80);
        paddingOfLine = DensityUtils.dp2px(context, 4);
        bitmapBlue = BitmapFactory.decodeResource(getResources(), R.drawable.icon_solid_blue);
        bitmapRed = BitmapFactory.decodeResource(getResources(), R.drawable.icon_solid_red);
        bitmapEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.icon_solid_empty);

        //文字画笔
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(DensityUtils.dp2px(context, 1));
        textPaint.setTextSize(DensityUtils.dp2px(context, 10));
        textPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));

        //横线
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(DensityUtils.dp2px(context, 2));
        linePaint.setColor(getResources().getColor(R.color.colorPrimary));

        //横线
        boldPaint.setAntiAlias(true);
        boldPaint.setStyle(Paint.Style.FILL);
        boldPaint.setStrokeWidth(DensityUtils.dp2px(context, 2));
        boldPaint.setColor(getResources().getColor(R.color.colorPrimary));

        //节点
        imagePaint.setAntiAlias(true);
        imagePaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        if (datas != null) {
            int line = 0;
            if (datas.size() % 6 == 0) {
                line = datas.size() / 6;
            } else {
                line = datas.size() / 6 + 1;
            }
            setMeasuredDimension(width, hightOfCell * line);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int temp = 0;
        if (datas == null) {
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            int lineIndex = i / 6;//当前索引对应的行号
            lastY = hightOfCell / 2 + hightOfCell * lineIndex;//Y坐标
            if (lineIndex % 2 == 1) {
                temp--;
                lastX = width / 12 + temp * (width / 6);
            } else {
                lastX = width / 12 + temp * (width / 6);
                temp++;
            }
            Point point = new Point(lastX, lastY);
            pointList.add(point);
            PointBean pointBean = datas.get(i);
            String action = pointBean.getAction();
            //画圆和上下的文字
            if ("submit".equals(action)) {
                bitmapTag = 0;
                drawCell(lastX, lastY, canvas, pointBean.getDate(), pointBean.getUser(), bitmapBlue);
            } else if ("refuse".equals(action)) {
                bitmapTag = 2;
                drawCell(lastX, lastY, canvas, pointBean.getDate(), pointBean.getUser(), bitmapRed);
            } else if ("待审批".equals(action)) {
                bitmapTag = 1;
                drawCell(lastX, lastY, canvas, pointBean.getDate(), pointBean.getUser(), bitmapEmpty);
            }
            if (i != 0 && (i + 1) % 6 == 0 && i != datas.size() - 1) {//画竖线，当结尾刚好在最右边时不画竖线
                drawLineByPaint(canvas, lastX, (int) (lastY + bitmapBlue.getHeight() / 2 + paddingOfLine + textPaint.getTextSize()) + 20, lastX, (int) (lastY + hightOfCell - bitmapBlue.getHeight() / 2 - paddingOfLine - textPaint.getTextSize()) - 20);
            } else {//横线
                if (lineIndex % 2 == 1) {//奇数行
                    if (i != datas.size() - 1) {
                        drawLineByPaint(canvas, lastX - bitmapBlue.getWidth() / 2 - paddingOfLine, lastY, lastX - width / 6 + bitmapBlue.getWidth() / 2 + paddingOfLine, lastY);
                    }
                } else {//偶数行
                    if (i != datas.size() - 1) {
                        drawLineByPaint(canvas, lastX + bitmapBlue.getWidth() / 2 + paddingOfLine, lastY, lastX + width / 6 - bitmapBlue.getWidth() / 2 - paddingOfLine, lastY);
                    }
                }
            }
        }
    }


    public void setData(List<PointBean> datas) {
        this.datas = datas;
        /**
         * 添加数据前将点的集合清空，防止数据重复
         */
        pointList.clear();
        /**
         * 会重新执行onMeasure()、onLayout()、onDraw()、方法，
         * 在频繁调用此方法时onDraw()会不执行，或者执行没效果，
         * 所以要重新调用invalidate()，
         * 防止view数据更新时不重新绘制
         */
        requestLayout();
        invalidate();
    }

    /**
     * 点的集合，每次绘制的时候添加进去
     */
    private List<Point> pointList = new ArrayList<>();

    private void drawCell(int circleX, int circleY, Canvas canvas, String topString, String bottomString, Bitmap bitmap) {

        canvas.drawBitmap(bitmap, circleX - bitmap.getWidth() / 2, circleY - bitmap.getHeight() / 2, imagePaint);
        float topLength = textPaint.measureText(topString);
        float bottomLength = textPaint.measureText(bottomString);
        canvas.drawText(topString, circleX - topLength / 2, circleY - bitmap.getHeight() / 2 - DensityUtils.dp2px(getContext(), 10), textPaint);
        canvas.drawText(bottomString, circleX - bottomLength / 2, circleY + bitmap.getHeight() / 2 + DensityUtils.dp2px(getContext(), 20), textPaint);
    }

    private void drawLineByPaint(Canvas canvas, int startX, int startY, int endX, int endY) {
        canvas.drawLine(startX, startY, endX, endY, boldPaint);
    }

    private IOnPointClick iOnPointClick;

    public void setiOnPointClick(IOnPointClick iOnPointClick) {
        this.iOnPointClick = iOnPointClick;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (isInCircle(x, y)) {
                    if (null != iOnPointClick) {
                        iOnPointClick.onPointClick(positionOfClick);
                    }
                }
                break;
        }
        return true;
    }

    private int positionOfClick = -1;

    /**
     * 判断当前点击的位置是否点击在红点上
     *
     * @param x
     * @param y
     */
    private boolean isInCircle(float x, float y) {
        int width = bitmapBlue.getWidth();

        boolean b = false;
        for (int i = 0; i < pointList.size(); i++) {
            Point point = pointList.get(i);
            if (x >= point.x - width && x <= point.x + width && y >= point.y - width && y <= point.y + width) {
                b = true;
                positionOfClick = i;
            }
        }
        return b;
    }
}
