package com.cheng.search;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cheng.common.base.BaseActivity;
import com.cheng.common.utils.ARouterPath;
import com.cheng.common.utils.LogUtil;
import com.cheng.common.utils.PermissionUtil;
import com.cheng.search.databinding.SearchActivityMainBinding;


import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;


@Route(path = ARouterPath.SEARCH_MAIN_PATH)
public class MainActivity extends BaseActivity<SearchActivityMainBinding> {

    private EditText mSearch;

    private SearchAdapter mSearchAdapter;
    private HistoryAdapter mHistoryAdapter;
    private SearchViewModel mSearchViewModel;
    private DividerItemDecoration mDividerItemDecoration;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setLeftImg(R.drawable.ic_back);
        setRightText("搜索");
        setToolbarBackground(R.color.colorPrimaryDark);
        setRightTextColor(R.color.white);
        mSearch = (EditText) addTitleView(R.layout.search_title);


        SearchViewModelFactory factory = InjectoryUtils.providerSearchViewModelFactory(this);
        mSearchViewModel = ViewModelProviders.of(this, factory).get(SearchViewModel.class);
        mDividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mViewDataBinding.searchRv.addItemDecoration(mDividerItemDecoration);
        initHistory();


        PermissionUtil.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

    }

    private void initHistory() {
        if (mHistoryAdapter == null) {
            mHistoryAdapter = new HistoryAdapter();
            mHistoryAdapter.setOnHistoryItemClickListener(history -> v -> {
                mSearch.setText(history);
                mSearch.setSelection(history.length());
                initSearch();
            });
            mViewDataBinding.searchHistoryRv.setAdapter(mHistoryAdapter);
            mSearchViewModel.bindHistoryLiveData().observe(this, list -> {
                mHistoryAdapter.submitList(list);
                if (list.size() <= 0) {
                    mViewDataBinding.searchHistoryLayout.setVisibility(View.GONE);
                }
            });
        }
        mViewDataBinding.searchHistoryLayout.setVisibility(View.VISIBLE);
        mViewDataBinding.searchRv.setVisibility(View.GONE);

        loadHistory();
    }

    @Override
    protected void initListener() {

        setLeftClickListener(v -> exit());
        setRightClickListener(v -> initSearch());

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    if (mViewDataBinding.searchHistoryLayout.getVisibility() == View.GONE) {
                        initHistory();
                    }
                }
            }
        });

        mViewDataBinding.searchHistoryDelete.setOnClickListener(v -> {
            //删除历史记录
            mSearchViewModel.deleteHistory();
        });

    }

    private void initSearch() {
        String keyword = mSearch.getText().toString().trim();

        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "请输入书名!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mSearchAdapter == null) {
            mSearchAdapter = new SearchAdapter();
            mSearchAdapter.setOnSearchItemClickListener(book -> (View.OnClickListener) v -> {
                //跳转到书籍详情页面
            });
            //设置adapter
            mViewDataBinding.searchRv.setAdapter(mSearchAdapter);
            //绑定liveData 并注册观察者（观察者为当前activity）
            mSearchViewModel.bindSearchLiveData().observe(this, books -> {
                if (books == null || books.size() <= 0) {
                    showLoadEmpty();
                    return;
                }
                showLoadSuccess();
                mSearchAdapter.submitList(books);
            });
        }

        mViewDataBinding.searchRv.setVisibility(View.VISIBLE);
        mViewDataBinding.searchHistoryLayout.setVisibility(View.GONE);

        search(keyword);


    }

    private void loadHistory() {
        mSearchViewModel.loadHistory();
    }


    //搜索
    private void search(String keyword) {
        showLoading();
        mSearchViewModel.addHistory(keyword);
        mSearchViewModel.search(keyword);
    }


    //权限通过
    @Override
    public void onGranted() {
        LogUtil.i("onGranted==============");
    }

    //权限未通过
    @Override
    public void onDenied(String[] deniedPermissions) {
        LogUtil.i("onDenied ===== " + Arrays.toString(deniedPermissions));
    }

    //未通过的权限中，用户点击了不再询问的权限
    @Override
    public void onRationale(String[] rationale) {
        LogUtil.i("onRationale ======== " + Arrays.toString(rationale));
    }

    @Override
    public int getLayoutId() {
        return R.layout.search_activity_main;
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.colorPrimaryDark;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        exit();
    }
}
