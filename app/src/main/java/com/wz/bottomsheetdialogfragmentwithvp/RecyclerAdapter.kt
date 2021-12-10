package com.wz.bottomsheetdialogfragmentwithvp

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.wz.bottomsheetdialogfragmentwithvp.databinding.LayoutItemBinding

class RecyclerAdapter(
    private val list: List<String>
) : RecyclerView.Adapter<RecyclerAdapter.MyHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolder {
        val binding =
            LayoutItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(myHolder: MyHolder, i: Int) = myHolder.bind(list[i])

    override fun getItemCount() = list.size

    inner class MyHolder(private val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String) {
            with(binding) {
                tvText.text = string
            }
        }
    }
}