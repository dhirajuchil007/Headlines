package com.example.headlines;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.headlines.DB.AppDatabase;
import com.example.headlines.Model.Article;
import com.example.headlines.Model.Headlines;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ArticleRepo {

    private LiveData<List<Article>> articles;
    public static final String TAG="ArticleRepo";
    AppDatabase mDb;

    public ArticleRepo(Application application){
         mDb=AppDatabase.getsInstance(application.getApplicationContext());
        getDataFromServer(application);



    }



    //Gets data from server and stores it into DB
   public  void getDataFromServer(final Application application) {
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create()).build();
       NewsApi newsApi=retrofit.create(NewsApi.class);
        Call<Headlines> call=newsApi.getPosts("in",application.getApplicationContext().getString(R.string.api_key));

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(!response.isSuccessful())
                {
                    Log.e(TAG, "onResponse: "+response.code());
                    return;
                }
                Headlines h=response.body();
                Log.d(TAG, "outputIs: "+response.body().getArticles().get(19).getDescription()   );
                final List<Article> articlesList=h.getArticles();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDb.articleDao().deleteAll();

                            mDb.articleDao().insert(articlesList);
                            Log.d(TAG, "run: ");











                    }
                }).start();

            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t)
            {
                Log.e(TAG, "onFailure:Failed to fetch data "+t.getMessage());

            }
        });

    }

   public LiveData<List<Article>> getArticlesFromDB(){
        LiveData<List<Article>> articles=mDb.articleDao().getArticles();
        return  articles;
    }


    public void refreshData(Application application, final SwipeRefreshLayout swipeRefreshLayout){

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        NewsApi newsApi=retrofit.create(NewsApi.class);
        Call<Headlines> call=newsApi.getPosts("in",application.getApplicationContext().getString(R.string.api_key));

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(!response.isSuccessful())
                {
                    Log.e(TAG, "onResponse: "+response.code());
                    return;
                }
                Headlines h=response.body();
                Log.d(TAG, "outputIs: "+response.body().getArticles().get(19).getDescription()   );
                final List<Article> articlesList=h.getArticles();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDb.articleDao().deleteAll();

                        mDb.articleDao().insert(articlesList);
                        Log.d(TAG, "run: ");
                        swipeRefreshLayout.setRefreshing(false);











                    }
                }).start();

            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t)
            {
                Log.e(TAG, "onFailure:Failed to fetch data "+t.getMessage());

            }
        });

    }



}
