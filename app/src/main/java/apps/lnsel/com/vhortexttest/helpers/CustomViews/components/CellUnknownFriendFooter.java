package apps.lnsel.com.vhortexttest.helpers.CustomViews.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ChatData;


public class CellUnknownFriendFooter extends Cell{
    //
    public TextView tv_cell_footer_reject;
    public LinearLayout cell;
    private Context context;
    private TextView tv_cell_footer_accept;

    public CellUnknownFriendFooter(Context context) {
        super(context);
        this.context = context;
    }

    public CellUnknownFriendFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public CellUnknownFriendFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void initComponent(Context context) {
        super.initComponent(context);
        LayoutInflater.from(getContext()).inflate(R.layout.cell_footer0, this, true);
        cell = (LinearLayout) findViewById(R.id.cell);
        tv_cell_footer_accept = (TextView)findViewById(R.id.tv_cell_footer_accept);
        tv_cell_footer_reject = (TextView) findViewById(R.id.tv_cell_footer_reject);
    }

    @Override
    public void setUpView(boolean isMine, ChatData mDataTextChat) {
        // Recognize all of the default link text patterns
        this.setGravity(Gravity.CENTER);
        tv_cell_footer_accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_cell_footer_reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }

}
