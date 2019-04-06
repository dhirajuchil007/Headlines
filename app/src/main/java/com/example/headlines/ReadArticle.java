package com.example.headlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ReadArticle extends AppCompatActivity {
    WebView articleWebView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        articleWebView=findViewById(R.id.artice_web_view);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        Intent intent=getIntent();
        final String url=intent.getStringExtra(MainActivity.ARTICLE_URL);
        articleWebView.getSettings().getJavaScriptEnabled();
        articleWebView.loadUrl(url);



        articleWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
