package com.cheng.parse.source;

public class SourceHtmlFormat {

    //a标签
    public static final String A_ELEMENT = "a";
    //a标签属性 title
    public static final String A_TITLE_ATTR = "title";
    //a标签属性  href
    public static final String A_HREF_ATTR = "href";
    //img标签
    public static final String IMG_ELEMENT = "img";
    //img标签的 src属性
    public static final String IMG_SRC_ATTR = "src";
    //span标签
    public static final String SPAN_ELEMENT = "span";
    //small标签
    public static final String SMALL_ELEMENT = "small";

    public static class LieWen{
        //图片所在根节点
        public static final String DIV_RESULT_GAME_ITEM_PIC = "div.result-game-item-pic";
        //书详情（名字、简介、作者、类型、更新时间、最新章节等）的根节点
        public static final String DIV_RESULT_GAME_ITEM_DETAIL = "div.result-game-item-detail";
        //书名根节点
        public static final String H3_BOOK_NAME_ROOT = "h3.result-item-title result-game-item-title";
        //书简介
        public static final String P_BOOK_DESC = "p.result-game-item-desc";
        //书信息根节点（作者、类型、更新时间、最新章节）
        public static final String DIV_BOOK_INFO_ROOT = "div.result-game-item-info";
        //存放book信息的 p标签  该标签有多个  每个该标签展示一种书信息
        public static final String P_BOOK_INFO = "p.result-game-item-info-tag";

    }


    /**
     * UC书盟
     */
    public static class UCTxt{
        public static String SPAN_BOOK_TYPE = ".class";
        public static String SPAN_BOOK_NAME = ".name";
        public static String SPAN_BOOK_OTHER = ".other";
    }

    /**
     * 衍墨轩
     */
    public static class YanMoXuan{
        public static String SPAN_BOOK_TYPE = ".nt";
        public static String SPAN_BOOK_NAME = ".n2";
        public static String SPAN_BOOK_NEWCHAPTER = ".c2";
        public static String SPAN_BOOK_AUTHOR = ".a2";
        public static String SPAN_BOOK_UPDATE = ".t";
    }

}
