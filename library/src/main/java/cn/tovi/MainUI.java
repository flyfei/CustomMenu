package cn.tovi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class MainUI extends RelativeLayout {
	private Context context;
	private FrameLayout leftMenu;
	private FrameLayout middleMenu;
	private FrameLayout rightMenu;
	private FrameLayout middleMask;
	private Scroller mScroller;
	public static final int LEFT_ID = 0xaabbcc;
	public static final int MIDEELE_ID = 0xaaccbb;
	public static final int RIGHT_ID = 0xccbbaa;

	public MainUI(Context context) {
		super(context);
		initView(context);
	}

	public MainUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
		mScroller = new Scroller(context, new DecelerateInterpolator());
		leftMenu = new FrameLayout(context);
		middleMenu = new FrameLayout(context);
		rightMenu = new FrameLayout(context);
		middleMask = new FrameLayout(context);
		leftMenu.setBackgroundColor(Color.RED);
		middleMenu.setBackgroundColor(Color.GREEN);
		rightMenu.setBackgroundColor(Color.RED);
		middleMask.setBackgroundColor(0x88000000);
//		leftMenu.setId(LEFT_ID);
//		middleMenu.setId(MIDEELE_ID);
//		rightMenu.setId(RIGHT_ID);
		addView(leftMenu);
		addView(middleMenu);
		addView(rightMenu);
		addView(middleMask);
		middleMask.setAlpha(0);
	}
	
	public float onMiddleMask(){
		System.out.println("透明度:"+middleMask.getAlpha());
		return middleMask.getAlpha();
	}
	
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		onMiddleMask();
		int curX = Math.abs(getScrollX());
		float scale = curX/(float)leftMenu.getMeasuredWidth();
		middleMask.setAlpha(scale);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		middleMenu.measure(widthMeasureSpec, heightMeasureSpec);
		middleMask.measure(widthMeasureSpec, heightMeasureSpec);
		int realWidth = MeasureSpec.getSize(widthMeasureSpec);
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
		leftMenu.layout(l - leftMenu.getMeasuredWidth(), t, r, b);
		rightMenu.layout(
				l + middleMenu.getMeasuredWidth(),
				t,
				l + middleMenu.getMeasuredWidth()
						+ rightMenu.getMeasuredWidth(), b);
	}

	private boolean isTestCompete;
	private boolean isleftrightEvent;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!isTestCompete) {
			getEventType(ev);
			return true;
		}
		if (isleftrightEvent) {
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_MOVE:
				int curScrollX = getScrollX();
				int dis_x = (int) (ev.getX() - point.x);
				int expectX = -dis_x + curScrollX;
				int finalX = 0;
				if (expectX < 0) {
					finalX = Math.max(expectX, -leftMenu.getMeasuredWidth());
				} else {
					finalX = Math.min(expectX, rightMenu.getMeasuredWidth());
				}
				scrollTo(finalX, 0);
				point.x = (int) ev.getX();
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				curScrollX = getScrollX();
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
				isleftrightEvent = false;
				isTestCompete = false;
				break;
			}
		} else {
			switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_UP:
				isleftrightEvent = false;
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
		super.computeScroll();
		if (!mScroller.computeScrollOffset()) {
			return;
		}
		int tempX = mScroller.getCurrX();
		scrollTo(tempX, 0);
	}

	private Point point = new Point();
	private static final int TEST_DIS = 20;

	private void getEventType(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			point.x = (int) ev.getX();
			point.y = (int) ev.getY();
			super.dispatchTouchEvent(ev);
			break;

		case MotionEvent.ACTION_MOVE:
			int dX = Math.abs((int) ev.getX() - point.x);
			int dY = Math.abs((int) ev.getY() - point.y);
			if (dX >= TEST_DIS && dX > dY) { // 左右滑动
				isleftrightEvent = true;
				isTestCompete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getY();
			} else if (dY >= TEST_DIS && dY > dX) { // 上下滑动
				isleftrightEvent = false;
				isTestCompete = true;
				point.x = (int) ev.getX();
				point.y = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			super.dispatchTouchEvent(ev);
			isleftrightEvent = false;
			isTestCompete = false;
			break;
		}
	}

}
