package com.admin.claire.lotto.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.admin.claire.lotto.R;

/**
 * Created by claire on 2017/9/12.
 * 幸運轉盤 參考教學
 * http://www.itread01.com/articles/1476633986.html
 * http://m.blog.csdn.net/lmj623565791/article/details/41722441
 */

public class LuckyTurnTable_SurfaceView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    //定義一個SurfaceHolder 用於接受獲取的SurfaceHolder
    private SurfaceHolder mHolder;

    //用於獲取綁定mHOlder的Canvas
    private Canvas mCanvas;

    //用於不斷繪圖的線程
    private Thread mThread;

    //用於控制線程的開關
    private boolean isRunning;

    // 每塊的顏色
    private int deepColor = 0xFFFF2081;
    private int lightColor = 0xFFac18f1;
    private int[] mColors = new int[]{deepColor, lightColor, deepColor,
            lightColor, deepColor, lightColor,};

    //抽獎文字
    private String[] mName = new String[]{"財運好", "做公益囉", "財運不好", "有機會", "下次再買", "做公益囉"};

    // 與文字對應的圖片
    private int[] mImgs = new int[]{R.drawable.rich, R.drawable.relieved,
            R.drawable.sad, R.drawable.happiness, R.drawable.happiness_1,
            R.drawable.relieved};

    // 與文字對應的圖片的數組
    private Bitmap[] mImgsBitmap;

    //盤塊的個數
    private final int mItemCount = 6;

    // 繪制盤塊的範圍
    private RectF mRange = new RectF();

    //圓的直徑
    private int mRadius;

    //繪製盤塊的畫筆
    private Paint mArcPaint;

    //繪製文字的畫筆
    private Paint mTextPaint;

    // 滾動的速度
    private double mSpeed;
    private volatile float mStartAngle = 0; //角度  volatile 用在多線程，同步變量。

    // 遞減的加速度
    private int aSpeed = 1;

    // 是否點擊了停止
    private boolean isShouldEnd;

    // 控件的中心位置
    private int mCenter;

    // 控件的padding
    private int mPadding;

    //背景圖片
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

    //文字大小
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            28, getResources().getDisplayMetrics());


    //做一些初始化的操作,三個參數的構造函數，一般自會在有使用自定義屬性的時候才會調用這個構造函數

    /**
     * @param context  上下文
     * @param attrs    xml文件定義的屬性
     * @param defStyle 自定義屬性
     */
    public LuckyTurnTable_SurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 獲得SurfaceHolder holder,和與之關聯的Canvas
        mHolder = getHolder();
        mHolder.addCallback(this);

       // setZOrderOnTop(true);// 设置画布 背景透明
       // mHolder.setFormat(PixelFormat.TRANSLUCENT);

        // 設置可獲得焦點，以及常亮
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

    }

    //當時用xml文件定義這個自定義View的時候，就會調用這個帶兩個參數的構造函數
    //去調用三個參數的構造函數
    public LuckyTurnTable_SurfaceView(Context context, AttributeSet attrs) {
        //this(context, null, 0);
        super(context, attrs);

        mHolder = getHolder();
        mHolder.addCallback(this);

        // setZOrderOnTop(true);// 设置画布 背景透明
        // mHolder.setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    // 一個參數的構造函數去調用兩個構造參數的構造函數
    public LuckyTurnTable_SurfaceView(Context context) {
        this(context, null);
    }


    /**
     * 控件的大小是以寬高當中最小的為為基準這樣做的目的是為了讓控件能成一個正方形,
     * 方便以後繪制圓形，以確定半徑，中心等，
     * 所以在xml文件裏面定義的時候別忘了centerInParent。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 獲得寬高當中最小的
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int min = width < height ? width : height;

        //獲得圖的直徑
        mRadius = min - getPaddingLeft() - getPaddingRight();

        //獲得padding的值，- paddingLeft為基準
        mPadding = getPaddingLeft();

        //設置中心點
        mCenter = min / 2;

        setMeasuredDimension(min, min);
    }

    //在這裏做一些初始化的工作，開啟線程
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 初始化繪制圓弧的畫筆,並設置鋸齒之類的
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        // 初始化繪制文字的畫筆
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(mTextSize);

        // 圓弧的繪制範圍,繪制的範圍剛好是一個正方形
        mRange = new RectF(mPadding, mPadding, mRadius + mPadding, mRadius + mPadding);

        // 初始化圖片
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }

        //開啟線程
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //當SurfaceView執行destroy的時候關閉線程
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //關閉線程只需設置isRunning
        isRunning = false;
    }

    //主要，也是最核心的工作都是在run方法裏面執行的，如draw（）
    @Override
    public void run() {
        try {
            //這裏通過死循環,不斷的進行繪圖，給你一種盤在不斷旋轉的錯覺

            while (isRunning) {
                // 這一次開始繪圖的時間
                long start = System.currentTimeMillis();

                //繪圖操作
                draw();

                //繪圖結束時間
                long end = System.currentTimeMillis();

                // 如果你的手機太快，繪圖分分鐘的事情，那也得讓他把那個50等完
                try {

                    if (end - start < 50) {
                        Thread.sleep(50 - (end - start));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 繪圖
    private void draw() {
        try {

            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //首先繪制背景圖
                drawBg();

                // 繪制每個弧形，以及每個弧形上的文字，以及每個弧形上的圖片
                float tmpAngle = mStartAngle;
                float sweepAngle = (float) (360 / mItemCount);

                for (int i = 0; i < mItemCount; i++) {
                    //轉盤背景顏色
                    mArcPaint.setColor(mColors[i]);

                    //畫了個扇形出來
                    // http://blog.sina.com.cn/s/blog_783ede0301012im3.html
                    // oval :指定圓弧的外輪廓矩形區域。
                    // startAngle: 圓弧起始角度，單位為度。
                    // sweepAngle: 圓弧掃過的角度，順時針方向，單位為度。
                    // useCenter: 如果為True時，在繪制圓弧時將圓心包括在內，通常用來繪制扇形。
                    // paint: 繪制圓弧的畫板屬性，如顏色，是否填充等。
                    mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true,
                            mArcPaint);

                    //繪制文本 (mName[i]抽獎文字)
                    drawText(tmpAngle, sweepAngle, mName[i]);

                    // 繪制Icon
                    drawIcon(tmpAngle, i);

                    // 轉換角度，不能再一個地方一直繪制，
                    tmpAngle += sweepAngle;

                }

                // 當mSpeed不等於0時，相當於滾動
                mStartAngle += mSpeed;

                // 當點擊停止時，設置mSpeed慢慢遞減，而不是一下就停了下來
                if (isShouldEnd) {
                    mSpeed -= aSpeed;
                }

                // mSpeed小於0的時候就該停止了
                if (mSpeed <= 0) {
                    mSpeed = 0;
                    isShouldEnd = false;
                }

                // 根據當前旋轉的mStartAngle計算當前滾動的區域
                // callInExactArea(mStartAngle);
                  //mStartAngle = 600;


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }


    }

    //繪制背景圖
    private void drawBg() {
        // 根據當前旋轉的mStartAngle計算當前滾動到的區域 繪制背景，不重要，完全為了美觀
        mCanvas.drawColor(0xFFFFFFFF);

        // 圓弧的繪制範圍大了那麽一圈
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2,
                mPadding / 2, getMeasuredWidth() - mPadding / 2,
                getMeasuredWidth() - mPadding / 2), null);

    }

    /**
     * 繪制文本
     * http://www.itread01.com/articles/1476633986.html
     *
     * @param startAngle
     * @param sweepAngle
     * @param mName2
     */
    private void drawText(float startAngle, float sweepAngle, String mName2) {

        //繪制這個文字是通過path先確定一個Arc，為一個弧形，
        // 然後通過水平和垂直偏移量共同定位文字的最終位置。
        Path path = new Path();

        //將寫字區域加上去 (mRange 繪制盤塊的範圍)
        path.addArc(mRange, startAngle, sweepAngle);

        //文字的寬度
        float textWidth = mTextPaint.measureText(mName2);

        //利用水平偏移和埀直偏移讓文字居中
        //水平 horizontal 解釋一下這個公式首先得到弧長，然後減去文字寬度的一半就得到了水平偏移量
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);

        //埀直 vertical 垂直偏移量就是float vOffset = (float) (mRadius / 2 / 6);也就是半徑的1/6
        float vOffset = (float) (mRadius / 2 / 6);

        //最後把文字畫上去
        mCanvas.drawTextOnPath(mName2, path, hOffset, vOffset, mTextPaint);
    }

    /**
     * 繪制Icon
     *
     * @param startAngle
     * @param i
     */
    private void drawIcon(float startAngle, int i) {

        // 設置圖片的寬度，為直徑的1/8，當然可以隨便改
        int imgWidth = mRadius / 8;

        //換算成弧度
        float angle = (float) ((30 + startAngle) * (Math.PI / 180));

        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        // 確定繪制圖片的位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
                / 2, y + imgWidth / 2);

        // 繪制
        mCanvas.drawBitmap(mImgsBitmap[i], null, rect, null);
    }

    public void luckyStart(int luckyIndex) {

        // 每一項的角度大小
        float angle = (float) (360 / mItemCount);

        // 中獎角度範圍，因為指針是朝上的所以範圍是在210-270
        float from = 270 - (luckyIndex + 1) * angle;
        float to = from + angle;

        // 停下來是旋轉的距離
        float targetFrom = 4 * 360 + from;


        float v1 = (float) (Math.sqrt(1 * 1 + 8 * targetFrom) - 1) / 2;

        float targetTo = 4 * 360 + to;

        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

        mSpeed = (float) (v1 + Math.random() * (v2 - v1));

        isShouldEnd = false;
    }

    public void luckyEnd() {
        mStartAngle = 0;
        isShouldEnd = true;
    }

    public boolean isStart() {
        return mSpeed != 0;
    }

    public boolean isShouldEnd() {
        return isShouldEnd;
    }

}
