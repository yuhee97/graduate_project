package com.example.ad2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private ArrayList<RankModel> rankData;
    private RankModel rankModel;
    int i=0;

    public ListAdapter(ArrayList<RankModel> rankData) {
        this.rankData = rankData;
    }

    @Override
    public int getCount() {
        return rankData.size() ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return rankData.get(position) ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rank_item, parent, false);
        }

        TextView rankTextView = convertView.findViewById(R.id.textrank);
        ImageView coverImageView = convertView.findViewById(R.id.imageView1);
        TextView titleTextView = convertView.findViewById(R.id.textView1);
        TextView authorTextView = convertView.findViewById(R.id.textView2);

        rankModel = rankData.get(position);
        i = i+1;
        rankTextView.setText(rankModel.getRank()+"");
        coverImageView.setImageBitmap(rankModel.getCover());
        titleTextView.setText(rankModel.getTitle());
        authorTextView.setText(rankModel.getAuthor());

        return convertView;
    }


}
