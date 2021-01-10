package com.example.ad2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter2 extends BaseAdapter {

    private ArrayList<RecommendModel> recommendData;
    private RecommendModel recommendModel;
    private Context c;

    public ListAdapter2(Context context, ArrayList<RecommendModel> recommendData){
        this.c = context;
        this.recommendData = recommendData;
    }
    @Override
    public int getCount() {
        return recommendData.size();
    }

    @Override
    public RecommendModel getItem(int position) {
        return this.recommendData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recommend_item, parent, false);
        }

        ImageView coverImageView = convertView.findViewById(R.id.imageView2);
        TextView titleTextView = convertView.findViewById(R.id.textView3);
        TextView authorTextView = convertView.findViewById(R.id.textView4);

        recommendModel = recommendData.get(position);
        coverImageView.setImageBitmap(recommendModel.getCover());
        titleTextView.setText(recommendModel.getTitle());
        authorTextView.setText(recommendModel.getAuthor());

//        LinearLayout info = (LinearLayout)convertView.findViewById(R.id.info);
//        info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return convertView;
    }
}
