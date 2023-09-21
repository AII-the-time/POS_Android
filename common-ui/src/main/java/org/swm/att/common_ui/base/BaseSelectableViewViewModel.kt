package org.swm.att.common_ui.base

abstract class BaseSelectableViewViewModel: BaseViewModel() {
    abstract fun getSelectedItem(storeId: Int, selectedItemId: Int)
    abstract fun setCurrentSelectedItemId(position: Int)
}