package com.example.whensunset.pictureprocessing.base;

import com.example.whensunset.pictureprocessing.impl.BaseMyConsumer;

/**
 * Created by whensunset on 2018/3/11.
 */

public interface Chain<T , M> {
    void init(T startParam);
    M runStart(BaseMyConsumer... consumers);
    M runNext(BaseMyConsumer consumer);
    M runNow(BaseMyConsumer consumer);
    M undo();
    M redo();
    void destroy();

}
