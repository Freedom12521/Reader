package com.cheng.search;

import com.cheng.repository.SearchRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private SearchRepository mSearchRepository;

    public SearchViewModelFactory(SearchRepository mSearchRepository) {
        this.mSearchRepository = mSearchRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(mSearchRepository);
    }
}
