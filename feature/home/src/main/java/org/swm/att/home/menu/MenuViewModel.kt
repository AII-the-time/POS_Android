package org.swm.att.home.menu

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.swm.att.domain.repository.AttPosRepository
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val attPosRepository: AttPosRepository
): ViewModel() {


}