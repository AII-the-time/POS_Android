package org.swm.att.common_ui.presenter.keypad

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TableRow
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import org.swm.att.common_ui.databinding.CustomKeypadBinding

class CustomKeyPad @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attr, defStyleAttr){

    private val binding: CustomKeypadBinding by lazy {
        CustomKeypadBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private lateinit var setLifeCycleOwner: LifecycleOwner
    private var onNumberItemClickListener: ((String) -> Unit)? = null
    private var onClearBtnClickListener: (() -> Unit)? = null
    private var onEnterBtnClickListener: (() -> Unit)? = null

    init {
        initialize()
    }

    private fun initialize() {
        setNumberBtnClickListener()
        setClearBtnClickListener()
        setEnterBtnClickListener()
    }

    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
        setLifeCycleOwner = lifecycleOwner
    }

    fun setOnNumberItemClickListener(listener: (String) -> Unit) {
        onNumberItemClickListener = listener
    }

    fun setOnClearBtnClickListener(listener: () -> Unit) {
        onClearBtnClickListener = listener
    }

    fun setOnEnterBtnClickListener(listener: () -> Unit) {
        onEnterBtnClickListener = listener
    }

    private fun setNumberBtnClickListener() {
        val tableLayout = binding.tlCustomKeypad
        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            for (j in 0 until tableRow.childCount) {
                val btn = tableRow.getChildAt(j) as AppCompatButton
                if (btn.id  == -1) {
                    val text = btn.text.toString()
                    btn.setOnClickListener {
                        onNumberItemClickListener?.let { listener ->
                            listener(text)
                        }
                    }
                }
            }
        }
    }

    private fun setClearBtnClickListener() {
        binding.btnClear.setOnClickListener {
            onClearBtnClickListener?.let { listener ->
                listener()
            }
        }
    }

    private fun setEnterBtnClickListener() {
        binding.btnEnter.setOnClickListener {
            onEnterBtnClickListener?.let { listener ->
                listener()
            }
        }
    }

}