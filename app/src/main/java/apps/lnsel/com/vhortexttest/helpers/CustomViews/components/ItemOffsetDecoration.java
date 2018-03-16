package apps.lnsel.com.vhortexttest.helpers.CustomViews.components;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by db on 8/11/2017.
 */
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemOffsetDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        // Add padding only to the zeroth item
       /* if (parent.getChildAdapterPosition(view) == 0)
        {

            outRect.right = 0;
            outRect.left = 0;
            outRect.top = 0;
            outRect.bottom = offset;
        }*/

        outRect.right = 0;
        outRect.left = 0;
        outRect.top = 0;
        outRect.bottom = offset;
    }
}
