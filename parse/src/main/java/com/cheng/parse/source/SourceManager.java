package com.cheng.parse.source;


import android.util.SparseArray;

/**
 * 书源管理
 */
public class SourceManager {

    public static final SparseArray<Source> SOURCES = new SparseArray<Source>(){
        {
            put(SourceID.LIEWEN,new Source(SourceID.LIEWEN,"猎文网","https://www.liewen.cc/search.php?keyword=%s",""));
            put(SourceID.UCSHUMENG,new Source(SourceID.UCSHUMENG,"UC书盟","http://www.uctxt.com/modules/article/search.php?searchkey=%s","GB2312"));
            put(SourceID.YANMOXUAN,new Source(SourceID.YANMOXUAN,"衍墨轩","http://www.ymoxuan.com/search.htm?keyword=%s","UTF-8"));
        }

    };
}
