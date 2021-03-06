package com.yuliang.tinnews.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.yuliang.tinnews.model.Article;

import java.util.List;

@Dao
public interface RoomDao {
    @Insert
    void saveArticle(Article news);

    @Query("SELECT * FROM article")
    LiveData<List<Article>> getAllArticles();

    @Delete
    void deleteArticle(Article... articles);
}
