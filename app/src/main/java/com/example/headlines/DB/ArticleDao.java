package com.example.headlines.DB;

import com.example.headlines.Model.Article;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
@Dao
public interface ArticleDao {

    @Insert
    void insert(List<Article> articles);

    @Query("Select * from article order by publishedAt Desc")
   LiveData<List<Article>> getArticles();

    @Query("delete from article")
    void deleteAll();
}
