package org.swm.att.common_ui.presenter.base

abstract class BaseSelectableViewViewModel: BaseViewModel() {
    abstract fun getSelectedItem(selectedItemId: Int)
    abstract fun setCurrentSelectedItemId(position: Int)
    abstract fun changeSelectedState()
}