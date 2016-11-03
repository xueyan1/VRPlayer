/*
 * System: FryingPan
 * @version     1.00
 * 
 * Copyright (C) 2011, TOSHIBA Corporation.
 * 
 */

package com.vstar3D.VRPlayer.bitmaputils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.vstar3D.VRPlayer.cache.ImageCache;
import com.vstar3D.VRPlayer.cache.ImageResizer;

public class TosImageViewer extends GridView
{
    private static final int COMPLETE_GET_IMAGES        = 0x01;
    private static final int COMPLETE_GET_THUMBNAIL     = 0x02;
    private static final int THUMB_WIDTH                = 300;
    private static final int THUMB_HEIGHT               = 330;

    private ImageInfoHandler        m_imageHandler      = new ImageInfoHandler();
    private ImageSearchUtil         m_imageSearcher         = null;
    private IImageViewerListener    m_imageViewerListener   = null;
    private MediaDataSet<MediaInfo> m_mediaDataSet          = null;
    
    private ImageResizer m_imageWorker           = null;
    
    public TosImageViewer(Context context)
    {
        super(context);
        
        initialize(context);
    }

    public TosImageViewer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        initialize(context);
    }
    
    public TosImageViewer(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        
        initialize(context);
    }
    
    public void setImageViewerListener(IImageViewerListener listener)
    {
        m_imageViewerListener = listener;
    }
    
    public void updateImageViewer()
    {
        // Start a thread to find images from SD Card.
        new Thread(null, new Runnable()
        {
            @Override
            public void run()
            {
                // Add images in SDCard to list.
                m_mediaDataSet = m_imageSearcher.getImagesFromSDCard(false);
                m_imageHandler.obtainMessage(COMPLETE_GET_IMAGES).sendToTarget();
            }
            
        }, "GetImageFromSDCardThread").start();
    }
    
    public void updateGridview()
    {
        if (null != this.getAdapter())
        {
            ImageGridViewAdapter imageAdapter = (ImageGridViewAdapter)this.getAdapter();
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void initialize(Context context)
    {
        m_imageSearcher = new ImageSearchUtil(context);
        m_imageSearcher.setThumbnailSize(240, 240);
        
        this.setHorizontalSpacing(0);
        this.setVerticalSpacing(0);
        this.setNumColumns(GridView.AUTO_FIT);
        this.setColumnWidth(THUMB_WIDTH);
        this.setSelector(new ColorDrawable(Color.TRANSPARENT));
        this.setBackgroundColor(Color.WHITE);
        this.setGravity(Gravity.CENTER);
        this.setHorizontalSpacing(20);
        this.setVerticalSpacing(20);
        
        m_imageWorker = new ImageResizer(context, 240);
        //m_imageWorker.setLoadingImage(android.R.drawable.picture_frame);
        m_imageWorker.setImageSize(240, 240);
        m_imageWorker.setImageSearcher(m_imageSearcher);
        m_imageWorker.setImageFadeIn(true);
        
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(context, "Image_cache_param");
        ImageCache cache = new ImageCache(cacheParams);
        m_imageWorker.setImageCache(cache);
    }
    
    private void updateThumbnail()
    {
        int nSize = (null != m_mediaDataSet) ? m_mediaDataSet.getCount() : 0;
        
        if (null != m_imageViewerListener)
        {
            m_imageViewerListener.onSearchCompleted(nSize);
        }
        
        if (nSize > 0)
        {
            this.setAdapter(new ImageGridViewAdapter(this.getContext()));
        }
    }

    protected class ImageInfoHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            case COMPLETE_GET_IMAGES:
                updateThumbnail();
                break;
                
            case COMPLETE_GET_THUMBNAIL:
                updateGridview();
                break;
            }
        }
    }

    protected class ImageGridViewAdapter extends BaseAdapter
    {
        private Context m_context = null;
        
        public ImageGridViewAdapter(Context context)
        {
            m_context = context;
        }
        
        @Override
        public int getCount()
        {
            return (null != m_mediaDataSet) ? m_mediaDataSet.getCount() : 0;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (null == convertView)
            {
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                        THUMB_WIDTH,
                        THUMB_HEIGHT);
                convertView = new ImageView(m_context);
                convertView.setLayoutParams(params);
                convertView.setBackgroundResource(android.R.drawable.picture_frame);
                ((ImageView)convertView).setScaleType(ScaleType.FIT_XY);
            }
            
            MediaInfo mediaInfo = m_mediaDataSet.getItemAt(position);
            
            ImageView imageView = (ImageView)convertView;
            
            m_imageWorker.loadImage(mediaInfo, imageView);
            
            return convertView;
        }
    }
}
