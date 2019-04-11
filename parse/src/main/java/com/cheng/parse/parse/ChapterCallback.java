package com.cheng.parse.parse;

import com.cheng.parse.bean.Book;
import com.cheng.parse.bean.Chapter;
import com.cheng.parse.bean.Catalog;

import java.util.List;

public interface ChapterCallback extends BaseParseCallback{


    void onContentResult(Chapter chapter);
}
