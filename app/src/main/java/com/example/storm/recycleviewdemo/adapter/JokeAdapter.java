package com.example.storm.recycleviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.storm.recycleviewdemo.R;
import com.example.storm.recycleviewdemo.ViewHolder.ViewHolder;
import com.example.storm.recycleviewdemo.base.BaseAdapter;
import com.example.storm.recycleviewdemo.bean.JokeBean;
import com.example.storm.recycleviewdemo.utils.TimeUtils;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Storm on 2017/5/29.
 */

public class JokeAdapter extends BaseAdapter<JokeBean.ListBean> {


    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_GIF = 3;
    public static final int TYPE_AD = 4;

    private TimeUtils timeUtils;

    @Override
    protected int getViewType(int position, JokeBean.ListBean listBean) {
        String type = listBean.getType();
        int itemType = -1;
        if ("text".equals(type)) {
            itemType = TYPE_TEXT;
        } else if ("image".equals(type)) {
            itemType = TYPE_IMAGE;
        } else if ("video".equals(type)) {
            itemType = TYPE_VIDEO;
        } else if ("gif".equals(type)) {
            itemType = TYPE_GIF;
        } else if ("ad".equals(type)) {
            itemType = TYPE_AD;
        }


        return itemType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return ViewHolder.create(mContext, getItemLayoutId(viewType), parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    private int getItemLayoutId(int viewType) {
        int layoutId = -1;
        switch (viewType) {
            case TYPE_TEXT:
                layoutId = R.layout.all_text_item;
                break;
            case TYPE_IMAGE:
                layoutId = R.layout.all_image_item;
                break;
            case TYPE_VIDEO:
                layoutId = R.layout.all_video_item;
                break;
            case TYPE_GIF:
                layoutId = R.layout.all_gif_item;
                break;
            case TYPE_AD:
                layoutId = R.layout.all_ad_item;
                break;
        }
        return layoutId;
    }

    public JokeAdapter(Context context, boolean isOpenLoadMore) {
        super(context, isOpenLoadMore);

        timeUtils = new TimeUtils();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int itemViewType = holder.getItemViewType();
        if (isCommonItemView(itemViewType)) {
            bindData(holder, position, itemViewType);
        }

    }

    private void bindData(RecyclerView.ViewHolder holder, int position, int itemViewType) {

        ViewHolder viewHolder = (ViewHolder) holder;

        setViewData(viewHolder, position, itemViewType);
    }

    private void setViewData(ViewHolder viewHolder, int position, int itemViewType) {
        JokeBean.ListBean listBean = mDatas.get(position);
        if (itemViewType == TYPE_TEXT) {

        } else if (itemViewType == TYPE_IMAGE) {

            ImageView iv_image_photo = viewHolder.getView(R.id.iv_image_photo);

            Glide.with(mContext).load(listBean.getImage().getBig().get(0)).into(iv_image_photo);

        } else if (itemViewType == TYPE_VIDEO) {

            TextView tv_play_nums = viewHolder.getView(R.id.tv_play_nums);
            TextView tv_video_duration = viewHolder.getView(R.id.tv_video_duration);
            TextView tv_commant_context = viewHolder.getView(R.id.tv_commant_context);
            JCVideoPlayerStandard jcVideoPlayer = viewHolder.getView(R.id.jcv_videoplayer);

            jcVideoPlayer.setUp(listBean.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            Glide.with(mContext).load(listBean.getVideo().getThumbnail_small().get(0)).into(jcVideoPlayer.thumbImageView);

            tv_play_nums.setText(String.valueOf(listBean.getVideo().getPlayfcount()));
            tv_video_duration.setText(timeUtils.stringForTime(listBean.getVideo().getDuration() * 1000));


        } else if (itemViewType == TYPE_GIF) {
            ImageView iv_image_gif = viewHolder.getView(R.id.iv_image_gif);

            Glide.with(mContext).asGif().load(listBean.getGif().getDownload_url().get(0)).into(iv_image_gif);

        } else if (itemViewType == TYPE_AD) {
            TextView tv_context = viewHolder.getView(R.id.tv_context);
            tv_context.setText(listBean.getText());

        }

        //公用


        if (itemViewType != TYPE_AD) {

            ImageView iv_headpic = viewHolder.getView(R.id.iv_headpic);
            TextView tv_name = viewHolder.getView(R.id.tv_name);
            TextView tv_time_refresh = viewHolder.getView(R.id.tv_time_refresh);
            TextView tv_shenhe_ding_number = viewHolder.getView(R.id.tv_shenhe_ding_number);
            TextView tv_shenhe_cai_number = viewHolder.getView(R.id.tv_shenhe_cai_number);
            TextView tv_context = viewHolder.getView(R.id.tv_context);
            TextView tv_posts_number = viewHolder.getView(R.id.tv_posts_number);
            TextView tv_download_number = viewHolder.getView(R.id.tv_download_number);
            TextView tv_video_kind_text = viewHolder.getView(R.id.tv_video_kind_text);


            tv_name.setText(listBean.getU().getName());
            tv_time_refresh.setText(listBean.getPasstime());
            tv_shenhe_ding_number.setText(listBean.getUp());
            tv_shenhe_cai_number.setText(String.valueOf(listBean.getDown()));
            tv_context.setText(listBean.getText());

            List<JokeBean.ListBean.TagsBean> tags =
                    listBean.getTags();

            String tag = "";
            for (int i = 0; i < 3; i++) {
                tag = tag + tags.get(i).getName();
            }
            tv_video_kind_text.setText(tag);

            Glide.with(mContext).load(listBean.getU().getRoom_icon()).into(iv_headpic);
        }


    }


}
