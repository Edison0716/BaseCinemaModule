package com.junlong0716.base.module.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.junlong0716.base.module.R;


/**
 * Author: FBi.
 * Email: bofu1993@163.com.
 * Date: 29/09/2017
 */

public class RoundView extends FrameLayout {
  private float mRadius = 0;
  private float mStrokeWidth = 0;
  private int mStrokeColor = Color.TRANSPARENT;
  private Path mBoundPath = null;
  private Type mType = Type.Rect;// 默认为 圆角矩形,即未声明type属性时,默认当成圆角矩形.

  public RoundView(Context context) {
    this(context, null);
  }

  public RoundView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }
  /**
   *通过自定义属性,设置圆角大小,形状,边框宽度,边框颜色等.参考CardView的自定义属性.
   **/
  public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setWillNotDraw(false);
    TypedArray a = context.obtainStyledAttributes(attrs,
        R.styleable.RoundView);
    mRadius = a.getDimension(R.styleable.RoundView_roundViewRadius, mRadius);
    mStrokeWidth = a.getDimension(R.styleable.RoundView_roundViewStrokeWidth,
        mStrokeWidth);
    mStrokeColor = a.getColor(R.styleable.RoundView_roundViewStrokeColor,
        mStrokeColor);
    int shape = a.getInt(R.styleable.RoundView_roundViewShape, mType.getType());
    mType = Type.from(shape);
    a.recycle();
  }

  public void setRadius(float radius) {
    if (mRadius == radius)
      return;
    this.mRadius = radius;
    postInvalidate();
  }

  public float getRadius() {
    return mRadius;
  }

  public void setStrokeWidth(float strok) {
    this.mStrokeWidth = strok;
    postInvalidate();
  }

  public float getStrokeWidth() {
    return mStrokeWidth;
  }

  public void setStrokeColor(int strokeColor) {
    this.mStrokeColor = strokeColor;
  }

  public int getStrokeColor() {
    return mStrokeColor;
  }

  //draw()函数是任何View在绘制自己前都会被调用的方法,通过在绘制自己前设置裁剪区,可以实现任意形状的布局.
  public void draw(Canvas canvas) {
    beforeDraw(canvas);
    super.draw(canvas);
  }
  //dispatchDraw是任何布局在内容绘制结束时都会被调用的方法.可以做一些其他操作,如描边等.
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    afterDraw(canvas);
  }

  //在绘制前计算出可以剪裁的矩形区域,并根据矩形区域设置自己的形状Path.
  private void beforeDraw(Canvas canvas) {
    Rect rect = new Rect();
    getLocalVisibleRect(rect);
    mBoundPath = onCaculatePath(rect);
    canvas.clipPath(mBoundPath);

    Log.i("RoundView", "beforeDraw");
  }

  //绘制结束后,可以给形状进行描边操作.
  private void afterDraw(Canvas canvas) {
    Rect rect = new Rect();
    getLocalVisibleRect(rect);
    // 进行描边操作.
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    p.setStyle(Paint.Style.STROKE);
    p.setColor(mStrokeColor);
    p.setStrokeWidth(mStrokeWidth);
    Path path = onGetPathStroke(rect, mBoundPath);
    if (path == null)
      return;
    canvas.drawPath(path, p);
  }

  protected Path onCaculatePath(Rect r) {
    switch (mType) {
      case Rect:
        return caculateRoundRectPath(r);
      case Circle:
        return caculateCirclePath(r);
      case Oval:
        return caculateOvalPath(r);
    }
    return caculateRoundRectPath(r);
  }

  protected Path onGetPathStroke(Rect r, Path boundPath) {
    switch (mType) {
      case Circle:
        return getCirclePathWithinStroke(r, boundPath);
      default:
        return getPathWithinStroke(r, boundPath);
    }
  }

  // 将path 进行变换,以容纳描边线宽.
  private Path getPathWithinStroke(Rect r, Path path) {
    if (mStrokeWidth <= 0)
      return path;

    // 防止边过宽,完全遮挡内容.
    int minWidth = r.width() > r.height() ? r.height() : r.width();
    if (minWidth <= 0)
      return null;

    if (mStrokeWidth >= minWidth / 2)
      mStrokeWidth = minWidth / 2.5f;

    Path p = new Path();
    Matrix matrix = new Matrix();
    float scaleX = (r.width() - mStrokeWidth / 2) / r.width();
    float scaleY = (r.height() - mStrokeWidth / 2) / r.height();

    matrix.setScale(scaleX, scaleY, r.centerX(), r.centerY());
    path.transform(matrix, p);
    return p;
  }

  private Path getCirclePathWithinStroke(Rect r, Path path) {
    if (mStrokeWidth <= 0)
      return path;
    // 防止边过宽,完全遮挡内容.
    int minWidth = r.width() > r.height() ? r.height() : r.width();
    if (minWidth <= 0)
      return null;

    if (mStrokeWidth >= minWidth / 2)
      mStrokeWidth = minWidth / 2.5f;

    Path p = new Path();
    Matrix matrix = new Matrix();
    float scale = (minWidth - mStrokeWidth / 2) / minWidth;

    matrix.setScale(scale, scale, r.centerX(), r.centerY());
    path.transform(matrix, p);
    return p;
  }

  // 以下方法留做备用,可用于生产各种外形的边框.仅供参考.
  private Path caculateRoundRectPath(Rect r) {
    Path path = new Path();
    float radius = getRadius();
    float elevation = 0;
    path.addRoundRect(new RectF(r.left + elevation, r.top + elevation,
            r.right - elevation, r.bottom - elevation), radius, radius,
        Path.Direction.CW);
    return path;
  }

  private Path caculateCirclePath(Rect r) {
    Path path = new Path();
    int radius = r.width() > r.height() ? r.height() / 2 : r.width() / 2;
    path.addCircle(r.left + radius, r.top + radius, radius,
        Path.Direction.CW);
    return path;
  }

  private Path caculateOvalPath(Rect r) {
    Path path = new Path();
    path.addOval(new RectF(r), Path.Direction.CW);
    return path;
  }

  public enum Type {
    Rect(0), Circle(1), Oval(2);
    private int type;

    Type(int type) {
      this.type = type;
    }

    public int getType() {
      return this.type;
    }

    public static Type from(int type) {
      switch (type) {
        case 0:
          return Rect;
        case 1:
          return Circle;
        case 2:
          return Oval;
        default:
          return Rect;
      }
    }
  }
}
