package kr.go.molit.nhsnes.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * This view will auto determine the width or height by determining if the height or width is set
 * and scale the other dimension depending on the images dimension
 *
 * This view also contains an ImageChangeListener which calls changed(boolean isEmpty) once a change
 * has been made to the ImageView
 *
 * @author Maurycy Wojtowicz
 */
public class ScaleImageView extends ImageView {

  public interface OnComplateListener{

    public void onComplateLoad(ScaleImageView view, int width, int height, Object tag);

  }

  private ImageChangeListener imageChangeListener;
  private boolean scaleToWidth = false; // this flag determines if should measure height manually dependent of width
  private OnComplateListener onComplateListener;

  public ScaleImageView(Context context) {
    super(context);
    init();
  }

  public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public ScaleImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    this.setScaleType(ScaleType.CENTER_INSIDE);
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    super.setImageBitmap(bm);
    if (imageChangeListener != null)
      imageChangeListener.changed((bm == null));
  }

  @Override
  public void setImageDrawable(Drawable d) {
    super.setImageDrawable(d);
    if (imageChangeListener != null)
      imageChangeListener.changed((d == null));
  }

  @Override
  public void setImageResource(int id) {
    super.setImageResource(id);
  }

  public interface ImageChangeListener {
    // a callback for when a change has been made to this imageView
    void changed(boolean isEmpty);
  }

  public ImageChangeListener getImageChangeListener() {
    return imageChangeListener;
  }

  public void setImageChangeListener(ImageChangeListener imageChangeListener) {
    this.imageChangeListener = imageChangeListener;
  }

  public OnComplateListener getOnComplateListener() {
    return onComplateListener;
  }

  public void setOnComplateListener(OnComplateListener onComplateListener) {
    this.onComplateListener = onComplateListener;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);

    /**
     * if both width and height are set scale width first. modify in future if necessary
     */

    if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
      scaleToWidth = true;
    } else if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
      scaleToWidth = false;
    } else
      throw new IllegalStateException("width or height needs to be set to match_parent or a specific dimension");

    if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0) {
      // nothing to measure

      if (this.onComplateListener != null) {
        this.onComplateListener.onComplateLoad(this, widthMeasureSpec, heightMeasureSpec, getTag());
      }

      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      return;

    } else {
      if (scaleToWidth) {
        int iw = this.getDrawable().getIntrinsicWidth();
        int ih = this.getDrawable().getIntrinsicHeight();
        int heightC = width * ih / iw;
        if (height > 0)
          if (heightC > height) {
            // dont let hegiht be greater then set max
            heightC = height;
            width = heightC * iw / ih;
          }

        this.setScaleType(ScaleType.CENTER_CROP);

        if (this.onComplateListener != null) {
          this.onComplateListener.onComplateLoad(this, width, heightC, getTag());
        }

        setMeasuredDimension(width, heightC);



      } else {
        // need to scale to height instead
        int marg = 0;
        if (getParent() != null) {
          if (getParent().getParent() != null) {
            marg += ((RelativeLayout) getParent().getParent()).getPaddingTop();
            marg += ((RelativeLayout) getParent().getParent()).getPaddingBottom();
          }
        }

        int iw = this.getDrawable().getIntrinsicWidth();
        int ih = this.getDrawable().getIntrinsicHeight();

        width = height * iw / ih;
        height -= marg;

        if (this.onComplateListener != null) {
          this.onComplateListener.onComplateLoad(this, width, height, getTag());
        }
        setMeasuredDimension(width, height);



      }

    }



  }

}
