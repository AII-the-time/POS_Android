package org.swm.att.home.option

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.swm.att.domain.entity.response.OptionVO
import org.swm.att.home.databinding.ItemMenuOptionBinding

class MenuOptionViewHolder(
    private val binding: ItemMenuOptionBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(menuOption: OptionVO) {
        binding.option= menuOption
        setChipGroup(menuOption)
    }

    private fun setChipGroup(menuOption: OptionVO) {
        menuOption.types?.let {
            for (type in it) {
                val chip = Chip(binding.root.context).apply {
                    id = type.id
                    text = type.name
                }
                binding.cgMenuOptionType.addView(chip)
            }
        }
    }
}