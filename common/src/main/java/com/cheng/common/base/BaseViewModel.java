package com.cheng.common.base;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BaseViewModel<T> extends ViewModel {

    protected BaseRepository[] mRepositorys;

    protected List<Disposable> mDisposable;
    protected MutableLiveData<T> mMutableLiveData;

    protected BaseViewModel(BaseRepository... repositorys) {
        this.mRepositorys = repositorys;
        mMutableLiveData = new MutableLiveData<>();
        mDisposable = new ArrayList<>();
    }


    protected Completable execute(Completable completable) {
        return completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected Flowable<T> execute(Flowable<T> flowable) {
        return flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    protected void onCleared() {
        if (mDisposable.size() > 0) {
            for (Disposable disposable : mDisposable) {
                disposable.dispose();
            }
        }
        super.onCleared();
    }
}
