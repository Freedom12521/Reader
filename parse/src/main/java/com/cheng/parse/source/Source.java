package com.cheng.parse.source;

import java.io.Serializable;

/**
 * 源
 */

public class Source implements Serializable {

    @SourceID
    public int id;

    public String name;

    public String searchURL;

    public String charset;

    /**
     * 最少输入字数 关键字
     */
    public int minKeywords = 1;

    public Source(@SourceID int id,String name,String searchURL,String charset){
        this.id = id;
        this.name = name;
        this.searchURL = searchURL;
        this.charset = charset;
    }


    public Source(@SourceID int id,String name,String searchURL,int minKeywords){
         this.id = id;
         this.name = name;
         this.searchURL = searchURL;
         this.minKeywords = minKeywords;
    }

}
