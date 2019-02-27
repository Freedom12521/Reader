package com.cheng.parse.parse;

import android.text.TextUtils;
import android.util.SparseArray;

import com.cheng.common.utils.LogUtil;
import com.cheng.parse.bean.Book;
import com.cheng.parse.source.Source;
import com.cheng.parse.source.SourceHtmlFormat;
import com.cheng.parse.source.SourceID;
import com.cheng.parse.source.SourceManager;
import com.cheng.parse.source.SourceType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SearchParseThread extends Thread {


    private String mKeyword; //搜索关键字
    private SparseArray<List<Book>> bookSource = new SparseArray<>();
    private ParseHtml.ParseHandler mParseHandler;

    SearchParseThread(String key, ParseHtml.ParseHandler handler) {
        mKeyword = key;
        this.mParseHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        try {
            for (int i = 0; i < SourceManager.SOURCES.size(); i++) {
                //获取对应书源
                Source source = SourceManager.SOURCES.get(i + 1);

                String url = "";
                if (!TextUtils.isEmpty(source.charset)) {
                    url = String.format(source.searchURL, URLEncoder.encode(mKeyword, source.charset));
                } else {
                    url = String.format(source.searchURL, mKeyword);
                }
                LogUtil.i("run: url ====== " + url);
                Document document = Jsoup.connect(url).get();

                switch (source.id) {
                    case SourceID.LIEWEN:
                        //解析猎文网搜索结果网页
                        getLieWenSearch(document);
                        break;
                    case SourceID.UCSHUMENG:
                        //解析UC书盟搜索结果网页
                        getUCTxtSearch(document);
                        break;
                    case SourceID.YANMOXUAN:
                        //解析衍墨轩
                        getYanMoXuan(document);
                        break;
                }
            }

            //书籍去重

            if (bookSource.size() > 0) {
                List<Book> lieWen = bookSource.get(SourceID.LIEWEN);
                List<Book> ucTxt = bookSource.get(SourceID.UCSHUMENG);
                List<Book> yanMoXuan = bookSource.get(SourceID.YANMOXUAN);

                List<Book> books = duplicate(duplicate(lieWen, ucTxt), yanMoXuan);

                if (mParseHandler != null) {
                    mParseHandler.obtainMessage(SourceType.SEARCH, books).sendToTarget();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 去重
     *
     * @param first
     * @param second
     * @return
     */

    private List<Book> duplicate(List<Book> first, List<Book> second) {
        List<Book> temps = new ArrayList<>(first);
        for (Book book : second) {
            if (!temps.contains(book)) {
                temps.add(book);
            }

        }
        return temps;

    }


    /**
     * 衍墨轩
     *
     * @param document
     */
    private void getYanMoXuan(Document document) {
        Elements types = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.YanMoXuan.SPAN_BOOK_TYPE);
        Elements names = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.YanMoXuan.SPAN_BOOK_NAME);
        Elements authors = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.YanMoXuan.SPAN_BOOK_AUTHOR);
        Elements newChapters = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.YanMoXuan.SPAN_BOOK_NEWCHAPTER);
        Elements updateTimes = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.YanMoXuan.SPAN_BOOK_UPDATE);
        List<Book> books = new ArrayList<>();
        for (int i = 1; i < types.size(); i++) {
            String type = types.get(i).ownText();
            String bookUrl = names.get(i).select(SourceHtmlFormat.A_ELEMENT).attr(SourceHtmlFormat.A_HREF_ATTR);
            String name = names.get(i).select(SourceHtmlFormat.A_ELEMENT).text();
            String newChapter = newChapters.get(i).select(SourceHtmlFormat.A_ELEMENT).text();
            String author = authors.get(i).select(SourceHtmlFormat.A_ELEMENT).text();
            String updateTime = updateTimes.get(i).ownText();

            Book book = new Book(name, author, "", type, updateTime, newChapter, bookUrl, "", SourceID.YANMOXUAN);
            books.add(book);
        }
        bookSource.put(SourceID.YANMOXUAN, books);
    }


    /**
     * uc书盟
     *
     * @param document
     */
    private void getUCTxtSearch(Document document) {
        Elements types = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.UCTxt.SPAN_BOOK_TYPE);
        Elements bookName = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.UCTxt.SPAN_BOOK_NAME);
        Elements bookInfo = document.select(SourceHtmlFormat.SPAN_ELEMENT + SourceHtmlFormat.UCTxt.SPAN_BOOK_OTHER);
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i).text().substring(1, 3);
            String name = bookName.get(i).select(SourceHtmlFormat.A_ELEMENT).text().split(" ")[0];
            String bookUrl = "http://www.uctxt.com" + bookName.get(i).select(SourceHtmlFormat.A_ELEMENT).attr(SourceHtmlFormat.A_HREF_ATTR);
            String newChapter = bookName.get(i).select(SourceHtmlFormat.SMALL_ELEMENT).select(SourceHtmlFormat.A_ELEMENT).text();
            String author = bookInfo.get(i).ownText();
            String updateTime = bookInfo.get(i).select(SourceHtmlFormat.SMALL_ELEMENT).get(1).text();

            Book book = new Book(name, author, "", type, updateTime, newChapter, bookUrl, "", SourceID.UCSHUMENG);
            books.add(book);
        }

        bookSource.put(SourceID.UCSHUMENG, books);


    }


    //获取猎文网  搜索结果网页
    private void getLieWenSearch(Document document) {
        //结果列表
        Elements bookImgs = document.select(SourceHtmlFormat.LieWen.DIV_RESULT_GAME_ITEM_PIC);
        Elements bookInfo = document.select(SourceHtmlFormat.LieWen.DIV_RESULT_GAME_ITEM_DETAIL);

        List<Book> books = new ArrayList<>();
        for (int i = 0; i < bookImgs.size(); i++) {
            String bookUrl = bookImgs
                    .get(i)
                    .select(SourceHtmlFormat.A_ELEMENT)
                    .attr(SourceHtmlFormat.A_HREF_ATTR);

            String imgUrl = bookImgs.get(i)
                    .select(SourceHtmlFormat.A_ELEMENT)
                    .select(SourceHtmlFormat.IMG_ELEMENT)
                    .attr(SourceHtmlFormat.IMG_SRC_ATTR);
            String name = bookInfo
                    .get(i).select(SourceHtmlFormat.A_ELEMENT)
                    .attr(SourceHtmlFormat.A_TITLE_ATTR);
            String desc = document.select(SourceHtmlFormat.LieWen.P_BOOK_DESC).get(i).text();
            String author = document.select(SourceHtmlFormat.LieWen.DIV_BOOK_INFO_ROOT)
                    .get(i)
                    .select(SourceHtmlFormat.LieWen.P_BOOK_INFO)
                    .get(0)
                    .select(SourceHtmlFormat.SPAN_ELEMENT)
                    .get(1).text();
            String type = document.select(SourceHtmlFormat.LieWen.DIV_BOOK_INFO_ROOT)
                    .get(i)
                    .select(SourceHtmlFormat.LieWen.P_BOOK_INFO)
                    .get(1)
                    .select(SourceHtmlFormat.SPAN_ELEMENT)
                    .get(1).text();
            String updateTime = document.select(SourceHtmlFormat.LieWen.DIV_BOOK_INFO_ROOT)
                    .get(i)
                    .select(SourceHtmlFormat.LieWen.P_BOOK_INFO)
                    .get(2)
                    .select(SourceHtmlFormat.SPAN_ELEMENT)
                    .get(1).text();
            String newChapter = document.select(SourceHtmlFormat.LieWen.DIV_BOOK_INFO_ROOT)
                    .get(i)
                    .select(SourceHtmlFormat.LieWen.P_BOOK_INFO)
                    .get(3)
                    .select(SourceHtmlFormat.A_ELEMENT)
                    .get(0).text();
            Book book = new Book(name, author, desc, type, updateTime, newChapter, bookUrl, imgUrl, SourceID.LIEWEN);
            books.add(book);

        }

        bookSource.put(SourceID.LIEWEN, books);

    }

}
