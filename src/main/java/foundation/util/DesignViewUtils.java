package foundation.util;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Created by li on 2017/2/24.
 */

public class DesignViewUtils {

    /**
     * AppBarLayout 完全显示 打开状态
     *
     * @param verticalOffset
     * @return
     */
    public static boolean isAppBarLayoutOpen(int verticalOffset) {
        return verticalOffset >= 0;
    }

    /**
     * AppBarLayout 关闭或折叠状态
     *
     * @param appBarLayout
     * @param verticalOffset
     * @return
     */
    public static boolean isAppBarLayoutClose(AppBarLayout appBarLayout, int verticalOffset) {
        return appBarLayout.getTotalScrollRange() == Math.abs(verticalOffset);
    }

    /**
     *      * RecyclerView 滚动到底部 最后一条完全显示
     *      *
     *      * @param recyclerView
     *      * @return
     *      
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    /**
     * RecyclerView 滚动到顶端
     *
     * @param recyclerView
     * @return      
     */
    public static boolean isSlideToTop(RecyclerView recyclerView) {
        return recyclerView.computeVerticalScrollOffset() <= 0;
    }
}
