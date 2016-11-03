package com.vstar3D.VRPlayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vstar3D.VRPlayer.R;
import com.vstar3D.VRPlayer.VPlayerActivity;
import com.vstar3D.VRPlayer.model.Videos;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class VideoFragmentAdapter extends BaseAdapter {
    private Context context;
    private List<Videos> videos;

    public VideoFragmentAdapter(Context context, List<Videos> videos) {
        this.context = context;
        this.videos = videos;
    }
    @Override
    public int getCount() {
        return   ( null != videos) ? videos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.videofragment_adapter_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageview);
            viewHolder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativelayout);
            viewHolder.mImageView.setImageResource(R.mipmap.movie);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Videos video = videos.get(position);
        if (video!=null) {
            viewHolder.mTextView.setText(video.getvName());
            viewHolder.mImageView.setImageBitmap(video.getBitmap());
            viewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VPlayerActivity.class);
                    intent.putExtra("fpath", video.getvPath());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }
    public  void  addView(Videos video){
        videos.add(video);
    }
    class ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        RelativeLayout mRelativeLayout;
    }
}
