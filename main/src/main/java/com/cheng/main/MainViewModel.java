package com.cheng.main;

import com.cheng.common.base.BaseRepository;
import com.cheng.common.base.BaseViewModel;
import com.cheng.repository.BookRepository;
import com.cheng.room.bean.Book;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel<List<Book>> {

    private BookRepository mBookRepository;

    MainViewModel(BaseRepository... repositorys) {
        super(repositorys);
        if (repositorys.length <= 0) {
            throw new IndexOutOfBoundsException("BaseRepository[] size <=0");
        }
        mBookRepository = mRepositorys[0] instanceof BookRepository ? (BookRepository) mRepositorys[0] : null;
        if (mBookRepository == null) {
            throw new NullPointerException("repository is null");
        }
    }

    //获取书架列表
    public LiveData<List<Book>> getList() {
        Disposable disposable = mBookRepository.loadBook()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> mMutableLiveData.postValue(books));
        mDisposable.add(disposable);
        return mMutableLiveData;
    }

    //把某一本书删除
    public void delete(int bookId) {
        mBookRepository.deleteById(bookId);
    }


}
