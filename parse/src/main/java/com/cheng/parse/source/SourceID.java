package com.cheng.parse.source;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * 书源ID
 */
@IntDef({
        SourceID.LIEWEN,
        SourceID.UCSHUMENG,
        SourceID.YANMOXUAN,
})
@Retention(RetentionPolicy.SOURCE)
public @interface SourceID {

    /**
     * 猎文网
     */
    int LIEWEN = 1;

    /**
     * UC书盟
     */
    int UCSHUMENG = 2;

    /**
     * 衍墨轩
     */
    int YANMOXUAN = 3;

}
