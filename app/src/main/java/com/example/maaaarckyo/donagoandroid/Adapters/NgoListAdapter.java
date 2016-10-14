package com.example.maaaarckyo.donagoandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maaaarckyo.donagoandroid.Models.NGO;
import com.example.maaaarckyo.donagoandroid.R;

import java.util.List;

// Custom list adapter for the list of NGOs in order to implement a custom-made setup for each item in the list
public class NgoListAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<NGO> mNgo;

    public NgoListAdapter(Context context, List<NGO> ngos) {
        mInflater = LayoutInflater.from(context);
        mNgo = ngos;
    }

    @Override
    public int getCount()
    {
        return mNgo.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mNgo.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        ViewHolder holder;
        if(convertView == null)
        {
            view = mInflater.inflate(R.layout.ngo_row_layout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.image = (ImageView)view.findViewById(R.id.backgroundImage);
            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        NGO ngo = mNgo.get(position);

        holder.image.setImageBitmap(ngo.getImage());
        holder.name.setText(ngo.getName());

        return view;
    }

    private class ViewHolder
    {
        public TextView name;
        public ImageView image;
    }
}