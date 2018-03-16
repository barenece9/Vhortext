package apps.lnsel.com.vhortexttest.adapters;

/**
 * Created by db on 11/28/2017.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import java.util.List;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.config.DatabaseHandler;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatRoundedView;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ProgressBarCircularIndeterminate;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.BlockUserScreen.BlockUserActivity;

/**
 * Created by db on 2/23/2017.
 */
public class AdapterBlockUser extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    private List<ContactData> block_list = null;
    DatabaseHandler DB;
    ImageLoader imageLoader;


    public AdapterBlockUser(Context context, List<ContactData> block_list) {
        this.context = context;
        this.block_list = block_list;
        DB=new DatabaseHandler(context);
        imageLoader = AppController.getInstance().getImageLoader();

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return block_list.size();
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
        TextView inflater_block_user_item_tv_ProfileName,inflater_block_user_item_tv_Status;
        ImageView inflater_block_user_item_iv_block;
        ProgressBarCircularIndeterminate inflater_block_user_item_progressBar_iv_block;
        ChatRoundedView inflater_block_user_item_iv_ProfileImage;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.inflater_block_user_item, null);

        holder.inflater_block_user_item_iv_ProfileImage=(ChatRoundedView)rowView.findViewById(R.id.inflater_block_user_item_iv_ProfileImage);
        holder.inflater_block_user_item_tv_ProfileName = (TextView) rowView.findViewById(R.id.inflater_block_user_item_tv_ProfileName);
        holder.inflater_block_user_item_tv_Status=(TextView) rowView.findViewById(R.id.inflater_block_user_item_tv_Status);

        holder.inflater_block_user_item_progressBar_iv_block=(ProgressBarCircularIndeterminate)rowView.findViewById(R.id.inflater_block_user_item_progressBar_iv_block);
        holder.inflater_block_user_item_iv_block=(ImageView) rowView.findViewById(R.id.inflater_block_user_item_iv_block);

        /*if(block_list.get(position).getUserKnownStatus()) {
            holder.inflater_block_user_item_tv_ProfileName.setText(block_list.get(position).getUsrUserName());
            holder.inflater_block_user_item_tv_Status.setText(block_list.get(position).getUsrProfileStatus());
        }else {
            holder.inflater_block_user_item_tv_ProfileName.setText(block_list.get(position).getUsrMobileNo());
            holder.inflater_block_user_item_tv_Status.setText("");
        }*/
        holder.inflater_block_user_item_tv_ProfileName.setText(block_list.get(position).getUsrUserName());
        holder.inflater_block_user_item_tv_Status.setText(block_list.get(position).getUsrProfileStatus());

        holder.inflater_block_user_item_iv_block.setSelected(true);

        if(!block_list.get(position).getUsrProfileImage().equalsIgnoreCase("")) {
            final String image_url = UrlUtil.IMAGE_BASE_URL + block_list.get(position).getUsrProfileImage();
            Picasso.with(context)
                    .load(image_url)
                    //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.inflater_block_user_item_iv_ProfileImage);
        }


        holder.inflater_block_user_item_iv_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isBlock=false;
                if(!InternetConnectivity.isConnectedFast(context)){
                    ToastUtil.showAlertToast(context, context.getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }else {

                    if(context instanceof BlockUserActivity){
                        ((BlockUserActivity)context).BlockDialog(holder.inflater_block_user_item_iv_block,holder.inflater_block_user_item_progressBar_iv_block,
                                isBlock, block_list.get(position).getUsrId(), position);
                    }
                }

            }
        });


        return rowView;
    }


}