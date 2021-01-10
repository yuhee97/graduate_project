package com.example.ad2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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

public class InfoActivity extends AppCompatActivity {

    private final String BASEURL = "https://br.pythonanywhere.com/";
//    Handler handler = new Handler();
    private Bitmap bm;
    private TextView book_title, book_author, book_info;
    private ImageView book_img;
    private Button btn1, btn2;
    private ScrollView sc;
    private ArrayList<String> isbnlist, titlelist;
    private Button btny, btna, btnk, btnl, btns, btnr;
    private String linky, linka, linkk, linkl, links, linkr, isbn, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        book_title = (TextView) findViewById(R.id.book_title);
        book_author = (TextView)findViewById(R.id.book_author);
        book_info = (TextView) findViewById(R.id.book_info);
        book_img = (ImageView) findViewById(R.id.book_img);
        btn1 = (Button) findViewById(R.id.infobtn);
        btn2 = (Button) findViewById(R.id.buybtn);
        sc = (ScrollView) findViewById(R.id.scroll);
        btny = (Button) findViewById(R.id.yes);
        btna = (Button) findViewById(R.id.ala);
        btnk = (Button) findViewById(R.id.kyo);
        btnl = (Button) findViewById(R.id.lib);
        btns = (Button) findViewById(R.id.rary);
        btnr = (Button) findViewById(R.id.rid);

        isbn = getIntent().getStringExtra("isbn");
        title = getIntent().getStringExtra("title");

        getInfo();

        linky = "http://m.yes24.com/search/search?query="+title+"&domain=BOOK";
        linka = "https://www.aladin.co.kr/search/wsearchresult.aspx?SearchTarget=All&SearchWord=" + title;
        linkk = "https://search.kyobobook.co.kr/mobile/search?keyword=" + title;
        linkl = "https://lib.sookmyung.ac.kr/searchTotal/result?st=KWRD&si=TOTAL&q=" + title;
        linkr = "https://ridibooks.com/search/?q=" + title;
        links = "https://www.nl.go.kr/NL/contents/search.do?pageNum=1&pageSize=30&srchTarget=total&kwd=" + title;

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sc.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        btny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linky));
                startActivity(intent);
            }
        });

        btna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linka));
                startActivity(intent);
            }
        });

        btnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkk));
                startActivity(intent);
            }
        });

        btnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkl));
                startActivity(intent);
            }
        });

        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkr));
                startActivity(intent);
            }
        });

        btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(links));
                startActivity(intent);
            }
        });

    }

    private void getInfo() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<GetBookInfo>> call = jsonPlaceHolderApi.getBookInfo(isbn);
        call.enqueue(new Callback<List<GetBookInfo>>() {
            @Override
            public void onResponse(Call<List<GetBookInfo>> call, Response<List<GetBookInfo>> response) {
                if (!response.isSuccessful()) {
//                    book_title.setText("code: " + response.code());
                    System.out.println("code: " + response.code());
                    return;
                }

                List<GetBookInfo> infos = response.body();

                for (GetBookInfo in : infos) {
                    String title = in.getTitle();
                    String isbn = in.getIsbn();
                    String author = in.getAuthor();
                    String info = in.getInfo();

                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(BASEURL + "media/images/" + isbn + ".jpg");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true);
                                conn.connect();

                                InputStream is = conn.getInputStream();
                                bm = BitmapFactory.decodeStream(is);

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
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
                    book_title.setText(title);
                    book_author.setText(author);
                    book_info.setText(info);
                    book_img.setImageBitmap(bm);
                }

            }

            @Override
            public void onFailure(Call<List<GetBookInfo>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}