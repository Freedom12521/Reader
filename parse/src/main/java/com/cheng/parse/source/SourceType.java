package com.cheng.parse.source;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({
        SourceType.SEARCH,
        SourceType.CATALOG,
        SourceType.CONTENT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface SourceType {

    //搜索
    int SEARCH = 1;
    //章节目录
    int CATALOG = 2;
    //内容
    int CONTENT = 3;

}
