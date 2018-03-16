package apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import apps.lnsel.com.vhortexttest.R;

import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;


public class GroupTooltipPopUpWindow extends PopupWindow {

    private TooltipItemClickListener itemClickListener;
    private OnDismissListener onDismissListener;
    private GroupChatData dataItem;
    SharedManagerUtil session;
    public GroupTooltipPopUpWindow(Context context,
                                   TooltipItemClickListener itemClickListener,
                                   OnDismissListener onDismissListener, GroupChatData dataItem) {
        super(((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.menu_tooltip, null), LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        this.itemClickListener = itemClickListener;
        this.onDismissListener = onDismissListener;
        session=new SharedManagerUtil(context);
        this.dataItem = dataItem;

        RelativeLayout rlRoot = (RelativeLayout) getContentView().findViewById(R.id.menu_tooltip_root);
        rlRoot.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        if(dataItem.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
            getContentView().findViewById(R.id.menu_tooltip_copy).setVisibility(View.VISIBLE);
        }else{
            getContentView().findViewById(R.id.menu_tooltip_copy).setVisibility(View.GONE);
        }

        if(dataItem.grpcType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE) || dataItem.grpcType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE)){
            getContentView().findViewById(R.id.menu_tooltip_share).setVisibility(View.GONE);
        }else if (dataItem.grpcType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)
                || dataItem.grpcType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)
                || dataItem.grpcType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)
                || dataItem.grpcType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)) {

            if(!dataItem.grpcSenId.equalsIgnoreCase(session.getUserId())) {
                if (dataItem.grpcFileStatus.equalsIgnoreCase("0") || dataItem.grpcFileStatus.equalsIgnoreCase("1")) {
                    getContentView().findViewById(R.id.menu_tooltip_share).setVisibility(View.GONE);
                } else {
                    getContentView().findViewById(R.id.menu_tooltip_share).setVisibility(View.VISIBLE);
                }
            }else {
                getContentView().findViewById(R.id.menu_tooltip_share).setVisibility(View.VISIBLE);
            }

        }else{
            getContentView().findViewById(R.id.menu_tooltip_share).setVisibility(View.VISIBLE);
        }


        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);

        setTouchable(true);

        setListeners();

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setItemBackGroundDrawable(Drawable bgDrawable) {

    }

    private void setListeners() {
        setOnDismissListener(onDismissListener);

        getContentView().findViewById(R.id.menu_tooltip_delete).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onFilterItemClick(v);
                    }
                });
        getContentView().findViewById(R.id.menu_tooltip_copy).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onFilterItemClick(v);
                    }
                });
        getContentView().findViewById(R.id.menu_tooltip_cancel).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onFilterItemClick(v);
                    }
                });
        getContentView().findViewById(R.id.menu_tooltip_forward).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onFilterItemClick(v);
                    }
                });
        getContentView().findViewById(R.id.menu_tooltip_share).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onFilterItemClick(v);
                    }
                });

    }

    private void onFilterItemClick(View v) {
        itemClickListener.onTooltipItemClick(v.getId());
        this.dismiss();
    }

}
