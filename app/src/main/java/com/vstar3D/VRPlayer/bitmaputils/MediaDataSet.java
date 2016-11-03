package com.vstar3D.VRPlayer.bitmaputils;

public abstract class MediaDataSet<V>
{
    int mIndex = 0;
    
    public void moveToFirst()
    {
        mIndex = 0;
    }
    
    public void moveToNext()
    {
        mIndex++;
    }
    
    @Override
    public String toString()
    {
        return super.toString() + "   getCount = " + getCount();
    }
    
    public abstract boolean hasNext();
    public abstract void addAll(MediaDataSet<V> dataSet);
    public abstract void add(V item);
    public abstract void add(int index, V item);
    public abstract V getItem();
    public abstract V getItemAt(int index);
    public abstract int getCount();
    public abstract void clear();
}
