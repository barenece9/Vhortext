package apps.lnsel.com.vhortexttest.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


import java.util.ArrayList;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.YouTubeVideo;
import apps.lnsel.com.vhortexttest.utils.CommonMethods;
import apps.lnsel.com.vhortexttest.utils.InternetConnectivity;
import apps.lnsel.com.vhortexttest.utils.ToastType;
import apps.lnsel.com.vhortexttest.utils.ToastUtil;
import apps.lnsel.com.vhortexttest.views.Dashboard.activities.ChatScreen.Attachment.ActivityYoutubePlayer;


public class AdapterYouTubeVideoList extends RecyclerView.Adapter<AdapterYouTubeVideoList.VH> {


    public interface OnItemClickListener {
        void onClick(YouTubeVideo itemData, int itemPosition);
    }

    private final DisplayImageOptions options;
    private Context mContext;
    private ArrayList<YouTubeVideo> mVideoList;

    private OnItemClickListener clickListener;

    public AdapterYouTubeVideoList(Context mContext) {
        this.mContext = mContext;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.black_bg)
                .showImageOnFail(R.drawable.black_bg)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                /*.bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)*/
                .showImageOnLoading(R.drawable.black_bg)
                .considerExifParams(true).build();
    }

    public AdapterYouTubeVideoList(Context mContext, ArrayList<YouTubeVideo> mVideoList) {
        this.mContext = mContext;
        options = new DisplayImageOptions.Builder()
                /*.showImageForEmptyUri(R.drawable.ic_chats_noimage_profile)
                .showImageOnFail(R.drawable.ic_chats_noimage_profile)*/
                .cacheOnDisk(true)
                /*.showImageOnLoading(R.drawable.ic_chats_noimage_profile)*/
                .considerExifParams(true).build();
        this.mVideoList = mVideoList;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH((LayoutInflater.from(parent.getContext())).inflate(R.layout.inflater_youtube_video_list, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {

        final YouTubeVideo youTubeVideo = mVideoList.get(position);

        holder.itemView.setTag(position);
        holder.tv_vid_title.setText(CommonMethods.getUTFDecodedString(youTubeVideo.getTitle()));
        holder.tv_vid_title.setSelected(true);
        holder.tv_desc.setText(CommonMethods.getUTFDecodedString(youTubeVideo.getDescription()));
        holder.tv_time.setText(CommonMethods.getFormattedDate(youTubeVideo.getPublishTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "MMMM d, yyyy"));
        holder.iv_vid_thumb.setImageResource(0);
//        ImageAware imageAware = new ImageViewAware(holder.iv_vid_thumb, false);
//        ImageLoader.getInstance().displayImage(youTubeVideo.getThumbLinkMedium(), imageAware, options);
        ImageLoader.getInstance().displayImage(youTubeVideo.getThumbLinkMedium(), holder.iv_vid_thumb,
                options);

        holder.youtube_share_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickListener != null) {
                    clickListener.onClick(youTubeVideo, position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Intent videoClient = new Intent(Intent.ACTION_VIEW);
////                    http://www.youtube.com/watch?v=
//                    videoClient.setData(Uri.parse("http://m.youtube.com/watch?v="
//                            + youTubeVideo.getVideoId()));
//                    ((Activity) mContext).startActivityForResult(videoClient, 1234);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ToastUtils.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_player_not_found), ToastType.FAILURE_ALERT);
//                }
                if(InternetConnectivity.isConnectedFast(mContext)) {

                    try {
                        Intent intent = new Intent(mContext, ActivityYoutubePlayer.class);
                        intent.putExtra("youtube_video_id", youTubeVideo.getVideoId());
                        intent.putExtra("youtube_video_title", youTubeVideo.getTitle());
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showAlertToast(mContext, mContext.getString(R.string.alert_failure_video_player_not_found), ToastType.FAILURE_ALERT);
                    }
                }else {
                    ToastUtil.showAlertToast(mContext, mContext.getResources().getString(R.string.no_internet), ToastType.FAILURE_ALERT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return mVideoList == null ? 0 : mVideoList.size();
    }

    public void refreshList(ArrayList<YouTubeVideo> videoList) {

        this.mVideoList = videoList;
        this.notifyDataSetChanged();
    }

    public static class VH extends RecyclerView.ViewHolder {
        private TextView tv_vid_title, tv_desc, tv_time;
        private ImageView iv_vid_thumb, youtube_share_ic;

        public VH(View itemView) {
            super(itemView);
            tv_vid_title = (TextView) itemView.findViewById(R.id.tv_vid_title);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_vid_thumb = (ImageView) itemView.findViewById(R.id.iv_vid_thumb);
            youtube_share_ic = (ImageView) itemView.findViewById(R.id.youtube_share_ic);
            iv_vid_thumb.setImageResource(0);

        }
    }
}
