package org.swm.att.common_ui.richtext

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import org.swm.att.domain.sever_driven_ui.response.text.ReportTextItemVO

class RichTextSpannable(richText: ReportTextItemVO): SpannableString(richText.text) {
    class Builder(private val richText: ReportTextItemVO) {
        private val spannableString = SpannableString(richText.text)

        fun setTextColor(color: String?): Builder {
            color?.let {
                Color.parseColor(it).let {
                    spannableString.setSpan(
                        ForegroundColorSpan(it),
                        0,
                        richText.text?.length ?: 0,
                        SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            return this
        }

        fun setFontSize(size: Float?): Builder {
            size?.let {
                spannableString.setSpan(
                    RelativeSizeSpan(it),
                    0,
                    richText.text?.length ?: 0,
                    SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return this
        }

        fun build() = spannableString
    }
}