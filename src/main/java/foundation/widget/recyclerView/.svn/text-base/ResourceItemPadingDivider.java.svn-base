package foundation.widget.recyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by li on 2017/1/2.
 */

public class ResourceItemPadingDivider extends RecyclerView.ItemDecoration {
    private Drawable mDrawable;
    private  int  pading;

    /**
     * 作为Divider的Drawable对象
     *
     * @param context 当前上下文用于获取资源
     * @param resId   color drawable等类型资源文件
     */
    public ResourceItemPadingDivider(Context context, int resId,int  pading) {
        mDrawable = ContextCompat.getDrawable(context,resId);
        this.pading=pading;
    }

    public void onDrawOver(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            //以下计算主要用来确定绘制的位置
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(pading, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    public void getItemOffsets(Rect outRect, int position, RecyclerView parent) {
        outRect.set(pading, 0, 0, mDrawable.getIntrinsicWidth());
    }
}
