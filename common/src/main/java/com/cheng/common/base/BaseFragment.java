package com.cheng.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getLayout() <=0){
            throw new RuntimeException("layout is null");
        }
        View view = inflater.inflate(getLayout(),container,false);

        initView();
        setListener();
        return view;
    }

    protected abstract void setListener();

    protected abstract void initView();


    protected abstract int getLayout();


}
