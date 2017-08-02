package cn.sbx.deeper.moblie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by caomang on 2016/12/16.
 */

public class ListViewForScorllView extends ListView{


    public ListViewForScorllView(Context context) {
        super(context);
    }

    public ListViewForScorllView(Context context,AttributeSet attrs){
        super(context, attrs);
    }




    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }




}
