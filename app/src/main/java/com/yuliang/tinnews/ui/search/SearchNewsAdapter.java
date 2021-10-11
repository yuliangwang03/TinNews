package com.yuliang.tinnews.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.yuliang.tinnews.R;
import com.yuliang.tinnews.model.Article;

import java.util.LinkedList;
import java.util.List;

public class SearchNewsAdapter extends RecyclerView.Adapter<SearchNewsAdapter.SearchNewsViewHolder>{

    interface  LikeListener{
        void onLike(Article article);
        void onClick(Article article);
    }

    private List<Article> articles = new LinkedList<>();
    private LikeListener likeListener;

    public void setLikeListener(LikeListener likeListener){
        this.likeListener = likeListener;
    }

    @NonNull
    @Override
    public SearchNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_news_item,parent,false);
        return new SearchNewsViewHolder(view);
    }

    public void setArticles(List<Article> newList){
        this.articles.clear();
        this.articles.addAll(newList);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull SearchNewsAdapter.SearchNewsViewHolder holder, int position) {
        Article article = articles.get(position);
        if(article.urlToImage == null){
            holder.newsImage.setImageResource(R.drawable.ic_empty_image);
        }
        else {
            Picasso.get().load(article.urlToImage).into(holder.newsImage);
        }

        if(article.favorite){
            holder.favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            holder.favorite.setOnClickListener(null);
        }
        else{
            holder.favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            holder.favorite.setOnClickListener(
                    v -> {
                        article.favorite = true;
                        likeListener.onLike(article);
                    });
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public  static class SearchNewsViewHolder extends RecyclerView.ViewHolder{
        ImageView newsImage;
        ImageView favorite;

        public SearchNewsViewHolder(View itemView){
            super(itemView);
            newsImage = itemView.findViewById(R.id.image);
            favorite = itemView.findViewById(R.id.favorite);
        }

    }
}
