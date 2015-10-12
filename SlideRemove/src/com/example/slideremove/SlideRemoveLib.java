package com.example.slideremove;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SlideRemoveLib extends ViewGroup {

	private View contentView;
	private View deleteView;
	private int deleteViewWidth;
	private ViewDragHelper mDragHelper;

	public SlideRemoveLib(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 实例花ViewDragHelper 第一个参数是ViewGroup容器，第二个是回调
		mDragHelper = ViewDragHelper.create(this, new MyCallBack());
	}

	private class MyCallBack extends Callback {
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			// 返回true表示处理事件
			// return false;
			// 表示接收处理contentView和deleteView的事件
			return child == contentView || child == deleteView;
		}

		// 这个方法是处理水平方法向的事件
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			// child表示你要处理容器中哪一个child left是返回的child的左上角的坐标控制水平移动
			if (child == contentView) {
				// 表示处理contentView这个控件的事件了
				if (left > 0) {
					return 0; // contentView的左上角大于了0就返回0 表示不可以再向右拖动
				} else if (left < -deleteView.getMeasuredWidth()) {
					return -deleteView.getMeasuredWidth();
				}
			}

			if (child == deleteView) {
				// 处理deleteView控件的事件了
				if (left > contentView.getMeasuredWidth()) {
					return contentView.getMeasuredWidth();
				} else if (left < contentView.getMeasuredWidth()
						- deleteView.getMeasuredWidth())
					return contentView.getMeasuredWidth()
							- deleteView.getMeasuredWidth();
			}
			return left;
		}

		// 控件位置改变的时候调用
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			// left就是控件改变的左上角的位置
			if (changedView == contentView) {
				// contentView位置改变了 ，那就动态去计算deleteView的位置并重新放置
				int deleteLeft = contentView.getMeasuredWidth() + left;
				int deleteTop = 0;
				int deleteRight = deleteLeft + deleteView.getMeasuredWidth();
				int deleteBottom = deleteView.getMeasuredHeight();

				deleteView.layout(deleteLeft, deleteTop, deleteRight,
						deleteBottom);
			} else if (changedView == deleteView) {
				// deleteView位置改变了 ，那就动态去计算contentView的位置并重新放置
				int contentViewLeft = -(contentView.getMeasuredWidth() - left);
				int contentViewRight = contentView.getMeasuredWidth()
						+ contentViewLeft;
				int contentViewTop = 0;
				int contentViewBottom = contentView.getMeasuredHeight();

				contentView.layout(contentViewLeft, contentViewTop,
						contentViewRight, contentViewBottom);
			}

		}

		// 滑动松开的时候调用
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			// 获取到contentView的左上标
			int contentViewLeft = contentView.getLeft();
			if (contentViewLeft < -deleteViewWidth / 2) {
				// 小于了deleteView的一半 完全显示删除
				showDeleteView();
			} else{
				// 完全不显示deleteView
				noShowDeleteView();
			}
		}

		/**
		 * 完全显示deleteView
		 */
		private void showDeleteView() {
			int contentViewLeft = -deleteView.getMeasuredWidth();
			int contentViewRight = contentViewLeft
					+ contentView.getMeasuredWidth();
			int contentViewTop = 0;
			int contentViewBottom = contentView.getMeasuredHeight();

			contentView.layout(contentViewLeft, contentViewTop,
					contentViewRight, contentViewBottom);
			// 放deleteView的位置
			int deleteLeft = contentView.getMeasuredWidth() + contentViewLeft;
			int deleteTop = 0;
			int deleteRight = deleteLeft + deleteView.getMeasuredWidth();
			int deleteBottom = deleteView.getMeasuredHeight();

			deleteView.layout(deleteLeft, deleteTop, deleteRight, deleteBottom);
		}

	}

	// 关系到拖动都会实现onTouchEvent方法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragHelper.processTouchEvent(event);
		// 返回ture 事件自己给消掉
		return true;
	}

	public SlideRemoveLib(Context context) {
		this(context, null);
	}

	@Override
	protected void onFinishInflate() {
		// 布局加载完成的时候调用
		// 在布局加载完成获取容器中两个子孩子
		contentView = getChildAt(0);
		deleteView = getChildAt(1);
		// 获取deleteView的宽度
		deleteViewWidth = deleteView.getLayoutParams().width;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 测量两个子孩子的大小 contentView的宽高和父容器一样 所以可以用父容器的这个两个参数来测量
		contentView.measure(widthMeasureSpec, heightMeasureSpec);
		// deleteView的宽和父容器不一样，所以带模式精确测量
		int deleteWdith = MeasureSpec.makeMeasureSpec(deleteViewWidth,
				MeasureSpec.EXACTLY);
		deleteView.measure(deleteWdith, heightMeasureSpec);

		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		noShowDeleteView();
	}

	public void noShowDeleteView() {
		// 放contentView的位置
		int dex = 0;
		int contentViewLeft = 0 + dex;
		int contentViewRight = contentView.getMeasuredWidth() + dex;
		int contentViewTop = 0;
		int contentViewBottom = contentView.getMeasuredHeight();

		contentView.layout(contentViewLeft, contentViewTop, contentViewRight,
				contentViewBottom);
		// 放deleteView的位置
		int deleteLeft = contentView.getMeasuredWidth() + dex;
		int deleteTop = 0;
		int deleteRight = contentView.getMeasuredWidth()
				+ deleteView.getMeasuredWidth() + dex;
		int deleteBottom = deleteView.getMeasuredHeight();

		deleteView.layout(deleteLeft, deleteTop, deleteRight, deleteBottom);
	}

}
