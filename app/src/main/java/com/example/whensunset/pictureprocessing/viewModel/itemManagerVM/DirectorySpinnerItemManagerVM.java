package com.example.whensunset.pictureprocessing.viewModel.itemManagerVM;

import com.example.whensunset.pictureprocessing.base.uiaction.ItemSelectedUIAction;
import com.example.whensunset.pictureprocessing.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessing.base.util.MyLog;
import com.example.whensunset.pictureprocessing.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemBaseVM;
import com.example.whensunset.pictureprocessing.base.viewmodel.ItemManagerBaseVM;
import com.example.whensunset.pictureprocessing.impl.SystemImageUriFetch;
import com.example.whensunset.pictureprocessing.staticParam.ObserverMapKey;

import io.reactivex.Flowable;

/**
 * Created by whensunset on 2018/3/5.
 */

public class DirectorySpinnerItemManagerVM extends ItemManagerBaseVM<DirectorySpinnerItemManagerVM.DirectorySpinnerItemVM> {
    public static final String TAG = "何时夕:DirectorySpinnerItemManagerVM";

    public DirectorySpinnerItemManagerVM() {
        super(1 , null);
        initDefaultUIActionManager();

        initItemVM();
        initItemSelected();
    }

    @Override
    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this , UIActionManager.ITEM_SELECTED_ACTION);
    }

    @Override
    protected void initItemVM() {
        final int[] position = {0};
        Flowable.fromIterable(SystemImageUriFetch.getInstance().getAllTag())
                .map(o -> (String)o)
                .subscribe(o -> mDataItemList.add(new DirectorySpinnerItemVM((String)o , position[0]++)));
    }

    private void initItemSelected() {
        mUIActionManager
                .<ItemSelectedUIAction>getDefaultThrottleFlowable(UIActionManager.ITEM_SELECTED_ACTION)
                .subscribe(itemSelectedUIAction -> {
                    Integer selectedPosition = itemSelectedUIAction.getSelectedItemPosition();
                    Integer eventListenerPosition = itemSelectedUIAction.getLastEventListenerPosition();
                    String directoryName = mDataItemList.get(selectedPosition).mDirectoryName;

                    ObserverParamMap observerParamMap = ObserverParamMap
                            .staticSet(ObserverMapKey.DirectorySpinnerItemManagerVM_directoryName , directoryName)
                            .set(ObserverMapKey.ItemBaseVM_mPosition , selectedPosition);
                    mEventListenerList.get(eventListenerPosition).set(observerParamMap);
                    MyLog.d(TAG, "onItemSelected", "状态:directoryName:observerParamMap:", "" , directoryName , observerParamMap);
                });
    }

    public static class DirectorySpinnerItemVM extends ItemBaseVM {
        private final String mDirectoryName;

        public DirectorySpinnerItemVM(String directoryName , int position) {
            super(position);
            mDirectoryName = directoryName;
        }

        @Override
        public String toString() {
            return mDirectoryName.substring(mDirectoryName.lastIndexOf("/") + 1);
        }
    }
}
