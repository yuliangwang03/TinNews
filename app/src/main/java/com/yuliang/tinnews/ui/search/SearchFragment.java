package com.yuliang.tinnews.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.yuliang.tinnews.R;
import com.yuliang.tinnews.databinding.FragmentSearchBinding;
import com.yuliang.tinnews.model.Article;
import com.yuliang.tinnews.repository.NewsRepository;
import com.yuliang.tinnews.repository.NewsViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        binding=FragmentSearchBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();
        newsAdapter.setLikeListener(new SearchNewsAdapter.LikeListener() {
            @Override
            public void onLike(Article article) {
                viewModel.setFavoriteArticleInput(article);
            }

            @Override
            public void onClick(Article article) {

            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return position == 0 ?2:1;
                    }
                }
        );
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setAdapter(newsAdapter);

        binding.searchView.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = binding.searchView.getText().toString();
            if(actionId== EditorInfo.IME_ACTION_DONE && !searchText.isEmpty()){
                viewModel.setSearchInput(searchText);
                return true;
            }
            else {
                return false;
            }
        });
        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this,new NewsViewModelFactory(repository)).get(SearchViewModel.class);
        viewModel.getSearchInput().observe(
                getViewLifecycleOwner(), newsResponse -> {
                    if(newsResponse != null){
                        Log.d("search_page",newsResponse.toString());
                        newsAdapter.setArticles(newsResponse.articles);
                    }
                });
        viewModel.onFavorite()
                .observe(getViewLifecycleOwner(),isSuccess->{
                   if(isSuccess){
                       Toast.makeText(requireActivity(),"Success",Toast.LENGTH_SHORT).show();
                       newsAdapter.notifyDataSetChanged();
                   }
                   else{
                       Toast.makeText(requireActivity(),"You might have liked before",Toast.LENGTH_SHORT).show();
                   }
                });
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        viewModel.onCancel();
    }
}