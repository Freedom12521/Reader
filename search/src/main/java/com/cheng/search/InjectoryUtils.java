package com.cheng.search;

import android.content.Context;

import com.cheng.repository.SearchRepository;
import com.cheng.room.BookDatabase;

class InjectoryUtils {

    private static SearchRepository getSearchRepository(Context context) {
        return SearchRepository.getInstance(BookDatabase.getInstance(context).historyDao());
    }

    static SearchViewModelFactory providerSearchViewModelFactory(Context context) {
        return new SearchViewModelFactory(getSearchRepository(context));
    }
}
