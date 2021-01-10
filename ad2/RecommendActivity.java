package com.example.ad2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendActivity extends AppCompatActivity {
    private ArrayList<String> isbnlist, titlelist;
    private final String BASEURL = "https://br.pythonanywhere.com/" +
            "";
    private ListView listView;
    private Button btn1, btn2;
    private BreakIterator textViewResult = null;
    private ArrayList<RecommendModel> recommendData = new ArrayList<>();
    private ListAdapter2 adapter;
    private Bitmap bm;
    private ArrayList<String> ilist = new ArrayList<>();
    private ArrayList<String> tlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        isbnlist = getIntent().getStringArrayListExtra("isbnlist");
        titlelist = getIntent().getStringArrayListExtra("titlelist");
        listView = findViewById(R.id.listview2);
        btn1 = findViewById(R.id.rankbtn);
        btn2 = findViewById(R.id.mainbtn2);
        addRecommendData();

        adapter = new ListAdapter2(this, recommendData);
        listView.setAdapter(adapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RankActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getBaseContext(), InfoActivity.class);
                String isbn = ilist.get(position);
                String title = tlist.get(position);
                intent.putExtra("isbn",isbn);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });

    }

    private void addRecommendData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<GetRecommendItem>> call = jsonPlaceHolderApi.getRecommendItem(isbnlist);

        call.enqueue(new Callback<List<GetRecommendItem>>() {
            @Override
            public void onResponse(Call<List<GetRecommendItem>> call, Response<List<GetRecommendItem>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("code: " + response.code());
                    return;
                }

                List<GetRecommendItem> items = response.body();

                for (GetRecommendItem item : items) {

                    String title = item.getTitle();
                    String author = item.getAuthor();
                    String isbn = item.getIsbn();
                    ilist.add(isbn);
                    tlist.add(title);

                    Thread mThread = new Thread(){
                        @Override
                        public  void run(){
                            try{
                                URL url = new URL(BASEURL+"media/images/"+isbn+".jpg");
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

                    RecommendModel recommendModel = new RecommendModel();
                    recommendModel.setCover(bm);
                    recommendModel.setTitle(title);
                    recommendModel.setAuthor(author);
                    recommendData.add(recommendModel);
                    System.out.println(adapter.getCount());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<GetRecommendItem>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}