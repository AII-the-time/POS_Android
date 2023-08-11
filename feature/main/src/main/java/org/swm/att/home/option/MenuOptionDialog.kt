package org.swm.att.home.option

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.swm.att.common_ui.base.BaseDialog
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.R
import org.swm.att.home.adapter.MenuOptionAdapter
import org.swm.att.home.databinding.DialogMenuOptionBinding

class MenuOptionDialog(
    private val menuOptions: List<OptionVO>
): BaseDialog<DialogMenuOptionBinding>(R.layout.dialog_menu_option) {
    private lateinit var menuOptionAdapter: MenuOptionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialogCloseBtnListener()
        initOptionsRecyclerView()
    }

    private fun setDialogCloseBtnListener() {
        binding.btnCloseMenuOptionPickerDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun initOptionsRecyclerView() {
        menuOptionAdapter = MenuOptionAdapter()
        binding.rvMenuOption.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = menuOptionAdapter
        }
        menuOptionAdapter.submitList(menuOptions)
    }

}