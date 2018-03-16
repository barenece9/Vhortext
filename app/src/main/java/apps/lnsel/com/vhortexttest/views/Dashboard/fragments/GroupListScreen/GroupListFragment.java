package apps.lnsel.com.vhortexttest.views.Dashboard.fragments.GroupListScreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.GroupListAdapter;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.GroupChatData;
import apps.lnsel.com.vhortexttest.data.GroupData;
import apps.lnsel.com.vhortexttest.data.GroupListData;
import apps.lnsel.com.vhortexttest.data.GroupUserData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.GroupModel;
import apps.lnsel.com.vhortexttest.models.GroupUserModel;
import apps.lnsel.com.vhortexttest.presenters.GroupListPresenter;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.SharedManagerUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;


/**
 * Created by apps2 on 7/11/2017.
 */
public class GroupListFragment extends Fragment implements GroupListFragmentView{

    ImageButton fragment_custom_header_iv_refresh,fragment_custom_header_iv_search,fragment_custom_header_iv_create_group;
    TextView fragment_custom_header_tv_title,fragment_group_tv_empty_text;
    ListView fragment_group_lv;
    ProgressBarCircularIndeterminate fragment_group_progressBarCircular;
    private static View rootView;
    GroupListPresenter presenter;
    SharedManagerUtil session;
    DatabaseHandler DB;
    GroupListAdapter groupListAdapter;
    //ArrayList<GroupData> groupDataListFromLocal;
    ArrayList<GroupListData> groupDataListFromLocal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_group_list, container, false);
        } catch (InflateException e) {

        }

        presenter = new GroupListPresenter(this);
        session = new SharedManagerUtil(getActivity());
        DB = new DatabaseHandler(getActivity());

        fragment_group_progressBarCircular = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.fragment_group_progressBarCircularIndetermininate);
        fragment_group_lv = (ListView) rootView.findViewById(R.id.fragment_group_lv);
        fragment_custom_header_tv_title = (TextView) rootView.findViewById(R.id.fragment_custom_header_tv_title);
        fragment_custom_header_tv_title.setText(getString(R.string.groups));
        fragment_group_tv_empty_text = (TextView) rootView.findViewById(R.id.fragment_group_tv_empty_text);
        fragment_group_tv_empty_text.setVisibility(View.GONE);

        fragment_custom_header_iv_refresh = (ImageButton) rootView.findViewById(R.id.fragment_custom_header_iv_refresh);
        fragment_custom_header_iv_search = (ImageButton) rootView.findViewById(R.id.fragment_custom_header_iv_search);
        fragment_custom_header_iv_create_group = (ImageButton) rootView.findViewById(R.id.fragment_custom_header_iv_create_group);

        fragment_custom_header_iv_refresh.setVisibility(View.GONE);
        fragment_custom_header_iv_search.setVisibility(View.GONE);
        fragment_custom_header_iv_create_group.setVisibility(View.VISIBLE);

        fragment_custom_header_iv_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ActivityUtil(getActivity()).startCreateGroupActivity(false);
            }
        });

        fragment_group_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (CommonMethods.isTimeAutomatic(getActivity())) {
                    ConstantUtil.grpId = groupDataListFromLocal.get(i).getGrpId();
                    ConstantUtil.grpName = groupDataListFromLocal.get(i).getGrpName();
                    ConstantUtil.grpPrefix = groupDataListFromLocal.get(i).getGrpPrefix();
                    ConstantUtil.grpImage = groupDataListFromLocal.get(i).getGrpImage();
                    ConstantUtil.grpCreatorId = groupDataListFromLocal.get(i).getGrpCreatorId();
                    ConstantUtil.grpStatusId = groupDataListFromLocal.get(i).getGrpStatusId();
                    ConstantUtil.grpStatusName = groupDataListFromLocal.get(i).getGrpStatusName();
                    ConstantUtil.grpCreatedAt = groupDataListFromLocal.get(i).getGrpCreatedAt();

                    ConstantUtil.grpMembersName = GroupUserModel.getAllMemberName(DB, groupDataListFromLocal.get(i).getGrpId(), session.getUserId());

                    ConstantUtil.grpAdminName = GroupUserModel.getAdminName(DB, groupDataListFromLocal.get(i).getGrpId(), ConstantUtil.grpCreatorId, session.getUserId());

                    System.out.println(" ConstantUtil.grpAdminName @@@@@@@@@@@@@@===============>> " + ConstantUtil.grpAdminName);
                    new ActivityUtil(getActivity()).startGroupChatActivity(false);
                } else {
                    Toast.makeText(getActivity(), "Please set Automatic Date Time", Toast.LENGTH_LONG).show();
                }
            }
        });

        ConstantUtil.fag_group_listing = true;
        groupDataListFromLocal = GroupModel.getGroupChatList(DB, session.getUserId());

        Collections.sort(groupDataListFromLocal, new Comparator<GroupListData>() {
            public int compare(GroupListData o1, GroupListData o2) {
                return o2.getTimeDate().compareTo(o1.getTimeDate());
            }
        });

        if (getActivity() != null){
            groupListAdapter = new GroupListAdapter(getActivity(), groupDataListFromLocal);
            fragment_group_lv.setAdapter(groupListAdapter);
        }

        if(groupDataListFromLocal.size()==0){
            fragment_group_tv_empty_text.setVisibility(View.VISIBLE);
        }
        ConstantUtil.fag_group_listing=false;


        getGroupList();


        Boolean notification =getArguments().getBoolean("notification",false);

        if(notification){
            new get_selected_chat().execute("");
            ConstantUtil.fag_group_listing=true;
        }


        return rootView;
    }

    public void getGroupList(){

        if(InternetConnectivity.isConnectedFast(getActivity())){
            //fragment_group_progressBarCircular.setVisibility(View.VISIBLE);
            presenter.getGroupService(UrlUtil.GET_ALL_GROUP_LIST_URL
                    +"?userId="+session.getUserId()
                    +"&API_KEY="+UrlUtil.API_KEY
                    +"&usrAppType="+ConstantUtil.APP_TYPE
                    +"&usrAppVersion="+ConstantUtil.APP_VERSION
                    +"&usrDeviceId="+ConstantUtil.DEVICE_ID);
        }
    }


    public void errorInfo(String message){
        fragment_group_progressBarCircular.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(getActivity(), "Failed", ToastType.FAILURE_ALERT);
        if(groupDataListFromLocal.size()<=0){
            fragment_group_tv_empty_text.setVisibility(View.VISIBLE);
        }else {
            fragment_group_tv_empty_text.setVisibility(View.GONE);
        }
        //fragment_group_tv_empty_text.setVisibility(View.GONE);

    }
    public void successInfo(ArrayList<GroupData> groupDataList, ArrayList<GroupUserData> groupUserDataList){
        groupDataListFromLocal=GroupModel.getGroupChatList(DB, session.getUserId());

        Collections.sort(groupDataListFromLocal, new Comparator<GroupListData>() {
            public int compare(GroupListData o1, GroupListData o2) {
                return o2.getTimeDate().compareTo(o1.getTimeDate());
            }
        });

        if(groupDataListFromLocal.size()<=0){
            fragment_group_tv_empty_text.setVisibility(View.VISIBLE);
        }else {
            fragment_group_tv_empty_text.setVisibility(View.GONE);
        }
        if (groupListAdapter!=null) {
            groupListAdapter.refresh(groupDataListFromLocal);
        }

        fragment_group_progressBarCircular.setVisibility(View.GONE);
    }

    public void addGroup(GroupData groupData,JSONArray userdata){
        if(!GroupModel.grpIdPresent(DB,groupData.getGrpId())){
            try{
                for(int j=0;j<userdata.length();j++){
                    JSONObject object = userdata.getJSONObject(j);
                    String grpuMemId = object.getString("grpuMemId");
                    String grpuMemStatusId = object.getString("grpuMemStatusId");
                    if(grpuMemId.equalsIgnoreCase(session.getUserId()) && !grpuMemStatusId.equalsIgnoreCase("4")){
                        GroupModel.addGroup(DB,groupData);
                        break;
                    }
                }
            }catch (JSONException e1){
                e1.printStackTrace();
            }
            //GroupModel.addGroup(DB,groupData);
        }else {
            if(groupData.getGrpStatusId().equalsIgnoreCase("1")){
                GroupModel.UpdateGroupInfo(DB,groupData,groupData.getGrpId());
                System.out.println("if active group updated in grouplist fragment=========> "+groupData.getGrpId()+""+groupData.getGrpName());
            }else {
                System.out.println("de-active group updated in grouplist fragment=========> "+groupData.getGrpId()+""+groupData.getGrpName());
            }
        }

    }
    public void addGroupUser(GroupUserData groupUserData){
        if(!GroupUserModel.grpuIdPresent(DB,groupUserData.getGrpuId())){
            GroupUserModel.addGroupUser(DB,groupUserData);
        }else {
            GroupUserModel.UpdateGroupUserInfo(DB,groupUserData,groupUserData.getGrpuId());
            System.out.println(" group user updated in grouplist fragment=========> "+groupUserData.getGrpuId()+"  "+groupUserData.getGrpuMemStatusId());
        }
    }

    public void notActiveInfo(String statusCode,String status,String message){
        session.updateDeviceStatus(false);
        fragment_group_progressBarCircular.setVisibility(View.GONE);
        fragment_group_tv_empty_text.setVisibility(View.GONE);
        //ToastUtil.showAlertToast(ContactSyncActivity.this, message, ToastType.FAILURE_ALERT);
    }

    public  void refreshGroupListFag(){
        Log.e("GroupListFragment","Refresh GroupListFragment");
    }

    public  void setGroupListCountView(String grpcGroupId,GroupChatData groupChatData,Boolean isTranslation){
        Log.e("LOG", "group list fragment callled== ============  " );
        System.out.println("group list fragment callled==========================================");

        Boolean isPresent=false;
        for(int i=0;i<groupDataListFromLocal.size();i++){
            if(groupDataListFromLocal.get(i).grpId.equalsIgnoreCase(grpcGroupId)){

                if(!isTranslation){
                    int groupChatCount=Integer.valueOf(groupDataListFromLocal.get(i).grpChatCount)+1;
                    groupDataListFromLocal.get(i).setGrpChatCount(String.valueOf(groupChatCount));
                }else {
                    int groupChatCount=Integer.valueOf(groupDataListFromLocal.get(i).grpChatCount);
                    groupDataListFromLocal.get(i).setGrpChatCount(String.valueOf(groupChatCount));
                }


                //String msg=groupChatData.grpcText;


                groupDataListFromLocal.get(i).setGrpChatType(groupChatData.grpcType);
                groupDataListFromLocal.get(i).setGrpChatTranslatorStatus(groupChatData.grpcTranslationStatus);

                if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.MESSAGE_TYPE)){
                    //groupDataListFromLocal.get(i).setGrpChatText(groupChatData.grpcText);
                    if(groupChatData.grpcTranslationStatus.equalsIgnoreCase("true")){
                        groupDataListFromLocal.get(i).setGrpChatText(groupChatData.grpcText);//modified
                        if(! groupChatData.grpcTranslationText.equalsIgnoreCase("")){
                            groupDataListFromLocal.get(i).setGrpChatTranslatorText(groupChatData.grpcTranslationText);
                        }else {
                            groupDataListFromLocal.get(i).setGrpChatTranslatorText(groupChatData.grpcText);
                        }
                    }else {
                        groupDataListFromLocal.get(i).setGrpChatText(groupChatData.grpcText);
                    }
                }
                else if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_ADDED)){
                    groupDataListFromLocal.get(i).setGrpChatText(groupChatData.grpcText);
                }else if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_REMOVED)){
                    groupDataListFromLocal.get(i).setGrpChatText(groupChatData.grpcText);
                }else if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_NEW_ADDED)){
                    groupDataListFromLocal.get(i).setGrpChatText(groupChatData.grpcText);
                }else if(groupChatData.grpcType.equalsIgnoreCase(ConstantUtil.NOTIFICATION_TYPE_LEFT)){
                    groupDataListFromLocal.get(i).setGrpChatText(groupChatData.grpcText);
                }



                groupDataListFromLocal.get(i).setGrpChatDate(groupChatData.grpcDate);
                groupDataListFromLocal.get(i).setGrpChatTime(groupChatData.grpcTime);
                groupDataListFromLocal.get(i).setGrpChatTimeZone(groupChatData.grpcTimeZone);
                groupDataListFromLocal.get(i).setGrpChatStatusName(groupChatData.grpcStatusName);



                isPresent=true;
                break;
            }
        }

        if(!isPresent){
            groupDataListFromLocal=GroupModel.getGroupChatList(DB, session.getUserId());
            for(int i=0;i<groupDataListFromLocal.size();i++){
                if(groupDataListFromLocal.get(i).grpId.equalsIgnoreCase(grpcGroupId)){
                    int groupChatCount=Integer.valueOf(groupDataListFromLocal.get(i).grpChatCount);
                    groupDataListFromLocal.get(i).setGrpChatCount(String.valueOf(groupChatCount));
                    //isPresent=true;
                    break;
                }
            }

            if(groupDataListFromLocal.size()<=0){
                fragment_group_tv_empty_text.setVisibility(View.VISIBLE);
            }else {
                fragment_group_tv_empty_text.setVisibility(View.GONE);
            }
        }






        Collections.sort(groupDataListFromLocal, new Comparator<GroupListData>() {
            public int compare(GroupListData o1, GroupListData o2) {
                return o2.getTimeDate().compareTo(o1.getTimeDate());
            }
        });

        if (groupListAdapter!=null) {
            groupListAdapter.refresh(groupDataListFromLocal);
        }

    }


    // get selected chat from local db..=========================================================================
    private class get_selected_chat extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String id = getArguments().getString("id");
            System.out.println("notification @@@@@===========>> TRUE");

            for(int i=0;i<groupDataListFromLocal.size();i++){
                if(groupDataListFromLocal.get(i).getGrpId().equalsIgnoreCase(id)){

                    if(CommonMethods.isTimeAutomatic(getActivity())) {
                        System.out.println("setSelection @@@@@=============>> " + groupDataListFromLocal.get(i).getGrpName());
                        fragment_group_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        // fragment_group_lv.setItemChecked(i,true);
                       // fragment_group_lv.setSelection(i);

                        ConstantUtil.grpId = groupDataListFromLocal.get(i).getGrpId();
                        ConstantUtil.grpName = groupDataListFromLocal.get(i).getGrpName();
                        ConstantUtil.grpPrefix = groupDataListFromLocal.get(i).getGrpPrefix();
                        ConstantUtil.grpImage = groupDataListFromLocal.get(i).getGrpImage();
                        ConstantUtil.grpCreatorId = groupDataListFromLocal.get(i).getGrpCreatorId();
                        ConstantUtil.grpStatusId = groupDataListFromLocal.get(i).getGrpStatusId();
                        ConstantUtil.grpStatusName = groupDataListFromLocal.get(i).getGrpStatusName();
                        ConstantUtil.grpCreatedAt = groupDataListFromLocal.get(i).getGrpCreatedAt();

                        ConstantUtil.grpMembersName = GroupUserModel.getAllMemberName(DB, groupDataListFromLocal.get(i).getGrpId(), session.getUserId());

                        ConstantUtil.grpAdminName = GroupUserModel.getAdminName(DB, groupDataListFromLocal.get(i).getGrpId(), ConstantUtil.grpCreatorId, session.getUserId());

                        new ActivityUtil(getActivity()).startGroupChatActivity(false);
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
            ConstantUtil.fag_group_listing=false;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }
}
