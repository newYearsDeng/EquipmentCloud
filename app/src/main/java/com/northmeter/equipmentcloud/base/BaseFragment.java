package com.northmeter.equipmentcloud.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *Created by dyd on 2018/8/29.
 */
public abstract  class BaseFragment extends Fragment {
    private Unbinder unbinder;

    public BaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGetArgument(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView=inflater.inflate(getLayoutResId(),container,false);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         unbinder = ButterKnife.bind(this, view);
        //初始化控件
        finishCreateView(savedInstanceState);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected abstract int getLayoutResId();

    /**
     * 初始化传递的参数
     */
    protected abstract void startGetArgument(Bundle savedInstanceState);

    /**
     * 初始化控件
     * @param savedInstanceState
     */
    protected abstract void finishCreateView(Bundle savedInstanceState);


}
