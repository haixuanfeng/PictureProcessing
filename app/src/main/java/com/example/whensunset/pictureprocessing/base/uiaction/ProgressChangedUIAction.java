package com.example.whensunset.pictureprocessing.base.uiaction;

import com.example.whensunset.pictureprocessing.base.viewmodel.BaseVM;
import com.example.whensunset.pictureprocessing.base.util.MyLog;

/**
 * Created by whensunset on 2018/3/22.
 */

public class ProgressChangedUIAction extends BaseUIAction{
    public static final String TAG = "何时夕:ProgressChangedUIAction";
    private UIActionListener<ProgressChangedUIAction> mOnProgressChangedListener = null;
    private int mProgress = Integer.MIN_VALUE;

    @Override
    public void onTriggerListener(int eventListenerPosition , BaseVM baseVM , UIActionManager.CallAllPreEventAction callAllPreEventAction, UIActionManager.CallAllAfterEventAction callAllAfterEventAction, Object... params) {
        super.onTriggerListener(eventListenerPosition , baseVM , callAllPreEventAction, callAllAfterEventAction, params);
        int progress = (int) params[0];
        mProgress = progress;
        mLastEventListenerPosition = eventListenerPosition;

        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition:progress:", "触发了点击事件监听器", eventListenerPosition , progress);
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mOnProgressChangedListener = (UIActionListener<ProgressChangedUIAction>) listener;
    }

    public int getProgress() {
        if (mProgress == Integer.MIN_VALUE) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mProgress;
    }

    @Override
    public boolean checkParams(Object[] params) {
        return (params != null && params.length >= 1 && params[0] != null);
    }

    @Override
    public String toString() {
        return "ProgressChangedUIAction{" +
                "mLastEventListenerPosition=" + mLastEventListenerPosition +
                ", mProgress=" + mProgress +
                '}';
    }
}
