package com.larry.comment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnScrollListener {
	private ListView mListView;
	private View mPlaceHolder;
	private TextView mHeaderContentView;
	private View mStickItemView;
	private PopupWindow mPopupWindow;
	
	private boolean mIsInit = false;
	
	private static final String TAG = "MainActivity";

	private float mOldY = 0;
	
	private int mHeaderTextContentViewHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	protected void onResume() {
		super.onStart();
		mListView.setOnScrollListener(this);
	}

	@Override
	protected void onPause() {
		super.onStop();
		mListView.setOnScrollListener(null);
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.list_view);
		mHeaderContentView = new TextView(this);
		mHeaderContentView.setText("用嘀嘀叫车。上车后司机问你装快的了没，我说有。司机说你用这个再下一个假单我来接，我们一起赚这两个冤大头的钱。");
		mListView.addHeaderView(mHeaderContentView);
		mPlaceHolder = new View(this);
		mPlaceHolder.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				Utils.dp2px(this, 200)));
		mPlaceHolder.setBackgroundColor(Color.BLACK);
		mListView.addHeaderView(mPlaceHolder);
		mListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items));
		
		mHeaderTextContentViewHeight = mHeaderContentView.getHeight();
		
		mStickItemView = new View(this);
		mPlaceHolder.setBackgroundColor(Color.BLUE);

		mPopupWindow = new PopupWindow(mStickItemView,
				LayoutParams.MATCH_PARENT,
				Utils.dp2px(this, 200), false);
		mPopupWindow.showAtLocation(parentView, Gravity.CENTER, -5, 30);
//		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mStickItemView.getLayoutParams();
		
	}

	private static final String[] items = new String[] {
			"Layout container for a view hierarchy that can be scrolled by the user, allowing it to be larger than the physical display. A ScrollView is a FrameLayout, meaning you should place one child in it containing the entire contents to scroll; this child may itself be a layout manager with a complex hierarchy of objects. A child that is often used is a LinearLayout in a vertical orientation, presenting a vertical array of top-level items that the user can scroll through.",
			"You should never use a ScrollView with a ListView, because ListView takes care of its own vertical scrolling. Most importantly, doing this defeats all of the important optimizations in ListView for dealing with large lists, since it effectively forces the ListView to display its entire list of items to fill up the infinite container supplied by ScrollView.",
			"The TextView class also takes care of its own scrolling, so does not require a ScrollView, but using the two together is possible to achieve the effect of a text view within a larger container.",
			"ScrollView only supports vertical scrolling. For horizontal scrolling, use HorizontalScrollView.",
			"Adds a child view. If no layout parameters are already set on the child, the default parameters for this ViewGroup are set on the child." };
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.d(TAG, "onScroll");
		int state = getStickItemState();
		if (state == FLOAT && mIsInit) {
//			mListView.getScrollY();
			float scrollY = mOldY - mPlaceHolder.getY();
			mOldY = mPlaceHolder.getY();
			mStickItemView.scrollBy(0, (int) mOldY);
			Log.d(TAG, "scrollY: " + scrollY);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.d(TAG, "onScrollStateChanged");
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
			mIsInit = true;
			mOldY = mPlaceHolder.getY();
		} else {
			mOldY = 0;
		}

	}
	
	private static final int HANG = 0;
	private static final int FLOAT = 0;
	private int getStickItemState() {
		final Adapter adapter = mListView.getAdapter();
		if (adapter == null || adapter.isEmpty()) {
		}
		
		if (mPlaceHolder.getTop() >= mListView.getTop()) {
			return HANG;
		} else {
			return FLOAT;
		}
	}
	
	private enum StickItemState {
		GONE,
		VISI
	}

}
