package com.vstar3D.VRPlayer.bitmaputils;

import java.util.ArrayList;

public class ImageDataSet<V> extends MediaDataSet<V>
{
    ArrayList<V> mDataList = new ArrayList<V>();
    int mIndex = 0;
    
    @Override
    public boolean hasNext()
    {
        return (mIndex < getCount());
    }

    @Override
    public void moveToFirst()
    {
        mIndex = 0;
    }

    @Override
    public void moveToNext()
    {
        mIndex++;
    }

    @Override
    public V getItem()
    {
        return mDataList.get(mIndex);
    }

    @Override
    public V getItemAt(int index)
    {
        return mDataList.get(index);
    }
    
    public void addAll(MediaDataSet<V> dataSet)
    {
        for (dataSet.moveToFirst(); dataSet.hasNext(); dataSet.moveToNext())
        {
            add(dataSet.getItem());
        }
    }
    
    public void add(V item)
    {
        mDataList.add(item);
    }
    
    public void add(int index, V item)
    {
        mDataList.add(index, item);
    }
    
    @Override
    public int getCount()
    {
        return mDataList.size();
    }

    @Override
    public void clear()
    {
        mDataList.clear();
        moveToFirst();
    }
}
