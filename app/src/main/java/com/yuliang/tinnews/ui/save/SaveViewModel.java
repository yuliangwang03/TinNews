package com.yuliang.tinnews.ui.save;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yuliang.tinnews.model.Article;
import com.yuliang.tinnews.repository.NewsRepository;

import java.util.List;

public class SaveViewModel extends ViewModel {

    private final NewsRepository repository;

    public  SaveViewModel(NewsRepository repository){
        this.repository = repository;
    }

    public LiveData<List<Article>> getAllSavedArticles(){
        return  repository.getAllSavedArticles();
    }

    public void deleteSavedArticle(Article article){
        repository.deleteSavedArticle(article);
    }

    public void onCancel(){
        repository.onCancel();
    }
}
