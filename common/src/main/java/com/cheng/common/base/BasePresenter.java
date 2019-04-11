package com.cheng.common.base;

import androidx.lifecycle.DefaultLifecycleObserver;

public abstract class BasePresenter<V extends IBaseView> implements DefaultLifecycleObserver {

    private V mView;

  public abstract  void setView(V view);
  

}
