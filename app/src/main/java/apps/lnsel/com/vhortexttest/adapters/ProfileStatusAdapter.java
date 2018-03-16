package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ProfileStatusData;

/**
 * Created by apps2 on 7/22/2017.
 */
public class ProfileStatusAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater=null;
    private List<ProfileStatusData> status_list = null;



    public ProfileStatusAdapter(Context context, List<ProfileStatusData> status_list) {
        this.context = context;
        this.status_list = status_list;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return status_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView inflater_profile_status_tv_status;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.inflater_profile_status, null);
        holder.inflater_profile_status_tv_status = (TextView) rowView.findViewById(R.id.inflater_profile_status_tv_status);
        if(status_list.get(position).getStatusSelected()){
            holder.inflater_profile_status_tv_status.setTextColor(context.getResources().getColor(R.color.app_Brown_StatusBar));
        }
        holder.inflater_profile_status_tv_status.setText(status_list.get(position).getStatusName());

        return rowView;
    }
}
