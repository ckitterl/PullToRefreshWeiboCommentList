package com.larry.comment;

import android.content.Context;

public class Utils {
	public static int dp2px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (dp * scale + 0.5f);
	}

}
