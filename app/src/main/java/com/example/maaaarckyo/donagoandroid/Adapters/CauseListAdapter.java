package com.example.maaaarckyo.donagoandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maaaarckyo.donagoandroid.Models.Cause;
import com.example.maaaarckyo.donagoandroid.R;

import java.util.List;

// Custom list adapter for the list of causes in order to implement a custom-made setup for each item
public class CauseListAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<Cause> causeList;

    public CauseListAdapter(Context context, List<Cause> causes)
    {
        mInflater = LayoutInflater.from(context);
        causeList = causes;
    }

    @Override
    public int getCount()
    {
        return causeList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return causeList.get(position);
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
            view = mInflater.inflate(R.layout.cause_row_layout, parent, false);
            holder = new ViewHolder();
            holder.avatar = (ImageView)view.findViewById(R.id.twCauseAvatar);
            holder.name = (TextView)view.findViewById(R.id.twCauseName);
            holder.supporters = (TextView)view.findViewById(R.id.twSupporters);
            holder.donatedMoney = (TextView)view.findViewById(R.id.twDonated);
            view.setTag(holder);
        } else
        {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        Cause cause = causeList.get(position);
        holder.avatar.setImageBitmap(cause.getImage());
        holder.name.setText(cause.getName());
        holder.supporters.setText("Støttes lige nu af " + cause.getSupporters() + " personer");
        holder.donatedMoney.setText(cause.getDonatedAmount() + " DKK indsamlet til dato");


        return view;
    }

    private class ViewHolder
    {
        public ImageView avatar;
        public TextView name, supporters, donatedMoney;
    }
}
