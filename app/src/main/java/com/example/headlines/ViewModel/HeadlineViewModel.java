package com.example.headlines.ViewModel;

import android.app.Application;
import android.util.Log;

import com.example.headlines.ArticleRepo;
import com.example.headlines.Model.Article;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.example.headlines.ArticleRepo.TAG;

public class HeadlineViewModel extends AndroidViewModel {

    ArticleRepo repo;
    private LiveData<List<Article>> articles;
    public HeadlineViewModel(@NonNull Application application) {
        super(application);
        repo=new ArticleRepo(application);
        repo.getDataFromServer(application);
        Log.d(TAG, "HeadlineViewModel: constructor");

    }

    public LiveData<List<Article>> loadDataFromDB(){
        articles=repo.getArticlesFromDB();

        return articles;

    }

    public  void refreshNews(Application application,SwipeRefreshLayout swipeRefreshLayout){
        repo.refreshData(application,swipeRefreshLayout);
        //Log.d(TAG, "refreshNews: ");

    }
}
