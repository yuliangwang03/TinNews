package com.yuliang.tinnews.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yuliang.tinnews.TinNewsApplication;
import com.yuliang.tinnews.database.AppDatabase;
import com.yuliang.tinnews.model.Article;
import com.yuliang.tinnews.model.NewsResponse;
import com.yuliang.tinnews.network.NewsApi;
import com.yuliang.tinnews.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private final NewsApi newsApi;
    private final AppDatabase database;
    private AsyncTask asyncTask;


    public NewsRepository(Context context) {
        newsApi = RetrofitClient.newInstance(context).create(NewsApi.class);
        database = TinNewsApplication.getDatabase();
    }

    public LiveData<NewsResponse> getTopHeadlines(String country) {
        MutableLiveData<NewsResponse> getTopHeadlinesLiveData = new MutableLiveData<>();
        newsApi.getTopHeadlines(country).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    getTopHeadlinesLiveData.setValue(response.body());
                } else {
                    getTopHeadlinesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                getTopHeadlinesLiveData.setValue(null);
            }
        });
        return getTopHeadlinesLiveData;
    }

    public LiveData<NewsResponse> searchNews(String query) {
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        newsApi.getEverything(query, 40).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    everyThingLiveData.setValue(response.body());
                } else {
                    everyThingLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                everyThingLiveData.setValue(null);
            }
        });
        return everyThingLiveData;
    }

    @SuppressLint("StaticFieldLeak")
    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>();
        asyncTask =
                new AsyncTask<Void, Void, Boolean>() {
        @Override
        protected Boolean doInBackground(Void... voids) {
                try {
                    database.dao().saveArticle(article);
                    } catch (Exception e) {
                    Log.e("test", e.getMessage());
                    return false;
                    }
                return true;
                }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
                article.favorite = isSuccess;
                isSuccessLiveData.setValue(isSuccess);
                }
        }.execute();
        return isSuccessLiveData;
        }

        public LiveData<List<Article>> getAllSavedArticles(){
            return database.dao().getAllArticles();
        }

        public void deleteSavedArticle(Article article){
            AsyncTask.execute(
                    ()->database.dao().deleteArticle(article));
        }

        public void onCancel() {
        if (asyncTask != null) {
            asyncTask.cancel(true);
            }
        }
}
