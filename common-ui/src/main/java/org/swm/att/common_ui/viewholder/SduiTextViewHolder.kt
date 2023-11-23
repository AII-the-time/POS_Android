package org.swm.att.common_ui.viewholder

import android.text.SpannableStringBuilder
import android.view.Gravity
import org.swm.att.common_ui.databinding.ItemSduiTextBinding
import org.swm.att.common_ui.richtext.RichTextSpannable
import org.swm.att.domain.sever_driven_ui.response.SduiReportContent
import org.swm.att.domain.sever_driven_ui.response.text.ReportTextVO

class SduiTextViewHolder(
    private val binding: ItemSduiTextBinding
): BaseSduiViewHolder(binding) {
    private var spanTextList = SpannableStringBuilder()

    override fun bind(reportContent: SduiReportContent) {
        reportContent as ReportTextVO

        reportContent.textItems.forEach {
            val spannableString = RichTextSpannable.Builder(it)
                .setTextColor(it.color)
                .setFontSize(it.size)
                .build()

            spanTextList.append(spannableString)
        }

        binding.tvSdui.text = spanTextList
        when(reportContent.align) {
            "LEFT" -> binding.tvSdui.setGravity(Gravity.START)
            "RIGHT" -> binding.tvSdui.setGravity(Gravity.END)
            "CENTER" -> binding.tvSdui.setGravity(Gravity.CENTER)
        }
    }
}