package apps.lnsel.com.vhortexttest.helpers.CustomViews.tooltippopupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;


public class FirstTimeChatOptionsPopUpWindow extends PopupWindow {

    public static final String FIRST_TIME_CHAT_OPTION_CLASSMATE = "Classmate";
    public static final String FIRST_TIME_CHAT_OPTION_FRIEND = "Friend";
    public static final String FIRST_TIME_CHAT_OPTION_ACQUAINTANCE = "Acquaintance";
    public static final String FIRST_TIME_CHAT_OPTION_CO_WORKER = "Co-Worker";
    public static final String FIRST_TIME_CHAT_OPTION_COLLEAGUE = "Colleague";
    public static final String FIRST_TIME_CHAT_OPTION_OTHER = "Other";


    private RelativeLayout rel_classmate;
    private ImageView iv_classmate;
    private RelativeLayout rel_friend;
    private ImageView iv_friend;
    private RelativeLayout rel_acquaintance;
    private ImageView iv_acquaintance;
    private RelativeLayout rel_coworker;
    private ImageView iv_coworker;
    private RelativeLayout rel_colleague;
    private ImageView iv_colleague;
    private RelativeLayout rel_other;
    private ImageView iv_other;


    Context conText;

    public FirstTimeChatOptionsPopUpWindow(Context context, final ImageView relation, final ImageView profile_pic, final ImageView translation, final ImageView attachment ) {
        super(((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.include_activity_chat_dropdown, null), LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);


        conText=context;

        int AppVhortex_width = CommonMethods.getScreenWidth(context).widthPixels;
        int width = (int) (AppVhortex_width * 0.7f);
        LinearLayout rlRoot = (LinearLayout) getContentView().findViewById(R.id.lnr_main);
        rlRoot.setLayoutParams(new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT));


        rel_classmate = (RelativeLayout) getContentView().findViewById(R.id.rel_classmate);
        rel_classmate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_classmate.setSelected(true);
                iv_friend.setSelected(false);
                iv_acquaintance.setSelected(false);
                iv_coworker.setSelected(false);
                iv_colleague.setSelected(false);
                iv_other.setSelected(false);
                callbackFistTimeChatPopUpWindow(FIRST_TIME_CHAT_OPTION_CLASSMATE,relation,profile_pic,translation,attachment);
                dismiss();

            }
        });
        iv_classmate = (ImageView) getContentView().findViewById(R.id.iv_classmate);
        rel_friend = (RelativeLayout) getContentView().findViewById(R.id.rel_friend);
        rel_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_classmate.setSelected(false);
                iv_friend.setSelected(true);
                iv_acquaintance.setSelected(false);
                iv_coworker.setSelected(false);
                iv_colleague.setSelected(false);
                iv_other.setSelected(false);
                callbackFistTimeChatPopUpWindow(FIRST_TIME_CHAT_OPTION_FRIEND,relation,profile_pic,translation,attachment);
                dismiss();
            }
        });
        iv_friend = (ImageView) getContentView().findViewById(R.id.iv_friend);
        rel_acquaintance = (RelativeLayout) getContentView().findViewById(R.id.rel_acquaintance);
        rel_acquaintance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_classmate.setSelected(false);
                iv_friend.setSelected(false);
                iv_acquaintance.setSelected(true);
                iv_coworker.setSelected(false);
                iv_colleague.setSelected(false);
                iv_other.setSelected(false);
                callbackFistTimeChatPopUpWindow(FIRST_TIME_CHAT_OPTION_ACQUAINTANCE,relation,profile_pic,translation,attachment);
                dismiss();
            }
        });
        iv_acquaintance = (ImageView) getContentView().findViewById(R.id.iv_acquaintance);
        rel_coworker = (RelativeLayout) getContentView().findViewById(R.id.rel_coworker);
        rel_coworker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_classmate.setSelected(false);
                iv_friend.setSelected(false);
                iv_acquaintance.setSelected(false);
                iv_coworker.setSelected(true);
                iv_colleague.setSelected(false);
                iv_other.setSelected(false);
                callbackFistTimeChatPopUpWindow(FIRST_TIME_CHAT_OPTION_CO_WORKER,relation,profile_pic,translation,attachment);
                dismiss();
            }
        });
        iv_coworker = (ImageView) getContentView().findViewById(R.id.iv_coworker);
        rel_colleague = (RelativeLayout) getContentView().findViewById(R.id.rel_colleague);
        rel_colleague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_classmate.setSelected(false);
                iv_friend.setSelected(false);
                iv_acquaintance.setSelected(false);
                iv_coworker.setSelected(false);
                iv_colleague.setSelected(true);
                iv_other.setSelected(false);
                callbackFistTimeChatPopUpWindow(FIRST_TIME_CHAT_OPTION_COLLEAGUE,relation,profile_pic,translation,attachment);
                dismiss();
            }
        });
        iv_colleague = (ImageView) getContentView().findViewById(R.id.iv_colleague);
        rel_other = (RelativeLayout) getContentView().findViewById(R.id.rel_other);
        rel_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_classmate.setSelected(false);
                iv_friend.setSelected(false);
                iv_acquaintance.setSelected(false);
                iv_coworker.setSelected(false);
                iv_colleague.setSelected(false);
                iv_other.setSelected(true);
                callbackFistTimeChatPopUpWindow(FIRST_TIME_CHAT_OPTION_OTHER,relation,profile_pic,translation,attachment);
                dismiss();
            }
        });
        iv_other = (ImageView) getContentView().findViewById(R.id.iv_other);



        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchable(true);

    }


    public void callbackFistTimeChatPopUpWindow(String relation,ImageView iv_relation,ImageView profile_pic,ImageView translation,ImageView attachment){
        /*attachment.setVisibility(View.VISIBLE);
        translation.setVisibility(View.VISIBLE);
        iv_relation.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)profile_pic.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        profile_pic.setLayoutParams(params);*/
        ConstantUtil.Relation_Status=relation;
        DatabaseHandler DB=new DatabaseHandler(conText);
        ContactUserModel.updateRelationStatus(DB,String.valueOf(true),ConstantUtil.msgRecId);
    }
}
