package com.example.admin.hn.widget;

/**
 * Created by WIN10 on 2018/4/18.
 * 当给其中一个添加addTextChangedListener时，移除其他EditText的该事件
 */

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class ExtendedEditText extends EditText {

    private ArrayList<TextWatcher> mListeners = null;

    public ExtendedEditText(Context ctx)
    {
        super(ctx);
    }

    public ExtendedEditText(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }

    public ExtendedEditText(Context ctx, AttributeSet attrs, int defStyle)
    {
        super(ctx, attrs, defStyle);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher)
    {
        if (mListeners == null)
        {
            mListeners = new ArrayList<>();
        }
        mListeners.add(watcher);

        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher)
    {
        if (mListeners != null)
        {
            int i = mListeners.indexOf(watcher);
            if (i >= 0)
            {
                mListeners.remove(i);
            }
        }

        super.removeTextChangedListener(watcher);
    }

    public void clearTextChangedListeners() {
        if(mListeners != null) {
            for(TextWatcher watcher : mListeners)
            {
                super.removeTextChangedListener(watcher);
            }
            mListeners.clear();
            mListeners = null;
        }
    }

}