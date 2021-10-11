package com.yuliang.tinnews.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.yuliang.tinnews.model.Article;
import com.yuliang.tinnews.model.NewsResponse;
import com.yuliang.tinnews.repository.NewsRepository;

public class SearchViewModel extends ViewModel {
    private final NewsRepository repository;
    private final MutableLiveData<String> searchInput = new MutableLiveData<>();
    private final MutableLiveData<Article> favoriteArticleInput = new MutableLiveData<>();

    public SearchViewModel(NewsRepository repository){
        this.repository = repository;
    }

    public void setSearchInput(String query){
        searchInput.setValue(query);
    }

    public void setFavoriteArticleInput(Article article){
        favoriteArticleInput.setValue(article);
    }

    public LiveData<NewsResponse> getSearchInput() {

        return Transformations.switchMap(searchInput,repository::searchNews);
    }

    public LiveData<Boolean> onFavorite(){
        return Transformations.switchMap(favoriteArticleInput,repository::favoriteArticle);
    }

    public void onCancel(){
        repository.onCancel();
    }
}
