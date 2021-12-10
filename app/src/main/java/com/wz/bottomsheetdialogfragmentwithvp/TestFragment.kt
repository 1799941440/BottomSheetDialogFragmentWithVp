package com.wz.bottomsheetdialogfragmentwithvp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wz.bottomsheetdialogfragmentwithvp.databinding.FragmentTestBinding
import java.util.ArrayList

class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentTestBinding.inflate(inflater, container, false)
        }
        initView()
        return binding.root
    }

    private fun initView() {
        val list: MutableList<String> = ArrayList()
        for (i in 0..29) {
            list.add("列表$i")
        }
        val recyclerAdapter = RecyclerAdapter(list)
        binding.recyclerview.adapter = recyclerAdapter
    }
}