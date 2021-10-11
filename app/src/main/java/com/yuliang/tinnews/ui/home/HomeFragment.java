package com.yuliang.tinnews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mindorks.placeholderview.SwipeDecor;
import com.yuliang.tinnews.R;
import com.yuliang.tinnews.databinding.FragmentHomeBinding;
import com.yuliang.tinnews.model.Article;
import com.yuliang.tinnews.repository.NewsRepository;
import com.yuliang.tinnews.repository.NewsViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements TinNewsCard.OnSwipeListener{

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentHomeBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        binding
                .swipeView
                .getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(
                        new SwipeDecor()
                            .setPaddingTop(20)
                            .setRelativeScale(0.01f)
                );

        binding.rejectBtn.setOnClickListener(v -> binding.swipeView.doSwipe(false));
        binding.acceptBtn.setOnClickListener(v -> binding.swipeView.doSwipe(true));

        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this,new NewsViewModelFactory(repository))
                .get(HomeViewModel.class);
        viewModel.setCountryInput("us");
        viewModel.getTopHeadlines()
                 .observe(getViewLifecycleOwner(),
                         newsResponse -> {
                        if(newsResponse != null){
                           for(Article article:newsResponse.articles){
                               TinNewsCard tinNewsCard = new TinNewsCard(article, this);
                               binding.swipeView.addView(tinNewsCard);
                           }
                        }
                         });
        viewModel
                .onFavorite()
                .observe(getViewLifecycleOwner(),isSuccess->{
                    if(isSuccess){
                        Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"You might have liked before",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onLike(Article news) {
        viewModel.setFavoriteArticleInput(news);
    }

    @Override
    public void onDislike(Article news) {
        if(binding.swipeView.getChildCount()<3){
            viewModel.setCountryInput("us");
        }
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        viewModel.onCancel();
    }
}