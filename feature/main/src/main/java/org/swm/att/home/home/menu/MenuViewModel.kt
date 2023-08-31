package org.swm.att.home.home.menu

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.swm.att.domain.repository.AttMenuRepository
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val attMenuRepository: AttMenuRepository
): ViewModel() {


}