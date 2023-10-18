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
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<T : ViewBinding>(
    @LayoutRes private val layoutRes: Int
) : DialogFragment() {
    private var _binding: T? = null
    protected val binding: T get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
        }
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
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
        hideSystemUI()
        setDialogSize()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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

    private fun setDialogSize() {
        val display = resources.displayMetrics
        val window:Window = dialog?.window ?: return
        val params:WindowManager.LayoutParams = window.attributes
        params.width = (display.widthPixels * 0.6).toInt()

        dialog?.window?.attributes = params
    }

}