package com.example.whensunset.pictureprocessing.base;

import android.databinding.Observable;

/**
 * Created by whensunset on 2018/3/21.
 */

public interface ObservableAction {
    void onPropertyChanged(Observable observable, int i);
}
