package com.example.zhuffei.ffei.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;


/**
 * A simple {@link Fragment} subclass.
 */

/**
 * 公用的界面，考虑到通用性，就先写一个简单的BaseFragment吧
 * 所有子类界面只需要设置简单的界面内容即可
 */
public abstract class BaseFragment extends Fragment {

    //公用内容设置
    private TextView mBaseTvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_base, container, false);
        mBaseTvTitle = ((TextView) view.findViewById(R.id.BaseTvTitle));
        setTitle(mBaseTvTitle);
        return view;
    }

    /**
     * 让子类去实现对应的内容
     * @param tvTitle
     */
    protected abstract void setTitle(TextView tvTitle);
}
