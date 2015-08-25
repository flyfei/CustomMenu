package cn.tovi;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class CustomMenu extends RelativeLayout {

    private static final String TAG = CustomMenu.class.getSimpleName();

    private Context context;
    /**
     * 左侧Menu
     */
    private FrameLayout leftMenu;
    /**
     * 中间Menu
     */
    private FrameLayout middleMenu;
    /**
     * 右侧Menu
     */
    private FrameLayout rightMenu;
    /**
     * 中部遮罩
     */
    private FrameLayout middleMask;
    /**
     * 滚动器
     */
    private Scroller mScroller;


    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    /**
     * 速度管理器
     */
    private VelocityTracker mVelocityTracker;

    public CustomMenu(Context context) {
        super(context);
        initView(context);
    }

    public CustomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        leftMenu = new FrameLayout(context);
        middleMenu = new FrameLayout(context);
        rightMenu = new FrameLayout(context);
        middleMask = new FrameLayout(context);

        leftMenu.setBackgroundResource(R.drawable.left_menu);
        middleMenu.setBackgroundResource(R.drawable.main_ui);
        rightMenu.setBackgroundResource(R.drawable.right_menu);
        middleMask.setBackgroundColor(0x88000000);
        middleMask.setAlpha(0f);

        addView(leftMenu);
        addView(middleMenu);
        addView(middleMask);
        addView(rightMenu);


        mScroller = new Scroller(context, new DecelerateInterpolator());
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);


        //设置遮罩的透明度(这里以 “当前视图的偏移量/菜单的宽度” 的值作为透明度)
        int curX = Math.abs(getScrollX());
        float scale = curX / (float) leftMenu.getMeasuredWidth();
        middleMask.setAlpha(scale);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        middleMenu.measure(widthMeasureSpec, heightMeasureSpec);
        middleMask.measure(widthMeasureSpec, heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        //宽度是middleMenu宽度的八成;MeasureSpec.EXACTLY:精确
        int tempWidthMeasure = MeasureSpec.makeMeasureSpec(
                (int) (realWidth * 0.8f), MeasureSpec.EXACTLY);
        leftMenu.measure(tempWidthMeasure, heightMeasureSpec);
        rightMenu.measure(tempWidthMeasure, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        middleMenu.layout(l, t, r, b);
        middleMask.layout(l, t, r, b);
        leftMenu.layout(l - leftMenu.getMeasuredWidth(), t,
                r - middleMenu.getMeasuredWidth(), b);
        rightMenu.layout(
                l + middleMenu.getMeasuredWidth(),
                t,
                l + middleMenu.getMeasuredWidth()
                        + rightMenu.getMeasuredWidth(), b);
    }

    private boolean isTestCompete;
    private boolean isLeftrightEvent;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        obtainVelocityTracker(ev);
        if (!isTestCompete) {
            getEventType(ev);
            return true;
        }
        if (isLeftrightEvent) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    int curScrollX = getScrollX();// 当前整体视图的位置(矢量。+表示视图发生了左移;-表示视图发生了右移。
                    // 执行scrollTo、scrollBy才会发生变化)
                    int dis_x = (int) (ev.getX() - point.x);// 需要移动dis_x的距离（不包括20的距离）（矢量。+表示手指右移；-表示手指左移），这时，视图还没有移动

                    // 由于curScrollX和dis_x都是矢量，所以按照常理相加，得到的就是最后的视图位置。但是dis_x和curScrollX的标准不一样，正好相反，看
                    // 括号里面+-的解释
                    // scrollTo、scrollBy
                    // 是控制视图位置的，是以视图为标准的，所以这里也以视图为标准，curScrollX不变，dis_x前面添加-

                    // 计算视图移动后的位置(矢量)
                    int expectX = -dis_x + curScrollX;

                    int finalX = 0;

                    if (expectX < 0) {// <0,按照现在的情况，视图发生了右移，左菜单将会显示。这里加个限制，不能看到左菜单左部的内容
                        finalX = Math.max(expectX, -leftMenu.getMeasuredWidth());// 显示左Menu
                    } else {// 同理
                        finalX = Math.min(expectX, rightMenu.getMeasuredWidth());// 显示右Menu
                    }
                    Log.e(TAG, "curScrollX:" + curScrollX + "   dis_x:" + dis_x
                            + "    expectX:" + expectX + "   finalX:" + finalX
                            + "   leftMenuW:" + leftMenu.getMeasuredWidth()
                            + "   rightMenuW:" + rightMenu.getMeasuredWidth());
                    scrollTo(finalX, 0);
                    point.x = (int) ev.getX();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchUp();
                    isLeftrightEvent = false;
                    isTestCompete = false;
                    break;
            }
        } else {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                    touchUp();
                    isLeftrightEvent = false;
                    isTestCompete = false;
                    break;

                default:
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {//调用此方法，想要获取最新的当前位置。返回true:滑动没有停止;返回false：滑动停止
            int tempX = mScroller.getCurrX();
            scrollTo(tempX, 0);
            postInvalidate();

            if (mScroller.isFinished())//如果滚动停止，检测位置
                checkLocationInStop();
        }
    }

    private Point point = new Point();
    private static final int TEST_DIS = 20;

    private void getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller.computeScrollOffset()) {
                    mScroller.forceFinished(true);
                }
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();
                super.dispatchTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                int dX = Math.abs((int) ev.getX() - point.x);
                int dY = Math.abs((int) ev.getY() - point.y);
                if (dX >= TEST_DIS && dX > dY) { // 左右滑动
                    isLeftrightEvent = true;
                    isTestCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                } else if (dY >= TEST_DIS && dY > dX) { // 上下滑动
                    isLeftrightEvent = false;
                    isTestCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                super.dispatchTouchEvent(ev);
                touchUp();
                isLeftrightEvent = false;
                isTestCompete = false;
                break;
        }
    }

    /**
     * 手指抬起后调用(滑动、静止时位置检测)
     */
    private void touchUp() {
        final VelocityTracker velocityTracker = mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
        // x轴方向的速度(矢量。+表示x轴正反向，及试图向右走；-表示x轴反方向，及试图向左走)
        int initialVelocity = (int) velocityTracker.getXVelocity();
        if ((Math.abs(initialVelocity) > mMinimumVelocity)
                && getChildCount() > 0) {//如果速度达到一定值，并且有子类，则开始滑动
            Log.e("", "initialVelocity:" + initialVelocity);
            fling(initialVelocity, 0);
        } else {//检测位置
            checkLocationInStop();
        }
        releaseVelocityTracker();
    }

    /**
     * 滑动/滚动停止的时候检查位置
     */
    private void checkLocationInStop() {
        int curScrollX = getScrollX();
        if (Math.abs(curScrollX) > leftMenu.getMeasuredWidth() >> 1) {
            if (curScrollX < 0) {
                mScroller.startScroll(curScrollX, 0,
                        -leftMenu.getMeasuredWidth() - curScrollX, 0,
                        200);
            } else {
                mScroller.startScroll(curScrollX, 0,
                        leftMenu.getMeasuredWidth() - curScrollX, 0,
                        200);
            }
        } else {
            mScroller.startScroll(curScrollX, 0, -curScrollX, 0, 200);
        }
        invalidate();
    }

    public void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            velocityX = -velocityX;
            //velocityX > 0，说明试图向右走，显示左菜单
            mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY, -rightMenu.getMeasuredWidth(), leftMenu.getMeasuredWidth(), 0,
                    0);

            final boolean movingDown = velocityY > 0;
            awakenScrollBars(mScroller.getDuration());
            invalidate();
        }
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }


    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
