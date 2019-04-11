package com.cheng.parse.parse;

import com.cheng.parse.bean.Catalog;

import java.util.List;

public interface CatalogCallback extends BaseParseCallback{
    void onCatalogResult(List<Catalog> list);
}
