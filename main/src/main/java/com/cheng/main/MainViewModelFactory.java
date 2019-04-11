package com.cheng.main;

import com.cheng.repository.BookRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private BookRepository mBookRepository;

    public MainViewModelFactory(BookRepository repository){
         this.mBookRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mBookRepository);
    }
}
