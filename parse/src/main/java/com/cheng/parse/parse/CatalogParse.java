package com.cheng.parse.parse;

import com.cheng.parse.bean.Catalog;
import com.cheng.parse.source.SourceHtmlFormat;
import com.cheng.parse.source.SourceID;
import com.cheng.parse.source.SourceType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogParse {

    private CatalogParse() {

    }

    private static final class HOLDER {
        private static final CatalogParse INSTANCE = new CatalogParse();
    }

    static CatalogParse newInstance() {
        return HOLDER.INSTANCE;
    }


    public List<Catalog> parse(@SourceID int id, String url) {
        try {
            Document document = Jsoup.connect(url).get();

           return getCatalog(id,document);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Catalog> getCatalog(int id,Document document) {
       List<Catalog> list = new ArrayList<>();
        switch (id) {
            case SourceID.LIEWEN:
            case SourceID.UCSHUMENG:
                list.addAll(getCatalog(document));
                break;
            case SourceID.YANMOXUAN:
                list.addAll(getCatalogByYanMoXuan(document));
                break;
        }
        return list;
    }

    private List<Catalog> getCatalog(Document document) {
        List<Catalog> list = new ArrayList<>();
        Elements elements = document.select(SourceHtmlFormat.DD_ELEMENT);
        for(int i = 0 ;i< elements.size(); i ++){
            String catalogUrl = elements.select(SourceHtmlFormat.A_ELEMENT).attr(SourceHtmlFormat.A_HREF_ATTR);
            String chapterName = elements.select(SourceHtmlFormat.A_ELEMENT).text();
            Catalog catalog = new Catalog(chapterName,catalogUrl);
            list.add(catalog);
        }
        return list;

    }

    private List<Catalog> getCatalogByYanMoXuan(Document document){
        List<Catalog> list = new ArrayList<>();
        Elements elements = document.select(SourceHtmlFormat.YanMoXuan.CATALOG_ELEMENT);
        for(int i = 0 ;i< elements.size(); i ++){
            String catalogUrl =SourceHtmlFormat.HTTPS+ elements.select(SourceHtmlFormat.A_ELEMENT).attr(SourceHtmlFormat.A_HREF_ATTR);
            String chapterName = elements.select(SourceHtmlFormat.A_ELEMENT).text();
            Catalog catalog = new Catalog(chapterName,catalogUrl);
            list.add(catalog);
        }
        return list;
    }

}
