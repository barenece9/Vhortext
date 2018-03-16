package apps.lnsel.com.vhortexttest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;


import java.util.ArrayList;
import java.util.TreeSet;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.YahooNews;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;


public class AdapterYahooNewsList extends RecyclerView.Adapter<AdapterYahooNewsList.VH> {
    private final DisplayImageOptions options;
    private TreeSet<String> expandedItemSet = new TreeSet<String>();
    private Context mContext;
    private ArrayList<YahooNews> dataYahooNewses;
    private OnItemClickListener clickListener;

    public AdapterYahooNewsList(Context mContext) {
        this.mContext = mContext;
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_yahoo_big_icon)
                .showImageOnFail(R.drawable.ic_yahoo_big_icon)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.ic_yahoo_big_icon)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .considerExifParams(true).build();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH((LayoutInflater.from(parent.getContext())).inflate(R.layout.inflater_yahoo_news_list, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {

        final YahooNews yahooNews = dataYahooNewses.get(position);

        holder.itemView.setTag(position);

        holder.tv_yahoo_news_title.setText(CommonMethods.getUTFDecodedString(yahooNews.getTitle()));
        holder.tv_yahoo_expanded_news_title.setText(CommonMethods.getUTFDecodedString(yahooNews.getTitle()));
        String description = CommonMethods.getUTFDecodedString(yahooNews.getDescription());
        holder.tv_desc.setText(Html.fromHtml(description));
        holder.tv_yahoo_expanded_desc.setText(Html.fromHtml(description));
       // holder.tv_desc.setText(TextUtils.isEmpty(description) ? mContext.getResources().getString(R.string.no_description) : description);
       // holder.tv_yahoo_expanded_desc.setText(TextUtils.isEmpty(description) ? mContext.getResources().getString(R.string.no_description) : description);
       // holder.tv_desc.setText(TextUtils.isEmpty(Html.fromHtml(description)) ? mContext.getResources().getString(R.string.no_description) : Html.fromHtml(description));
       // holder.tv_yahoo_expanded_desc.setText(TextUtils.isEmpty(Html.fromHtml(description)) ? mContext.getResources().getString(R.string.no_description) : Html.fromHtml(description));
        String time = CommonMethods.getFormattedDate(yahooNews.getPubDate(), "ccc, dd MMM yyyy HH:mm:ss Z", "ccc, dd MMM yyyy HH:mm");
        holder.tv_pubdate.setText(time);
        holder.tv_yahoo_expanded_pubdate.setText(time);

//        holder.iv_yahoo_thumb.setImageDrawable(null);
//        holder.iv_yahoo_thumb.setImageResource(0);
//        holder.iv_yahoo_expanded.setImageResource(0);
//        holder.iv_yahoo_expanded.setImageDrawable(null);
        ImageAware imageAwareYahooThumb = new ImageViewAware(holder.iv_yahoo_thumb, false);
//        ImageAware imageAwareYahooExpanded = new ImageViewAware(holder.iv_yahoo_expanded, false);
        ImageLoader.getInstance().displayImage(yahooNews.getUrl(), imageAwareYahooThumb, options);
//        ImageLoader.getInstance().displayImage(yahooNews.getUrl(), imageAwareYahooExpanded, options);


        if (expandedItemSet.contains(yahooNews.getLink())) {
//            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.yahoo_news_bg));
            holder.yahoo_news_short_view.setVisibility(View.GONE);
            holder.yahoo_news_expanded_view.setVisibility(View.VISIBLE);
        } else {
//            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.yahoo_news_bg_dark));
            holder.yahoo_news_short_view.setVisibility(View.VISIBLE);
            holder.yahoo_news_expanded_view.setVisibility(View.GONE);
        }

        holder.yahoo_share_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(yahooNews,position);
            }
        });

        holder.yahoo_expanded_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(yahooNews,position);
            }
        });

        holder.iv_yahoo_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedItemSet.contains(yahooNews.getLink())) {
                    expandedItemSet.remove(yahooNews.getLink());
                } else {
                    expandedItemSet.add(yahooNews.getLink());
                }
                notifyDataSetChanged();
            }
        });
        holder.iv_yahoo_expanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedItemSet.contains(yahooNews.getLink())) {
                    expandedItemSet.remove(yahooNews.getLink());
                } else {
                    expandedItemSet.add(yahooNews.getLink());
                }
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnectivity.isConnectedFast(mContext)) {
                    try {
                        Intent yahooClient = new Intent(Intent.ACTION_VIEW);
                        yahooClient.setData(Uri.parse(yahooNews.getLink()));
                        ((Activity) mContext).startActivityForResult(yahooClient, 1234);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_yahoo_not_found), ToastType.FAILURE_ALERT);
                    }
                }else {
                    ToastUtil.showAlertToast(mContext, mContext.getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return dataYahooNewses == null ? 0 : dataYahooNewses.size();
    }

    public void refreshList(ArrayList<YahooNews> dataYahooNewses) {
        this.dataYahooNewses = dataYahooNewses;
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(YahooNews itemData, int itemPosition);
    }

    public static class VH extends RecyclerView.ViewHolder {
        private ImageView iv_yahoo_thumb;
        private TextView tv_yahoo_news_title;
        private TextView tv_desc;
        private TextView tv_pubdate;
        private RelativeLayout yahoo_news_short_view;
        private RelativeLayout yahoo_news_expanded_view;
        private RelativeLayout yahoo_share_small;

        private ImageView iv_yahoo_expanded;
        private TextView tv_yahoo_expanded_news_title;
        private TextView tv_yahoo_expanded_pubdate;
        private RelativeLayout yahoo_expanded_share;
        private TextView tv_yahoo_expanded_desc;

        public VH(View itemView) {
            super(itemView);
            yahoo_news_short_view = (RelativeLayout) itemView.findViewById(R.id.yahoo_news_short_view);

            iv_yahoo_thumb = (ImageView) itemView.findViewById(R.id.iv_yahoo_thumb);
            tv_yahoo_news_title = (TextView) itemView.findViewById(R.id.tv_yahoo_news_title);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_pubdate = (TextView) itemView.findViewById(R.id.tv_pubdate);
            yahoo_share_small = (RelativeLayout) itemView.findViewById(R.id.yahoo_share_small);

            yahoo_news_expanded_view = (RelativeLayout) itemView.findViewById(R.id.yahoo_news_expanded_view);
            iv_yahoo_expanded = (ImageView) itemView.findViewById(R.id.iv_yahoo_expanded);
            tv_yahoo_expanded_news_title = (TextView) itemView.findViewById(R.id.tv_yahoo_expanded_news_title);
            tv_yahoo_expanded_pubdate = (TextView) itemView.findViewById(R.id.tv_yahoo_expanded_pubdate);
            yahoo_expanded_share = (RelativeLayout) itemView.findViewById(R.id.yahoo_expanded_share);
            tv_yahoo_expanded_desc = (TextView) itemView.findViewById(R.id.tv_yahoo_expanded_desc);
        }
    }
}
