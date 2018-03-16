package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ContactData;
import apps.lnsel.com.vhortexttest.helpers.CustomViews.ChatRoundedView;
import apps.lnsel.com.vhortexttest.helpers.VolleyLibrary.AppController;
import apps.lnsel.com.vhortexttest.utils.UrlUtil;

/**
 * Created by db on 2/23/2017.
 */
public class AdapterFavoriteList extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;
    private List<ContactData> favorite_list = null;

    ImageLoader imageLoader ;


    public AdapterFavoriteList(Context context, List<ContactData> favorite_list) {
        this.context = context;
        this.favorite_list = favorite_list;
        imageLoader = AppController.getInstance().getImageLoader();
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return favorite_list.size();
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
        TextView inflater_fragment_favourite_item_tv_ProfileName,inflater_fragment_favourite_item_tv_Status;
        ChatRoundedView inflater_fragment_favourite_item_iv_ProfileImage;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.inflater_fragment_favourite_items, null);
        holder.inflater_fragment_favourite_item_tv_ProfileName = (TextView) rowView.findViewById(R.id.inflater_fragment_favourite_item_tv_ProfileName);
        holder.inflater_fragment_favourite_item_tv_Status=(TextView) rowView.findViewById(R.id.inflater_fragment_favourite_item_tv_Status);
        holder.inflater_fragment_favourite_item_iv_ProfileImage=(ChatRoundedView)rowView.findViewById(R.id.inflater_fragment_favourite_item_iv_ProfileImage);


        holder.inflater_fragment_favourite_item_tv_ProfileName.setText(favorite_list.get(position).getUsrUserName());
        holder.inflater_fragment_favourite_item_tv_Status.setText(favorite_list.get(position).getUsrProfileStatus());



        if(!favorite_list.get(position).getUsrProfileImage().equalsIgnoreCase("")) {
            final String image_url = UrlUtil.IMAGE_BASE_URL + favorite_list.get(position).getUsrProfileImage();
            Picasso.with(context)
                    .load(image_url)
                    //.networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.inflater_fragment_favourite_item_iv_ProfileImage);
        }


        return rowView;
    }


}