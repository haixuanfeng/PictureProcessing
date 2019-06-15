package com.example.whensunset.pictureprocessing.base;

import android.databinding.ObservableField;

import com.example.whensunset.pictureprocessing.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessing.base.util.MyUtil;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemManagerBaseVM;
import com.example.whensunset.pictureprocessing.viewModel.includeLayoutVM.PictureParamMenuVM;

/**
 * Created by whensunset on 2018/3/21.
 */

public abstract class BaseSeekBarRecycleViewVM<T extends ItemBaseVM> extends ItemManagerBaseVM<T> {
    public static final String TAG = "何时夕:BaseSeekBarRecycleViewVM";

    public static final int LEAVE_BSBRV_VM_LISTENER = 1;
    public static final int PROGRESS_CHANGE = 2;

    public static final int PROGRESS_MAX = 100;

    public static final int MENU_PADDING = 10;
    public static final int SEEK_BAR_HEIGHT = 26;
    public static final int MENU_HEIGHT = PictureParamMenuVM.MENU_HEIGHT;
    public static final int MENU_WIDTH = MyUtil.getDisplayWidthDp();
    public static final int MENU_ITEM_HEIGHT = PictureParamMenuVM.MENU_ITEM_HEIGHT;
    public static final int MENU_ITEM_WIDTH = MENU_ITEM_HEIGHT;

    public final ObservableField<Integer> mSelectParam = new ObservableField<>(PROGRESS_MAX / 2);

    public BaseSeekBarRecycleViewVM(int listenerSize, int viewModelId, int viewItemLayoutId) {
        super(listenerSize, viewModelId, viewItemLayoutId);
        initDefaultUIActionManager();
    }

    @Override
    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this , UIActionManager.PROGRESS_CHANGED_ACTION);
    }

    protected abstract void initClick();
    protected abstract void initProgressChanged();

    @Override
    public void resume() {
        super.resume();
        mSelectParam.set(PROGRESS_MAX / 2);
    }

}
