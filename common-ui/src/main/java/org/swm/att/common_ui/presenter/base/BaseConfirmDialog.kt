package org.swm.att.common_ui.presenter.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.swm.att.common_ui.R
import org.swm.att.common_ui.databinding.BaseDialogConfirmBinding

abstract class BaseConfirmDialog(
    private val textRes: Int
) : DialogFragment() {
    private var _binding: BaseDialogConfirmBinding? = null
    protected val binding: BaseDialogConfirmBinding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
        }
        _binding = DataBindingUtil.inflate(inflater, R.layout.base_dialog_confirm, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object: Dialog(requireContext()) {
            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                return super.dispatchTouchEvent(ev)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setConfirmText()
        setCancelBtnClickListener()
        setConfirmBtnClickListener()
        setCloseDialogBtnClickListener()
        hideSystemUI()
    }

    private fun setConfirmText() {
        binding.tvConfirm.setText(textRes)
    }

    private fun setCancelBtnClickListener() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUI() = dialog?.window?.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(this, decorView).run {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }

    private fun setConfirmBtnClickListener() {
        binding.btnConfirm.setOnClickListener {
            dismiss()
            deleteItem()
        }
    }

    private fun setCloseDialogBtnClickListener() {
        binding.btnCloseConfirmDialog.setOnClickListener {
            dismiss()
        }
    }

    abstract fun deleteItem()

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}