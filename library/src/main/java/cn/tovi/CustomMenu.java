package cn.tovi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * @author zhaotengfei9@gmail.com
 */
public class CustomMenu extends RelativeLayout {

    private static final String TAG = CustomMenu.class.getSimpleName();
    private static final int TEST_DIS = 20;
    private Context context;
    /**
     * To the left menu
     */
    private FrameLayout leftMenu;
    /**
     * Middle view
     */
    private FrameLayout middleView;
    /**
     * To the right menu
     */
    private FrameLayout rightMenu;
    /**
     * middle Mask
     */
//    private FrameLayout middleMask;
    /**
     * Scroller, more slowly
     */
    private Scroller mScrollerMoreSlowly;
    /**
     * Scroller, slow down after
     */
    private Scroller mScrollerSlowDownAfter;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    /**
     * Velocity Tracker
     */
    private VelocityTracker mVelocityTracker;
    private State state;
    /**
     * Whether the finger touch(In touch event, you must first set)
     */
    private boolean fingerTouch;
    private boolean isTestCompete;
    /**
     * Is not sliding around event
     */
    private boolean isLeftrightEvent;
    private Point point = new Point();

//    private ImageView leftMask;
//    private ImageView rightMask;

    private Drawable mShadowLeft;
    private Drawable mShadowRight;


    public CustomMenu(Context context) {
        this(context, null);
    }

    public CustomMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public void setContentView(@LayoutRes int resId) {
        setContentView(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setContentView(View view) {
        if (view != null) {
            middleView.removeView(view);
        }

        middleView.setPadding(0, 0, 0, 0);
        middleView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        //设置阴影
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            if (leftMask != null)
//                middleView.removeView(leftMask);
//            else {
//                leftMask = new ImageView(context);
//                leftMask.setBackgroundResource(R.drawable.middle_left);
//            }
//            if (rightMask != null)
//                middleView.removeView(rightMask);
//            else {
//                rightMask = new ImageView(context);
//                rightMask.setBackgroundResource(R.drawable.middle_right);
//            }
//            FrameLayout.LayoutParams f = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
//            f.gravity = Gravity.RIGHT;
//            middleView.addView(leftMask, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT));
//            middleView.addView(rightMask, f);
//        }

    }

    public void setLeftMenu(@LayoutRes int resId) {
        setLeftMenu(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setLeftMenu(View view) {
        initLeftMenu();
        if (view != null) {
            leftMenu.removeView(view);
        }
        leftMenu.setPadding(0, 0, 0, 0);
        leftMenu.addView(view);
    }

    public void setLeftShadow(@DrawableRes int resId) {
        setLeftShadow(getResources().getDrawable(resId));
    }

    public void setLeftShadow(Drawable shadowLeft) {
        mShadowLeft = shadowLeft;
        invalidate();
    }

    public void setRightMenu(@LayoutRes int resId) {
        setRightMenu(LayoutInflater.from(getContext()).inflate(resId, null));
    }

    public void setRightMenu(View view) {
        initRightMenu();
        if (view != null) {
            rightMenu.removeView(view);
        }
        rightMenu.setPadding(0, 0, 0, 0);
        rightMenu.addView(view);
    }

    public void setRightShadow(@DrawableRes int resId) {
        setRightShadow(getResources().getDrawable(resId));
    }

    public void setRightShadow(Drawable shadowRight) {
        mShadowRight = shadowRight;
        invalidate();
    }

    public State getState() {
        return this.state;
    }

    public void openLeftMenuIfPossible() {
        if (leftMenu != null) {
            // If the view is rolling, stop
            stopAllScroll();
            if (getScrollX() != -leftMenu.getMeasuredWidth()) {
                mScrollerSlowDownAfter.startScroll(getScrollX(), 0, -leftMenu.getMeasuredWidth() - getScrollX(), 0);
                invalidate();
            }
        }
    }

    public void openRightMenuIfPossible() {
        if (rightMenu != null) {
            // If the view is rolling, stop
            stopAllScroll();
            if (getScrollX() != rightMenu.getMeasuredWidth()) {
                mScrollerSlowDownAfter.startScroll(getScrollX(), 0, rightMenu.getMeasuredWidth() - getScrollX(), 0);
                invalidate();
            }
        }
    }

    public void closeMenu() {
        // If the view is rolling, stop
        stopAllScroll();
        if (getScrollX() != 0) {
            mScrollerSlowDownAfter.startScroll(getScrollX(), 0, -getScrollX(), 0);
            invalidate();
        }
    }

    private void initView(Context context) {
        this.context = context;
        middleView = new FrameLayout(context);
//        middleView.setBackgroundColor(Color.TRANSPARENT);
//        middleMask = new FrameLayout(context);

//        middleMask.setBackgroundColor(0x88000000);
//        middleMask.setAlpha(0f);


        addView(middleView);
//        addView(middleMask);//note mask


        mScrollerMoreSlowly = new Scroller(context, new DecelerateInterpolator());
        mScrollerSlowDownAfter = new Scroller(context, new AccelerateDecelerateInterpolator());
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        state = State.CLOSE_MENU;

    }

    private void initLeftMenu() {
        if (leftMenu != null)
            return;
        leftMenu = new FrameLayout(context);

        //设置阴影
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            leftMenu.setElevation(120);
//        }
//        leftMenu.setBackgroundColor(Color.TRANSPARENT);
        addView(leftMenu);
    }

    private void initRightMenu() {
        if (rightMenu != null)
            return;
        rightMenu = new FrameLayout(context);

        //设置阴影
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            rightMenu.setElevation(120);
//        }
//        rightMenu.setBackgroundColor(Color.TRANSPARENT);
        addView(rightMenu);
    }

    private void stopAllScroll() {
        if (mScrollerMoreSlowly.computeScrollOffset()) {
            mScrollerMoreSlowly.forceFinished(true);
        }
        if (mScrollerSlowDownAfter.computeScrollOffset()) {
            mScrollerSlowDownAfter.forceFinished(true);
        }
    }

//    @Override
//    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
//
//
//        //设置遮罩的透明度(这里以 “当前视图的偏移量/菜单的宽度” 的值作为透明度)
////        int curX = Math.abs(getScrollX());
////        float scale = curX / (curX > 0 ? (float) (rightMenu == null ? 0 : rightMenu.getMeasuredWidth()) : (float) (leftMenu == null ? 0 : leftMenu.getMeasuredWidth()));
////        middleMask.setAlpha(scale * 0.8f);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        middleView.measure(widthMeasureSpec, heightMeasureSpec);
//        middleMask.measure(widthMeasureSpec, heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        //宽度是middleMenu宽度的八成;MeasureSpec.EXACTLY:精确
        int tempWidthMeasure = MeasureSpec.makeMeasureSpec(
                (int) (realWidth * 0.8f), MeasureSpec.EXACTLY);
        if (leftMenu != null)
            leftMenu.measure(tempWidthMeasure, heightMeasureSpec);
        if (rightMenu != null)
            rightMenu.measure(tempWidthMeasure, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        middleView.layout(l, t, r, b);
//        middleMask.layout(l, t, r, b);
        if (leftMenu != null)
            leftMenu.layout(l - leftMenu.getMeasuredWidth(), t,
                    r - middleView.getMeasuredWidth(), b);
        if (rightMenu != null)
            rightMenu.layout(
                    l + middleView.getMeasuredWidth(),
                    t,
                    l + middleView.getMeasuredWidth()
                            + rightMenu.getMeasuredWidth(), b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        obtainVelocityTracker(ev);
        if (!isTestCompete) {
            getEventType(ev);
            return true;
        }
        if (isLeftrightEvent) {
            //getAction() & ACTION_MASK
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
                        finalX = Math.max(expectX, -(leftMenu == null ? 0 : leftMenu.getMeasuredWidth()));// 显示左Menu
                    } else {// 同理
                        finalX = Math.min(expectX, rightMenu == null ? 0 : rightMenu.getMeasuredWidth());// 显示右Menu
                    }
                    Log.e(TAG, "curScrollX:" + curScrollX + "   dis_x:" + dis_x
                            + "    expectX:" + expectX + "   finalX:" + finalX
                            + "   leftMenuW:" + (leftMenu == null ? 0 : leftMenu.getMeasuredWidth())
                            + "   rightMenuW:" + (rightMenu == null ? 0 : rightMenu.getMeasuredWidth()));
                    scrollTo(finalX, 0);
                    point.x = (int) ev.getX();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    fingerTouch = false;
                    touchUp();
                    isLeftrightEvent = false;
                    isTestCompete = false;
                    break;
            }
        } else {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                    fingerTouch = false;
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
        if (mScrollerMoreSlowly.computeScrollOffset()) {//调用此方法，想要获取最新的当前位置。返回true:滑动没有停止;返回false：滑动停止
            int tempX = mScrollerMoreSlowly.getCurrX();
            scrollTo(tempX, 0);
            postInvalidate();

            if (mScrollerMoreSlowly.isFinished()) {//如果滚动停止，检测位置
                checkLocationInStop();
            } else {
                if (state != State.SCROLLING)
                    state = State.SCROLLING;
            }
        } else if (mScrollerSlowDownAfter.computeScrollOffset()) {//调用此方法，想要获取最新的当前位置。返回true:滑动没有停止;返回false：滑动停止
            int tempX = mScrollerSlowDownAfter.getCurrX();
            scrollTo(tempX, 0);
            postInvalidate();

            if (mScrollerSlowDownAfter.isFinished()) {//如果滚动停止，检测位置
                checkLocationInStop();
            } else {
                if (state != State.SCROLLING)
                    state = State.SCROLLING;
            }
        } else {
            //Stop scrolling view,Check location
            checkLocationInStop();
        }
    }

    private void getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                fingerTouch = true;
                // stop all scroll
                stopAllScroll();
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
                fingerTouch = false;
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
        //如果手指触屏(当前是按下或滑动状态),不做处理
        if (fingerTouch) {
            return;
        }
        int curScrollX = getScrollX();
        int menuWith = curScrollX > 0 ? (rightMenu == null ? 0 : rightMenu.getMeasuredWidth()) : (leftMenu == null ? 0 : leftMenu.getMeasuredWidth());
        if (Math.abs(curScrollX) > menuWith >> 1) {
            if (curScrollX < 0) {
                if (curScrollX != -leftMenu.getMeasuredWidth()) {
                    mScrollerMoreSlowly.startScroll(curScrollX, 0,
                            -(leftMenu == null ? 0 : leftMenu.getMeasuredWidth()) - curScrollX, 0,
                            200);
                    invalidate();
                } else {
                    //完全显示左菜单
                    state = State.LEFT_MENU_OPENS;
                }
            } else {
                if (curScrollX != rightMenu.getMeasuredWidth()) {
                    mScrollerMoreSlowly.startScroll(curScrollX, 0,
                            (rightMenu == null ? 0 : rightMenu.getMeasuredWidth()) - curScrollX, 0,
                            200);
                    invalidate();
                } else {
                    // 完全显示右菜单
                    state = State.RIGHT_MENU_OPENS;
                }
            }
        } else {
            if (curScrollX != 0) {
                mScrollerMoreSlowly.startScroll(curScrollX, 0, -curScrollX, 0, 200);
                invalidate();
            } else {
                // 完全关闭菜单
                state = State.CLOSE_MENU;
            }
        }
    }

    public void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            velocityX = -velocityX;
            //velocityX > 0，说明视图向右走，显示左菜单
            mScrollerMoreSlowly.fling(getScrollX(), getScrollY(), velocityX, velocityY, -(leftMenu == null ? 0 : leftMenu.getMeasuredWidth()), (rightMenu == null ? 0 : rightMenu.getMeasuredWidth()), 0,
                    0);

//            final boolean movingDown = velocityY > 0;
            awakenScrollBars(mScrollerMoreSlowly.getDuration());
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

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean result = super.drawChild(canvas, child, drawingTime);
        if (mShadowLeft != null) {
            final int shadowWidth = mShadowLeft.getIntrinsicWidth();
            final int childRight = child.getRight();
            mShadowLeft.setBounds(childRight, child.getTop(),
                    childRight + shadowWidth, child.getBottom());
            mShadowLeft.draw(canvas);
        }
        if (mShadowRight != null) {
            final int shadowWidth = mShadowRight.getIntrinsicWidth();
            final int childLeft = child.getLeft();
            mShadowRight.setBounds(childLeft - shadowWidth, child.getTop(),
                    childLeft, child.getBottom());
            mShadowRight.draw(canvas);
        }
        return result;
    }

    public enum State {
        /**
         * View is scrolling
         */
        SCROLLING,
        /**
         * Left menu opens
         */
        LEFT_MENU_OPENS,
        /**
         * Right menu opens
         */
        RIGHT_MENU_OPENS,
        /**
         * Stop scrolling view,Left and Right menu menu is closed
         */
        CLOSE_MENU,
    }
}
