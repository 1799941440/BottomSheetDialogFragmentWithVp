package com.wz.bottomsheetdialogfragmentwithvp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ref.WeakReference
import java.lang.reflect.Field

abstract class MyDialog<VB : ViewBinding> : BottomSheetDialogFragment() {

    protected lateinit var binding: VB
    private lateinit var b: BottomSheetBehavior<*>
    private var contentHeight = 0
    private val heightBias = DEFAULT_HEIGHT_BIAS
    private val shadowAlpha = DEFAULT_SHADOW_ALPHA

    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogStyle);
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = newViewBinding(inflater)
        }
        return binding.root
    }

    abstract fun newViewBinding(inflater: LayoutInflater): VB

    override fun onStart() {
        super.onStart()
        if (contentHeight == 0) contentHeight = (2340 * heightBias).toInt()
        (dialog as? BottomSheetDialog)?.let {
            it.window?.run {
                attributes = attributes.apply {
                    dimAmount = shadowAlpha
                }
            }
            val bottomSheet: FrameLayout? = it.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                bottomSheet.layoutParams = (it.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
                    // 下面功能会导致部分机型底部 导航栏遮住内容
                    height = contentHeight
                }
                b = BottomSheetBehavior.from(bottomSheet)
                b.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    protected fun setupViewPager(vp: ViewPager) {
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
            override fun onPageScrollStateChanged(p0: Int) { }

            override fun onPageSelected(p0: Int) {
                setNestedScrollerView(findScrollingChild(vp.getChildAt(vp.currentItem + 1)))
            }
        })
    }

    private fun setNestedScrollerView(view: View?) {
        val declaredField: Field? = BottomSheetBehavior::class.java.getDeclaredField("nestedScrollingChildRef")
        declaredField?.isAccessible = true
        declaredField?.set(b, WeakReference(view))
    }

    open fun findScrollingChild(view: View): View? {
        return if (ViewCompat.isNestedScrollingEnabled(view)) {
            view
        } else {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    return findScrollingChild(view.getChildAt(i))
                }
            }
            null
        }
    }

    companion object {
        private const val DEFAULT_HEIGHT_BIAS = 0.7f
        private const val DEFAULT_SHADOW_ALPHA = 0.5f
    }
}