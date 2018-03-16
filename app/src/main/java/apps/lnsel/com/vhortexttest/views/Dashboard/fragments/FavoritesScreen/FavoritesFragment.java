package apps.lnsel.com.vhortexttest.views.Dashboard.fragments.FavoritesScreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.adapters.AdapterFavoriteList;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.models.ContactUserModel;
import apps.lnsel.com.vhortexttest.utils.ActivityUtil;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.ConstantUtil;

/**
 * Created by apps2 on 7/8/2017.
 */
public class FavoritesFragment extends Fragment {

    ImageButton fragment_custom_header_iv_refresh,fragment_custom_header_iv_search,fragment_custom_header_iv_create_group;
    TextView fragment_custom_header_tv_title;
    private static View rootView;


    ListView fragment_favorite_lv;
    TextView fragment_favorite_tv_empty_text;
    ProgressBarCircularIndeterminate fragment_favorite_progressBarCircularIndetermininate;
    ArrayList<ContactData> arrayList=new ArrayList<>();
    AdapterFavoriteList adapterFragmentFavorite;
    DatabaseHandler DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        } catch (InflateException e) {

        }

        fragment_custom_header_tv_title=(TextView)rootView.findViewById(R.id.fragment_custom_header_tv_title);
        fragment_custom_header_tv_title.setText(getString(R.string.favorite));

        fragment_custom_header_iv_refresh=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_refresh);
        fragment_custom_header_iv_search=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_search);
        fragment_custom_header_iv_create_group=(ImageButton)rootView.findViewById(R.id.fragment_custom_header_iv_create_group);
        fragment_favorite_tv_empty_text=(TextView)rootView.findViewById(R.id.fragment_favorite_tv_empty_text);
        fragment_favorite_progressBarCircularIndetermininate=(ProgressBarCircularIndeterminate)rootView
                .findViewById(R.id.fragment_favorite_progressBarCircularIndetermininate);
        fragment_favorite_lv=(ListView)rootView.findViewById(R.id.fragment_favorite_lv);

        fragment_custom_header_iv_refresh.setVisibility(View.GONE);
        fragment_custom_header_iv_search.setVisibility(View.VISIBLE);
        fragment_custom_header_iv_create_group.setVisibility(View.GONE);
        fragment_custom_header_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startFindPeopleActivity(false);
            }
        });



        fragment_favorite_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConstantUtil.msgRecId=arrayList.get(i).getUsrId();
                ConstantUtil.msgRecName=arrayList.get(i).getUsrUserName();
                ConstantUtil.msgRecPhoto=arrayList.get(i).getUsrProfileImage();
                ConstantUtil.msgRecPhoneNo=arrayList.get(i).getUsrMobileNo();

                ConstantUtil.msgRecGender=arrayList.get(i).getUsrGender();
                ConstantUtil.msgRecLanguageName=arrayList.get(i).getUsrLanguageName();

                ConstantUtil.msgRecBlockStatus=arrayList.get(i).getUsrBlockStatus();

                ConstantUtil.msgRecRelationshipStatus=arrayList.get(i).getUserRelation();
                ConstantUtil.msgRecKnownStatus=arrayList.get(i).getUserKnownStatus();

                ConstantUtil.msgNumberPrivatePermission=arrayList.get(i).getUsrNumberPrivatePermission();
                ConstantUtil.msgRecMyBlockStatus=arrayList.get(i).getUsrMyBlockStatus();

                ConstantUtil.backActivityFromChatActivity="MainActivity";

                if(CommonMethods.isTimeAutomatic(getActivity())){
                    String currentTimeZone = TimeZone.getDefault().getDisplayName();
                    Calendar mCalendar = new GregorianCalendar();
                    TimeZone mTimeZone = mCalendar.getTimeZone();
                    int mGMTOffset = mTimeZone.getRawOffset();
                    System.out.printf("GMT offset is %s minutes", TimeUnit.MINUTES.convert(mGMTOffset, TimeUnit.MILLISECONDS));
                    new ActivityUtil(getActivity()).startChatActivity(false);
                }else{
                    Toast.makeText(getActivity(), "Please set Automatic Date Time", Toast.LENGTH_LONG).show();
                }
            }
        });

        DB=new DatabaseHandler(getActivity());
        new Get_Favorite_User_From_Local_DB().execute();



        return rootView;
    }








    //////////////////////////////////////////////////////


    // get favorite user list from local db..=========================================================================
    private class Get_Favorite_User_From_Local_DB extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            arrayList.clear();
            arrayList= ContactUserModel.getAllFavoriteContact(DB);
            return null;
        }

        @Override
        protected void onPostExecute(String profile_status) {
            fragment_favorite_progressBarCircularIndetermininate.setVisibility(View.GONE);
            if (getActivity()!=null) {
                adapterFragmentFavorite = new AdapterFavoriteList(getActivity(), arrayList);//
                fragment_favorite_lv.setAdapter(adapterFragmentFavorite);
            }
            ConstantUtil.fag_favorites_listing=false;
            if(arrayList.size()>0){
                fragment_favorite_tv_empty_text.setVisibility(View.GONE);
            }else {
                fragment_favorite_tv_empty_text.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onPreExecute() {
            fragment_favorite_progressBarCircularIndetermininate.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }


}