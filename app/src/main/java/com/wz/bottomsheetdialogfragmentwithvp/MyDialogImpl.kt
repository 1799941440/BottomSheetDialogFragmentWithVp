package com.wz.bottomsheetdialogfragmentwithvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.wz.bottomsheetdialogfragmentwithvp.databinding.DialogMyTestBinding
import java.util.ArrayList

class MyDialogImpl : MyDialog<DialogMyTestBinding>() {

    val titles: MutableList<String> = ArrayList()
    val fragments: MutableList<Fragment> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (i in 0..3) {
            titles.add("标题$i")
            val testFragment = TestFragment()
            fragments.add(testFragment)
        }
        binding.vp.offscreenPageLimit = titles.size
        binding.vp.adapter = ViewPagerAdapter(childFragmentManager)
        setupViewPager(binding.vp)
    }

    override fun newViewBinding(inflater: LayoutInflater): DialogMyTestBinding {
        return DialogMyTestBinding.inflate(inflater)
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(i: Int): Fragment {
            return fragments[i]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}