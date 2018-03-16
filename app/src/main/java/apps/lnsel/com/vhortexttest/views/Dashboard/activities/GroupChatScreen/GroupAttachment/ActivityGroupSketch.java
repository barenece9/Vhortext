package apps.lnsel.com.vhortexttest.views.Dashboard.activities.GroupChatScreen.GroupAttachment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.GroupDataShareImage;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.DrawingView;
import apps.lnsel.com.vhortexttest.utils.ConstantGroupChat;
import apps.lnsel.com.vhortexttest.utils.MediaUtils;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;

public class ActivityGroupSketch extends Activity implements SlidingDrawer.OnDrawerOpenListener, SlidingDrawer.OnDrawerCloseListener {

    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick,toolbar_custom_iv_sketch_save;
    TextView toolbar_custom_lnr_right_tv_action;
    ImageView toolbar_custom_iv_logo,toolbar_custom_iv_profile_pic;

    int arrPen[] = {4, 8, 12, 16, 20};
    int arrBrush[] = {8, 12, 16, 20, 24};
    private ImageView pen, brush, eraser, size, clear, send,
            red_pen, yellow_pen, blue_dark_pen, orange_pen, purple_pen, blue_light_pen, black_pen,
            red_brush, yellow_brush, blue_brush, orange_brush, purple_brush, blue_light_brush, black_brush,
            size_one, size_two, size_three, size_four, size_five,
            handle;
    private LinearLayout lnr_option_pen, lnr_option_brush, lnr_option_size, side_panel;
    private SlidingDrawer drawer;
    private DrawingView drawing;
    //brush size
    private float mediumBrush;
    private int sizeValuePosition = 0;
    private int draw_type = 1;
    private int TYPE_PEN = 1, TYPE_BRUSH = 2;
    private File sketchSavedFile;
    private String mSketchFileName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_group_sketch);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        mSketchFileName = "Sketch_" + timeStamp + ".jpg";
        initComponent();
        initViews();
        drawing.setColor("#ff0000");
        drawing.setBrushSize(getPenSize(sizeValuePosition));
    }

    private void initComponent() {
        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton)findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton)findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton)findViewById(R.id.toolbar_custom_iv_search);

        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick=(ImageButton)findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_iv_sketch_save=(ImageButton)findViewById(R.id.toolbar_custom_iv_sketch_save);
        toolbar_custom_iv_sketch_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveClick();
            }
        });
        toolbar_custom_iv_logo=(ImageView)findViewById(R.id.toolbar_custom_iv_logo);
        toolbar_custom_iv_logo.setVisibility(View.GONE);
        //toolbar_custom_ivbtn_back.setBackgroundResource(R.drawable.ic_header_back_icon);
        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText(getString(R.string.sketch));
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_sketch_save.setVisibility(View.VISIBLE);
        // end toolbar section.........................................................................
    }


    private void initViews() {
        drawer = (SlidingDrawer) findViewById(R.id.drawer);

        handle = (ImageView) findViewById(R.id.handle);
        drawer.setOnDrawerOpenListener(this);
        drawer.setOnDrawerCloseListener(this);
        drawing = (DrawingView) findViewById(R.id.drawing);
        pen = (ImageView) findViewById(R.id.pen);
        brush = (ImageView) findViewById(R.id.brush);
        eraser = (ImageView) findViewById(R.id.eraser);
        size = (ImageView) findViewById(R.id.size);
        clear = (ImageView) findViewById(R.id.clear);
        send = (ImageView) findViewById(R.id.send);

        //option views
        lnr_option_pen = (LinearLayout) findViewById(R.id.lnr_option_pen);
        lnr_option_brush = (LinearLayout) findViewById(R.id.lnr_option_brush);
        lnr_option_size = (LinearLayout) findViewById(R.id.lnr_option_size);

        //pen
        red_pen = (ImageView) findViewById(R.id.red_pen);
        yellow_pen = (ImageView) findViewById(R.id.yellow_pen);
        blue_dark_pen = (ImageView) findViewById(R.id.blue_dark_pen);
        orange_pen = (ImageView) findViewById(R.id.orange_pen);
        purple_pen = (ImageView) findViewById(R.id.purple_pen);
        blue_light_pen = (ImageView) findViewById(R.id.blue_light_pen);
        black_pen = (ImageView) findViewById(R.id.black_pen);

        //brush
        red_brush = (ImageView) findViewById(R.id.red_brush);
        yellow_brush = (ImageView) findViewById(R.id.yellow_brush);
        blue_brush = (ImageView) findViewById(R.id.blue_brush);
        orange_brush = (ImageView) findViewById(R.id.orange_brush);
        purple_brush = (ImageView) findViewById(R.id.purple_brush);
        blue_light_brush = (ImageView) findViewById(R.id.blue_light_brush);
        black_brush = (ImageView) findViewById(R.id.black_brush);

        //size
        size_one = (ImageView) findViewById(R.id.size_one);
        size_three = (ImageView) findViewById(R.id.size_three);
        size_two = (ImageView) findViewById(R.id.size_two);
        size_four = (ImageView) findViewById(R.id.size_four);
        size_five = (ImageView) findViewById(R.id.size_five);

        side_panel = (LinearLayout) findViewById(R.id.side_panel);

        clickListeners();
    }

    private void clickListeners() {
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw_type = TYPE_PEN;
                hideViews(lnr_option_brush);
                hideViews(lnr_option_size);
//                toggleViews(lnr_option_pen);
                if (!lnr_option_pen.isShown())
                    openPenChoice();
                else
                    hideViews(lnr_option_pen);
            }
        });
        brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw_type = TYPE_BRUSH;
                hideViews(lnr_option_pen);
                hideViews(lnr_option_size);
                // toggleViews(lnr_option_brush);
                if (!lnr_option_brush.isShown())
                    openBrushChoice();
                else
                    hideViews(lnr_option_brush);
            }
        });
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideViews(lnr_option_pen);
                hideViews(lnr_option_brush);
                hideViews(lnr_option_size);
                drawing.setErase(true);

                //drawing.setColor("#ffffff");
                drawing.setBrushSize(getPenSize(sizeValuePosition));
                drawer.close();

                setDrawSizeWithDrawType(draw_type);
            }
        });
        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideViews(lnr_option_pen);
                hideViews(lnr_option_brush);
                // toggleViews(lnr_option_size);
                if (!lnr_option_size.isShown())
                    openSizeChoice();
                else
                    hideViews(lnr_option_size);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.startNew();
                drawing.setIsPaint(false);
                drawer.close();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawing.isPaint()) {
                    drawing.setDrawingCacheEnabled(true);
                    //attempt to save
                    sketchSavedFile = MediaUtils.saveImageStringToPublicFile(drawing.getDrawingCache(),mSketchFileName, ActivityGroupSketch.this);
                    if (sketchSavedFile != null && sketchSavedFile.exists()) {

//                        ToastUtils.showAlertToast(mActivity, "Drawing successfully sent", ToastType.SUCESS_ALERT);
//                        ToastUtils.showAlertToast(mActivity, imgSaved, ToastType.SUCESS_ALERT);
                        if(!alreadyNotifiedMediaScanner) {
                            try {
                                int index = sketchSavedFile.getPath().lastIndexOf('.') + 1;
                                String ext = sketchSavedFile.getPath().substring(index).toLowerCase();
                                MediaUtils.refreshGalleryAppToShowTheFile(getApplicationContext(), sketchSavedFile.getPath(), ext);
                                alreadyNotifiedMediaScanner = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        sendSkecthTochat(sketchSavedFile.getPath());

                    } else {

                    }
                    drawing.destroyDrawingCache();
                } else {
                    ToastUtil.showAlertToast(ActivityGroupSketch.this, "Oops! Image could not be saved. Please try again", ToastType.FAILURE_ALERT);
                }

                drawer.close();
            }
        });


        //views
        red_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#ff0000");
                drawing.setBrushSize(getPenSize(sizeValuePosition));
                drawer.close();
            }
        });
        yellow_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#fcff00");
                drawing.setBrushSize(getPenSize(sizeValuePosition));
                drawer.close();
            }
        });
        blue_dark_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#1666d0");
                drawing.setBrushSize(getPenSize(sizeValuePosition));
                drawer.close();
            }
        });
        orange_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#e27408");
                drawing.setBrushSize(getPenSize(sizeValuePosition));
                drawer.close();
            }
        });
        purple_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#fb67f2");
                drawing.setBrushSize(getPenSize(sizeValuePosition));
                drawer.close();
            }
        });
        blue_light_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#4ea1bb");
                drawer.close();
            }
        });
        black_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#000000");
                drawer.close();
            }
        });



        red_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#ff0000");
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                drawer.close();
            }
        });
        yellow_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#fcff00");
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                drawer.close();
            }
        });
        blue_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#1666d0");
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                drawer.close();
            }
        });
        orange_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#e27408");
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                drawer.close();
            }
        });
        purple_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#fb67f2");
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                drawer.close();
            }
        });
        blue_light_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#4ea1bb");
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                drawer.close();
            }
        });
        black_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing.setErase(false);
                drawing.setColor("#000000");
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                drawer.close();
            }
        });


        size_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 0;
                setDrawSizeWithDrawType(draw_type);
                drawer.close();
            }
        });
        size_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 1;
                setDrawSizeWithDrawType(draw_type);
                drawer.close();
            }
        });
        size_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 2;
                setDrawSizeWithDrawType(draw_type);
                drawer.close();
            }
        });
        size_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 3;
                setDrawSizeWithDrawType(draw_type);
                drawer.close();
            }
        });
        size_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 4;
                setDrawSizeWithDrawType(draw_type);
                drawer.close();
            }
        });
    }




    boolean alreadyNotifiedMediaScanner = false;



    public void SaveClick() {
        if (drawing.isPaint()) {
            drawing.setDrawingCacheEnabled(true);

            sketchSavedFile = MediaUtils.saveImageStringToPublicFile(drawing.getDrawingCache(), mSketchFileName,this);

            if (sketchSavedFile != null && sketchSavedFile.exists()) {

                if(!alreadyNotifiedMediaScanner) {
                    try {
                        int index = sketchSavedFile.getPath().lastIndexOf('.') + 1;
                        String ext = sketchSavedFile.getPath().substring(index).toLowerCase();
                        MediaUtils.refreshGalleryAppToShowTheFile(getApplicationContext(), sketchSavedFile.getPath(), ext);
                        alreadyNotifiedMediaScanner = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //feedback
            if (sketchSavedFile != null && sketchSavedFile.exists()) {
                ToastUtil.showAlertToast(this, "Drawing successfully saved", ToastType.SUCCESS_ALERT);
            } else {
                ToastUtil.showAlertToast(this, "Oops! Image could not be saved.", ToastType.FAILURE_ALERT);
            }
            drawing.destroyDrawingCache();
        } else {
            ToastUtil.showAlertToast(this, "No image to save", ToastType.FAILURE_ALERT);
        }
    }

    private void hideViews(View mView) {
        if (mView != null)
            if (mView.isShown())
                mView.setVisibility(View.GONE);


    }

    private void toggleViews(View mView) {
        if (mView != null)
            if (!mView.isShown())
                mView.setVisibility(View.VISIBLE);
            else
                mView.setVisibility(View.GONE);
    }


    private void sendSkecthTochat(String imgSaved) {
//        String imgUri = getImagePatFromURI(imgSaved);

        GroupDataShareImage mDataShareImage = new GroupDataShareImage();
        mDataShareImage.setImgUrl(imgSaved);
        mDataShareImage.setSketchType(true);

        ArrayList<GroupDataShareImage> mListString = new ArrayList<GroupDataShareImage>();
        mListString.add(mDataShareImage);

        Intent mIntent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(ConstantGroupChat.B_RESULT, mListString);
        mIntent.putExtras(mBundle);
        setResult(ConstantGroupChat.SketchSelect, mIntent);
        finish();

    }

    private void openPenChoice() {

        int arr[] = new int[2];
        pen.getLocationInWindow(arr);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lnr_option_pen.getLayoutParams();
        layoutParams.leftMargin = arr[0] + getDensityPixel(ActivityGroupSketch.this);
        layoutParams.topMargin = arr[1] / 9;
        lnr_option_pen.setLayoutParams(layoutParams);
        lnr_option_pen.setVisibility(View.VISIBLE);
    }

    private void openBrushChoice() {

        int arr[] = new int[2];
        brush.getLocationInWindow(arr);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lnr_option_brush.getLayoutParams();
        layoutParams.leftMargin = arr[0] + getDensityPixel(ActivityGroupSketch.this);
        layoutParams.topMargin = arr[1] / 2;
        lnr_option_brush.setLayoutParams(layoutParams);
        lnr_option_brush.setVisibility(View.VISIBLE);
    }

    private void openSizeChoice() {
        int arr[] = new int[2];
        eraser.getLocationInWindow(arr);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lnr_option_size.getLayoutParams();
        layoutParams.leftMargin = arr[0] + getDensityPixel(this);
        layoutParams.topMargin = arr[1];
        lnr_option_size.setLayoutParams(layoutParams);
        lnr_option_size.setVisibility(View.VISIBLE);
    }

    //define size array value for pen,brush,eraser

    @Override
    public void onDrawerClosed() {
        if (lnr_option_pen.getVisibility() == View.VISIBLE)
            lnr_option_pen.setVisibility(View.GONE);

        if (lnr_option_brush.getVisibility() == View.VISIBLE)
            lnr_option_brush.setVisibility(View.GONE);

        if (lnr_option_size.getVisibility() == View.VISIBLE)
            lnr_option_size.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerOpened() {

    }

    public int getPenSize(int pos) {
        int a = arrPen[pos];
        return a;
    }

    public int getBrushSize(int pos) {
        int a = arrBrush[pos];
        return a;
    }


    public void setDrawSizeWithDrawType(int draw_type) {

        switch (draw_type) {

            case 1:
                //pen
                drawing.setBrushSize(getPenSize(sizeValuePosition));

                break;
            case 2:
                //brush
                drawing.setBrushSize(getBrushSize(sizeValuePosition));
                break;
            default:
                break;

        }
    }


    public  int getDensityPixel(Context mContext) {

        int value = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                value = 8;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                value = 60;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                value = 80;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                value = 110;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                value = 160;
                break;
        }
        return value;
    }
}
