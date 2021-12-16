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
        if (contentHeight == 0) contentHeight = (2340 * heightBias).toInt() // 此处2340应该替换为xxxUtil.getScreenHeight()
        (dialog as? BottomSheetDialog)?.let {
            it.window?.run {
                attributes = attributes.apply {
                    dimAmount = shadowAlpha
                }
            }
            // 如果是support库，应该换为对应的id
            val bottomSheet: FrameLayout? = it.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                bottomSheet.layoutParams = (it.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
                    // 下面这行会导致部分机型底部 导航栏遮住内容，但是绝大部分是好的
                    height = contentHeight
                }
                b = BottomSheetBehavior.from(bottomSheet)
                b.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    /**
     * 此处为vp的情况下的解决方案，如果在其他有两个可滑动的view在BottomSheetDialogFragment里面的话，要自行确定反射时机
     */
    protected fun setupViewPager(vp: ViewPager) {
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
            override fun onPageScrollStateChanged(p0: Int) { }

            override fun onPageSelected(p0: Int) {
                // 设置vp的监听,切换tab则反射修改behavior的view。此处加1的缘故是 xml里面的结构是vp嵌套tab(可以免去setupWithViewPager)，所以加一
                setNestedScrollerView(findScrollingChild(vp.getChildAt(vp.currentItem + 1)))
            }
        })
    }

    /**
     * 由于nestedScrollingChildRef是私有变量，反射修改behavior判定滑动的view
     */
    private fun setNestedScrollerView(view: View?) {
        BottomSheetBehavior::class.java.getDeclaredField("nestedScrollingChildRef")?.run {
            isAccessible = true
            set(b, WeakReference(view))
        }
    }

    /**
     * 摘自Behavior的同名方法，用kt简化了下
     */
    fun findScrollingChild(view: View): View? {
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
        /**
         * 默认屏占比
         */
        private const val DEFAULT_HEIGHT_BIAS = 0.7f

        /**
         * 默认背景不透明度
         */
        private const val DEFAULT_SHADOW_ALPHA = 0.5f
    }
}