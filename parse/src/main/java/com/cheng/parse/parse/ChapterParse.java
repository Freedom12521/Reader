package com.cheng.parse.parse;

import com.cheng.parse.bean.Chapter;
import com.cheng.parse.source.SourceHtmlFormat;
import com.cheng.parse.source.SourceID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChapterParse {

    private ChapterParse() {
    }


    private static class HOLDER {
        private static final ChapterParse INSTANCE = new ChapterParse();
    }

    public static ChapterParse newInstance() {
        return HOLDER.INSTANCE;
    }

    public Chapter parse(@SourceID int id, String chapterUrl) {

        try {
            Element element = Jsoup.connect(chapterUrl).get();
            return getChapter(id, element);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Chapter getChapter(@SourceID int id, Element element) {
        switch (id) {
            case SourceID.LIEWEN:
                return getChapterByLieWen(element);
            case SourceID.UCSHUMENG:
                return getChapterByUC(element);
            case SourceID.YANMOXUAN:
                return getChapterByYanMoxuan(element);
        }
        return null;
    }


    private Chapter getChapterByLieWen(Element element) {
        String title  = element.select(SourceHtmlFormat.LieWen.DIV_CHAPTER_NAME).select(SourceHtmlFormat.H1_ELEMENT).text();
        String content = element.select(SourceHtmlFormat.LieWen.DIV_CHAPTER_CONTENT).text();
        return new Chapter(content,title);
    }
    private Chapter getChapterByUC(Element element) {
        String title = element.select(SourceHtmlFormat.UCTxt.CHAPTER_TITLE).text();
        String content = element.select(SourceHtmlFormat.UCTxt.CHAPTER_CONTENT).text();
        return new Chapter(content,title);
    }



    private Chapter getChapterByYanMoxuan(Element element) {
        String title = element.select(SourceHtmlFormat.YanMoXuan.CHAPTER_NAME).select(SourceHtmlFormat.H1_ELEMENT).text();
        String content = element.select(SourceHtmlFormat.YanMoXuan.CHAPTER_CONTENT).text();
        return new Chapter(content,title);
    }
}
