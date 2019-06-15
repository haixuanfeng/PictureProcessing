package com.example.whensunset.pictureprocessing.viewModel.includeLayoutVM;

import android.databinding.ObservableField;
import android.net.Uri;
import android.text.TextUtils;

import com.example.whensunset.pictureprocessing.base.BaseSeekBarRecycleViewVM;
import com.example.whensunset.pictureprocessing.base.IImageUriFetch;
import com.example.whensunset.pictureprocessing.base.uiaction.ClickUIAction;
import com.example.whensunset.pictureprocessing.base.uiaction.ProgressChangedUIAction;
import com.example.whensunset.pictureprocessing.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessing.base.util.MyLog;
import com.example.whensunset.pictureprocessing.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemManagerBaseVM;
import com.example.whensunset.pictureprocessing.impl.LocalFrameImageUriFetch;
import com.example.whensunset.pictureprocessing.mete.CutView;
import com.example.whensunset.pictureprocessing.pictureProcessing.PictureFrameMyConsumer;
import com.example.whensunset.pictureprocessing.pictureProcessing.StringConsumerChain;
import com.example.whensunset.pictureprocessing.staticParam.ObserverMapKey;
import com.example.whensunset.pictureprocessing.staticParam.StaticParam;
import com.example.whensunset.pictureprocessinggraduationdesign.BR;
import com.example.whensunset.pictureprocessinggraduationdesign.R;

import org.opencv.core.Rect;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;


/**
 * Created by whensunset on 2018/3/6.
 */

public class PictureFrameMenuVM extends BaseSeekBarRecycleViewVM<PictureFrameMenuVM.PictureFrameItemVM> implements CutView.OnLimitRectChangedListener {
    public static final String TAG = "何时夕:PictureFrameMenuVM";

    public final ObservableField<String> mInsertImagePath = new ObservableField<>();
    private final IImageUriFetch mLocalFrameImageUriFetch = LocalFrameImageUriFetch.getInstance();
    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();

    public PictureFrameMenuVM() {
        super(3 , BR.viewModel , R.layout.activity_picture_processing_picture_frame_item);

        initItemVM();
        initClick();
        initProgressChanged();
    }

    @Override
    protected void initItemVM() {
        mDataItemList.clear();

        PictureFrameItemVM firstPictureFrameItemVM = new PictureFrameItemVM(mEventListenerList , Uri.fromFile(new File(StaticParam.PICTURE_FRAME_ADD_IMAGE)).toString() , 0 , true);
        mDataItemList.add(firstPictureFrameItemVM);

        final int[] nowPosition = {1};
        Flowable.fromIterable(mLocalFrameImageUriFetch.getAllImageUriList())
                .map(frameImageUri -> new PictureFrameMenuVM.PictureFrameItemVM(mEventListenerList , frameImageUri , nowPosition[0]++))
                .subscribe(mDataItemList::add);
        MyLog.d(TAG, "initItemVM", "状态:mDataItemList", "" , mDataItemList);
    }

    @Override
    protected void initClick() {
        initListener(this, (observable, i) -> {
            String frameImagePath = ObserverParamMap.staticGetValue(observable , ObserverMapKey.PictureFrameItemVM_frameImagePath);
            UIActionManager.CallAllAfterEventAction callAllAfterEventAction = ObserverParamMap.staticGetValue(observable , ObserverMapKey.PictureFrameItemVM_CallAllAfterEventAction);
            mInsertImagePath.set(frameImagePath);
            if (callAllAfterEventAction != null) {
                callAllAfterEventAction.callAllAfterEventAction();
            }
            MyLog.d(TAG, "initItemListener", "状态:selectPosition:frameImagePath:", "" , frameImagePath);
        }, ItemManagerBaseVM.CLICK_ITEM);
    }

    @Override
    protected void initProgressChanged() {
        mUIActionManager
                .<ProgressChangedUIAction>getDefaultThrottleFlowable(UIActionManager.PROGRESS_CHANGED_ACTION)
                .subscribe(progressChangedUIAction -> {
                    mSelectParam.set(progressChangedUIAction.getProgress());
                    MyLog.d(TAG, "initProgressChanged", "状态:", "滑动了");
                });
    }

    @Override
    public void resume() {
        super.resume();
        mSelectParam.set(PROGRESS_MAX);
        mInsertImagePath.set("");
        nowInsertImageRect = mStringConsumerChain.getNowRect();
    }

    public void runInsertImage(UIActionManager.CallAllAfterEventAction callAllAfterEventAction) {
        if (!TextUtils.isEmpty(mInsertImagePath.get())) {
            PictureFrameMyConsumer consumer = new PictureFrameMyConsumer(mSelectParam.get() , mInsertImagePath.get() , nowInsertImageRect);
            mStringConsumerChain
                    .rxRunNextConvenient(consumer)
                    .subscribe(mat -> {
                        mEventListenerList.get(LEAVE_BSBRV_VM_LISTENER).set(ObserverParamMap.staticSet(ObserverMapKey.PictureFrameItemVM_mat , mat));
                        callAllAfterEventAction.callAllAfterEventAction();
                    });
        }

        MyLog.d(TAG, "runInsertImage", "状态:mInsertImagePath:mSelectParam:", "在离开frame的时候进行图片插入" , mInsertImagePath.get(), mSelectParam.get());
    }

    private Rect nowInsertImageRect = new Rect();
    @Override
    public void onLimitRectChanged(Rect cutRect) {
        if (isImageSizeChanged(cutRect , nowInsertImageRect)) {
            nowInsertImageRect = cutRect;
            MyLog.d(TAG, "onLimitRectChanged", "状态:cutRect", "图片限制框发生改变" , cutRect);
        }
    }

    public static class PictureFrameItemVM extends ItemBaseVM {
        public static final String TAG = "何时夕:PictureFilterItemVM";

        public static final int ITEM_PICTURE_RESIZE_WIDTH = 80;
        public static final int ITEM_PICTURE_RESIZE_HEIGHT = 80;

        private boolean isAdd = false;

        public final ObservableField<String> mImageUri=new ObservableField<>();

        public PictureFrameItemVM(List<ObservableField<? super Object>> clickItemListenerList , String imageUri , Integer position , boolean isAdd) {
            this(clickItemListenerList , imageUri , position);
            this.isAdd = isAdd;
        }

        public PictureFrameItemVM(List<ObservableField<? super Object>> clickItemListenerList , String imageUri , Integer position) {
            super(clickItemListenerList , position);
            initDefaultUIActionManager();

            mImageUri.set(imageUri);
            initClick();
        }

        private void initClick() {
            mUIActionManager
                    .<ClickUIAction>getDefaultThrottleFlowable(UIActionManager.CLICK_ACTION)
                    .filter(clickUIAction -> {
                        MyLog.d(TAG, "initClick", "状态:isAdd", "判断当前的item是否是 add" , isAdd);
                        return !isAdd;
                    }).subscribe(clickUIAction -> {
                        ObserverParamMap observerParamMap = getPositionParamMap()
                                .set(ObserverMapKey.PictureFrameItemVM_frameImagePath , Uri.parse(mImageUri.get()).getPath())
                                .set(ObserverMapKey.PictureFrameItemVM_CallAllAfterEventAction, clickUIAction.getCallAllAfterEventAction());
                        mEventListenerList.get(ItemManagerBaseVM.CLICK_ITEM).set(observerParamMap);
                        MyLog.d(TAG, "initClick", "状态:observerParamMap:", "结束了为图片添加图片框" , observerParamMap);
                    });
        }

        @Override
        public String toString() {
            return "PictureFrameItemVM{" +
                    ", mPosition=" + mPosition +
                    ", isSelected=" + isSelected +
                    ", isAdd=" + isAdd +
                    ", mPosition=" + mPosition +
                    ", mImageUri=" + mImageUri +
                    '}';
        }
    }
}
