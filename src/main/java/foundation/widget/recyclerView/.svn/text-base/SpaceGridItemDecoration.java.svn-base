package foundation.widget.recyclerView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by li on 2016/10/5.
 */

public class SpaceGridItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean includeEdge;
    public SpaceGridItemDecoration(int spacing, boolean includeEdge) {
        this.space = spacing;
        this.includeEdge = includeEdge;
    }
    public SpaceGridItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position=parent.getChildAdapterPosition(view);

            outRect.top = space;
            if(position==parent.getChildCount()&&includeEdge){
                outRect.bottom = space;
            }


    }
}
