package com.cheng.parse.parse;

import com.cheng.parse.bean.Book;

import java.util.List;

public interface ParseCallback {

    void onSearchResult(List<Book> list);
}
