package com.modim.lan.lanandroid;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.modim.lan.lanandroid.LanStorage.mScreenHeight;
import static com.modim.lan.lanandroid.LanStorage.mScreenWidth;
import static com.modim.lan.lanandroid.NativeImplement.lanSetMapKind;
import static com.modim.lan.lanandroid.NativeImplement.lanSingleTouch;
import static com.modim.lan.lanandroid.NativeImplement.lanZoomByPosition;
import static java.lang.Math.abs;

class LANGLSurfaceView extends GLSurfaceView {
    private LANRenderer mRenderer;
    private final static String TAG = Constants.TAG;

    private static final int NONE = 9000;
    private static final int MOVE = 10000;
    private static final int ZOOM = 11000;
    private static final int ROTATION = 12000;
    private static final int INVALID_POINTER_ID = -1;

    private long prevTime = 0;
    private long nowTime;
    public double fX, fY, sX, sY;
    private int mTouchMode = NONE;
    private int ptrID1, ptrID2;

    private double zoom; // zoom
    private int distance; // distance
    private boolean isSignleFingerTap;
    private int prevMode;
    private float mAngle;

    private Handler mHandler    =   null;

    public LANGLSurfaceView(Context context) {
        super(context);

        // OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        Rect rect = getHolder().getSurfaceFrame();
        mRenderer = new LANRenderer(getHolder());
        setRenderer(mRenderer);
    }

    @Override
    public void onPause() {
        super.onPause();
        //mNative.lanPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mNative.lanResume();
    }

    private float angleBetweenLines (float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY)
    {
        float angle1 = (float) Math.atan2( (fY - sY), (fX - sX) );
        float angle2 = (float) Math.atan2( (nfY - nsY), (nfX - nsX) );

        float angle = ((float)Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return angle;
    }

    public void setEventHadler(Handler h){
        this.mHandler = h;
    }

    /**
    * 롱클릭 판단을 위한 핸들러
    * @author FIESTA
    * @since  오전 6:46
    **/
    final Handler handler = new Handler();
    private boolean isLongPress = false;
    private float beforeX = 0;
    private float beforeY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            nowTime = System.currentTimeMillis();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mTouchMode = NONE;
                    Log.d("터치이벤트", "MotionEvent.ACTION_DOWN");
                    ptrID1 = event.getPointerId(event.getActionIndex());
                    sX = event.getX(event.findPointerIndex(ptrID1));
                    sY = event.getY(event.findPointerIndex(ptrID1));
                    lanSingleTouch(Constants.NAVI_TOUCH_EVENT_DOWN, sX, sY);
                    //NativeImplement.lanSingleTouch(Constants.NAVI_TOUCH_EVENT_DOWN, sX, sY);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            isLongPress = true;

                            if(mHandler!=null){
                                Message m = mHandler.obtainMessage();
                                m.what = 111;
                                mHandler.sendMessage(m);
                            }
                        }

                    }, 1500);

                    beforeX = event.getX();
                    beforeY = event.getY();

                    break;
                case MotionEvent.ACTION_UP:
                {

                    if (isLongPress) {
                        isLongPress = false;
                    } else {
                        if (event.getPointerCount() == 1) { // MOVE
                            fX = event.getX(event.findPointerIndex(ptrID1));
                            fY = event.getY(event.findPointerIndex(ptrID1));
                            Log.d("터치이벤트", "MotionEvent.ACTION_UP " + Double.toString(fX) + "  " + Double.toString(fY));
                            if(abs(sX - fX) < 10 &&
                                    abs(sY -fY) < 10)
                            {
                            //if (abs(sX - fX) < 100 &&
                            //    abs(sY - fY) < 100) {
                                //MainActivity.optionMenu.show();
                                if (mHandler != null) {
                                    Message m = mHandler.obtainMessage();
                                    m.what = 100;
                                    mHandler.sendMessage(m);
                                }
                            } else {
                                lanSingleTouch(Constants.NAVI_TOUCH_EVENT_UP, fX, fY);
                                //NativeImplement.lanSingleTouch(Constants.NAVI_TOUCH_EVENT_UP, fX, fY);
                            }
                        }
                    }
                    handler.removeCallbacksAndMessages(null);
                }
                ptrID1 = INVALID_POINTER_ID;
                break;
                case MotionEvent.ACTION_MOVE:

                    float x = event.getX();
                    float y = event.getY();

                    float calX = Math.abs(Math.abs(x) - Math.abs(beforeX));
                    float calY = Math.abs(Math.abs(y) - Math.abs(beforeY));

                    if (calX > 100 || calY >100) {

                        handler.removeCallbacksAndMessages(null);

                        //if (nowTime - prevTime > Constants.NAVI_TOUCH_TIME) {
                        if (event.getPointerCount() == 1) { // MOVE
                            mTouchMode = MOVE;
                            fX = event.getX(event.findPointerIndex(ptrID1));
                            fY = event.getY(event.findPointerIndex(ptrID1));
                            Log.d("터치이벤트", "MotionEvent.ACTION_MOVE " + Double.toString(fX) + "  " + Double.toString(fY));

                            lanSingleTouch(Constants.NAVI_TOUCH_EVENT_MOVE, fX, fY);
                            //NativeImplement.lanSingleTouch(Constants.NAVI_TOUCH_EVENT_MOVE, fX, fY);

                            //sX = event.getX(event.findPointerIndex(ptrID1));
                            //sY = event.getY(event.findPointerIndex(ptrID1));

                        } else if (event.getPointerCount() == 2) {
                            mTouchMode = ZOOM;
/*
                            float nfX, nfY, nsX, nsY;
                            nsX = event.getX(event.findPointerIndex(ptrID1));
                            nsY = event.getY(event.findPointerIndex(ptrID1));
                            nfX = event.getX(event.findPointerIndex(ptrID2));
                            nfY = event.getY(event.findPointerIndex(ptrID2));
                            int compareNum = MathUtil.distance(event) - distance;

                            if (compareNum > 100) {
                                Log.d("터치이벤트", "MotionEvent.ZOOMIN");
                                distance = MathUtil.distance(event);
                                //mNavi.zoomIn();
                                lanZoomByPosition(1,(nsX+nfX)/2,(nsY+nfY)/2);

                            } else if (compareNum < -100){
                                Log.d("터치이벤트", "MotionEvent.ZOOMOUT");
                                distance = MathUtil.distance(event);
                                //mNavi.zoomOut();
                                lanZoomByPosition(0,(nsX+nfX)/2,(nsY+nfY)/2);

                            }



                            if (mAngle < -360.0f) mAngle += 360.0f;
                            else if (mAngle > 360.0f) mAngle -= 360.0f;
                            mAngle += angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);
                            Log.d("angle", mAngle + "");
                            //mNavi.setDgree((int) mAngle);

                            fX = nfX;
                            fY = nfY;
                            sX = nsX;
                            sY = nsY;
*/
                        }

                    }

                    //}
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.d("터치이벤트", "MotionEvent.ACTION_POINTER_DOWN");
                    distance = MathUtil.distance(event);
                    Point pt = MathUtil.midPoint(event);
                    //mNavi.setCenterForDraw(pt.x, pt.y);
                    ptrID2 = event.getPointerId(event.getActionIndex());

                    sX = event.getX(event.findPointerIndex(ptrID1));
                    sY = event.getY(event.findPointerIndex(ptrID1));
                    fX = event.getX(event.findPointerIndex(ptrID2));
                    fY = event.getY(event.findPointerIndex(ptrID2));
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.d("터치이벤트", "MotionEvent.ACTION_POINTER_UP");

                    if(mTouchMode == ZOOM)
                    {
                        sX = event.getX(event.findPointerIndex(ptrID1));
                        sY = event.getY(event.findPointerIndex(ptrID1));
                        fX = event.getX(event.findPointerIndex(ptrID2));
                        fY = event.getY(event.findPointerIndex(ptrID2));

                        int compareNum = MathUtil.distance(event) - distance;
                        if (compareNum > 0) {
                            Log.d("터치이벤트", "MotionEvent.ZOOMIN");
                            distance = MathUtil.distance(event);
                            //mNavi.zoomIn();
                            lanZoomByPosition(1,(sX+fX)/2, (sY+fY)/2);

                        } else {
                            Log.d("터치이벤트", "MotionEvent.ZOOMOUT");
                            distance = MathUtil.distance(event);
                            //mNavi.zoomOut();
                            lanZoomByPosition(0,(sX+fX)/2, (sY+fY)/2);

                        }
                    }

                    ptrID2 = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d("터치이벤트", "MotionEvent.ACTION_CANCEL");
                    ptrID1 = INVALID_POINTER_ID;
                    ptrID2 = INVALID_POINTER_ID;
                    break;
            }
            if (prevMode != event.getAction()) prevTime = nowTime;

            prevMode = event.getAction();
        } catch (Exception e) {
            Log.d("터치이벤트 error", e.getMessage());
            return false;
        }
        return true;
    }
}

class LANRenderer implements GLSurfaceView.Renderer {
    private SurfaceHolder mSurfaceHolder = null;
    private final static String TAG = Constants.TAG;

    public LANRenderer(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }

    public void ChangeScreenSize(int w, int h)
    {
        LanStorage.mNative.lanResize(w, h);

        mScreenWidth = w;
        mScreenHeight = h;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        LanStorage.mNative.lanRenderInitialize();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h)
    {
        ChangeScreenSize(w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        LanStorage.mNative.lanUpdateWrapper(0);
        LanStorage.mNative.lanRenderWrapper(0);

    }
}

