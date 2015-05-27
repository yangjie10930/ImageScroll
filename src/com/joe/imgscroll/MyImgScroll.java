package com.joe.imgscroll;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 图片滚动类
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class MyImgScroll extends ViewPager {
	Activity mActivity; // 上下文
	List<View> mListViews; // 图片组
	int mScrollTime = 0;
	Timer timer;
	int oldIndex = 0;
	int curIndex = 0;

	public MyImgScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 开始广告滚动
	 * 
	 * @param mainActivity
	 *            显示广告的界面
	 * @param imgList
	 *            图片列表, 不能为null ,最少一张
	 * @param scrollTime
	 *            滚动间隔 ,0为不滚动
	 * @param ovalLayout
	 *            圆点容器,可为空,LinearLayout类型
	 */
	public void start(Activity mainActivity, List<View> imgList,
			int scrollTime, LinearLayout ovalLayout) {
		mActivity = mainActivity;
		mListViews = imgList;
		mScrollTime = scrollTime;
		// 设置圆点
		setOvalLayout(ovalLayout);
		this.setAdapter(new MyPagerAdapter());// 设置适配器
		if (scrollTime != 0 && imgList.size() > 1) {
			// 设置滑动动画时间 ,如果用默认动画时间可不用 ,反射技术实现
			new FixedSpeedScroller(mActivity).setDuration(this, 700);
			startTimer();
			// 触摸时停止滚动
			this.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						startTimer();
					} else {
						stopTimer();
					}
					return false;
				}
			});
		}
		if (mListViews.size() > 1) {
			this.setCurrentItem((Integer.MAX_VALUE / 2)
					- (Integer.MAX_VALUE / 2) % mListViews.size());// 设置选中为中间/图片为和第0张一样
		}
	}

	// 设置圆点
	private void setOvalLayout(final LinearLayout ovalLayout) {
		// 选中状态小圆点样式
		final GradientDrawable focusedGD = new GradientDrawable();
		focusedGD.setCornerRadius(5);
		focusedGD.setColor(Color.argb(170, 102, 102, 102));
		// 取消状态小圆点样式
		final GradientDrawable normalGD = new GradientDrawable();
		normalGD.setCornerRadius(5);
		normalGD.setColor(Color.argb(51, 0, 0, 0));

		if (ovalLayout != null) {
			for (int i = 0; i < mListViews.size(); i++) {
				// 添加圆点
				LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(
						12, 12);
				LP_WW.leftMargin = 5;
				LP_WW.rightMargin = 5;
				View view = new View(mActivity);
				view.setBackground(normalGD);
				view.setLayoutParams(LP_WW);
				ovalLayout.addView(view);
			}

			// 选中第一个
			ovalLayout.getChildAt(0).setBackground(focusedGD);
			this.setOnPageChangeListener(new OnPageChangeListener() {
				public void onPageSelected(int i) {
					curIndex = i % mListViews.size();
					// 取消圆点选中
					ovalLayout.getChildAt(oldIndex).setBackground(normalGD);
					// 圆点选中
					ovalLayout.getChildAt(curIndex).setBackground(focusedGD);
					oldIndex = curIndex;
				}

				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}
	}

	/**
	 * 取得当明选中下标
	 * 
	 * @return
	 */
	public int getCurIndex() {
		return curIndex;
	}

	/**
	 * 停止滚动
	 */
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * 开始滚动
	 */
	public void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						MyImgScroll.this.setCurrentItem(MyImgScroll.this
								.getCurrentItem() + 1);
					}
				});
			}
		}, mScrollTime, mScrollTime);
	}

	// 适配器 //循环设置
	private class MyPagerAdapter extends PagerAdapter {
		public void finishUpdate(View arg0) {
		}

		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		public int getCount() {
			if (mListViews.size() == 1) {// 一张图片时不用流动
				return mListViews.size();
			}
			return Integer.MAX_VALUE;
		}

		public Object instantiateItem(View v, int i) {
			if (((ViewPager) v).getChildCount() == mListViews.size()) {
				((ViewPager) v)
						.removeView(mListViews.get(i % mListViews.size()));
			}
			((ViewPager) v).addView(mListViews.get(i % mListViews.size()), 0);
			return mListViews.get(i % mListViews.size());
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
		}
	}
}
