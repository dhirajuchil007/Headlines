package com.example.headlines;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.example.headlines.DB.AppDatabase;
import com.example.headlines.Model.Article;
import com.example.headlines.ViewModel.HeadlineViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements HeadlineAdapter.OnItemClicked{
    AppDatabase mDb;
    private HeadlineViewModel headlineViewModel;
    public static final String TAG="mainactivity";
    TextView loadingIndicator;
    public static final String ARTICLE_URL="articleurl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SwipeRefreshLayout swipeRefreshLayout;
        swipeRefreshLayout=findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        loadingIndicator=findViewById(R.id.loading_tv);

        RecyclerView headlinesRecyclerView=findViewById(R.id.headlines_list);
        headlinesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final HeadlineAdapter headlineAdapter=new HeadlineAdapter();
        headlinesRecyclerView.setAdapter(headlineAdapter);
        headlineAdapter.setOnClick(this);

        headlineViewModel= ViewModelProviders.of(this).get(HeadlineViewModel.class);
        headlineViewModel.loadDataFromDB().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if(articles.size()>0)
                {loadingIndicator.setVisibility(View.INVISIBLE);}
                headlineAdapter.setData(articles);


            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                headlineViewModel.refreshNews(getApplication(),swipeRefreshLayout);



            }


        }


        );






    }

    @Override
    public void onItemClick(int position,String url) {
        Log.d(TAG, "onItemClick: "+url);
        Intent intent=new Intent(MainActivity.this,ReadArticle.class);
        intent.putExtra(ARTICLE_URL,url);
        startActivity(intent);

    }
}
