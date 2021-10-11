package com.yuliang.tinnews.ui.home;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.squareup.picasso.Picasso;
import com.yuliang.tinnews.R;
import com.yuliang.tinnews.model.Article;

@Layout(R.layout.tin_news_card)
public class TinNewsCard {

    @View(R.id.news_images)
    private ImageView image;

    @View(R.id.news_title)
    private TextView newsTitle;

    @View(R.id.news_description)
    private TextView newsDescription;

    private final Article article;
    private final OnSwipeListener onSwipeListener;

    public TinNewsCard(Article news,OnSwipeListener onSwipeListener){
        this.article = news;
        this.onSwipeListener = onSwipeListener;
    }

    @Resolve
    private void onResolved(){
        if(article.urlToImage==null || article.urlToImage.isEmpty()){
            image.setImageResource(R.drawable.ic_empty_image);
        }
        Picasso.get().load(article.urlToImage).into(image);
        newsTitle.setText(article.title);
        newsDescription.setText(article.description);
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("Event","onSwipedOut");
        onSwipeListener.onDislike(article);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("Event","onSwipeCancelState");
    }

    @SwipeIn
    private void OnSwipeIn(){
        Log.d("Event","onSwipeIn");
        article.favorite = true;
        onSwipeListener.onLike(article);
    }

    interface OnSwipeListener{
        void onLike(Article news);
        void onDislike(Article news);
    }
}
