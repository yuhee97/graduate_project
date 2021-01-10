package com.example.ad2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.collection.ArraySet;

import java.util.ArrayList;

import static com.example.ad2.R.*;

public class gridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MovieModel> moviesData;
    private MovieModel movieModel;
    LayoutInflater inflater;
    private ArrayList<String> title;
    private ArrayList<String> number;
    Activity my;

    public gridAdapter(Activity act, ArrayList<MovieModel> moviesData){
        this.my = act;
        this.moviesData = moviesData;
        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        title = new ArrayList<>();
        number = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return moviesData.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getTitle() {
        String s = "";

        for(String t : title){
            s += t ;
        }
        return s;
    }

    public ArrayList<String> getNumber() {
        return number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(layout.list_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(id.movie_cover);
        TextView textView = convertView.findViewById(id.movie_title);
        TextView numView = convertView.findViewById(id.movie_number);
        CheckBox checkView = convertView.findViewById(id.itemCheckBox);

        movieModel = moviesData.get(position);

        imageView.setImageBitmap(movieModel.getCover());
        textView.setText(movieModel.getTitle());
        numView.setText(movieModel.getNumber());
        numView.setVisibility(View.GONE);

        checkView.setChecked(movieModel.isIschecked());
        imageView.setTag(position);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                boolean b = moviesData.get(currentPos).isIschecked();

                if (b){
                    moviesData.get(currentPos).setIschecked(false);
                    notifyDataSetChanged();
                    title.remove(textView.getText() +",");
                    number.remove(numView.getText());
                }
                else {
                    moviesData.get(currentPos).setIschecked(true);
                    notifyDataSetChanged();
                    title.add(textView.getText() +",");
                    number.add((String) numView.getText());
                }
                if(title.size()>3){
                    AlertDialog.Builder alert = new AlertDialog.Builder(my);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int i) {
                            d.dismiss();
                            moviesData.get(currentPos).setIschecked(false);
                            notifyDataSetChanged();
                            title.remove(textView.getText() +",");
                            number.remove(numView.getText());
                        }
                    });
                    alert.setMessage("3개까지 선택 가능합니다.");
                    alert.show();
                }
            }
        });


        return convertView;
    }

    public void addItem(Bitmap cover, String number, String title, boolean ischecked){
        MovieModel movieModel = new MovieModel();
        movieModel.setCover(cover);
        movieModel.setNumber(number);
        movieModel.setTitle(title);
        movieModel.setIschecked(ischecked);
        moviesData.add(movieModel);
    }
}