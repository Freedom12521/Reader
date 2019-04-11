package com.cheng.main;


import android.content.Context;

import com.cheng.repository.BookRepository;
import com.cheng.room.BookDatabase;

/**
 * 静态方法 用于注入各种activity和fragment所需类的静态方法。
 */
class InjectorUtils {
    //得到book仓库
    private static BookRepository getBookRepository(Context context) {
        return BookRepository.getInstance(BookDatabase.getInstance(context.getApplicationContext()).bookDao());
    }

    //得到ViewModel的工厂类
    static MainViewModelFactory providerMainViewModelFactory(Context context) {
        return new MainViewModelFactory(getBookRepository(context));
    }
}
