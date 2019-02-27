package com.cheng.searchbookdemo;

import android.os.Bundle;
import android.util.Log;

import com.cheng.parse.bean.Book;
import com.cheng.parse.parse.ParseHtml;
import com.cheng.parse.source.SourceType;


import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private List<Book> mBooks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseHtml.getInstance().parse(SourceType.SEARCH,"修真",list -> {
            mBooks = list;

            Log.i("reader", "onCreate: ============"+ mBooks.toString());
        });


    }



}
