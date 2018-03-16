package apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.PhotoAnnotateColorPickerAdapter;
import apps.lnsel.com.vhortexttest.data.DataShareImage;
import apps.lnsel.com.vhortexttest.photoannotatesdk.BrushDrawingView;
import apps.lnsel.com.vhortexttest.photoannotatesdk.OnPhotoEditorSDKListener;
import apps.lnsel.com.vhortexttest.photoannotatesdk.PhotoEditorSDK;
import apps.lnsel.com.vhortexttest.photoannotatesdk.ViewType;
import apps.lnsel.com.vhortexttest.utils.ConstantChat;
import apps.lnsel.com.vhortexttest.utils.MediaUtils;


public class ActivitySketch extends AppCompatActivity implements View.OnClickListener, OnPhotoEditorSDKListener{


    ImageButton toolbar_custom_ivbtn_back;
    TextView toolbar_custom_tv_title;
    LinearLayout toolbar_custom_lnr_group_chat_left,toolbar_custom_lnr_right;
    TextView toolbar_custom_lnr_group_chat_tv_group_name;
    TextView toolbar_custom_lnr_group_chat_tv_group_member_name;
    ImageButton custom_dialog_iv_translator,toolbar_custom_iv_attachment,toolbar_custom_iv_new_chat_relation,
            toolbar_custom_iv_search,toolbar_custom_lnr_right_iv_tick,toolbar_custom_iv_sketch_save,toolbar_custom_iv_camera,toolbar_custom_iv_bg;

    TextView toolbar_custom_lnr_right_tv_action;
    ImageView toolbar_custom_iv_profile_pic;
    ImageView toolbar_custom_iv_logo;

    int arrPen[] = {4, 8, 12, 16, 20};
    int arrBrush[] = {8, 12, 16, 20, 24};
    private ImageView
            red_pen, yellow_pen, blue_dark_pen, orange_pen, purple_pen, blue_light_pen, black_pen,
            red_brush, yellow_brush, blue_brush, orange_brush, purple_brush, blue_light_brush, black_brush,
            size_one, size_two, size_three, size_four, size_five;
    private LinearLayout lnr_option_pen, lnr_option_brush, lnr_option_size;

    ImageButton pen, brush, eraser, size, clear, send,text;
    private int sizeValuePosition = 0;
    private int draw_type = 1;
    private int TYPE_PEN = 1, TYPE_BRUSH = 2;
    ImageButton btn_slider;
    LinearLayout ll_include;
    private Animation animShow, animHide;






    private final String TAG = "ActivitySketch";
    private RelativeLayout parentImageRelativeLayout;
    private RecyclerView drawingViewColorPickerRecyclerView;
    private TextView undoTextView, undoTextTextView, doneDrawingTextView;


    private RelativeLayout bottomShadowRelativeLayout;
    private ArrayList<Integer> colorPickerColors;
    private int colorCodeTextView = -1;
    private PhotoEditorSDK photoEditorSDK;

    Boolean brushDrawingMode=false;
    int brushColor;
    float brushSize;

    Uri selectedImageUri;
    String  selectedPath;
    Boolean background=false;
    ImageView photoEditImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);

        initComponent();
        initViews();




        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_white_bg);


        Typeface newFont = Typeface.createFromAsset(getAssets(), "Eventtus-Icons.ttf");


        BrushDrawingView brushDrawingView = (BrushDrawingView) findViewById(R.id.drawing_view);
        drawingViewColorPickerRecyclerView = (RecyclerView) findViewById(R.id.drawing_view_color_picker_recycler_view);
        parentImageRelativeLayout = (RelativeLayout) findViewById(R.id.parent_image_rl);
        // TextView addTextView = (TextView) findViewById(R.id.add_text_tv);
        RelativeLayout deleteRelativeLayout = (RelativeLayout) findViewById(R.id.delete_rl);
        TextView deleteTextView = (TextView) findViewById(R.id.delete_tv);

        undoTextView = (TextView) findViewById(R.id.undo_tv);
        undoTextTextView = (TextView) findViewById(R.id.undo_text_tv);
        doneDrawingTextView = (TextView) findViewById(R.id.done_drawing_tv);

        photoEditImageView = (ImageView) findViewById(R.id.photo_edit_iv);

        bottomShadowRelativeLayout = (RelativeLayout) findViewById(R.id.bottom_parent_rl);


        photoEditImageView.setImageBitmap(bitmap);

        // addTextView.setTypeface(newFont);
        undoTextView.setTypeface(newFont);
        deleteTextView.setTypeface(newFont);



        photoEditorSDK = new PhotoEditorSDK.PhotoEditorSDKBuilder(ActivitySketch.this)
                .parentView(parentImageRelativeLayout) // add parent image view
                .childView(photoEditImageView) // add the desired image view
                .deleteView(deleteRelativeLayout) // add the deleted view that will appear during the movement of the views
                .brushDrawingView(brushDrawingView) // add the brush drawing view that is responsible for drawing on the image view
                .buildPhotoEditorSDK(); // build photo editor sdk
        photoEditorSDK.setOnPhotoEditorSDKListener(this);




        //addTextView.setOnClickListener(this);
        undoTextView.setOnClickListener(this);
        undoTextTextView.setOnClickListener(this);
        doneDrawingTextView.setOnClickListener(this);


        colorPickerColors = new ArrayList<>();
        colorPickerColors.add(getResources().getColor(R.color.black));
        colorPickerColors.add(getResources().getColor(R.color.blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.brown_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.green_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.sky_blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.violet_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.white));
        colorPickerColors.add(getResources().getColor(R.color.yellow_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.yellow_green_color_picker));


    }

    private boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }



    public void addImage(Bitmap image) {
        photoEditorSDK.addImage(image);
        /*if (mLayout != null)
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);*/
    }

    private void addText(String text, int colorCodeTextView,float x,float y) {
        photoEditorSDK.addText(text, colorCodeTextView,x,y);
    }

    private void clearAllViews() {
        photoEditorSDK.clearAllViews();
    }

    private void undoViews() {
        photoEditorSDK.viewUndo();
    }

    private void eraseDrawing() {
        photoEditorSDK.brushEraser();
    }

    private void openAddTextPopupWindow(String text, int colorCode,final float x,final float y) {
        colorCodeTextView = colorCode;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null);
        final EditText addTextEditText = (EditText) addTextPopupWindowRootView.findViewById(R.id.add_text_edit_text);
        TextView addTextDoneTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_done_tv);
        RecyclerView addTextColorPickerRecyclerView = (RecyclerView) addTextPopupWindowRootView.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivitySketch.this, LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        PhotoAnnotateColorPickerAdapter colorPickerAdapter = new PhotoAnnotateColorPickerAdapter(ActivitySketch.this, colorPickerColors);
        colorPickerAdapter.setOnColorPickerClickListener(new PhotoAnnotateColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                addTextEditText.setTextColor(colorCode);
                colorCodeTextView = colorCode;
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        if (stringIsNotEmpty(text)) {
            addTextEditText.setText(text);
            addTextEditText.setTextColor(colorCode == -1 ? getResources().getColor(R.color.white) : colorCode);
        }
        final PopupWindow pop = new PopupWindow(ActivitySketch.this);
        pop.setContentView(addTextPopupWindowRootView);
        pop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(null);
        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addText(addTextEditText.getText().toString(), colorCodeTextView,x,y);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                pop.dismiss();
            }
        });
    }

    private void updateView(int visibility) {
        //topShadow.setVisibility(visibility);
        //topShadowRelativeLayout.setVisibility(visibility);
        //bottomShadow.setVisibility(visibility);
        bottomShadowRelativeLayout.setVisibility(visibility);
    }



    private void updateBrushDrawingView(boolean brushDrawingMode,final int colorCode,final float size) {
        photoEditorSDK.setBrushDrawingMode(brushDrawingMode);
        if (brushDrawingMode) {
            this.brushDrawingMode=true;
            updateView(View.GONE);
            toolbar_custom_iv_camera.setVisibility(View.GONE);
            drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
            doneDrawingTextView.setVisibility(View.VISIBLE);
            //eraseDrawingTextView.setVisibility(View.GONE);
            photoEditorSDK.setBrushColor(colorCode);
            photoEditorSDK.setBrushSize(size);

            LinearLayoutManager layoutManager = new LinearLayoutManager(ActivitySketch.this, LinearLayoutManager.HORIZONTAL, false);
            drawingViewColorPickerRecyclerView.setLayoutManager(layoutManager);
            drawingViewColorPickerRecyclerView.setHasFixedSize(true);


        } else {
            this.brushDrawingMode=false;
            updateView(View.VISIBLE);
            toolbar_custom_iv_camera.setVisibility(View.VISIBLE);
            drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
            doneDrawingTextView.setVisibility(View.GONE);
            //eraseDrawingTextView.setVisibility(View.GONE);
        }
    }


    public void saveAndSendImage(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "IMG_" + timeStamp + ".jpg";
        String save_path=photoEditorSDK.saveImage(MediaUtils.IMAGE_DIRECTORY_NAME, imageName,ActivitySketch.this);
        sendSkecthTochat(save_path);
    }
    public void saveImage(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "IMG_" + timeStamp + ".jpg";
        String save_path=photoEditorSDK.saveImage(MediaUtils.IMAGE_DIRECTORY_NAME, imageName,ActivitySketch.this);

        //after save open in gallery
        /*Uri uri=Uri.parse("file://"+save_path);
        if(uri!=null){
            Log.e("save_uri",uri.toString());
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image*//*");
            startActivity(intent);

        }else {
            Log.e("error",save_path);
        }*/


        //after save image finish activity
        finish();

        System.out.println("save_path === "+save_path);
    }
    private void returnBackWithSavedImage() {
        /*updateView(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        parentImageRelativeLayout.setLayoutParams(layoutParams);*/
        new CountDownTimer(1000, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageName = "IMG_" + timeStamp + ".jpg";
                String save_path=photoEditorSDK.saveImage(MediaUtils.IMAGE_DIRECTORY_NAME, imageName,ActivitySketch.this);

                Uri uri=Uri.parse("file://"+save_path);

                if(uri!=null){
                    Log.e("save_uri",uri.toString());

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    //Uri data = Uri.parse(path);
                    intent.setDataAndType(uri, "image/*");
                    startActivity(intent);

                    //startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }else {
                    Log.e("error",save_path);
                    //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }

                System.out.println("save_path === "+save_path);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.add_text_tv) {
            openAddTextPopupWindow("", -1,0,0);
        }else*/ if (v.getId() == R.id.done_drawing_tv) {
            updateBrushDrawingView(false,brushColor,brushSize);
        } /*else if (v.getId() == R.id.save_tv || v.getId() == R.id.save_text_tv) {
            returnBackWithSavedImage();
        }*/ else if (v.getId() == R.id.undo_text_tv || v.getId() == R.id.undo_tv) {
            undoViews();
        }
    }

    @Override
    public void onEditTextChangeListener(String text, int colorCode,float x,float y) {
        openAddTextPopupWindow(text, colorCode,x,y);
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        if (numberOfAddedViews > 0) {
            undoTextView.setVisibility(View.VISIBLE);
            undoTextTextView.setVisibility(View.VISIBLE);
        }
        switch (viewType) {
            case BRUSH_DRAWING:
                Log.i("BRUSH_DRAWING", "onAddViewListener");
                break;
            case EMOJI:
                Log.i("EMOJI", "onAddViewListener");
                break;
            case IMAGE:
                Log.i("IMAGE", "onAddViewListener");
                break;
            case TEXT:
                Log.i("TEXT", "onAddViewListener");
                break;
        }
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.i(TAG, "onRemoveViewListener");
        if (numberOfAddedViews == 0) {
            undoTextView.setVisibility(View.GONE);
            undoTextTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        switch (viewType) {
            case BRUSH_DRAWING:
                Log.i("BRUSH_DRAWING", "onStartViewChangeListener");
                break;
            case EMOJI:
                Log.i("EMOJI", "onStartViewChangeListener");
                break;
            case IMAGE:
                Log.i("IMAGE", "onStartViewChangeListener");
                break;
            case TEXT:
                Log.i("TEXT", "onStartViewChangeListener");
                break;
        }
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        switch (viewType) {
            case BRUSH_DRAWING:
                Log.i("BRUSH_DRAWING", "onStopViewChangeListener");
                break;
            case EMOJI:
                Log.i("EMOJI", "onStopViewChangeListener");
                break;
            case IMAGE:
                Log.i("IMAGE", "onStopViewChangeListener");
                break;
            case TEXT:
                Log.i("TEXT", "onStopViewChangeListener");
                break;
        }
    }

    private class PreviewSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;

        PreviewSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragments == null) {
                return (null);
            }
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    public void photoDialog(int req_code){

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (resultCode == RESULT_OK) {
            if(data.getData() != null){
                selectedImageUri = data.getData();
            }else{
                Log.d("selectedPath1 : ","Came here its null !");
                Toast.makeText(getApplicationContext(), "failed to get Image!", Toast.LENGTH_SHORT).show();
            }

            if (requestCode == 100 && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedPath = getPath(selectedImageUri);
                //preview.setImageURI(selectedImageUri);
                Log.d("selectedPath1 : " ,selectedPath);
                addImage(photo);

            }

            if (requestCode == 10)

            {

                selectedPath = getPath(selectedImageUri);

                if(background){

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_chats_header_logo);
                        photoEditImageView.setImageBitmap(bitmap);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"something wrong",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_chats_header_logo);
                        addImage(bitmap);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"something wrong",Toast.LENGTH_SHORT).show();
                    }
                }

                // preview.setImageURI(selectedImageUri);

                Log.d("selectedPath1 : " ,selectedPath);

            }

        }

    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }

    public void backgroundChoseDialog(){
        final Dialog dialog = new Dialog(ActivitySketch.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_background_choose);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);


        TextView dialog_white = (TextView) dialog.findViewById(R.id.dialog_white);
        dialog_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_white_bg);
                photoEditImageView.setImageBitmap(bitmap);
                dialog.cancel();
            }
        });

        TextView dialog_gallery = (TextView) dialog.findViewById(R.id.dialog_gallery);
        dialog_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background=true;
                photoDialog(10);
                dialog.cancel();
            }
        });

        dialog.show();
    }





    // ====================================start old sketch ===================================================
    private void initComponent() {
        //start toolbar section...........................................................................
        toolbar_custom_ivbtn_back=(ImageButton)findViewById(R.id.toolbar_custom_ivbtn_back);
        toolbar_custom_tv_title=(TextView)findViewById(R.id.toolbar_custom_tv_title);

        custom_dialog_iv_translator=(ImageButton) findViewById(R.id.custom_dialog_iv_translator);
        toolbar_custom_iv_attachment=(ImageButton) findViewById(R.id.toolbar_custom_iv_attachment);
        toolbar_custom_iv_new_chat_relation=(ImageButton) findViewById(R.id.toolbar_custom_iv_new_chat_relation);
        toolbar_custom_iv_profile_pic=(ImageView)findViewById(R.id.toolbar_custom_iv_profile_pic);
        toolbar_custom_iv_search=(ImageButton) findViewById(R.id.toolbar_custom_iv_search);

        toolbar_custom_lnr_group_chat_left=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_group_chat_left);
        toolbar_custom_lnr_group_chat_tv_group_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_friend_or_group_name);
        toolbar_custom_lnr_group_chat_tv_group_member_name=(TextView)findViewById(R.id.toolbar_custom_lnr_group_chat_tv_status_or_group_member_name);

        toolbar_custom_lnr_right=(LinearLayout)findViewById(R.id.toolbar_custom_lnr_right);
        toolbar_custom_lnr_right_iv_tick=(ImageButton) findViewById(R.id.toolbar_custom_lnr_right_iv_tick);
        toolbar_custom_lnr_right_tv_action=(TextView)findViewById(R.id.toolbar_custom_lnr_right_tv_action);

        toolbar_custom_iv_sketch_save=(ImageButton) findViewById(R.id.toolbar_custom_iv_sketch_save);
        toolbar_custom_iv_sketch_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SaveClick();
                saveImage();
                // returnBackWithSavedImage();
            }
        });
        toolbar_custom_iv_camera=(ImageButton)findViewById(R.id.toolbar_custom_iv_camera);
        toolbar_custom_iv_camera.setVisibility(View.VISIBLE);
        toolbar_custom_iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background=false;
                photoDialog(10);
            }
        });
        toolbar_custom_iv_bg=(ImageButton)findViewById(R.id.toolbar_custom_iv_bg);
        toolbar_custom_iv_bg.setVisibility(View.VISIBLE);
        toolbar_custom_iv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundChoseDialog();
            }
        });
        toolbar_custom_iv_logo=(ImageView)findViewById(R.id.toolbar_custom_iv_logo);
        toolbar_custom_iv_logo.setVisibility(View.VISIBLE);
        //toolbar_custom_ivbtn_back.setBackgroundResource(R.drawable.ic_header_back_icon);
        toolbar_custom_ivbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar_custom_tv_title.setVisibility(View.VISIBLE);
        toolbar_custom_tv_title.setText("Image Annotate");
        toolbar_custom_lnr_group_chat_left.setVisibility(View.GONE);
        toolbar_custom_lnr_right.setVisibility(View.GONE);
        toolbar_custom_iv_sketch_save.setVisibility(View.VISIBLE);
        // end toolbar section.........................................................................
    }


    private void initViews() {
        /*drawer = (SlidingDrawer) findViewById(R.id.drawer);

        handle = (ImageView) findViewById(R.id.handle);
        drawer.setOnDrawerOpenListener(ActivitySketch.this);
        drawer.setOnDrawerCloseListener(ActivitySketch.this);*/

        //drawing = (DrawingView) findViewById(R.id.drawing);
        pen = (ImageButton) findViewById(R.id.pen);
        brush = (ImageButton) findViewById(R.id.brush);
        eraser = (ImageButton) findViewById(R.id.eraser);
        size = (ImageButton) findViewById(R.id.size);
        clear = (ImageButton) findViewById(R.id.clear);
        send = (ImageButton) findViewById(R.id.send);
        text = (ImageButton) findViewById(R.id.text);


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

        //side_panel = (LinearLayout) findViewById(R.id.side_panel);
        animShow = AnimationUtils.loadAnimation(this, R.anim.sketch_slide_view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.sketch_slide_view_hide);
        ll_include=(LinearLayout) findViewById(R.id.ll_include);
        btn_slider=(ImageButton) findViewById(R.id.btn_slider);
        btn_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();

            }
        });


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

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                setDrawSizeWithDrawType(draw_type);
                if(brushDrawingMode) {
                    eraseDrawing();
                }else {
                    Toast.makeText(getApplicationContext(),"pls on drawing mode",Toast.LENGTH_LONG).show();
                }
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
                // drawing.startNew();
                // drawing.setIsPaint(false);
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                clearAllViews();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveAndSendImage();
                // returnBackWithSavedImage();
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTextPopupWindow("", -1,0,0);
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
            }
        });
        //views
        red_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#ff0000");
                //brushColor=getResources().getColor(R.color.red_color_picker);
                brushSize=getPenSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        yellow_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#fcff00");
                //brushColor=getResources().getColor(R.color.yellow_color_picker);
                brushSize=getPenSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        blue_dark_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#1666d0");
                // brushColor=getResources().getColor(R.color.blue_color_picker);
                brushSize=getPenSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        orange_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#e27408");
                // brushColor=getResources().getColor(R.color.orange_color_picker);
                brushSize=getPenSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        purple_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#fb67f2");
                // brushColor=getResources().getColor(R.color.violet_color_picker);
                brushSize=getPenSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        blue_light_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#4ea1bb");
                // brushColor=getResources().getColor(R.color.sky_blue_color_picker);
                brushSize=getPenSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        black_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#000000");
                //brushColor=getResources().getColor(R.color.black);
                brushSize=getPenSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });



        red_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#ff0000");
                // brushColor=getResources().getColor(R.color.red_color_picker);
                brushSize=getBrushSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        yellow_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#fcff00");
                //brushColor=getResources().getColor(R.color.yellow_color_picker);
                brushSize=getBrushSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        blue_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#1666d0");
                // brushColor=getResources().getColor(R.color.blue_color_picker);
                brushSize=getBrushSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        orange_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#e27408");
                // brushColor=getResources().getColor(R.color.orange_color_picker);
                brushSize=getBrushSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        purple_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#fb67f2");
                //brushColor=getResources().getColor(R.color.violet_color_picker);
                brushSize=getBrushSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        blue_light_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#4ea1bb");
                //brushColor=getResources().getColor(R.color.sky_blue_color_picker);
                brushSize=getBrushSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });
        black_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                brushColor= Color.parseColor("#000000");
                //brushColor=getResources().getColor(R.color.black);
                brushSize=getBrushSize(sizeValuePosition);
                updateBrushDrawingView(true,brushColor,brushSize);
            }
        });


        size_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 0;
                setDrawSizeWithDrawType(draw_type);
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();

                if(draw_type==TYPE_BRUSH){
                    brushSize=getBrushSize(sizeValuePosition);
                }else {
                    brushSize=getPenSize(sizeValuePosition);
                }

                if(brushDrawingMode) {
                    //photoEditorSDK.setBrushColor(colorCode);
                    photoEditorSDK.setBrushSize(brushSize);
                }else {
                    Toast.makeText(getApplicationContext(),"pls on drawing mode",Toast.LENGTH_LONG).show();
                }
            }
        });
        size_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 1;
                setDrawSizeWithDrawType(draw_type);
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();

                if(draw_type==TYPE_BRUSH){
                    brushSize=getBrushSize(sizeValuePosition);
                }else {
                    brushSize=getPenSize(sizeValuePosition);
                }
                if(brushDrawingMode) {
                    //photoEditorSDK.setBrushColor(colorCode);
                    photoEditorSDK.setBrushSize(brushSize);
                }else {
                    Toast.makeText(getApplicationContext(),"pls on drawing mode",Toast.LENGTH_LONG).show();
                }
            }
        });
        size_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 2;
                setDrawSizeWithDrawType(draw_type);
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                if(draw_type==TYPE_BRUSH){
                    brushSize=getBrushSize(sizeValuePosition);
                }else {
                    brushSize=getPenSize(sizeValuePosition);
                }
                if(brushDrawingMode) {
                    //photoEditorSDK.setBrushColor(colorCode);
                    photoEditorSDK.setBrushSize(brushSize);
                }else {
                    Toast.makeText(getApplicationContext(),"pls on drawing mode",Toast.LENGTH_LONG).show();
                }
            }
        });
        size_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 3;
                setDrawSizeWithDrawType(draw_type);
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                if(draw_type==TYPE_BRUSH){
                    brushSize=getBrushSize(sizeValuePosition);
                }else {
                    brushSize=getPenSize(sizeValuePosition);
                }
                if(brushDrawingMode) {
                    //photoEditorSDK.setBrushColor(colorCode);
                    photoEditorSDK.setBrushSize(brushSize);
                }else {
                    Toast.makeText(getApplicationContext(),"pls on drawing mode",Toast.LENGTH_LONG).show();
                }
            }
        });
        size_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeValuePosition = 4;
                setDrawSizeWithDrawType(draw_type);
                if(ll_include.isShown()){
                    ll_include.startAnimation(animHide);
                    ll_include.setVisibility(View.GONE);
                }else {
                    ll_include.startAnimation(animShow);
                    ll_include.setVisibility(View.VISIBLE);
                }
                closedOptionDrawer();
                if(draw_type==TYPE_BRUSH){
                    brushSize=getBrushSize(sizeValuePosition);
                }else {
                    brushSize=getPenSize(sizeValuePosition);
                }
                if(brushDrawingMode) {
                    //photoEditorSDK.setBrushColor(colorCode);
                    photoEditorSDK.setBrushSize(brushSize);
                }else {
                    Toast.makeText(getApplicationContext(),"pls on drawing mode",Toast.LENGTH_LONG).show();
                }
            }
        });
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

        DataShareImage mDataShareImage = new DataShareImage();
        mDataShareImage.setImgUrl(imgSaved);
        mDataShareImage.setSketchType(true);

        ArrayList<DataShareImage> mListString = new ArrayList<DataShareImage>();
        mListString.add(mDataShareImage);

        Intent mIntent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(ConstantChat.B_RESULT, mListString);
        mIntent.putExtras(mBundle);
        setResult(ConstantChat.SketchSelect, mIntent);
        finish();

    }



    private void openPenChoice() {

        int arr[] = new int[2];
        pen.getLocationInWindow(arr);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lnr_option_pen.getLayoutParams();
        layoutParams.leftMargin = arr[0] + getDensityPixel(ActivitySketch.this);
        layoutParams.topMargin = arr[1] / 3;
        lnr_option_pen.setLayoutParams(layoutParams);
        lnr_option_pen.setVisibility(View.VISIBLE);
    }

    private void openBrushChoice() {

        int arr[] = new int[2];
        brush.getLocationInWindow(arr);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lnr_option_brush.getLayoutParams();
        layoutParams.leftMargin = arr[0] + getDensityPixel(ActivitySketch.this);
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

    /*@Override
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

    }*/

    public void closedOptionDrawer(){
        if (lnr_option_pen.getVisibility() == View.VISIBLE)
            lnr_option_pen.setVisibility(View.GONE);

        if (lnr_option_brush.getVisibility() == View.VISIBLE)
            lnr_option_brush.setVisibility(View.GONE);

        if (lnr_option_size.getVisibility() == View.VISIBLE)
            lnr_option_size.setVisibility(View.GONE);
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
                // drawing.setBrushSize(getPenSize(sizeValuePosition));

                break;
            case 2:
                //brush
                // drawing.setBrushSize(getBrushSize(sizeValuePosition));
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
