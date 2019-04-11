package com.cheng.search;


import com.cheng.common.base.BaseRepository;
import com.cheng.common.base.BaseViewModel;
import com.cheng.parse.bean.Book;
import com.cheng.repository.SearchRepository;
import com.cheng.room.bean.History;

import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class SearchViewModel extends BaseViewModel<List<History>> {
    private SearchRepository mSearchRepository;
    //此Book为parse中的Book 和room中的Book不同
    private MutableLiveData<List<Book>> mBookLiveData;


    SearchViewModel(BaseRepository... repositorys) {
        super(repositorys);
        mSearchRepository = (SearchRepository) repositorys[0];
        mBookLiveData = new MutableLiveData<>();
    }

    LiveData<List<History>> bindHistoryLiveData() {
        return mMutableLiveData;
    }

    void addHistory(String name) {
        mDisposable.add(execute(mSearchRepository.addHistory(new History(name)))
                .subscribe());

    }

    void loadHistory() {
        mDisposable.add(execute(mSearchRepository.getHistory()
                .concatMap((Function<List<History>, Publisher<List<History>>>) list -> {

                    Collections.sort(list, (o1, o2) -> o1.getId() > o2.getId() ? -1 : 1);
                    return Observable.just(list).toFlowable(BackpressureStrategy.BUFFER);
                }))
                .subscribe(histories -> mMutableLiveData.postValue(histories)));
    }

    void deleteHistory() {
        mDisposable.add(execute(mSearchRepository.deleteAllHistory(mMutableLiveData.getValue()))
                .subscribe());
    }

    LiveData<List<Book>> bindSearchLiveData() {
        return mBookLiveData;
    }

    public void search(String keyword) {
        mSearchRepository.search(keyword, list -> mBookLiveData.postValue(list)
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!mSearchRepository.isCancelled()) {
            mSearchRepository.cancel();
        }
    }
}
