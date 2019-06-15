package com.example.whensunset.pictureprocessing.viewModel.itemManagerVM;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessing.base.IImageUriFetch;
import com.example.whensunset.pictureprocessing.base.util.MyLog;
import com.example.whensunset.pictureprocessing.base.util.MyUtil;
import com.example.whensunset.pictureprocessing.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemManagerBaseVM;
import com.example.whensunset.pictureprocessing.impl.SystemImageUriFetch;
import com.example.whensunset.pictureprocessing.staticParam.ObserverMapKey;
import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by whensunset on 2018/3/5.
 */

public class PictureItemManagerVM extends ItemManagerBaseVM<PictureItemManagerVM.PictureItemVM> {


    public static final int ITEM_PICTURE_RESIZE_WIDTH = 100;
    public static final int ITEM_PICTURE_RESIZE_HEIGHT = 100;
    public static final int MENU_ITEM_WIDTH = MyUtil.getDisplayWidthDp() / 3 - MyUtil.px2dip(10);
    public static final int MENU_ITEM_HEIGHT = MENU_ITEM_WIDTH;

    private final IImageUriFetch mIImageUriFetch;

    public PictureItemManagerVM() {
        super(1 ,BR.viewModel , R.layout.activity_main_picture_item);
        mIImageUriFetch = SystemImageUriFetch.getInstance();

        initItemVM();
    }

    @Override
    protected void initItemVM() {
        freshPictureList(mIImageUriFetch.getAllImageUriList());
    }

    public void freshPictureList(String directoryName) {
        freshPictureList(mIImageUriFetch.getALlImageUriListFromTag(directoryName));
    }

    private void freshPictureList(List<String> imageUriList) {
        mDataItemList.clear();
        final int[] nowPosition = {0};
        Flowable.fromIterable(imageUriList)
                .map(imageUri -> new PictureItemVM(mEventListenerList , nowPosition[0]++ , imageUri))
                .subscribe(mDataItemList::add);
    }

    public static class PictureItemVM extends ItemBaseVM {
        public final ObservableField<String> mImageUri=new ObservableField<>();

        public PictureItemVM(List<ObservableField<? super Object>> clickListenerList, int position , String imageUri ) {
            super(clickListenerList, position);
            initDefaultUIActionManager();

            mImageUri.set(imageUri);
            initClick();
        }

        private void initClick() {
            getDefaultClickThrottleFlowable(3000)
                    .subscribe(LastEventListenerPosition -> {
                        ObserverParamMap paramMap = getPositionParamMap()
                                .set(ObserverMapKey.PictureItemManagerVM_mImageUri , mImageUri.get());
                        mEventListenerList.get(LastEventListenerPosition).set(paramMap);
                        MyLog.d(TAG, "onTextChanged", "状态:LastEventListenerPosition:ObserverParamMap:", LastEventListenerPosition , paramMap);
                    });
        }
    }
}
