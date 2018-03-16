package apps.lnsel.com.vhortexttest.views.Dashboard.fragments.ChatListScreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.ChatListAdapter;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ChatData;
import apps.lnsel.com.vhortexttest.data.ChatListData;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatTextView;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ChatModel;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;

/**
 * Created by apps2 on 7/8/2017.
 */
public class ChatListFragment extends Fragment {

    ImageButton fragment_custom_header_iv_refresh,fragment_custom_header_iv_search,fragment_custom_header_iv_create_group;
    TextView fragment_custom_header_tv_title;
    Button custom_invite_header_close;
    LinearLayout ll_inviteHeaderBar;
    RelativeLayout rl_header_invite_logo;
    ChatTextView fragment_chat_tv_empty_text;
    ProgressBarCircularIndeterminate fragment_chat_progressBarCircularIndetermininate;

    private SwipeListView fragment_chat_swipeList;
    private ChatListAdapter adapterFragmentChat;
    private ArrayList<ChatListData> arrayList_chat = new ArrayList<ChatListData>();
    private ArrayList<ChatListData> arrayList_chat1 = new ArrayList<ChatListData>();
    private ArrayList<ChatListData> arrayList_chat2 = new ArrayList<ChatListData>();

    private static View rootView;

    SharedManagerUtil session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        } catch (InflateException e) {

        }

        session = new SharedManagerUtil(getActivity());

        fragment_custom_header_tv_title=(TextView)rootView.findViewById(R.id.fragment_custom_header_tv_title);
        fragment_custom_header_tv_title.setText(getString(R.string.chats));

        fragment_custom_header_iv_refresh=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_refresh);
        fragment_custom_header_iv_search=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_search);
        fragment_custom_header_iv_create_group=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_create_group);

        rl_header_invite_logo=(RelativeLayout) rootView.findViewById(R.id.rl_header_invite_logo);
        custom_invite_header_close=(Button)rootView.findViewById(R.id.custom_invite_header_close);
        ll_inviteHeaderBar=(LinearLayout) rootView.findViewById(R.id.ll_inviteHeaderBar);

        fragment_custom_header_iv_refresh.setVisibility(View.GONE);
        fragment_custom_header_iv_search.setVisibility(View.VISIBLE);
        fragment_custom_header_iv_create_group.setVisibility(View.GONE);

        fragment_custom_header_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startFindPeopleActivity(false);
            }
        });

        custom_invite_header_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_inviteHeaderBar.setVisibility(View.GONE);
            }
        });

        rl_header_invite_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startInviteFriendActivity(false);
            }
        });


        fragment_chat_swipeList = (SwipeListView) rootView.findViewById(R.id.swipeList);

        fragment_chat_tv_empty_text = (ChatTextView) rootView.findViewById(R.id.fragment_chat_tv_empty_text);
        fragment_chat_tv_empty_text.setVisibility(View.GONE);
        fragment_chat_swipeList.setVisibility(View.VISIBLE);

        fragment_chat_progressBarCircularIndetermininate = (ProgressBarCircularIndeterminate) rootView.
                findViewById(R.id.fragment_chat_progressBarCircularIndetermininate);

        float width = CommonMethods.getScreenWidth(getActivity()).widthPixels
                - CommonMethods.convertDpToPixel(getActivity().getResources().
                getDimension(R.dimen.width_swipe_section), getActivity());
        fragment_chat_swipeList.setOffsetLeft(getActivity().getResources().getDimension(R.dimen.width_swipe_section));

        fragment_chat_swipeList.setOffsetRight(width);

        //fragment_chat_swipeList.setOffsetLeft(250);
        //fragment_chat_swipeList.setOffsetRight(50);

        new Get_ALL_Chat_From_Local_DB().execute();


        ConstantUtil.fag_chat_listing=true;
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        //new Get_ALL_Chat_From_Local_DB().execute();
       // ConstantUtil.fag_chat_listing=true;
    }

    public boolean check_arraylist_duplicate(String msgSenId, String msgRecId){
        for(int i=0; i<arrayList_chat.size(); i++){
            if(arrayList_chat.get(i).msgSenId.equalsIgnoreCase(msgRecId) || (arrayList_chat.get(i).msgSenId.equalsIgnoreCase(msgSenId) && arrayList_chat.get(i).msgRecId.equalsIgnoreCase(msgRecId))){
                return true;
            }
        }
        return false;

    }


    public boolean check_arraylist_inner_duplicate(String msgSenId,String msgRecId){
        for(int i=0; i<arrayList_chat.size(); i++){
            if(arrayList_chat.get(i).msgSenId.equalsIgnoreCase(msgSenId) && arrayList_chat.get(i).msgRecId.equalsIgnoreCase(msgRecId)){
                return true;
            }
        }
        return false;

    }




    public void setChatListCountView(String msgSenId,ChatData chat,Boolean isTranslation){
        Log.e("LOG", "chat list fragment callled== ============  " );
        System.out.println("chat list fragment callled==========================================");

        Boolean isPresent=false;
        for(int i=0;i<arrayList_chat.size();i++){
            if(arrayList_chat.get(i).msgSenId.equalsIgnoreCase(msgSenId)){

                if(!isTranslation){
                    int privetChatCount=Integer.valueOf(arrayList_chat.get(i).msgUnreadCount)+1;
                    arrayList_chat.get(i).setMsgUnreadCount(String.valueOf(privetChatCount));
                }else {
                    int privetChatCount=Integer.valueOf(arrayList_chat.get(i).msgUnreadCount);
                    arrayList_chat.get(i).setMsgUnreadCount(String.valueOf(privetChatCount));
                }


                arrayList_chat.get(i).setMsgType(chat.msgType);
                arrayList_chat.get(i).setTranslationStatus(chat.translationStatus);

                if(chat.msgType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)) {

                    if(chat.translationStatus.equalsIgnoreCase("true")){
                        arrayList_chat.get(i).setMsgText(chat.msgText);//modified
                        if(! chat.translationText.equalsIgnoreCase("")){
                            arrayList_chat.get(i).setTranslationText(chat.translationText);
                        }else {
                            arrayList_chat.get(i).setTranslationText(chat.msgText);
                        }
                    }else {
                        arrayList_chat.get(i).setMsgText(chat.msgText);
                    }
                    //arrayList_chat.get(i).setMsgText(chat.msgText);
                }/*else if(chat.msgType.equalsIgnoreCase(ConstantUtil.CONTACT_TYPE)){
                    arrayList_chat.get(i).setMsgText("Contact");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.IMAGE_TYPE)){
                    arrayList_chat.get(i).setMsgText("Image");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.IMAGECAPTION_TYPE)){
                    arrayList_chat.get(i).setMsgText("Image");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.VIDEO_TYPE)){
                    arrayList_chat.get(i).setMsgText("Video");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.LOCATION_TYPE)){
                    arrayList_chat.get(i).setMsgText("Location");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.YOUTUBE_TYPE)){
                    arrayList_chat.get(i).setMsgText("YouTube Video");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.YAHOO_TYPE)){
                    arrayList_chat.get(i).setMsgText("Yahoo News");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.SKETCH_TYPE)){
                    arrayList_chat.get(i).setMsgText("Sketch");
                }else if(chat.msgType.equalsIgnoreCase(ConstantUtil.FIRST_TIME_STICKER_TYPE)){
                    arrayList_chat.get(i).setMsgText("Sticker");
                }*/



                arrayList_chat.get(i).setMsgDate(chat.msgDate);
                arrayList_chat.get(i).setMsgTime(chat.msgTime);
                arrayList_chat.get(i).setMsgTimeZone(chat.msgTimeZone);
                arrayList_chat.get(i).setMsgStatusName(chat.msgStatusName);


                isPresent=true;
                break;
            }
        }


        if(!isPresent){
            new Get_ALL_Chat_From_Local_DB().execute();

            ConstantUtil.fag_chat_listing=true;

            for(int i=0;i<arrayList_chat.size();i++){
                if(arrayList_chat.get(i).msgSenId.equalsIgnoreCase(msgSenId)){
                    int privetChatCount=Integer.valueOf(arrayList_chat.get(i).msgUnreadCount);
                    arrayList_chat.get(i).setMsgUnreadCount(String.valueOf(privetChatCount));
                    //isPresent=true;
                    break;
                }
            }
        }

       // adapterFragmentChat.notifyDataSetChanged();
        Collections.sort(arrayList_chat, new Comparator<ChatListData>() {
            public int compare(ChatListData o1, ChatListData o2) {
                return o2.getTimeDate().compareTo(o1.getTimeDate());
            }
        });

        if (adapterFragmentChat!=null) {
            adapterFragmentChat.refresh(arrayList_chat);
        }

    }





    ////////////////////////////////////////////////

    // Get_ALL_Chat_From_Local_DB..=========================================================================
    private class Get_ALL_Chat_From_Local_DB extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            arrayList_chat.clear();
            arrayList_chat1.clear();
            arrayList_chat2.clear();
            DatabaseHandler DB = new DatabaseHandler(getActivity());

            arrayList_chat = ChatModel.getChatList1(DB, session.getUserId());
            arrayList_chat1 = arrayList_chat;//ChatModel.getChatList1(DB, session.getUserId());
            arrayList_chat2 = ChatModel.getChatList2(DB, session.getUserId());


            if(arrayList_chat2.size()>0){

                for(int i=0; i<arrayList_chat2.size(); i++){

                    int msgId=arrayList_chat2.get(i).msgId;
                    String msgTokenId=arrayList_chat2.get(i).msgTokenId;

                    String msgSenId=arrayList_chat2.get(i).msgSenId;
                    String msgRecId=arrayList_chat2.get(i).msgRecId;

                    String msgType= arrayList_chat2.get(i).msgType;
                    String msgText=arrayList_chat2.get(i).msgText;


                    String translationStatus=arrayList_chat2.get(i).translationStatus;
                    String translationText=arrayList_chat2.get(i).translationText;

                    String msgDate=arrayList_chat2.get(i).msgDate;
                    String msgTime=arrayList_chat2.get(i).msgTime;
                    String msgTimeZone=arrayList_chat2.get(i).msgTimeZone;

                    String msgStatusId=arrayList_chat2.get(i).msgStatusId;
                    String msgStatusName=arrayList_chat2.get(i).msgStatusName;

                    String msgSenPhone=arrayList_chat2.get(i).msgSenPhone;
                    String msgRecPhone=arrayList_chat2.get(i).msgRecPhone;
                    String msgUnreadCount=arrayList_chat2.get(i).msgUnreadCount;

                    String userName="";
                    String userPhoto="";
                    String userFavoriteStatus="";
                    String userBlockStatus="";

                    ChatListData chat2 = new ChatListData(msgId,msgTokenId,msgSenId,msgRecId,msgType,msgText,translationStatus,translationText,msgDate,msgTime,msgTimeZone,msgStatusId,msgStatusName,msgSenPhone,msgRecPhone,msgUnreadCount,
                            userName,userPhoto,userFavoriteStatus,userBlockStatus);

                    if(arrayList_chat1.size()>0){
                        for(int j=0; j<arrayList_chat1.size(); j++){

                            if(arrayList_chat1.get(j).msgSenId.equalsIgnoreCase(msgRecId)){
                                String dateTime1 = arrayList_chat1.get(j).msgDate.toString()+" "+arrayList_chat1.get(j).msgTime.toString();
                                String dateTime2 = msgDate+" "+msgTime;
                                String pattern = "yyyy-MM-dd HH:mm:ss";
                                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                                Date one, two;
                                try {
                                    one = dateFormat.parse(dateTime1);
                                    two = dateFormat.parse(dateTime2);

                                    if(one.before(two)){
                                        System.out.println("--------------Date Before ----------------");
                                        arrayList_chat.get(j).setMsgType(msgType);
                                        arrayList_chat.get(j).setMsgText(msgText);

                                        arrayList_chat.get(j).setMsgStatusId(msgStatusId);
                                        arrayList_chat.get(j).setMsgStatusName(msgStatusName);

                                        arrayList_chat.get(j).setTranslationStatus(translationStatus);
                                        arrayList_chat.get(j).setTranslationText(translationText);


                                        arrayList_chat.get(j).setMsgDate(msgDate);
                                        arrayList_chat.get(j).setMsgTime(msgTime);
                                        arrayList_chat.get(j).setMsgTimeZone(msgTimeZone);
                                    }else if(one.after(two)){
                                        System.out.println("--------------Date After ----------------");
                                    }else{
                                        System.out.println("--------------Date Equal ----------------");
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                if(!check_arraylist_duplicate(msgSenId,msgRecId)){
                                    arrayList_chat.add(chat2);
                                }
                            }

                        }
                    }else{
                        arrayList_chat.add(chat2);
                    }

                }

            }


            // arrayList_chat = ChatModel.getChatList(DB, session.getUserId());
            //System.out.println("CHAT LIST :    "+arrayList_chat.size());
            for(int i=0;i<arrayList_chat.size();i++){

                if(arrayList_chat.get(i).msgSenId.equals(session.getUserId())){

                    ContactData contact = ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgRecId);

                    String userId=contact.getUsrId();
                    String userName=contact.getUsrUserName();
                    String userPhoto=contact.getUsrProfileImage();
                    String userFavoriteStatus=String.valueOf(contact.getUsrFavoriteStatus());
                    String userBlockStatus=String.valueOf(contact.getUsrBlockStatus());

                    arrayList_chat.get(i).setUserId(userId);
                    arrayList_chat.get(i).setUserName(userName);
                    arrayList_chat.get(i).setUserPhoto(userPhoto);
                    arrayList_chat.get(i).setUserFavoriteStatus(userFavoriteStatus);
                    arrayList_chat.get(i).setUserBlockStatus(userBlockStatus);

                    /*if(userName.equalsIgnoreCase("")){
                        arrayList_chat.get(i).setUserName("");
                        arrayList_chat.get(i).setUserPhoto("");
                        arrayList_chat.get(i).setUserFavoriteStatus("false");
                        arrayList_chat.get(i).setUserBlockStatus("false");
                    }else {
                        arrayList_chat.get(i).setUserName(userName);
                        arrayList_chat.get(i).setUserPhoto(userPhoto);
                        arrayList_chat.get(i).setUserFavoriteStatus(userFavoriteStatus);
                        arrayList_chat.get(i).setUserBlockStatus(userBlockStatus);
                    }*/

                }else{

                    ContactData contact = ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgSenId);

                    String userId = contact.getUsrId();
                    String userName=contact.getUsrUserName();
                    String userPhoto=contact.getUsrProfileImage();
                    String userFavoriteStatus=String.valueOf(contact.getUsrFavoriteStatus());
                    String userBlockStatus=String.valueOf(contact.getUsrBlockStatus());

                    /*if(userName.equalsIgnoreCase("")){
                        arrayList_chat.get(i).setUserName("");
                        arrayList_chat.get(i).setUserPhoto("");
                        arrayList_chat.get(i).setUserFavoriteStatus("false");
                        arrayList_chat.get(i).setUserBlockStatus("false");
                    }else {
                        arrayList_chat.get(i).setUserName(userName);
                        arrayList_chat.get(i).setUserPhoto(userPhoto);
                        arrayList_chat.get(i).setUserFavoriteStatus(userFavoriteStatus);
                        arrayList_chat.get(i).setUserBlockStatus(userBlockStatus);
                    }*/
                    arrayList_chat.get(i).setUserId(userId);
                    arrayList_chat.get(i).setUserName(userName);
                    arrayList_chat.get(i).setUserPhoto(userPhoto);
                    arrayList_chat.get(i).setUserFavoriteStatus(userFavoriteStatus);
                    arrayList_chat.get(i).setUserBlockStatus(userBlockStatus);

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String profile_status) {
            // adapterFragmentChat.notifyDataSetChanged();

            if (getActivity()!=null) {

                if(ConstantUtil.onlineStatusArrayList.size()>0){

                    for(int i=0;i<arrayList_chat.size();i++){
                        System.out.println("Online Status Function Loop");

                        if(ConstantUtil.onlineStatusArrayList.contains(arrayList_chat.get(i).userId)){
                            System.out.println("Online Status Function Loop IF");
                            arrayList_chat.get(i).setUserOnlineStatus("true");
                        }else{
                            System.out.println("Online Status Function Loop IF");
                            arrayList_chat.get(i).setUserOnlineStatus("false");
                        }
                    }

                }




                adapterFragmentChat = new ChatListAdapter(getActivity(), arrayList_chat);

                Collections.sort(arrayList_chat, new Comparator<ChatListData>() {
                    public int compare(ChatListData o1, ChatListData o2) {
                        return o2.getTimeDate().compareTo(o1.getTimeDate());
                    }
                });

                fragment_chat_swipeList.setAdapter(adapterFragmentChat);
                if(arrayList_chat.size()>0){
                    fragment_chat_tv_empty_text.setVisibility(View.GONE);
                }else {
                    fragment_chat_tv_empty_text.setVisibility(View.VISIBLE);
                }
            }

            ConstantUtil.fag_chat_listing=false;
            Boolean notification =getArguments().getBoolean("notification",false);
            if(notification){
                new get_selected_chat().execute("");
                ConstantUtil.fag_chat_listing=true;
            }


        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    // get selected chat from local db..=========================================================================
    private class get_selected_chat extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            DatabaseHandler DB = new DatabaseHandler(getActivity());
            String id = getArguments().getString("id");
            System.out.println("notification @@@@@===========>> TRUE");

            System.out.println("arrayList_chat get_selected_chat2 "+arrayList_chat.size());
            for(int i=0;i<arrayList_chat.size();i++){
                if(arrayList_chat.get(i).msgSenId.equalsIgnoreCase(id)){
                    System.out.println("arrayList_chat get_selected_chat3 "+arrayList_chat.size());
                    if(CommonMethods.isTimeAutomatic(getActivity())){

                        fragment_chat_swipeList.setChoiceMode(SwipeListView.CHOICE_MODE_SINGLE);
                        // fragment_group_lv.setItemChecked(i,true);
                       // fragment_chat_swipeList.setSelection(i);

                        //stop
                        System.out.println("arrayList_chat get_selected_chat4 "+arrayList_chat.size());
                        if(arrayList_chat.get(i).msgSenId.equals(session.getUserId())){
                            System.out.println("arrayList_chat get_selected_chat5 "+arrayList_chat.size());
                            ConstantUtil.msgRecId=arrayList_chat.get(i).msgRecId;
                            ConstantUtil.msgRecName=arrayList_chat.get(i).userName;
                            ConstantUtil.msgRecPhoto=arrayList_chat.get(i).userPhoto;
                            ConstantUtil.msgRecPhoneNo=arrayList_chat.get(i).msgRecPhone;
                            ConstantUtil.backActivityFromChatActivity="MainActivity";


                            ContactData contactData=ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgRecId);
                            ConstantUtil.msgRecGender=contactData.getUsrGender();
                            ConstantUtil.msgRecLanguageName=contactData.getUsrLanguageName();
                            ConstantUtil.msgRecBlockStatus=contactData.getUsrBlockStatus();
                            ConstantUtil.msgRecRelationshipStatus=contactData.getUserRelation();
                            ConstantUtil.msgRecKnownStatus=contactData.getUserKnownStatus();
                            ConstantUtil.msgNumberPrivatePermission=contactData.getUsrNumberPrivatePermission();
                            ConstantUtil.msgRecMyBlockStatus=contactData.getUsrMyBlockStatus();

                                /*if(ContactUserModel.isUserPresent(DB,arrayList_chat.get(i).msgRecId)){
                                    ContactData contactData=ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgRecId);
                                    ConstantUtil.msgRecGender=contactData.getUsrGender();
                                    ConstantUtil.msgRecLanguageName=contactData.getUsrLanguageName();
                                    ConstantUtil.msgRecBlockStatus=contactData.getUsrBlockStatus();
                                    ConstantUtil.msgRecRelationshipStatus=contactData.getUserRelation();
                                }else {
                                    ConstantUtil.msgRecGender="";
                                    ConstantUtil.msgRecLanguageName="";
                                    ConstantUtil.msgRecBlockStatus=false;
                                    ConstantUtil.msgRecRelationshipStatus=true;
                                }*/

                                /*if(ContactUserModel.isUserPresent(DB,arrayList_chat.get(i).msgRecId)){
                                    ConstantUtil.msgRecRelationshipStatus=ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgRecId).getUserRelation();
                                }else {
                                    ConstantUtil.msgRecRelationshipStatus=true;
                                }*/
                        }else{
                            System.out.println("arrayList_chat get_selected_chat4 "+arrayList_chat.size());
                            ConstantUtil.msgRecId=arrayList_chat.get(i).msgSenId;
                            ConstantUtil.msgRecName=arrayList_chat.get(i).userName;
                            ConstantUtil.msgRecPhoto=arrayList_chat.get(i).userPhoto;
                            ConstantUtil.msgRecPhoneNo=arrayList_chat.get(i).msgSenPhone;
                            ConstantUtil.backActivityFromChatActivity="MainActivity";

                                /*if(ContactUserModel.isUserPresent(DB,arrayList_chat.get(i).msgSenId)){
                                    ContactData contactData=ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgSenId);
                                    ConstantUtil.msgRecGender=contactData.getUsrGender();
                                    ConstantUtil.msgRecLanguageName=contactData.getUsrLanguageName();
                                    ConstantUtil.msgRecBlockStatus=contactData.getUsrBlockStatus();
                                    ConstantUtil.msgRecRelationshipStatus=contactData.getUserRelation();
                                }else {
                                    ConstantUtil.msgRecGender="";
                                    ConstantUtil.msgRecLanguageName="";
                                    ConstantUtil.msgRecBlockStatus=false;
                                    ConstantUtil.msgRecRelationshipStatus=true;
                                }*/

                            ContactData contactData=ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgSenId);
                            ConstantUtil.msgRecGender=contactData.getUsrGender();
                            ConstantUtil.msgRecLanguageName=contactData.getUsrLanguageName();
                            ConstantUtil.msgRecBlockStatus=contactData.getUsrBlockStatus();
                            ConstantUtil.msgRecRelationshipStatus=contactData.getUserRelation();
                            ConstantUtil.msgRecKnownStatus=contactData.getUserKnownStatus();
                            ConstantUtil.msgNumberPrivatePermission=contactData.getUsrNumberPrivatePermission();
                            ConstantUtil.msgRecMyBlockStatus=contactData.getUsrMyBlockStatus();

                                /*if(ContactUserModel.isUserPresent(DB,arrayList_chat.get(i).msgSenId)){
                                    ConstantUtil.msgRecRelationshipStatus=ContactUserModel.getUserData(DB,arrayList_chat.get(i).msgSenId).getUserRelation();
                                }else {
                                    ConstantUtil.msgRecRelationshipStatus=true;
                                }*/
                        }

                        new ActivityUtil(getActivity()).startChatActivity(false);

                    }else{
                        Toast.makeText(getActivity(), "Please set Automatic Date Time", Toast.LENGTH_LONG).show();
                    }

                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            ConstantUtil.fag_chat_listing=false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


    public void setOnlineStatus(){

        System.out.println("Online Status Function 1");

        for(int i=0;i<arrayList_chat.size();i++){
            System.out.println("Online Status Function Loop");

            if(ConstantUtil.onlineStatusArrayList.contains(arrayList_chat.get(i).userId)){
                System.out.println("Online Status Function Loop IF");
                arrayList_chat.get(i).setUserOnlineStatus("true");
            }else{
                System.out.println("Online Status Function Loop IF");
                arrayList_chat.get(i).setUserOnlineStatus("false");
            }
        }

        adapterFragmentChat.refresh(arrayList_chat);

    }

}
