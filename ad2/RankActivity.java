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

public class RankActivity extends AppCompatActivity {
    private final String BASEURL = "https://br.pythonanywhere.com/" +
            "";
    private ListView listView;
    private Button btn1, btn2;
    private BreakIterator textViewResult = null;
    private ArrayList<RankModel> rankData;
    private ListAdapter adapter;
    private Bitmap bm;
    private int i=0;
    private ArrayList<String> isbnlist, titlelist;
    private ArrayList<String> ilist = new ArrayList<>();
    private ArrayList<String> tlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        listView = findViewById(R.id.listview);
        btn1 = findViewById(R.id.listbtn);
        btn2 = findViewById(R.id.mainbtn);
        rankData = new ArrayList<>();
        addRankData();

        adapter = new ListAdapter(rankData);
        listView.setAdapter(adapter);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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

    private void addRankData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<GetRankItem>> call = jsonPlaceHolderApi.getRankItem();

        call.enqueue(new Callback<List<GetRankItem>>() {
            @Override
            public void onResponse(Call<List<GetRankItem>> call, Response<List<GetRankItem>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("code: " + response.code());
                    return;
                }

                List<GetRankItem> items = response.body();

                for (GetRankItem item : items) {
                    String title = item.getTitle();
                    String author = item.getAuthor();
                    String isbn = item.getIsbn_id();
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
                    RankModel rankModel = new RankModel();
                    rankModel.setCover(bm);
                    rankModel.setTitle(title);
                    rankModel.setAuthor(author);
                    i += 1;
                    rankModel.setRank(i);
                    rankData.add(rankModel);
                    System.out.println(adapter.getCount());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<GetRankItem>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}