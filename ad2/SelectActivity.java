package com.example.ad2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectActivity extends AppCompatActivity {

    private final String BASEURL = "https://br.pythonanywhere.com/" +
            "";
    private Activity act1 = this;
    private GridView gridView;
    private TextView titleView;
    private Button b1, b2;
    private Bitmap bm;
    private Context context;
    private ArrayList<MovieModel> moviesData;
    private gridAdapter adapter;
    private ArrayList<String> selected = new ArrayList<>();
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_select);

        titleView = findViewById(R.id.movie_select);
        titleView.setVisibility(View.GONE);
        b1 = findViewById(R.id.btn1);
        b2 = findViewById(R.id.btn2);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        moviesData = new ArrayList<>();
        adapter = new gridAdapter(act1, moviesData);
        gridView = findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        addMoviesData(retrofit);

        adapter.notifyDataSetChanged();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = adapter.getTitle();
                input = input.replaceAll(".$", "");
                titleView.setText(input);
                titleView.setGravity(Gravity.CENTER);
                titleView.setVisibility(View.VISIBLE);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecommend(retrofit);
            }

        });

    }

    private void getRecommend(Retrofit retrofit) {
        ArrayList<String> isbn = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        selected = adapter.getNumber();

        for(String s : selected){

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<List<Post>> call = jsonPlaceHolderApi.postSimilarity(s);

            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if(!response.isSuccessful()){
                        System.out.println("code: "+response.code());
                        return;
                    }
                    List<Post> postResponse = response.body();

                    for (Post p : postResponse){
                        i++;
                        isbn.add(p.getIsbn());
                        title.add(p.getTitle());

                        System.out.println(i);
                        if(i==selected.size()*5){
                            System.out.println(isbn);
                            Intent intent = new Intent(getBaseContext(), RecommendActivity.class);
                            intent.putExtra("isbnlist",isbn);
                            intent.putExtra("titlelist",title);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });

        }
    }


    private void addMoviesData(Retrofit retrofit) {
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<GetMovieItem>> call = jsonPlaceHolderApi.getMovieItem();
        call.enqueue(new Callback<List<GetMovieItem>>() {
            @Override
            public void onResponse(Call<List<GetMovieItem>> call, Response<List<GetMovieItem>> response) {
                if (!response.isSuccessful()) {
//                    textViewResult.setText("code: " + response.code());
                    return;
                }

                List<GetMovieItem> items = response.body();

                for (GetMovieItem item : items) {
                    String movieTitle = item.getTitle();
                    String movieNumber = item.getNumber();

                    Thread mThread = new Thread(){
                        @Override
                        public  void run(){
                            try{
                                URL url = new URL(BASEURL+"media/mimages/"+movieNumber+".jpg");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true);
                                conn.connect();

                                InputStream is = conn.getInputStream();
                                bm = BitmapFactory.decodeStream(is);

                            }catch (MalformedURLException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                        }
                    };

                    mThread.start();

                    try{
                        mThread.join();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
//                    mThread.interrupt();
                    adapter.addItem(bm, movieNumber, movieTitle, false);
                    System.out.println(adapter.getCount());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GetMovieItem>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}